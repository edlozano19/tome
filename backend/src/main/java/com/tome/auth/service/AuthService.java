package com.tome.auth.service;

import com.tome.auth.domain.AccountEntity;
import com.tome.auth.domain.AccountRepository;
import com.tome.auth.domain.RefreshTokenEntity;
import com.tome.auth.domain.RefreshTokenRepository;
import com.tome.auth.domain.Role;
import com.tome.auth.web.dto.AccountResponseDTO;
import com.tome.auth.web.dto.LoginRequestDTO;
import com.tome.auth.web.dto.RefreshRequestDTO;
import com.tome.auth.web.dto.RegisterRequestDTO;
import com.tome.auth.web.dto.TokenResponseDTO;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class AuthService {
  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final RefreshTokenRepository refreshTokenRepository;
  private final long refreshTokenTtlDays;

  public AuthService(
      AccountRepository accountRepository,
      PasswordEncoder passwordEncoder,
      JwtService jwtService,
      RefreshTokenRepository refreshTokenRepository,
      @Value("${tome.jwt.refresh-token-ttl-days}") long refreshTokenTtlDays) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.refreshTokenRepository = refreshTokenRepository;
    this.refreshTokenTtlDays = refreshTokenTtlDays;
  }

  public TokenResponseDTO register(RegisterRequestDTO request) {
    log.info(
        "Register request for email={} username={}", request.getEmail(), request.getUsername());

    if (accountRepository.existsByEmail(request.getEmail())) {
      log.warn("Register rejected: email already exists email={}", request.getEmail());
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
    }

    if (accountRepository.existsByUsername(request.getUsername())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
    }

    String passwordHash = passwordEncoder.encode(request.getPassword());

    AccountEntity account =
        new AccountEntity(
            UUID.randomUUID(),
            Role.USER,
            request.getEmail(),
            request.getUsername(),
            request.getFirstName(),
            request.getLastName(),
            passwordHash);

    AccountEntity saved = accountRepository.save(account);
    log.info("Account created id={} email={}", saved.getId(), saved.getEmail());
    return buildTokenResponse(saved);
  }

  public TokenResponseDTO login(LoginRequestDTO request) {
    log.info("Login request for email={}", request.getEmail());

    AccountEntity account =
        accountRepository
            .findByEmail(request.getEmail())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

    if (!passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    log.info("Login succeeded id={} email={}", account.getId(), account.getEmail());
    return buildTokenResponse(account);
  }

  @Transactional
  public TokenResponseDTO refresh(RefreshRequestDTO request) {
    String hash = hashToken(request.getRefreshToken());

    RefreshTokenEntity stored =
        refreshTokenRepository
            .findByTokenHash(hash)
            .orElseThrow(
                () ->
                    new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

    if (!stored.isUsable()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
    }

    stored.revoke();
    refreshTokenRepository.save(stored);

    AccountEntity account = stored.getAccount();
    return buildTokenResponse(account);
  }

  private AccountResponseDTO toResponse(AccountEntity account) {
    return new AccountResponseDTO(
        account.getId(),
        account.getEmail(),
        account.getUsername(),
        account.getFirstName(),
        account.getLastName(),
        account.getRole());
  }

  private TokenResponseDTO buildTokenResponse(AccountEntity account) {
    String accessToken = jwtService.generateAccessToken(account);
    String rawRefreshToken = createRefreshToken(account);
    return new TokenResponseDTO(accessToken, rawRefreshToken, "Bearer", toResponse(account));
  }

  private String createRefreshToken(AccountEntity account) {
    String raw = UUID.randomUUID().toString() + UUID.randomUUID();
    String hash = hashToken(raw);

    Instant expiresAt = Instant.now().plus(refreshTokenTtlDays, ChronoUnit.DAYS);
    RefreshTokenEntity entity = new RefreshTokenEntity(UUID.randomUUID(), account, hash, expiresAt);
    refreshTokenRepository.save(entity);

    return raw;
  }

  private String hashToken(String raw) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashed = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hashed);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 not available", e);
    }
  }
}
