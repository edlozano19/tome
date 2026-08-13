package com.tome.auth.service;

import com.tome.auth.domain.AccountEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final SecretKey secretKey;
  private final long accessTokenTtlMinutes;

  public JwtService(
      @Value("${tome.jwt.secret}") String secret,
      @Value("${tome.jwt.access-token-ttl-minutes}") long accessTokenTtlMinutes) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTokenTtlMinutes = accessTokenTtlMinutes;
  }

  public String generateAccessToken(AccountEntity account) {
    Instant now = Instant.now();
    Instant expiry = now.plusSeconds(accessTokenTtlMinutes * 60);

    return Jwts.builder()
        .subject(account.getId().toString())
        .claim("email", account.getEmail())
        .claim("role", account.getRole().name())
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiry))
        .signWith(secretKey)
        .compact();
  }

  public UUID parseAccountId(String token) {
    return UUID.fromString(parseClaims(token).getSubject());
  }

  private Claims parseClaims(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
  }
}
