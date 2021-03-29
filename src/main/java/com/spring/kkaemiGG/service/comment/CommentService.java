package com.spring.kkaemiGG.service.comment;

import com.spring.kkaemiGG.domain.comment.Comment;
import com.spring.kkaemiGG.domain.comment.CommentRepository;
import com.spring.kkaemiGG.domain.posts.Posts;
import com.spring.kkaemiGG.domain.posts.PostsRepository;
import com.spring.kkaemiGG.web.dto.comment.CommentSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;

    public Long save(CommentSaveRequestDto requestDto) {

        Posts posts = postsRepository.findById(requestDto.getPostsId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시글이 없습니다."));

        // 만약 부모 댓글 ID가 있는 경우
        if (requestDto.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(requestDto.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID의 댓글이 없습니다."));
            Comment childComment = requestDto.toEntity();
            Comment grandParentComment = parentComment.getParentComment();

            childComment.setPosts(posts);
            childComment.setGroupOrder(grandParentComment.getChildComments().size() + 1);
            childComment.setParentComment(grandParentComment);

            commentRepository.save(grandParentComment);
            postsRepository.save(posts);

            return commentRepository.save(childComment).getId();
        }

        Comment comment = requestDto.toEntity();
        comment.setPosts(posts);
        comment.setParentComment(comment);
        postsRepository.save(posts);

        return commentRepository.save(comment).getId();
    }

}
