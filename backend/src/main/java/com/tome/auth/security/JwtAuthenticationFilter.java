package com.tome.auth.security;

import com.tome.auth.domain.AccountEntity;
import com.tome.auth.domain.AccountRepository;
import com.tome.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final AccountRepository accountRepository;

  public JwtAuthenticationFilter(JwtService jwtService, AccountRepository accountRepository) {
    this.jwtService = jwtService;
    this.accountRepository = accountRepository;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String header = request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);

    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = header.substring(7);

    try {
      UUID accountId = jwtService.parseAccountId(token);
      AccountEntity account = accountRepository.findById(accountId).orElse(null);

      if (account != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        var authority = new SimpleGrantedAuthority("ROLE_" + account.getRole().name());
        var authentication =
            new UsernamePasswordAuthenticationToken(account, null, List.of(authority));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception ex) {
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
  }
}
