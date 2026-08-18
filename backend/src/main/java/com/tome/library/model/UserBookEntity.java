package com.tome.library.model;

import com.tome.auth.domain.AccountEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import lombok.Getter;

@Entity
@Getter
@Table(name = "user_book")
public class UserBookEntity {

  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "account_id", nullable = false)
  private AccountEntity account;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "book_id", nullable = false)
  private BookEntity book;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 32)
  private UserBookStatus status;

  @JdbcTypeCode(SqlTypes.SMALLINT)
  @Column 
  private Integer rating;

  @Column(name = "current_chapter")
  private Integer currentChapter;

  @Column(name = "current_cfi", length = 500)
  private String currentCfi;

  @Column(name = "added_at", nullable = false)
  private Instant addedAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected UserBookEntity() {}

  public UserBookEntity(UUID id, AccountEntity account, BookEntity book) {
    this.id = id;
    this.account = account;
    this.book = book;
    this.status = UserBookStatus.WANT_TO_READ;
    Instant now = Instant.now();
    this.addedAt = now;
    this.updatedAt = now;
  }
}
