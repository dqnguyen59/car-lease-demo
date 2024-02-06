package org.smartblackbox.carleasedemo.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * This class will provide token and extractions of username and expirations
 * based on the token.
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Service
public class JwtService {
  
  /**
   * The secret key for the token.
   */
  @Value("${security.jwt.secret-key}")
  private String secretKey;

  /**
   * The expiration time the token.
   */
  @Value("${security.jwt.expiration-time}")
  private int jwtExpiration;

  /**
   * A constructor to create a JwtService.
   */
  public JwtService() {

  }

  /**
   * Extracts the username from the given token.
   * 
   * @param token the token provided from the http header.
   * @return the user name.
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extracts from token by the given claimResolver..
   * 
   * @param <T> the claim resolver type
   * @param token the token
   * @param claimsResolver the claim resolver
   * @return the object of the claim resolver type
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Generates a token based on the user details object.
   * 
   * @param userDetails user details object
   * @return a token
   */
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  /**
   * Generates a token based on the user details object and extract claims.
   * 
   * @param extraClaims the extract claims
   * @param userDetails the user details
   * @return a token
   */
  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * Gets the expiration time.
   * 
   * @return the expiration in milliseconds
   */
  public int getExpirationTime() {
    return jwtExpiration;
  }

  /**
   * Indicates whether the token is valid.
   * The token is compared with the userDetails object.
   * It will extract the username from the token and compares it with the username from userDetails.
   * If both username matched, then it will returns true,
   * but if the the token is expired then it will returns false anyway.
   * 
   * @param token the token
   * @param userDetails the user details
   * @return a boolean
   */
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  /**
   * Indicates whether the token has expired.
   * 
   * @param token the token
   * @return a boolean
   */
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Gets the expiration date from the token.
   * 
   * @param token the token
   * @return the date
   */
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extracts all claims of the given token.
   * 
   * @param token the token
   * @return the claim
   */
  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  /**
   * Gets the sign in key based on the secret key.
   * 
   * @return the key
   */
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
