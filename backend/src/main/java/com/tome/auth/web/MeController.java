package com.tome.auth.web;

import com.tome.auth.domain.AccountEntity;
import com.tome.auth.web.dto.AccountResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class MeController {
  @GetMapping("/me")
  public AccountResponseDTO me(Authentication authentication) {
    if (authentication == null
        || !(authentication.getPrincipal() instanceof AccountEntity account)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    return new AccountResponseDTO(
        account.getId(),
        account.getEmail(),
        account.getUsername(),
        account.getFirstName(),
        account.getLastName(),
        account.getRole());
  }
}
