package com.spring.kkaemiGG.web.dto.posts;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.function.Function;


import static com.spring.kkaemiGG.domain.user.QUser.user;

public enum SearchType {

//    TITLE(posts.title::containsIgnoreCase),
    AUTHOR(user.nickname::containsIgnoreCase);

    private final Function<String, BooleanExpression> expressionFunction;

    SearchType(Function<String, BooleanExpression> expressionFunction) {
        this.expressionFunction = expressionFunction;
    }

    public BooleanExpression getEq(String keyword) {
        return expressionFunction.apply(keyword);
    }
}
