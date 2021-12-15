package com.spring.kkaemiGG.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostQueryRepository {

    @Query("SELECT p " +
            "FROM Post p " +
            "INNER JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.views " +
            "WHERE p.id = :postId")
    Optional<Post> fetchUserAndViews(@Param("postId") Long postId);

    Optional<Post> findByIdAndDeletedDateIsNull(Long postId);
}
