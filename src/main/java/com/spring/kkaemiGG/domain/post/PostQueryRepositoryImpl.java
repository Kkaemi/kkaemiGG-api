package com.spring.kkaemiGG.domain.post;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.kkaemiGG.web.dto.posts.PostPageResponseDto;
import com.spring.kkaemiGG.web.dto.posts.QPostPageResponseDto_PostDto;
import com.spring.kkaemiGG.web.dto.posts.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import static com.spring.kkaemiGG.domain.comment.QComment.comment;
import static com.spring.kkaemiGG.domain.post.QPost.post;
import static com.spring.kkaemiGG.domain.user.QUser.user;

@RequiredArgsConstructor
@Repository
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostPageResponseDto.PostDto> getPage(
            SearchType target,
            String keyword,
            Pageable pageable
    ) {
        QueryResults<PostPageResponseDto.PostDto> results = queryFactory
                .select(new QPostPageResponseDto_PostDto(
                        post.id,
                        user.nickname,
                        post.title,
                        comment.id.count(),
                        post.createdDate
                )).from(post)
                .leftJoin(user).on(post.user.id.eq(user.id))
                .leftJoin(comment).on(post.id.eq(comment.post.id))

                .where(getLike(target, keyword))

                .groupBy(post.id)

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
