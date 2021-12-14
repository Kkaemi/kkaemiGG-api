package com.spring.kkaemiGG.domain.comment;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.spring.kkaemiGG.domain.comment.QComment.comment;

@RequiredArgsConstructor
public class CommentQueryRepositoryImpl implements CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> getCommentPage(Long postId, Pageable pageable) {
        QComment parentComment = new QComment("parentComment");

        QueryResults<Comment> queryResults = queryFactory.selectFrom(comment)
                .innerJoin(comment.user).fetchJoin()
                .leftJoin(comment.parentComment, parentComment).fetchJoin()
                .where(
                        comment.post.id.eq(postId),
                        comment.deletedDate.isNull()
                )
                .orderBy(parentComment.id.coalesce(comment.id).desc(), comment.groupOrder.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }
}
