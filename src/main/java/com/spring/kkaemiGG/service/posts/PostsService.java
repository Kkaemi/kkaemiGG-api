package com.spring.kkaemiGG.service.posts;

import com.spring.kkaemiGG.config.auth.dto.SessionUser;
import com.spring.kkaemiGG.domain.posts.Posts;
import com.spring.kkaemiGG.domain.posts.PostsRepository;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.domain.user.UserRepository;
import com.spring.kkaemiGG.web.dto.posts.PostsListResponseDto;
import com.spring.kkaemiGG.web.dto.posts.PostsResponseDto;
import com.spring.kkaemiGG.web.dto.posts.PostsSaveRequestDto;
import com.spring.kkaemiGG.web.dto.posts.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
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
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new) // function(posts) {return new PostsListResponseDto(posts);}
                .collect(Collectors.toList());
    }
}
