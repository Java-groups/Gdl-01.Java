package com.softserve.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import com.softserve.exceptions.ArticleException;
import com.softserve.model.Team;
import com.softserve.model.User;
import com.softserve.security.user.UserServices;
import com.softserve.util.Constants;
import com.softserve.util.FireBaseProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.softserve.dto.CategoryDTO;
import com.softserve.dto.LocationDTO;
import com.softserve.dto.NewArticleDTO;
import com.softserve.dto.TeamDTO;
import com.softserve.model.Article;
import com.softserve.repository.IArticleRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArticleService {

	@Autowired
	private IArticleRepository articleRepository;
	
	@Autowired
	private SubCategoryService subCategoryService;

	@Autowired
	private TeamService teamService;
	
	@Autowired
	private LocationService locationService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserServices userServices;

	@Autowired
	private FireBaseProcess fireBaseProcess;

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final String baseUrl;

	
	public ArticleService(@Value("${firebase.base-url}") String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public void loadArticleDescription(Model model, Integer idArticle) {
		this.subCategoryService.loadContentMain(model);
		final Optional<Article> articleOptional = this.articleRepository.findById(idArticle);
		
		if(articleOptional.isPresent()) {
			Article article = articleOptional.get();
			article.setImage(String.format(this.baseUrl, article.getImage()));
			model.addAttribute("article", article);	
		}
		
		
	}
	public void welcomeTo(UserDetails userDetails) {
		log.info("User log in: {} with roles: {}", userDetails.getUsername(), userDetails.getAuthorities());
	}
	
	public void loadNewArticleContent(Model model, NewArticleDTO newArticleDTO) {
		final List<TeamDTO> teamList = this.teamService.findAll().stream().map(team ->
			new TeamDTO(team.getIdTeam(), team.getDescription())
		).collect(Collectors.toList());
		
		final List<LocationDTO> locationList = this.locationService.findAll().stream().map(location ->
												new LocationDTO(location.getIdLocation(), location.getDescription()))
												.collect(Collectors.toList());
		List<CategoryDTO> categoriesList = this.categoryService.findByIdParentCategoryIsNull().stream()
											.map(category -> 
											new CategoryDTO(category.getIdCategory(), category.getName())
											).collect(Collectors.toList());
		
		model.addAttribute("teamList", teamList);
		model.addAttribute("locationList", locationList);
		model.addAttribute("categoriesList", categoriesList);
	}

	public void saveArticle(Model model, NewArticleDTO newArticleDTO) {

		try {
			loadArticle(newArticleDTO);

			model.addAttribute("generalMessage", "The article was created successfully.");
			loadNewArticleContent(model, newArticleDTO);
		}catch (ArticleException articleException){
			log.error("Error when article was be builded. ->{}", articleException);
			model.addAttribute("error", articleException.getMessage());
			loadNewArticleContent(model, newArticleDTO);
		}

	}

	/**
	 *
	 * @param json
	 * @return Article saved
	 */
	public ResponseEntity<?> saveArticle(Map<String, Object> json, MultipartFile articleImage) throws ArticleException {
		final NewArticleDTO newArticleDTO = mapJsonToArticleDTO(json, articleImage);
		final Article article = loadArticle(newArticleDTO);

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("message", Constants.ARTICLE_CREATED);
		responseBody.put("articleId",article.getIdArticle());
		return ResponseEntity.ok(responseBody);
	}

	private NewArticleDTO mapJsonToArticleDTO(Map<String, Object> json, MultipartFile articleImage) {

		try {
			NewArticleDTO newArticleDTO = new NewArticleDTO();
			newArticleDTO.setArticleImage(articleImage);
			newArticleDTO.setTeam(Integer.parseInt(json.get("team").toString()));
			newArticleDTO.setSubCategory(Integer.parseInt(json.get("subCategory").toString()));
			newArticleDTO.setLocation(Integer.parseInt(json.get("location").toString()));
			newArticleDTO.setHeadLine(json.get("headLine").toString());
			newArticleDTO.setCaption(json.get("caption").toString());
			newArticleDTO.setArticleDescriptionHtml(json.get("articleDescriptionHtml").toString());
			newArticleDTO.setArticleDescription(json.get("articleDescription").toString());
			return newArticleDTO;
		}catch (NumberFormatException i){
			log.error("Number format exception -> {}", i);
			throw new ArticleException("Something went wrong when Article was saving");
		}

	}

	private Article loadArticle(NewArticleDTO newArticleDTO) throws ArticleException{

		if(newArticleDTO.getArticleDescriptionHtml().equals(""))
			throw new ArticleException("The article description is required");
		if(newArticleDTO.getArticleImage().getOriginalFilename().equals(""))
			throw new ArticleException("The article image is required");

		Article article = new Article();

		article.setCreationDate(Timestamp.from(Instant.now()));
		article.setModificationDate(Timestamp.from(Instant.now()));
		article.setDescriptionHtml(newArticleDTO.getArticleDescriptionHtml());
		article.setDescription(newArticleDTO.getArticleDescription());
		article.setIsCommentable(true);
		article.setStatus(1);
		final Optional<Team> teamOptional = this.teamService.finById(newArticleDTO.getTeam());

		if(!teamOptional.isPresent())
			throw new ArticleException("The team was not founded.");

		final Team team = teamOptional.get();
		article.setTeam(team);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		final String userName = authentication.getName();

		User user = this.userServices.findByEmail(userName).get();

		article.setIdAppUser(user.getIdAppUser());
		article.setTitle(newArticleDTO.getHeadLine());

		final String ALT_MEDIA = "?alt=media";

		final String articleImageName = String.format("%s%s",this.fireBaseProcess.upload(newArticleDTO.getArticleImage(), article), ALT_MEDIA);
		article.setImage(articleImageName);
		this.articleRepository.save(article);
		return article;
	}

    public void loadTop3Popular(Model model) {
		List<Article>top3Commented = articleRepository.findByTop3MostCommented();
		List<Article>top3Liked = articleRepository.findByTop3MostLiked();
		model.addAttribute("articleListMostCommented", top3Commented);
		model.addAttribute("articleListMostLiked", top3Liked);
    }

	public ResponseEntity<?> loadNewArticleContentApi() {

		final List<TeamDTO> teamList = this.teamService.findAll().stream().map(team ->
				new TeamDTO(team.getIdTeam(), team.getDescription())
		).collect(Collectors.toList());

		final List<LocationDTO> locationList = this.locationService.findAll().stream().map(location ->
						new LocationDTO(location.getIdLocation(), location.getDescription()))
				.collect(Collectors.toList());
		List<TeamDTO> categoriesList = this.categoryService.findByIdParentCategoryIsNull().stream()
				.map(category ->
						new TeamDTO(category.getIdCategory(), category.getName())
				).collect(Collectors.toList());

		Map<String, Object> list = new HashMap<>();
		list.put("categories", categoriesList);
		list.put("locations", locationList);
		list.put("teams", teamList);
		return ResponseEntity.ok(list);
	}
}
