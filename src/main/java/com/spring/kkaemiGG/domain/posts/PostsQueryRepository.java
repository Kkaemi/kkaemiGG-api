package com.spring.kkaemiGG.domain.posts;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostsQueryRepository {

    private final JPAQueryFactory queryFactory;

}
