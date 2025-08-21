package com.millie.repository;

import com.millie.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    // 제목으로 검색
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    // 저자로 검색
    List<Book> findByAuthorContainingIgnoreCase(String author);
    
    // 카테고리별 검색
    List<Book> findByCategoryId(Long categoryId);
    
    // 제목 또는 저자로 검색
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Book> findByTitleOrAuthorContainingIgnoreCase(String keyword);
    
    // 최신순 정렬 (랭킹용)
    @Query("SELECT b FROM Book b ORDER BY b.createdAt DESC")
    List<Book> findAllOrderByCreatedAtDesc();
    
    // 출판일 최신순 정렬
    @Query("SELECT b FROM Book b ORDER BY b.publishedAt DESC")
    List<Book> findAllOrderByPublishedAtDesc();
}