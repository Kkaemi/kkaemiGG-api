package com.spring.kkaemiGG.domain.comment;

import java.util.List;

public interface CommentQueryRepository {

    List<Comment> findByPostId(Long postId);
}
