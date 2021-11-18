package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.auth.dto.SessionUser;
import com.spring.kkaemiGG.domain.post.Post;
import com.spring.kkaemiGG.domain.post.PostsQueryRepository;
import com.spring.kkaemiGG.domain.post.PostsRepository;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.domain.user.UserRepository;
import com.spring.kkaemiGG.web.dto.posts.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostsRepository postsRepository;
    private final PostsQueryRepository queryRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(PostSaveRequestDto requestDto, SessionUser sessionUser) {

        User user = userRepository.findByEmail(sessionUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        Post post = requestDto.toEntityWithUser(user);

        user.getPosts().add(post);

        userRepository.save(user);
        postsRepository.save(post);

        return post.getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        return id;
    }

    public void delete(Long id) {
        postsRepository.deleteById(id);
    }

    public PostsUpdateResponseDto findById(Long id) {
        return postsRepository.findById(id)
                .map(PostsUpdateResponseDto::new)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시물이 없습니다."));
    }

    @Transactional
    public PostsResponseDto findByIdWithSession(Long id, SessionUser sessionUser) {

        PostsResponseDto responseDto = postsRepository.findById(id)
                .map(PostsResponseDto::new)
                .orElseGet(() -> new PostsResponseDto(-1L));

        if (sessionUser != null && sessionUser.getId().equals(responseDto.getUserId())) {
            responseDto.setOwner(true);
        }

        return responseDto;
    }

    @Transactional(readOnly = true)
    public Page<PostsPageResponseDto> findPage(PostsPageRequestDto requestDto) {

        PageRequest pageRequest = PageRequest
                .of(requestDto.getPage(), 20);

        // 인기순 정렬도 구현 예정

        return queryRepository.findDynamic(requestDto, pageRequest);
    }
}
