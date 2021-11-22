package com.spring.kkaemiGG.web.dto.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostPageRequestDto {

    private SearchType target;
    private String keyword;
}
