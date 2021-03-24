package com.spring.kkaemiGG.service.posts;

import com.spring.kkaemiGG.config.auth.dto.SessionUser;
import com.spring.kkaemiGG.domain.posts.Posts;
import com.spring.kkaemiGG.domain.posts.PostsRepository;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.domain.user.UserRepository;
import com.spring.kkaemiGG.web.dto.posts.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto, SessionUser sessionUser) {

        User user = userRepository.findByEmail(sessionUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        Posts posts = requestDto.toEntityWithUser(user);

        user.getPosts().add(posts);

        userRepository.save(user);
        postsRepository.save(posts);

        return posts.getId();
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
    public Page<PostsListResponseDto> findByRequest(PostsPageRequestDto requestDto) {

        PageRequest pageRequest = PageRequest
                .of(requestDto.getPage(), 20, Sort.Direction.DESC, "createdDate");

        if (requestDto.isSearched()) {

            if (requestDto.getTarget().equals("title")) {
                return postsRepository.findByTitleContaining(requestDto.getKeyword(), pageRequest)
                        .map(PostsListResponseDto::new);
            }

            if (requestDto.getTarget().equals("author")) {
                return postsRepository.findByAuthorContaining(requestDto.getKeyword(), pageRequest)
                        .map(PostsListResponseDto::new);
            }
        }

        // 인기순 정렬도 구현 예정

        return postsRepository.findAll(pageRequest).map(PostsListResponseDto::new);
    }
}
