package com.tome.library.dto;

import com.tome.library.model.UserBookStatus;
import java.time.Instant;
import java.util.UUID;

public class UserBookResponseDTO {
  private UUID id;
  private UserBookStatus status;
  private Instant addedAt;
  private BookResponseDTO book;

  public UserBookResponseDTO() {}

  public UserBookResponseDTO(
      UUID id, UserBookStatus status, Instant addedAt, BookResponseDTO book) {
    this.id = id;
    this.status = status;
    this.addedAt = addedAt;
    this.book = book;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UserBookStatus getStatus() {
    return status;
  }

  public void setStatus(UserBookStatus status) {
    this.status = status;
  }

  public Instant getAddedAt() {
    return addedAt;
  }

  public void setAddedAt(Instant addedAt) {
    this.addedAt = addedAt;
  }

  public BookResponseDTO getBook() {
    return book;
  }

  public void setBook(BookResponseDTO book) {
    this.book = book;
  }
}
