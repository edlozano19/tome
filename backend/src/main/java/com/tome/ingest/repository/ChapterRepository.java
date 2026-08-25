package com.tome.ingest.repository;

import com.tome.ingest.model.ChapterEntity;
import com.tome.library.model.BookEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<ChapterEntity, UUID> {
  List<ChapterEntity> findByBookOrderByOrdinalAsc(BookEntity book);

  void deleteByBook(BookEntity book);
}
