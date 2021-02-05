package com.spring.kkaemiGG.service.posts;

import com.spring.kkaemiGG.domain.posts.Posts;
import com.spring.kkaemiGG.domain.posts.PostsRepository;
import com.spring.kkaemiGG.web.dto.PostsListResponseDto;
import com.spring.kkaemiGG.web.dto.PostsResponseDto;
import com.spring.kkaemiGG.web.dto.PostsSaveRequestDto;
import com.spring.kkaemiGG.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {

        Posts posts = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id)
        );

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {

        Posts entity = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id)
        );

        return new PostsResponseDto(entity);

    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                // posts -> new PostsListResponseDto(posts) == PostsListResponseDto::new
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }
}
