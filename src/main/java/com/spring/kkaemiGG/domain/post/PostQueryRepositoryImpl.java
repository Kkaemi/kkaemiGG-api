package com.spring.kkaemiGG.domain.post;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.kkaemiGG.web.dto.posts.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import static com.spring.kkaemiGG.domain.post.QPost.post;
import static com.spring.kkaemiGG.domain.user.QUser.user;

@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> getPage(
            SearchType target,
            String keyword,
            Pageable pageable
    ) {
        QueryResults<Post> results = queryFactory
                .selectFrom(post)
                .innerJoin(post.user, user).fetchJoin()

                .where(
                        getLike(target, keyword),
                        post.deletedDate.isNull()
                )

                .orderBy(post.id.desc())

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression getLike(SearchType searchType, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        return searchType.getLike(keyword);
    }
}
