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
    private String searchType;
    private String searchKeyword;

    @Builder
    public PostsPageRequestDto(int page, String sort, String searchType, String searchKeyword) {
        this.page = page;
        this.sort = sort;
        this.searchType = searchType;
        this.searchKeyword = searchKeyword;
    }

    public boolean isSearched() {
        return StringUtils.hasText(searchType) || StringUtils.hasText(searchKeyword);
    }
}
