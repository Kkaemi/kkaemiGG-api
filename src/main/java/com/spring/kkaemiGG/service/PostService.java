package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.domain.post.Post;
import com.spring.kkaemiGG.domain.post.PostRepository;
import com.spring.kkaemiGG.domain.user.User;
import com.spring.kkaemiGG.domain.view.View;
import com.spring.kkaemiGG.exception.BadRequestException;
import com.spring.kkaemiGG.web.dto.posts.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final ViewService viewService;

    public Post findById(Long postId) {
        return postRepository.findByIdAndDeletedDateIsNull(postId)
                .orElseThrow(() -> new BadRequestException("해당 아이디의 게시물을 찾울 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public PostPageResponseDto getPage(PostPageRequestDto requestDto, Pageable pageable) {
        Page<PostPageResponseDto.PostDto> data = postRepository.getPage(
                requestDto.getTarget(),
                requestDto.getKeyword(),
                pageable
        );

        return new PostPageResponseDto(data);
    }

    public Long save(PostSaveRequestDto requestDto, User user) {
        return postRepository.save(requestDto.toEntity(user)).getId();
    }

    public PostResponseDto viewPost(Long id, String ipAddress) {
        Post post = postRepository.fetchUserAndViews(id)
                .orElseThrow(() -> new BadRequestException("해당 아이디의 게시물을 찾울 수 없습니다."));
        View view = viewService.saveOrUpdate(ipAddress, post);

        post.getViews().add(view);

        return new PostResponseDto(postRepository.save(post));
    }

    public Long update(Long postId, PostUpdateRequestDto requestDto) throws BadRequestException {
        Post post = findById(postId);

        post.update(requestDto.getTitle(), requestDto.getContent());

        return postRepository.save(post).getId();
    }

    public void delete(Long postId) {
        Post post = findById(postId);
        post.delete();
        postRepository.save(post);
    }
}
