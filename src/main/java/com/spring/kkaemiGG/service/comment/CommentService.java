package com.spring.kkaemiGG.service.comment;

import com.spring.kkaemiGG.config.auth.dto.SessionUser;
import com.spring.kkaemiGG.domain.comment.Comment;
import com.spring.kkaemiGG.domain.comment.CommentQueryRepository;
import com.spring.kkaemiGG.domain.comment.CommentRepository;
import com.spring.kkaemiGG.domain.posts.Posts;
import com.spring.kkaemiGG.domain.posts.PostsRepository;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.domain.user.UserRepository;
import com.spring.kkaemiGG.web.dto.comment.CommentResponseDto;
import com.spring.kkaemiGG.web.dto.comment.CommentSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private final CommentQueryRepository queryRepository;

    public Long save(CommentSaveRequestDto requestDto) {

        Posts posts = postsRepository.findById(requestDto.getPostsId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시글이 없습니다."));

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저가 없습니다."));

        // 만약 부모 댓글 ID가 있는 경우
        if (requestDto.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(requestDto.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID의 댓글이 없습니다."));
            Comment grandParentComment = parentComment.getParentComment();
            Comment childComment = requestDto.toEntity();

            childComment.setPosts(posts);
            childComment.setUser(user);
            childComment.setGroupOrder(grandParentComment.getChildComments().size() + 1);
            childComment.setParentComment(grandParentComment);

            if (!requestDto.getParentCommentId().equals(grandParentComment.getId())) {
                childComment.addTargetNickname(requestDto.getTargetNickname());
            }

            commentRepository.save(grandParentComment);
            postsRepository.save(posts);

            return commentRepository.save(childComment).getId();
        }

        Comment comment = requestDto.toEntity();
        comment.setPosts(posts);
        comment.setUser(user);
        comment.setParentComment(comment);
        postsRepository.save(posts);

        return commentRepository.save(comment).getId();
    }

    public List<CommentResponseDto> find(Long postsId, SessionUser sessionUser) {
        List<CommentResponseDto> responseDtoList = queryRepository.getCommentList(postsId);
        if (sessionUser != null) {
            responseDtoList.forEach(dto -> dto.setOwner(sessionUser.getId()));
        }
        return responseDtoList;
    }

    public void delete(Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 댓글이 없습니다."));
        Comment parentComment = comment.getParentComment();

        // 부모 댓글일 경우 상태만 변경
        if (comment.getChildComments().size() > 1) {
            comment.setDeletion(true);
            return;
        }

        if (parentComment.getDeletion()) {
            commentRepository.delete(comment);
            commentRepository.delete(parentComment);
            return;
        }

        // 삭제 되는 댓글들의 groupOrder를 하나씩 빼줌
        comment.getParentComment().getChildComments().stream()
                .filter((childComment) -> childComment.getGroupOrder() > comment.getGroupOrder())
                .forEach((childComment) -> childComment.setGroupOrder(childComment.getGroupOrder() - 1));

        commentRepository.delete(comment);

    }
}
