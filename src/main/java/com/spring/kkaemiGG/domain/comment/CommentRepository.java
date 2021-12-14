package com.spring.kkaemiGG.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQueryRepository {

    @Query("SELECT c " +
            "FROM Comment c " +
            "LEFT JOIN FETCH c.childComments " +
            "WHERE c.id = :parentCommentId")
    Optional<Comment> findParentCommentFetchedChildCommentsById(@Param("parentCommentId") Long parentCommentId);

    @Query("SELECT c " +
            "FROM Comment c " +
            "LEFT JOIN FETCH c.user " +
            "WHERE c.id = :parentCommentId")
    Optional<Comment> findCommentFetchedUserById(@Param("parentCommentId") Long parentCommentId);

    Optional<Comment> findByIdAndDeletedDateIsNull(Long commentId);

    @Modifying
    @Query("UPDATE Comment c " +
            "SET c.deletedDate = :now " +
            "WHERE c.id = :commentId " +
            "OR c.parentComment.id = :commentId")
    void deleteWithChildComments(
            @Param("commentId") Long commentId,
            @Param("now") LocalDateTime now
    );
}
