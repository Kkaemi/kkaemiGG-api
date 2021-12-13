package com.spring.kkaemiGG.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentQueryRepository {

    Page<Comment> getCommentPage(Long postId, Pageable pageable);

    void deleteWithChildComments(List<Long> commentIdList);
}
