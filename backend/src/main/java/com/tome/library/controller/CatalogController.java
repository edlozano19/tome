package com.tome.library.controller;

import com.tome.library.dto.BookResponseDTO;
import com.tome.library.service.CatalogService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {
  private final CatalogService catalogService;

  public CatalogController(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  @GetMapping("/books")
  public List<BookResponseDTO> listBooks() {
    return catalogService.listCatalog();
  }
}
