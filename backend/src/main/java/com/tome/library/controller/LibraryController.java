package com.tome.library.controller;

import com.tome.auth.domain.AccountEntity;
import com.tome.library.dto.AddLibraryBookRequestDTO;
import com.tome.library.dto.UserBookResponseDTO;
import com.tome.library.service.LibraryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/library")
public class LibraryController {
  private final LibraryService libraryService;

  public LibraryController(LibraryService libraryService) {
    this.libraryService = libraryService;
  }

  @GetMapping("/books")
  public List<UserBookResponseDTO> listBooks(Authentication authentication) {
    return libraryService.listLibrary(requireAccount(authentication));
  }

  @PostMapping("/books")
  @ResponseStatus(HttpStatus.CREATED)
  public UserBookResponseDTO addBook(
      Authentication authentication, @Valid @RequestBody AddLibraryBookRequestDTO request) {
    return libraryService.addToLibrary(requireAccount(authentication), request.getBookId());
  }

  @PostMapping(value = "/books/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public UserBookResponseDTO uploadBook(
      Authentication authentication, @RequestPart("file") MultipartFile file) {
    return libraryService.uploadEpub(requireAccount(authentication), file);
  }

  private AccountEntity requireAccount(Authentication authentication) {
    if (authentication == null
        || !(authentication.getPrincipal() instanceof AccountEntity account)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
    return account;
  }
}
