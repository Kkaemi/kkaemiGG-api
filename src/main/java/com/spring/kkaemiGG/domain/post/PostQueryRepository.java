package com.spring.kkaemiGG.domain.post;

import com.spring.kkaemiGG.web.dto.posts.PostPageResponseDto;
import com.spring.kkaemiGG.web.dto.posts.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostQueryRepository {

    Page<PostPageResponseDto.PostDto> getPage(
            SearchType target,
            String keyword,
            Pageable pageable
    );
}
