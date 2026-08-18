package com.tome.library.repository;

import com.tome.library.model.BookEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, UUID> {
  Optional<BookEntity> findByTitle(String title);

  Optional<BookEntity> findBySlug(String slug);
}
