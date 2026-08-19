package com.tome.library;

import com.tome.library.model.BookEntity;
import com.tome.library.repository.BookRepository;
import java.util.UUID;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CatalogSeed implements ApplicationRunner {
  private final BookRepository bookRepository;

  public CatalogSeed(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  public void run(ApplicationArguments args) {
    seed(
        "dracula",
        "Dracula",
        "Bram Stoker",
        "A Gothic horror novel about Count Dracula's attempts to move from Tansylvania to England.",
        "gutenberg");
    seed(
        "frankenstein",
        "Frankenstein",
        "Mary Shelley",
        "A scientist creates life and faces the consequences of his ambition.",
        "gutenberg");
    seed(
        "pride-and-prejudice",
        "Pride and Prejudice",
        "Jane Austen",
        "Elizabeth Bennet navigates manners, marriage, and misunderstanding in Georgian England.",
        "gutenberg");
    seed(
        "the-adventures-of-sherlock-holmes",
        "The Adventures of Sherlock Holmes",
        "Arthur Conan Doyle",
        "A collection of twelve short stories featuring Shelock Holmes and Dr. Watson.",
        "gutenberg");
  }

  private void seed(String slug, String title, String author, String description, String source) {
    if (bookRepository.findBySlug(slug).isPresent()) {
      return;
    }

    bookRepository.save(
        new BookEntity(UUID.randomUUID(), title, author, slug, description, source, null));
  }
}
