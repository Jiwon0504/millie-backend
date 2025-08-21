package com.millie.service;

import com.millie.dto.BookDto;
import com.millie.entity.Like;
import com.millie.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LikeService {
    
    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private BookService bookService;
    
    // 좋아요 추가
    public boolean addLike(Long userId, Long bookId) {
        try {
            // 이미 좋아요가 있는지 확인
            if (likeRepository.existsByUserIdAndBookId(userId, bookId)) {
                return false; // 이미 좋아요한 책
            }
            
            Like like = new Like(userId, bookId);
            likeRepository.save(like);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // 좋아요 제거
    public boolean removeLike(Long userId, Long bookId) {
        try {
            likeRepository.deleteByUserIdAndBookId(userId, bookId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // 좋아요 토글 (있으면 제거, 없으면 추가)
    public boolean toggleLike(Long userId, Long bookId) {
        if (likeRepository.existsByUserIdAndBookId(userId, bookId)) {
            return !removeLike(userId, bookId); // 제거했으면 false 반환
        } else {
            return addLike(userId, bookId); // 추가했으면 true 반환
        }
    }
    
    // 특정 사용자가 좋아요한 책 목록 조회
    public List<BookDto> getLikedBooks(Long userId) {
        List<Long> likedBookIds = likeRepository.findBookIdsByUserId(userId);
        
        return likedBookIds.stream()
                .map(bookId -> bookService.getBookById(bookId))
                .filter(bookDto -> bookDto.isPresent())
                .map(bookDto -> bookDto.get())
                .collect(Collectors.toList());
    }
    
    // 특정 책의 좋아요 개수
    public long getLikeCount(Long bookId) {
        return likeRepository.countByBookId(bookId);
    }
    
    // 특정 사용자가 특정 책을 좋아요했는지 확인
    public boolean isLikedByUser(Long userId, Long bookId) {
        return likeRepository.existsByUserIdAndBookId(userId, bookId);
    }
    
    // 사용자의 좋아요한 책 ID 목록
    public List<Long> getLikedBookIds(Long userId) {
        return likeRepository.findBookIdsByUserId(userId);
    }
}
