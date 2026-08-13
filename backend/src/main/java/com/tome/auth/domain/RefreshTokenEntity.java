package com.tome.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Entity
@Getter
@Table(name = "refresh_token")
public class RefreshTokenEntity {

  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "account_id", nullable = false)
  private AccountEntity account;

  @Column(name = "token_hash", nullable = false, unique = true, length = 64)
  private String tokenHash;

  @Column(name = "expires_at", nullable = false)
  private Instant expiresAt;

  @Column(name = "revoked_at")
  private Instant revokedAt;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  protected RefreshTokenEntity() {}
  ;

  public RefreshTokenEntity(UUID id, AccountEntity account, String tokenHash, Instant expiresAt) {
    this.id = id;
    this.account = account;
    this.tokenHash = tokenHash;
    this.expiresAt = expiresAt;
    this.createdAt = Instant.now();
  }

  public void revoke() {
    this.revokedAt = Instant.now();
  }

  public boolean isUsable() {
    return revokedAt == null && Instant.now().isBefore(expiresAt);
  }
}
