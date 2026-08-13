package com.tome.auth.web.dto;

public class TokenResponseDTO {
  private String accessToken;
  private String refreshToken;
  private String tokenType;
  private AccountResponseDTO account;

  public TokenResponseDTO() {}

  public TokenResponseDTO(
      String accessToken, String refreshToken, String tokenType, AccountResponseDTO account) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.tokenType = tokenType;
    this.account = account;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public AccountResponseDTO getAccount() {
    return account;
  }

  public void setAccount(AccountResponseDTO account) {
    this.account = account;
  }
}
