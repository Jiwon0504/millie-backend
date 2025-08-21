package com.millie.repository;

import com.millie.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    // 특정 사용자가 특정 책에 좋아요를 눌렀는지 확인
    Optional<Like> findByUserIdAndBookId(Long userId, Long bookId);
    
    // 특정 사용자가 좋아요한 모든 책 조회
    List<Like> findByUserId(Long userId);
    
    // 특정 책의 좋아요 개수
    long countByBookId(Long bookId);
    
    // 특정 사용자가 좋아요한 책들의 ID 목록
    @Query("SELECT l.bookId FROM Like l WHERE l.userId = :userId")
    List<Long> findBookIdsByUserId(@Param("userId") Long userId);
    
    // 좋아요 삭제
    void deleteByUserIdAndBookId(Long userId, Long bookId);
    
    // 특정 사용자가 특정 책에 좋아요를 눌렀는지 확인 (boolean)
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
}
