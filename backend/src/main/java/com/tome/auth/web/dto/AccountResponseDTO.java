package com.tome.auth.web.dto;

import com.tome.auth.domain.Role;
import java.util.UUID;

public class AccountResponseDTO {
  private UUID id;
  private String email;
  private String username;
  private String firstName;
  private String lastName;
  private Role role;

  public AccountResponseDTO() {}

  public AccountResponseDTO(
      UUID id, String email, String username, String firstName, String lastName, Role role) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}
