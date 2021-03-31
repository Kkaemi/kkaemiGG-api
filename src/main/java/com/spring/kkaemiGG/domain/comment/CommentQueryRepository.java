package com.spring.kkaemiGG.domain.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.kkaemiGG.web.dto.comment.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.spring.kkaemiGG.domain.comment.QComment.comment;

@RequiredArgsConstructor
@Repository
public class CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<CommentResponseDto> getCommentList(Long postId) {
        List<Comment> commentList = queryFactory.selectFrom(comment)
                .where(comment.posts.id.eq(postId))
                .orderBy(comment.parentComment.id.asc(), comment.groupOrder.asc())
                .fetch();

        return commentList.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

}
