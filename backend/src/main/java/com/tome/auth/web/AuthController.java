package com.tome.auth.web;

import com.tome.auth.service.AuthService;
import com.tome.auth.web.dto.LoginRequestDTO;
import com.tome.auth.web.dto.RegisterRequestDTO;
import com.tome.auth.web.dto.TokenResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public TokenResponseDTO register(@Valid @RequestBody RegisterRequestDTO request) {
    return authService.register(request);
  }

  @PostMapping("/login")
  public TokenResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
    return authService.login(request);
  }
}
