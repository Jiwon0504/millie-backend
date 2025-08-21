package com.millie.service;

import com.millie.dto.BookDto;
import com.millie.entity.Book;
import com.millie.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BookDto> getBooksOrderByRanking() {
        return bookRepository.findAllOrderByCreatedAtDesc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<BookDto> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public List<BookDto> searchBooks(String keyword) {
        return bookRepository.findByTitleOrAuthorContainingIgnoreCase(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<BookDto> getBooksByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public BookDto createBook(BookDto bookDto) {
        Book book = convertToEntity(bookDto);
        Book savedBook = bookRepository.save(book);
        return convertToDto(savedBook);
    }
    
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다: " + id));
        
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPublisher(bookDto.getPublisher());
        book.setCategoryId(bookDto.getCategoryId());
        book.setPublishedAt(bookDto.getPublishedAt());
        book.setTags(bookDto.getTags());
        book.setDescription(bookDto.getDescription());
        
        Book updatedBook = bookRepository.save(book);
        return convertToDto(updatedBook);
    }
    
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("책을 찾을 수 없습니다: " + id);
        }
        bookRepository.deleteById(id);
    }
    
    private BookDto convertToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublisher(book.getPublisher());
        dto.setCategoryId(book.getCategoryId());
        dto.setPublishedAt(book.getPublishedAt());
        dto.setTags(book.getTags());
        dto.setDescription(book.getDescription());
        
        // 카테고리 ID를 카테고리 이름으로 변환 (간단한 매핑)
        dto.setCategory(getCategoryName(book.getCategoryId()));
        
        return dto;
    }
    
    private Book convertToEntity(BookDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublisher(dto.getPublisher());
        book.setCategoryId(dto.getCategoryId());
        book.setPublishedAt(dto.getPublishedAt());
        book.setTags(dto.getTags());
        book.setDescription(dto.getDescription());
        return book;
    }
    
    private String getCategoryName(Long categoryId) {
        if (categoryId == null) return "전체";
        switch (categoryId.intValue()) {
            case 1: return "소설";
            case 2: return "경제경영";
            case 3: return "인문교양";
            case 4: return "자기계발";
            case 5: return "에세이";
            case 6: return "시/에세이";
            case 7: return "역사";
            case 8: return "과학";
            case 9: return "정치사회";
            case 10: return "종교";
            default: return "전체";
        }
    }
}