package com.tome.library.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EpubStorageService {

  private static final String EPUB_FILE_EXTENSION = ".epub";

  private final Path epubDir;

  public EpubStorageService(@Value("${tome.storage.epub-dir}") String epubDir) {
    this.epubDir = Path.of(epubDir).toAbsolutePath().normalize();
  }

  public byte[] readBytes(MultipartFile file) {
    try {
      return file.getBytes();
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not read uploaded file", e);
    }
  }

  public String sha256(byte[] bytes) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(digest.digest(bytes));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 not available", e);
    }
  }

  public String store(byte[] bytes, String sha256) {
    try {
      Files.createDirectories(epubDir);
      Path target = epubDir.resolve(sha256 + EPUB_FILE_EXTENSION);
      if (!Files.exists(target)) {
        Files.write(target, bytes);
      }
      return target.toString();
    } catch (IOException e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Could not store EPUB file", e);
    }
  }

  public void validatePath(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EPUB file is required");
    }

    String filename = file.getOriginalFilename();
    if (filename == null || !filename.toLowerCase().endsWith(EPUB_FILE_EXTENSION)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File must be an .epub");
    }
  }

  public String titleFromFilename(String originalFilename) {
    if (originalFilename == null || originalFilename.isBlank()) {
      return "Uploaded book";
    }
    String name = Path.of(originalFilename).getFileName().toString();
    if (name.toLowerCase().endsWith(EPUB_FILE_EXTENSION)) {
      name = name.substring(0, name.length() - 5);
    }
    return name.isBlank() ? "Uploaded book" : name;
  }

  public String slugFromTitle(String title) {
    String base = title.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-)|(-$)", "");
    if (base.isBlank()) {
      base = "book";
    }
    return base + "-" + UUID.randomUUID().toString().substring(0, 8);
  }
}
