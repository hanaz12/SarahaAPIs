package com.example.spring.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private  String SECRET_KEY ;

    @Value("${jwt.expiration}")
    private long jwtExpiration ;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public String extractUsername(String token) {

        return extractClaim(token,Claims::getSubject);

    }
    public String generateToken(UserDetails userDetails){

        return generateToken(new HashMap<>(),userDetails);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }
     public String generateToken(
             Map<String,Object> extraClaims,
             UserDetails userDetails) {
        return buildToken(extraClaims,userDetails,jwtExpiration);
     }

     public String generateRefreshToken(
            UserDetails userDetails) {
        return buildToken(new HashMap<>(),userDetails,refreshTokenExpiration);
    }

     public String buildToken(
         Map<String,Object> extraClaims,
         UserDetails userDetails,
         long expiration){
        return Jwts
                .builder()
             .setClaims(extraClaims)
             .setSubject(userDetails.getUsername())
             .setIssuedAt(new Date(System.currentTimeMillis()))
             .setExpiration(new Date(System.currentTimeMillis() +expiration ))
             .signWith(getSignKey(), SignatureAlgorithm.HS256)
             .compact();
    }

     public boolean isTokenValid(String token,UserDetails userDetails){
         final String username = extractUsername(token);
         return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
     }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
    return Jwts
            .parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
