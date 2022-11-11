package com.softserve.security.jwt;

import com.softserve.exceptions.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
@Data
public class JwtUtils {
    @Value("${jwt.jwt-secret}")
    private String jwtSecret;

    @Value("${jwt.jwt-expiration}")
    private int jwtExpirationMs;

    @Value("${jwt.cookie-name}")
    private String jwtCookie;

    public String getJwtFromAuthorization(HttpServletRequest request) {
        final String authorization = request.getHeader("Authorization");
        final String BEARER_KEYWORD= "Bearer ";
        return Objects.isNull(authorization) || authorization.equals("") ? null : authorization.replace(BEARER_KEYWORD,"");
    }

    public String generateJwtCookie(String userPrincipal) {
        return generateTokenFromUsername(userPrincipal);
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
        return cookie;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken, HttpServletRequest request) throws ServletException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new CustomException(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new CustomException(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        } catch (ExpiredJwtException e) {
            request.logout();
            log.error("JWT token is expired: {}", e.getMessage());
            throw new CustomException("Your authorization has expired, please, send another request for new access", HttpStatus.UNAUTHORIZED.value());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw new CustomException(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new CustomException(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        }
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString().replaceAll("-", ""))
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256,jwtSecret)
                .compact();
    }
}
