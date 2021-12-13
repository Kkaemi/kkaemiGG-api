package com.spring.kkaemiGG.domain.comment;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.spring.kkaemiGG.domain.comment.QComment.comment;

@RequiredArgsConstructor
public class CommentQueryRepositoryImpl implements CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> getCommentPage(Long postId, Pageable pageable) {
        QComment parentComment = new QComment("parentComment");

        QueryResults<Comment> queryResults = queryFactory.selectFrom(comment)
                .leftJoin(comment.parentComment, parentComment).fetchJoin()
                .leftJoin(comment.user).fetchJoin()
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

    @Transactional
    @Override
    public void deleteWithChildComments(List<Long> commentIdList) {
        queryFactory.update(comment)
                .set(comment.deletedDate, LocalDateTime.now())
                .where(comment.id.in(commentIdList))
                .execute();
    }
}
