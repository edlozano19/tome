package com.tome.auth.domain;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
  Optional<AccountEntity> findByEmail(String email);

  Optional<AccountEntity> findByUsername(String email);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);
}
