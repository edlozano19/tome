package com.tome.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Entity
@Getter
@Table(name = "account")
public class AccountEntity {

  @Id private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 32)
  private Role role;

  @Column(nullable = false, unique = true, length = 320)
  private String email;

  @Column(name = "password_hash", nullable = false, length = 100)
  private String passwordHash;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected AccountEntity() {}

  public AccountEntity(UUID id, Role role, String email, String passwordHash) {
    this.id = id;
    this.role = role;
    this.email = email;
    this.passwordHash = passwordHash;
    Instant now = Instant.now();
    createdAt = now;
    updatedAt = now;
  }
}
