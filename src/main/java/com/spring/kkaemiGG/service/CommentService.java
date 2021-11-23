package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.domain.comment.Comment;
import com.spring.kkaemiGG.domain.comment.CommentQueryRepositoryImpl;
import com.spring.kkaemiGG.domain.comment.CommentRepository;
import com.spring.kkaemiGG.domain.post.Post;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.exception.BadRequestException;
import com.spring.kkaemiGG.web.dto.comment.CommentListResponseDto;
import com.spring.kkaemiGG.web.dto.comment.CommentSaveRequestDto;
import com.spring.kkaemiGG.web.dto.comment.CommentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;
    private final CommentQueryRepositoryImpl queryRepository;

    public Long save(User requestUser, CommentSaveRequestDto requestDto) throws BadRequestException {
        // 만약 부모 댓글 ID가 없는 경우
        if (requestDto.getParentCommentId() == null) {
            return saveParentComment(requestUser, requestDto);
        }

        return saveChildComment(requestUser, requestDto);
    }

    private Long saveParentComment(User requestUser, CommentSaveRequestDto requestDto) throws BadRequestException {
        Comment comment = Comment.builder(requestDto.getContent(), 1L).build();
        comment.setParentComment(comment);

        Post post = postService.addComment(requestDto.getPostId(), comment);
        User user = userService.addComment(requestUser, comment);

        comment.setPost(post);
        comment.setUser(user);
        return commentRepository.save(comment).getId();
    }

    private Long saveChildComment(User requestUser, CommentSaveRequestDto requestDto) throws BadRequestException {
        Comment parentComment = commentRepository.findParentCommentById(requestDto.getParentCommentId())
                .orElseThrow(() -> new BadRequestException("해당 아이디의 부모댓글을 찾울 수 없습니다."));
        Comment childComment = Comment.builder(
                requestDto.getContent(),
                parentComment.getChildComments().stream()
                        .mapToLong(Comment::getGroupOrder)
                        .max().orElseThrow() + 1
        ).build();
        Post post = postService.addComment(requestDto.getPostId(), childComment);
        User user = userService.addComment(requestUser, childComment);

        childComment.setPost(post);
        childComment.setUser(user);
        childComment.setParentComment(parentComment);
        parentComment.addChildComment(childComment);

        commentRepository.save(parentComment);
        return commentRepository.save(childComment).getId();
    }

    public void delete(Long commentId) throws BadRequestException {
//        Comment parentComment = queryRepository.findParentById(commentId);
//        Comment comment = parentComment.getChildComments().stream()
//                .filter(childComment -> childComment.getId().equals(commentId))
//                .findFirst().orElseThrow(() -> new IllegalArgumentException("잘못된 ID 입니다."));

        // 부모 댓글일 경우 상태만 변경
//        if (comment.equals(parentComment) && parentComment.getChildComments().size() > 1) {
//            parentComment.setDeletion(true);
//            return;
//        }


        // 부모 댓글의 상태가 삭제된 상태고 자식 댓글이 하나밖에 없으면 자식댓글과 부모댓글 같이 삭제
//        if (parentComment.getDeletion() && parentComment.getChildComments().size() == 2) {
//            commentRepository.deleteById(commentId);
//            commentRepository.delete(parentComment);
//            return;
//        }

        // 삭제 되는 댓글 보다 뒤에 있는 댓글들의 groupOrder를 하나씩 빼줌
//        queryRepository.updateGroupOrder(parentComment.getId(), comment.getGroupOrder());

        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new BadRequestException("해당 아이디의 댓글을 찾을 수 없습니다."));

        commentRepository.delete(comment);
    }

    public CommentListResponseDto findByPostId(Long postId, User user) {
        List<CommentListResponseDto.CommentDto> data = commentRepository.findByPostId(postId).stream()
                .map(comment -> new CommentListResponseDto.CommentDto(comment, user))
                .collect(Collectors.toList());

        return new CommentListResponseDto(data);
    }

    public Long update(Long commentId, CommentUpdateRequestDto requestDto) throws BadRequestException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException("해당 아이디의 댓글을 찾을 수 없습니다."));

        comment.update(requestDto.getContent());
        return commentRepository.save(comment).getId();
    }
}
