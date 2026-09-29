package com.contractormanagement.backend.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtService {
  @Value("${jwt.secret}")
  private String JwtSecret;
  private SecretKey key;

  @PostConstruct
  private void init() {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtSecret));
  }

  public String generateJwt(String email) {
    return Jwts.builder()
      .subject(email)
      .expiration(new Date(new Date().getTime() + 30*60*1000))
      .signWith(key)
      .compact();
  };


  protected String getEmailFromJwt(String token) {
    try {
      return Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token)
          .getPayload().getSubject();
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Fail to extract email from token!");
    }
  }

  protected boolean validateJwt(String token) {
    try {
      Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token);
      return true;
    } catch (JwtException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid authentication token! Error: " + e);
    }
  }

}
