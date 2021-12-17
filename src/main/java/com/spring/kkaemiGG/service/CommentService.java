package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.domain.comment.Comment;
import com.spring.kkaemiGG.domain.comment.CommentRepository;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.exception.BadRequestException;
import com.spring.kkaemiGG.web.dto.comment.CommentListResponseDto;
import com.spring.kkaemiGG.web.dto.comment.CommentResponseDto;
import com.spring.kkaemiGG.web.dto.comment.CommentSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    public Comment findById(Long commentId) {
        return commentRepository.findByIdAndDeletedDateIsNull(commentId)
                .orElseThrow(() -> new BadRequestException("해당 아이디의 댓글을 찾을 수 없습니다."));
    }

    public Long save(User requestUser, CommentSaveRequestDto requestDto) {
        // 만약 부모 댓글 ID가 없는 경우
        if (requestDto.getParentCommentId() == null && requestDto.getGroupId() == null) {
            return saveParentComment(requestUser, requestDto);
        }

        return saveChildComment(requestUser, requestDto);
    }

    private Long saveParentComment(User requestUser, CommentSaveRequestDto requestDto) {
        Comment comment = Comment.builder(
                requestUser,
                postService.findById(requestDto.getPostId()),
                requestDto.getContent(),
                1L
        ).build();

        return commentRepository.save(comment).getId();
    }

    private Long saveChildComment(User requestUser, CommentSaveRequestDto requestDto) {
        // 일반 댓글일 경우
        if (requestDto.getParentCommentId() != null && requestDto.getGroupId() == null) {
            Comment parentComment = commentRepository.findParentCommentFetchedChildCommentsById(requestDto.getParentCommentId())
                    .orElseThrow(() -> new BadRequestException("해당 아이디의 부모댓글을 찾울 수 없습니다."));

            return getIdAfterSave(requestUser, requestDto.getPostId(), requestDto.getContent(), parentComment);
        }

        // 대댓글일 경우
        Comment parentComment = commentRepository.findParentCommentFetchedChildCommentsById(requestDto.getGroupId())
                .orElseThrow(() -> new BadRequestException("해당 아이디의 부모댓글을 찾울 수 없습니다."));

        String userNickname = commentRepository.findCommentFetchedUserById(requestDto.getParentCommentId())
                .map(comment -> comment.getUser().getNickname())
                .orElseThrow(() -> new BadRequestException("해당 아이디의 부모댓글을 찾울 수 없습니다."));

        String content = "<span class=\"text-no-wrap green lighten-4 green--text \">" +
                "@" + userNickname + "</span><br>" +
                requestDto.getContent();

        return getIdAfterSave(requestUser, requestDto.getPostId(), content, parentComment);
    }

    private Long getIdAfterSave(
            User requestUser,
            Long postId,
            String content,
            Comment parentComment
    ) {
        Comment childComment = Comment.builder(
                        requestUser,
                        postService.findById(postId),
                        content,
                        parentComment.getChildComments().stream()
                                .mapToLong(Comment::getGroupOrder)
                                .max().orElse(1L) + 1
                ).parentComment(parentComment)
                .build();

        return commentRepository.save(childComment).getId();
    }

    public void delete(Long commentId) {
        commentRepository.deleteWithChildComments(commentId, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public CommentListResponseDto findByPostId(Long postId, Pageable pageable) {
        Page<CommentResponseDto> data = commentRepository.getCommentPage(postId, pageable)
                .map(CommentResponseDto::new);

        return new CommentListResponseDto(data);
    }

    public Long update(Long commentId, String content) {
        Comment comment = findById(commentId);
        comment.updateContent(content);
        return commentRepository.save(comment).getId();
    }
}
