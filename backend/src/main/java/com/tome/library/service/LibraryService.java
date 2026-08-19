package com.tome.library.service;

import com.tome.auth.domain.AccountEntity;
import com.tome.library.dto.BookResponseDTO;
import com.tome.library.dto.UserBookResponseDTO;
import com.tome.library.model.BookEntity;
import com.tome.library.model.UserBookEntity;
import com.tome.library.repository.BookRepository;
import com.tome.library.repository.UserBookRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LibraryService {
  private final BookRepository bookRepository;
  private final UserBookRepository userBookRepository;

  public LibraryService(BookRepository bookRepository, UserBookRepository userBookRepository) {
    this.bookRepository = bookRepository;
    this.userBookRepository = userBookRepository;
  }

  @Transactional(readOnly = true)
  public List<UserBookResponseDTO> listLibrary(AccountEntity account) {
    return userBookRepository.findByAccount(account).stream()
        .map(this::toUserBookResponse)
        .toList();
  }

  @Transactional
  public UserBookResponseDTO addToLibrary(AccountEntity account, UUID bookId) {
    BookEntity book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

    if (userBookRepository.existsByAccountAndBook(account, book)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Book already in library");
    }

    UserBookEntity saved =
        userBookRepository.save(new UserBookEntity(UUID.randomUUID(), account, book));

    return toUserBookResponse(saved);
  }

  private UserBookResponseDTO toUserBookResponse(UserBookEntity userBook) {
    return new UserBookResponseDTO(
        userBook.getId(),
        userBook.getStatus(),
        userBook.getAddedAt(),
        toBookResponse(userBook.getBook()));
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
