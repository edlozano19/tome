package com.tome.library.service;

import com.tome.library.dto.BookResponseDTO;
import com.tome.library.model.BookEntity;
import com.tome.library.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {
  private final BookRepository bookRepository;

  public CatalogService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public List<BookResponseDTO> listCatalog() {
    return bookRepository.findAll().stream().map(this::toBookResponse).toList();
  }

  private BookResponseDTO toBookResponse(BookEntity book) {
    return new BookResponseDTO(
        book.getId(),
        book.getTitle(),
        book.getAuthor(),
        book.getSlug(),
        book.getDescription(),
        book.getLanguage(),
        book.isPublicDomain(),
        book.getSource(),
        book.getCoverUrl());
  }
}
