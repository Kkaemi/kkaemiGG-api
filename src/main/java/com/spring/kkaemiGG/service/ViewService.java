package com.spring.kkaemiGG.service;

import com.spring.kkaemiGG.domain.post.Post;
import com.spring.kkaemiGG.domain.view.View;
import com.spring.kkaemiGG.domain.view.ViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
public class ViewService {

    private final ViewRepository viewRepository;

    public View saveOrUpdate(String ipAddress, Post post) {
        LocalDateTime now = LocalDateTime.now();
        View view = viewRepository.findByIpAddressAndPost(ipAddress, post)
                .orElseGet(() -> View.builder(
                        post,
                        ipAddress,
                        now,
                        1L
                ).build());

        if (now.isAfter(view.getLastViewed().plusMinutes(30L))) {
            view.hitCount(now);
        }

        return viewRepository.save(view);
    }
}
