package com.softserve.security.user;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.mail.MessagingException;

import com.softserve.dto.UserDTO;
import com.softserve.exceptions.UserException;
import com.softserve.util.AuthoritiesMap;
import com.softserve.util.HtmlTemplate;
import com.softserve.util.ThymeleafAttributes;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.softserve.dto.CodeVerificationDTO;
import com.softserve.dto.EmailContent;
import com.softserve.dto.ForgotPasswordDT;
import com.softserve.dto.ResetPasswordDTO;
import com.softserve.exceptions.ForgotPasswordProcessException;
import com.softserve.model.Request;
import com.softserve.model.Role;
import com.softserve.model.Token;
import com.softserve.model.User;
import com.softserve.repository.IUserRepository;
import com.softserve.service.RequestService;
import com.softserve.service.TokenService;
import com.softserve.util.EmailService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServices implements UserDetailsService {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private RequestService requestService;
	
	private final String TEMPLATE_FORGOT_PASSWORD;
	
	private final String URL_RECOVERY_PASSWORD;
	private final String emailNotSent;
	private final String forgotPasswordWrong;
	private final String emailNotFound;
	private final String codeNotFound;
	private final String tokenWrong;
	private final String codeExpired;
	private final String codeIncorrect;
	private final String accountCreated;
	private final String passwordRestored;
	private final String emailSent;


	public UserServices(@Value("${template.forgot-password}") String TEMPLATE_FORGOT_PASSWORD,
						@Value("${spring.mail.username}") String EMAIL_USERNAME,
						IUserRepository userRepository, EmailService emailService, TokenService tokenService, RequestService requestService, @Value("${custom-url.recovery-password}") String URL_RECOVERY_PASSWORD,
						@Value("${email-not-sent}") String emailNotSent,
						@Value("${forgot-password-wrong}") String forgotPasswordWrong,
						@Value("${email-not-found}") String emailNotFound,
						@Value("${code-not-found}") String codeNotFound,
						@Value("${token-wrong}") String tokenWrong,
						@Value("${code-expired}") String codeExpired,
						@Value("${code-incorrect}") String codeIncorrect,
						@Value("${account-created}") String accountCreated,
						@Value("${password-restored}") String passwordRestored,
						@Value("${email-sent}") String emailSent) {

		this.TEMPLATE_FORGOT_PASSWORD = TEMPLATE_FORGOT_PASSWORD;
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.tokenService = tokenService;
		this.requestService = requestService;
		this.URL_RECOVERY_PASSWORD = URL_RECOVERY_PASSWORD;
		this.emailNotSent = emailNotSent;
		this.forgotPasswordWrong = forgotPasswordWrong;
		this.emailNotFound = emailNotFound;
		this.codeNotFound = codeNotFound;
		this.tokenWrong = tokenWrong;
		this.codeExpired = codeExpired;
		this.codeIncorrect = codeIncorrect;
		this.accountCreated = accountCreated;
		this.passwordRestored = passwordRestored;
		this.emailSent = emailSent;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		final Optional<User> optionalUseruser = userRepository.findByEmail(userName);
		if (optionalUseruser.isPresent()) {

			User user = optionalUseruser.get();
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getUserPassword(),
					AuthoritiesMap.mapAuthorities(user.getRoles()));
		} else
			throw new UsernameNotFoundException("User or password wrong.");
	}

	public Optional<User> findByEmail(final String email) {
		return this.userRepository.findByEmail(email);
	}

	public void forgotPasswordProcess(ForgotPasswordDT forgotPasswordDT, Model model) {
		final Optional<User> userOp = this.userRepository.findByEmail(forgotPasswordDT.getEmail());
		forgotPasswordDT.setIdRequest(3);
		if (userOp.isPresent()) {
			try {
				
				
				Map<String, Object> emailContentData = loadMap(forgotPasswordDT, userOp.get());
				
				EmailContent emailContent = new EmailContent();
				emailContent.setSubject("Restore password");
				emailContent.setTo(forgotPasswordDT.getEmail());
				emailContent.setTemplate(TEMPLATE_FORGOT_PASSWORD);
				emailContent.setModel(emailContentData);
				
				this.emailService.sendMessage(emailContent);
				
				model.addAttribute("emailSended", this.emailSent);
				log.info("Email was sended successfully for -> {}", forgotPasswordDT.getEmail());
				
			} catch (MailException | InterruptedException | ExecutionException | MessagingException | IOException
					| TemplateException e) {
				
				log.error("Error when email was builded -> {}", e);
				model.addAttribute(ThymeleafAttributes.ERROR , this.emailNotSent);
			} catch (ForgotPasswordProcessException e) {
				model.addAttribute(ThymeleafAttributes.ERROR , this.forgotPasswordWrong);
				log.error("Forgot password went wrong -> {}", e);
				
			}

		} else {
			model.addAttribute(ThymeleafAttributes.USER_NOT_FOUND, this.emailNotFound);
			log.error("Email was not sended successfully for -> {}", forgotPasswordDT.getEmail());
		}

	}

	public void verificationCodeProcess(int id, Model model) {
		Optional<Token> tokenOptional = this.tokenService.findById(id);
		model.addAttribute(ThymeleafAttributes.ID_TOKEN, id);

		if(tokenOptional.isPresent()) {
			final String template = "We emailed you the five digit code to %s";
			final String message = String.format(template, tokenOptional.get().getUser().getEmail());
			model.addAttribute(ThymeleafAttributes.MESSAGE, message);
			model.addAttribute(ThymeleafAttributes.TOKEN, id);
		}else {
			model.addAttribute(ThymeleafAttributes.ERROR, this.tokenWrong);
		}

	}

	public String codeVerificationRequest(int id, Model model, CodeVerificationDTO codeVerificationDTO, RedirectAttributes redirectAttributes) {
		Optional<Token> tokenOptional = this.tokenService.findById(id);
		if(tokenOptional.isPresent()) {
			final String code = codeVerificationDTO.toString();
			final Token token = tokenOptional.get();
			Timestamp now = Timestamp.from(Instant.now());
			if(now.after(token.getExpireDate())) {
				model.addAttribute(ThymeleafAttributes.ERROR, this.codeExpired);
				return HtmlTemplate.WELCOME_CODE_VERIFICATION;
			}else {
				if(code.equals(token.getValue())) {

					redirectAttributes.addFlashAttribute(ThymeleafAttributes.ID_USER, token.getUser().getIdAppUser());
					this.tokenService.deleteToken(token);
					return "redirect:/reset-password";

				}else {
					log.error("Secure code bad -> {}", codeVerificationDTO.toString());
					model.addAttribute(ThymeleafAttributes.ERROR, this.codeIncorrect);
					model.addAttribute(ThymeleafAttributes.ID_TOKEN,id);
					return HtmlTemplate.WELCOME_CODE_VERIFICATION;
				}
			}
		}else {
			model.addAttribute(ThymeleafAttributes.ERROR, this.codeNotFound);
			return HtmlTemplate.WELCOME_CODE_VERIFICATION;
		}

	}

    public void saveAccount(Model model, UserDTO userDTO) {

		try{
			validateNewUser(userDTO);
			loadAndSaveUSer(userDTO);

			model.addAttribute(ThymeleafAttributes.GENERAL_MESSAGE,this.accountCreated);
		}catch (UserException e){
			model.addAttribute(ThymeleafAttributes.ERROR, e.getMessage());
		}
    }

	private void loadAndSaveUSer(UserDTO userDTO) {
		User user = new User();
		final Timestamp now = Timestamp.from(Instant.now());
		final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

 		user.setFirstName(userDTO.getFirstName());
		user.setEmail(userDTO.getEmail());
		user.setLastName(userDTO.getLastName());
		user.setStatus((byte) 1);
		user.setModificationDate(now);
		user.setCreationDate(now);
		user.setUserPassword(encoder.encode(userDTO.getPassword()));

		this.userRepository.save(user);
	}

	private void validateNewUser(UserDTO userDTO) throws UserException {

		if (!userDTO.getPassword().equals(userDTO.getConfirmPassword()))
			throw new UserException("Your password must be equals");
	}

	private Map<String, Object> loadMap(ForgotPasswordDT forgotPasswordDT, User user) throws ForgotPasswordProcessException {
		Map<String, Object> content = new HashMap<>();
		LocalDateTime myDateObj = LocalDateTime.now();
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy");

		content.put("date", myDateObj.format(myFormatObj));

		String secureCodeToken = RandomStringUtils.random(5, false, true);
		content.put(ThymeleafAttributes.TOKEN, secureCodeToken);
		
		Optional<Request> request = this.requestService.findById(forgotPasswordDT.getIdRequest());
		
		if(request.isPresent()) {
			Token token = new Token();
			token.setValue(secureCodeToken);
			token.setIsValid(true);
			
			Instant instant = Instant.now();
			
			token.setCreationDate(Timestamp.from(instant));
			token.setExpireDate(Timestamp.from(instant.plusSeconds(15*60)));
			token.setUser(user);
			token.setRequest(request.get());
			
			this.tokenService.save(token);
			
			final String urlRecovery = String.format(URL_RECOVERY_PASSWORD, token.getIdToken());
			
			content.put("urlForgot", urlRecovery);
			
			return content;
		}else {
			throw new ForgotPasswordProcessException("The request of forgot password don't exists");
		}
		
	}

	public void saveNewPasswordProcess(Model model, ResetPasswordDTO resetPasswordDTO, int idUser) {
		try {
			
			User user = this.userRepository.findById(idUser).get();

			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			validateInputs(resetPasswordDTO, encoder);

			user.setUserPassword(encoder.encode(resetPasswordDTO.getNewPassword()));
			this.userRepository.save(user);
				
			model.addAttribute(ThymeleafAttributes.GENERAL_MESSAGE, this.passwordRestored);
			model.addAttribute(ThymeleafAttributes.ID_USER, idUser);
		} catch (ForgotPasswordProcessException e) {
			model.addAttribute(ThymeleafAttributes.ERROR, e.getMessage());
			model.addAttribute(ThymeleafAttributes.ID_USER, idUser);
			log.error("Error when inputs were validated");
		}
		
	}

	private void validateInputs(ResetPasswordDTO resetPasswordDTO, BCryptPasswordEncoder encoder) throws ForgotPasswordProcessException {
		if(!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getRepeatNewPassword())) {
			throw new ForgotPasswordProcessException("Your new password are not the same");
		}
	}
}
