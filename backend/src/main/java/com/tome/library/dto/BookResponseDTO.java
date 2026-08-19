package com.tome.library.dto;

import com.tome.library.model.BookEntity;
import java.util.UUID;

public class BookResponseDTO {
  private UUID id;
  private String title;
  private String author;
  private String slug;
  private String description;
  private String language;
  private boolean publicDomain;
  private String source;
  private String coverUrl;

  public BookResponseDTO() {} // NOSONAR - required by Jackson

  public static BookResponseDTO from(BookEntity book) {
    BookResponseDTO dto = new BookResponseDTO();
    dto.id = book.getId();
    dto.title = book.getTitle();
    dto.author = book.getAuthor();
    dto.slug = book.getSlug();
    dto.description = book.getDescription();
    dto.language = book.getLanguage();
    dto.publicDomain = book.isPublicDomain();
    dto.source = book.getSource();
    dto.coverUrl = book.getCoverUrl();
    return dto;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public boolean isPublicDomain() {
    return publicDomain;
  }

  public void setPublicDomain(boolean publicDomain) {
    this.publicDomain = publicDomain;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getCoverUrl() {
    return coverUrl;
  }

  public void setCoverUrl(String coverUrl) {
    this.coverUrl = coverUrl;
  }
}
