package com.spring.kkaemiGG.domain.post;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.kkaemiGG.web.dto.posts.PostPageResponseDto;
import com.spring.kkaemiGG.web.dto.posts.QPostPageResponseDto_PostDto;
import com.spring.kkaemiGG.web.dto.posts.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;

import static com.spring.kkaemiGG.domain.comment.QComment.comment;
import static com.spring.kkaemiGG.domain.post.QPost.post;
import static com.spring.kkaemiGG.domain.user.QUser.user;
import static com.spring.kkaemiGG.domain.view.QView.view;

@RequiredArgsConstructor
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
                        post.title,
                        comment.id.count(),
                        user.nickname,
                        post.createdDate,
                        view.count.sum().coalesce(0L)
                )).from(post)
                .innerJoin(post.user, user)
                .leftJoin(post.comments, comment)
                .leftJoin(post.views, view)

                .where(
                        getLike(target, keyword),
                        post.deletedDate.isNull()
                )

                .groupBy(post.id)

                .orderBy(getOrderSpecifier(pageable.getSort()))

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

    private OrderSpecifier<?> getOrderSpecifier(Sort sort) {
        if (sort.isUnsorted()) {
            return post.id.desc();
        }

        Map<String, ComparableExpressionBase<?>> propertyMap = Map.of(
                "postId", post.id,
                "title", post.title,
                "userNickname", user.nickname,
                "createdDate", post.createdDate,
                "views", view.count.sum()
        );

        return propertyMap.keySet().stream()
                .map(property -> {
                    Sort.Order order = sort.getOrderFor(property);

                    if (order == null) {
                        return null;
                    }

                    return order.isAscending()
                            ? propertyMap.get(property).asc()
                            : propertyMap.get(property).desc();
                }).filter(Objects::nonNull)
                .findFirst()
                .orElseGet(post.id::desc);
    }
}
