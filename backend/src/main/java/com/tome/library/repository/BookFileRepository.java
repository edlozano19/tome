package com.tome.library.repository;

import com.tome.library.model.BookFileEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileRepository extends JpaRepository<BookFileEntity, UUID> {
  Optional<BookFileEntity> findBySha256(String sha256);
}
