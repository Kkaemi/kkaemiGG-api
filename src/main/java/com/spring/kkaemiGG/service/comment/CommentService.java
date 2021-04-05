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
            Comment parentComment = queryRepository.findParentById(requestDto.getParentCommentId());
            Comment childComment = requestDto.toEntity();

            childComment.setPosts(posts);
            childComment.setUser(user);
            childComment.setGroupOrder(parentComment.getChildComments().size() + 1);
            childComment.setParentComment(parentComment);

            if (!requestDto.getParentCommentId().equals(parentComment.getId())) {
                childComment.addTargetNickname(requestDto.getTargetNickname());
            }

            commentRepository.save(parentComment);
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

        Comment parentComment = queryRepository.findParentById(commentId);
        Comment comment = parentComment.getChildComments().stream()
                .filter(childComment -> childComment.getId().equals(commentId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("잘못된 ID 입니다."));

        // 부모 댓글일 경우 상태만 변경
        if (comment.equals(parentComment) && parentComment.getChildComments().size() > 1) {
            parentComment.setDeletion(true);
            return;
        }


        // 부모 댓글의 상태가 삭제된 상태고 자식 댓글이 하나밖에 없으면 자식댓글과 부모댓글 같이 삭제
        if (parentComment.getDeletion() && parentComment.getChildComments().size() == 2) {
            commentRepository.deleteById(commentId);
            commentRepository.delete(parentComment);
            return;
        }

        // 삭제 되는 댓글 보다 뒤에 있는 댓글들의 groupOrder를 하나씩 빼줌
        queryRepository.updateGroupOrder(parentComment.getId(), comment.getGroupOrder());

        commentRepository.delete(comment);

    }
}
