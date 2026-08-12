package com.tome.auth.service;

import com.tome.auth.domain.AccountEntity;
import com.tome.auth.domain.AccountRepository;
import com.tome.auth.domain.Role;
import com.tome.auth.web.dto.AccountResponseDTO;
import com.tome.auth.web.dto.LoginRequestDTO;
import com.tome.auth.web.dto.RegisterRequestDTO;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class AuthService {
  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public AccountResponseDTO register(RegisterRequestDTO request) {
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
    return toResponse(saved);
  }

  public AccountResponseDTO login(LoginRequestDTO request) {
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
    return toResponse(account);
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
}
