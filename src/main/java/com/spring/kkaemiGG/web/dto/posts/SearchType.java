package com.spring.kkaemiGG.web.dto.posts;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

import static com.spring.kkaemiGG.domain.post.QPost.post;
import static com.spring.kkaemiGG.domain.user.QUser.user;

@RequiredArgsConstructor
public enum SearchType {

    TITLE(post.title::containsIgnoreCase),
    AUTHOR(user.nickname::containsIgnoreCase);

    private final Function<String, BooleanExpression> function;

    public BooleanExpression getLike(String keyword) {
        return function.apply(keyword);
    }
}
