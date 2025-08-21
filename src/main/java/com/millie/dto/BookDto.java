package com.millie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class BookDto {
    
    private Long id;
    
    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다")
    private String title;
    
    @Size(max = 255, message = "저자는 255자를 초과할 수 없습니다")
    private String author;
    
    @Size(max = 255, message = "출판사는 255자를 초과할 수 없습니다")
    private String publisher;
    
    private Long categoryId;
    private String category; // 카테고리 이름 (프론트엔드용)
    private LocalDate publishedAt;
    private String tags;
    private String description;
    
    // Constructors
    public BookDto() {}
    
    public BookDto(String title, String author) {
        this.title = title;
        this.author = author;
    }
    
    public BookDto(Long id, String title, String author, String publisher, Long categoryId, LocalDate publishedAt, String tags) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.categoryId = categoryId;
        this.publishedAt = publishedAt;
        this.tags = tags;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public LocalDate getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDate publishedAt) { this.publishedAt = publishedAt; }
    
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}