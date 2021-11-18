package com.spring.kkaemiGG.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTitleContaining(String keyword, Pageable pageable);

    Page<Post> findByAuthorContaining(String keyword, Pageable pageable);

}
