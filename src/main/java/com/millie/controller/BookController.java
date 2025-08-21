package com.millie.controller;

import com.millie.dto.BookDto;
import com.millie.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }
    
    @GetMapping("/ranking")
    public ResponseEntity<List<BookDto>> getBooksRanking() {
        List<BookDto> books = bookService.getBooksOrderByRanking();
        return ResponseEntity.ok(books);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(book -> ResponseEntity.ok(book))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<BookDto>> searchBooks(@RequestParam String keyword) {
        List<BookDto> books = bookService.searchBooks(keyword);
        return ResponseEntity.ok(books);
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BookDto>> getBooksByCategory(@PathVariable Long categoryId) {
        List<BookDto> books = bookService.getBooksByCategory(categoryId);
        return ResponseEntity.ok(books);
    }
    
    @PostMapping
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto) {
        try {
            BookDto createdBook = bookService.createBook(bookDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @Valid @RequestBody BookDto bookDto) {
        try {
            BookDto updatedBook = bookService.updateBook(id, bookDto);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/category-name/{categoryName}")
    public ResponseEntity<List<BookDto>> getBooksByCategoryName(@PathVariable String categoryName) {
        Long categoryId = getCategoryId(categoryName);
        if (categoryId == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<BookDto> books = bookService.getBooksByCategory(categoryId);
        return ResponseEntity.ok(books);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories() {
        Map<String, Object> categories = new HashMap<>();
        categories.put("전체", null);
        categories.put("소설", 1L);
        categories.put("경제경영", 2L);
        categories.put("인문교양", 3L);
        categories.put("자기계발", 4L);
        categories.put("에세이", 5L);
        categories.put("시/에세이", 6L);
        categories.put("역사", 7L);
        categories.put("과학", 8L);
        categories.put("정치사회", 9L);
        categories.put("종교", 10L);
        
        return ResponseEntity.ok(categories);
    }
    
    private Long getCategoryId(String categoryName) {
        switch (categoryName) {
            case "소설": return 1L;
            case "경제경영": return 2L;
            case "인문교양": return 3L;
            case "자기계발": return 4L;
            case "에세이": return 5L;
            case "시/에세이": return 6L;
            case "역사": return 7L;
            case "과학": return 8L;
            case "정치사회": return 9L;
            case "종교": return 10L;
            default: return null;
        }
    }
}