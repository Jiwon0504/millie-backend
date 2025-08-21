package com.millie.controller;

import com.millie.dto.BookDto;
import com.millie.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "http://localhost:3000")
public class LikeController {
    
    @Autowired
    private LikeService likeService;
    
    // 좋아요 토글 (추가/제거)
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @RequestParam Long userId, 
            @RequestParam Long bookId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean isLiked = likeService.toggleLike(userId, bookId);
            long likeCount = likeService.getLikeCount(bookId);
            
            response.put("success", true);
            response.put("isLiked", isLiked);
            response.put("likeCount", likeCount);
            response.put("message", isLiked ? "좋아요를 추가했습니다." : "좋아요를 취소했습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "좋아요 처리 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 특정 사용자의 좋아요한 책 목록 (내서재용)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookDto>> getLikedBooks(@PathVariable Long userId) {
        try {
            List<BookDto> likedBooks = likeService.getLikedBooks(userId);
            return ResponseEntity.ok(likedBooks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 특정 책의 좋아요 상태 확인
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getLikeStatus(
            @RequestParam Long userId, 
            @RequestParam Long bookId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean isLiked = likeService.isLikedByUser(userId, bookId);
            long likeCount = likeService.getLikeCount(bookId);
            
            response.put("isLiked", isLiked);
            response.put("likeCount", likeCount);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("isLiked", false);
            response.put("likeCount", 0);
            return ResponseEntity.ok(response);
        }
    }
    
    // 특정 책의 좋아요 개수
    @GetMapping("/count/{bookId}")
    public ResponseEntity<Map<String, Object>> getLikeCount(@PathVariable Long bookId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            long likeCount = likeService.getLikeCount(bookId);
            response.put("likeCount", likeCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("likeCount", 0);
            return ResponseEntity.ok(response);
        }
    }
}
