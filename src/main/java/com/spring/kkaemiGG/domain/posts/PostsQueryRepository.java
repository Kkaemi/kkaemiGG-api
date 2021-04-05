package com.spring.kkaemiGG.domain.posts;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.kkaemiGG.web.dto.posts.PostsPageRequestDto;
import com.spring.kkaemiGG.web.dto.posts.PostsPageResponseDto;
import com.spring.kkaemiGG.web.dto.posts.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import static com.spring.kkaemiGG.domain.comment.QComment.comment;
import static com.spring.kkaemiGG.domain.posts.QPosts.posts;

@RequiredArgsConstructor
@Repository
public class PostsQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<PostsPageResponseDto> findDynamic(PostsPageRequestDto requestDto, Pageable pageable) {
        QueryResults<PostsPageResponseDto> results = queryFactory
                .select(Projections.constructor(PostsPageResponseDto.class,
                        posts,
                        comment.id.count()))
                .from(posts)
                .leftJoin(posts.comments, comment)
                .where(eqKeyword(requestDto.getTarget(), requestDto.getKeyword()))
                .groupBy(posts.id)
                .orderBy(posts.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression eqKeyword(SearchType searchType, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        return searchType.getEq(keyword);
    }

}
