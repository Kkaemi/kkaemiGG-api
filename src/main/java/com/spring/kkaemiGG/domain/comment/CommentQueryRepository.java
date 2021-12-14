package com.spring.kkaemiGG.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentQueryRepository {

    Page<Comment> getCommentPage(Long postId, Pageable pageable);
}
