package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.domain.post.Post;
import com.spring.kkaemiGG.domain.post.PostsQueryRepository;
import com.spring.kkaemiGG.domain.post.PostRepository;
import com.spring.kkaemiGG.domain.user.Role;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.domain.user.UserRepository;
import com.spring.kkaemiGG.exception.BadRequestException;
import com.spring.kkaemiGG.web.dto.posts.PostsResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostsQueryRepository postsQueryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    @DisplayName("아이디로 게시글 찾기")
    @Test
    void getPostById() {
        User user = User.builder("kitys2k3@test.com", "nickname", Role.USER)
                .id(1L)
                .build();
        Post post = Post.builder("title", "content")
                .id(1L)
                .user(user)
                .comments(Collections.emptyList())
                .build();
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        PostsResponseDto result = null;
        try {
            result = postService.getPostById(1L);
            assertThat(result.getPostsId()).isEqualTo(post.getId());
            verify(postRepository).findById(anyLong());
        } catch (BadRequestException e) {
            e.printStackTrace();
        }

    }
}