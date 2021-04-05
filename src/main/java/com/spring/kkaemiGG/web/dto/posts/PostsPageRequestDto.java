package com.spring.kkaemiGG.web.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsPageRequestDto {

    private Integer page;
    private String sort;
    private SearchType target;
    private String keyword;

    @Builder
    public PostsPageRequestDto(Integer page, String sort, SearchType target, String keyword) {
        this.page = page;
        this.sort = sort;
        this.target = target;
        this.keyword = keyword;
    }

}
