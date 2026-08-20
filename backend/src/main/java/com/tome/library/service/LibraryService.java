package com.tome.library.service;

import com.tome.auth.domain.AccountEntity;
import com.tome.library.dto.BookResponseDTO;
import com.tome.library.dto.UserBookResponseDTO;
import com.tome.library.model.BookEntity;
import com.tome.library.model.BookFileEntity;
import com.tome.library.model.UserBookEntity;
import com.tome.library.repository.BookFileRepository;
import com.tome.library.repository.BookRepository;
import com.tome.library.repository.UserBookRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LibraryService {
  private static final String SOURCE_UPLOAD = "upload";

  private final BookRepository bookRepository;
  private final UserBookRepository userBookRepository;
  private final BookFileRepository bookFileRepository;
  private final EpubStorageService epubStorageService;

  public LibraryService(
      BookRepository bookRepository,
      UserBookRepository userBookRepository,
      BookFileRepository bookFileRepository,
      EpubStorageService epubStorageService) {
    this.bookRepository = bookRepository;
    this.userBookRepository = userBookRepository;
    this.bookFileRepository = bookFileRepository;
    this.epubStorageService = epubStorageService;
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

    return checkout(account, book);
  }

  @Transactional
  public UserBookResponseDTO uploadEpub(AccountEntity account, MultipartFile file) {
    epubStorageService.validatePath(file);

    byte[] bytes = epubStorageService.readBytes(file);
    String sha256 = epubStorageService.sha256(bytes);

    BookFileEntity existing = bookFileRepository.findBySha256(sha256).orElse(null);
    if (existing != null) {
      return checkout(account, existing.getBook());
    }

    String originalFilename = file.getOriginalFilename();
    String title = epubStorageService.titleFromFilename(originalFilename);
    String slug = epubStorageService.slugFromTitle(title);
    String storagePath = epubStorageService.store(bytes, sha256);

    BookEntity book =
        bookRepository.save(
            new BookEntity(UUID.randomUUID(), title, "Unknown", slug, null, SOURCE_UPLOAD, null));

    bookFileRepository.save(
        new BookFileEntity(
            UUID.randomUUID(), book, sha256, originalFilename, (long) bytes.length, storagePath));

    return checkout(account, book);
  }

  private UserBookResponseDTO checkout(AccountEntity account, BookEntity book) {
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
    return BookResponseDTO.from(book);
  }
}
