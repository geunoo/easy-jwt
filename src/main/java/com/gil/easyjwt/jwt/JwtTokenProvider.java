package com.gil.easyjwt.jwt;

import com.gil.easyjwt.exception.ExpiredTokenException;
import com.gil.easyjwt.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;
    private static final String PREFIX = "Bearer ";
    private static final String HEADER = "Authorization";

    public LocalDateTime getExpiredAt(TokenType type) {
        return switch (type) {
            case ACCESS -> LocalDateTime.now().plusSeconds(jwtProperties.getAccessExp());
            case REFRESH -> LocalDateTime.now().plusSeconds(jwtProperties.getRefreshExp());
        };
    }

    public String generateAccessToken(String subject, Map<String, String> claims) {
        return generateToken(subject, jwtProperties.getAccessExp(), TokenType.ACCESS, claims);
    }

    public String generateAccessToken(String subject) {
        return generateToken(subject, jwtProperties.getAccessExp(), TokenType.ACCESS, null);
    }

    public String generateRefreshToken(String subject, Map<String, String> claims) {
        return generateToken(subject, jwtProperties.getRefreshExp(), TokenType.REFRESH, claims);
    }

    public String generateRefreshToken(String subject) {
        return generateToken(subject, jwtProperties.getRefreshExp(), TokenType.REFRESH, null);
    }

    private String generateToken(String subject, Integer exp, TokenType type, Map<String, String> claims) {
        return Jwts.builder()
                .signWith(getSecretKey())
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (exp * 1000)))
                .header()
                .type(type.name())
                .and()
                .claims(claims)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Jws<Claims> claims = getClaims(token);

        if (!claims.getHeader().getType().equals(TokenType.ACCESS.name())) {
            throw new InvalidTokenException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getPayload().getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER);
        if (token != null && token.startsWith(PREFIX)) {
            return token.replace(PREFIX, "");
        }
        return null;
    }

    private Jws<Claims> getClaims(String token) throws ExpiredTokenException, InvalidTokenException  {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
