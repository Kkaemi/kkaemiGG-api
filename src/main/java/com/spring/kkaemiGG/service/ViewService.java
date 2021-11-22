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
        View view = viewRepository.findByIpAddress(ipAddress)
                .orElseGet(() -> View.builder(
                        post,
                        ipAddress,
                        LocalDateTime.now(),
                        1L
                ).build());

        LocalDateTime lastViewed = view.getLastViewed();

        if (lastViewed.isAfter(lastViewed.plusMinutes(30L))) {
            view.hitCount();
        }

        return viewRepository.save(view);
    }
}
