package com.example.demo.services.impl;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.services.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTServiceImpl implements JWTService{
  
  public String generateToken(UserDetails userDetails){
    return Jwts.builder().setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
            .signWith(getSiginKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails){
    return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 604800000))
            .signWith(getSiginKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  private Key getSiginKey(){
    byte[] key = Decoders.BASE64.decode("A3B1C2D3E4F5061728394A5B6C7D8E9FA1B2C3D4E5F60718A9B0C1D2E3F40516");
    return Keys.hmacShaKeyFor(key);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers){
    final Claims claims = extractAllClaims(token);
    return claimsResolvers.apply(claims);
  }

  private Claims extractAllClaims(String token){
    return Jwts.parser().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody();
  }

  public String extractUserName(String token){
    return extractClaim(token, Claims::getSubject);
  }

  public boolean isTokenValid(String token, UserDetails userDetails){
    final String username = extractUserName(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token){
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }
}
