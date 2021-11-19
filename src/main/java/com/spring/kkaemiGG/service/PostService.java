package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.auth.dto.SessionUser;
import com.spring.kkaemiGG.domain.post.Post;
import com.spring.kkaemiGG.domain.post.PostsQueryRepository;
import com.spring.kkaemiGG.domain.post.PostRepository;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.domain.user.UserRepository;
import com.spring.kkaemiGG.exception.BadRequestException;
import com.spring.kkaemiGG.web.dto.posts.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostsQueryRepository queryRepository;
    private final UserRepository userRepository;

    public Long save(PostSaveRequestDto requestDto, SessionUser sessionUser) {

        User user = userRepository.findByEmail(sessionUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        Post post = requestDto.toEntityWithUser(user);

        user.getPosts().add(post);

        userRepository.save(user);
        postRepository.save(post);

        return post.getId();
    }

    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        return id;
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public PostsUpdateResponseDto findById(Long id) {
        return postRepository.findById(id)
                .map(PostsUpdateResponseDto::new)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시물이 없습니다."));
    }

    public PostsResponseDto getPostById(Long id) throws BadRequestException {
        // TODO
        // 조회수 올리는 로직

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("해당 아이디의 게시물을 찾울 수 없습니다."));

        return new PostsResponseDto(post);
    }

    @Transactional(readOnly = true)
    public Page<PostsPageResponseDto> findPage(PostsPageRequestDto requestDto) {

        PageRequest pageRequest = PageRequest
                .of(requestDto.getPage(), 20);

        // 인기순 정렬도 구현 예정

        return queryRepository.findDynamic(requestDto, pageRequest);
    }
}
