package com.spring.kkaemiGG.web.dto.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor
public class PostsPageRequestDto {

    private int page;
    private String sort;
    private String target;
    private String keyword;

    @Builder
    public PostsPageRequestDto(int page, String sort, String target, String keyword) {
        this.page = page;
        this.sort = sort;
        this.target = target;
        this.keyword = keyword;
    }

    public boolean isSearched() {
        return StringUtils.hasText(target) && StringUtils.hasText(keyword);
    }
}
