package com.avidbikers.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {

    // Key not for production but testing
    private final String SECRET_KEY = "AfridbikersSecurityKey";

    public String generateToken(UserPrincipal fetchedUser) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, fetchedUser.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
//                // Jwt expiration time is 10hr after jwt is issued
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public String extractEmail(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaim(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaim(String jwtToken) {
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwtToken).getBody();
    }

    public boolean validateToken(String token, UserDetails user) {
        final String username = extractEmail(token);
        log.info("Has expired ==> {}", !isTokenExpired(token));
        return username.equals(user.getUsername()) && isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        log.info("Extracted time => {}", extractExpirationDate(token));
        log.info("has token expired ==> {}", extractExpirationDate(token).before(new Date(System.currentTimeMillis())));
        return extractExpirationDate(token).before(new Date(System.currentTimeMillis()));
    }

    private Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
