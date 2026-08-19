package com.tome.library.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class AddLibraryBookRequestDTO {
  @NotNull private UUID bookId;

  public AddLibraryBookRequestDTO() {} // NOSONAR - required by Jackson

  public AddLibraryBookRequestDTO(UUID bookId) {
    this.bookId = bookId;
  }

  public UUID getBookId() {
    return bookId;
  }

  public void setBookId(UUID bookId) {
    this.bookId = bookId;
  }
}
