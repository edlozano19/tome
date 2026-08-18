package com.tome.library.repository;

import com.tome.library.model.UserBookEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookRepository extends JpaRepository<UserBookEntity, UUID> {}
