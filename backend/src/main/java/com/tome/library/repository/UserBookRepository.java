package com.tome.library.repository;

import com.tome.auth.domain.AccountEntity;
import com.tome.library.model.BookEntity;
import com.tome.library.model.UserBookEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookRepository extends JpaRepository<UserBookEntity, UUID> {
  List<UserBookEntity> findByAccount(AccountEntity account);

  Optional<UserBookEntity> findByAccountAndBook(AccountEntity account, BookEntity book);

  boolean existsByAccountAndBook(AccountEntity account, BookEntity book);
}
