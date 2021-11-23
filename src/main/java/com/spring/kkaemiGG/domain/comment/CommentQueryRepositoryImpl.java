package com.spring.kkaemiGG.domain.comment;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.spring.kkaemiGG.domain.comment.QComment.comment;

@RequiredArgsConstructor
@Repository
public class CommentQueryRepositoryImpl implements CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findByPostId(Long postId) {
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.user).fetchJoin()
                .where(comment.post.id.eq(postId))
                .orderBy(comment.parentComment.id.asc(), comment.groupOrder.asc())
                .fetch();
    }

    public Comment findParentById(Long id) {

        QComment comment1 = new QComment("c1");
        QComment comment2 = new QComment("c2");

        return queryFactory.selectFrom(comment1)
                .leftJoin(comment1.childComments, comment2)
                .fetchJoin()
                .where(comment1.id.eq(
                        JPAExpressions.select(comment.parentComment.id)
                                .from(comment)
                                .where(comment.id.eq(id))
                ))
                .fetchOne();
    }

    @Transactional
    public void updateGroupOrder(Long parentId, Integer groupOrder) {
        queryFactory.update(comment)
                .set(comment.groupOrder, comment.groupOrder.subtract(1))
                .where(comment.parentComment.id.eq(parentId), comment.groupOrder.gt(groupOrder))
                .execute();
    }
}
