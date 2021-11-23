package com.spring.kkaemiGG.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQueryRepository {

    @Query("SELECT c " +
            "FROM Comment c " +
            "LEFT JOIN FETCH c.childComments " +
            "WHERE c.id = :parentCommentId")
    Optional<Comment> findParentCommentById(@Param("parentCommentId") Long parentCommentId);
}
