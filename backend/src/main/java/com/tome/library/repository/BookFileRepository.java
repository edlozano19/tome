package com.tome.library.repository;

import com.tome.library.model.BookFileEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileRepository extends JpaRepository<BookFileEntity, UUID> {}
