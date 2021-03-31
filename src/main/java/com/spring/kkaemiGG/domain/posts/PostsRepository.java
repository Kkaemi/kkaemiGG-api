package com.spring.kkaemiGG.domain.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    Page<Posts> findByTitleContaining(String keyword, Pageable pageable);

    Page<Posts> findByAuthorContaining(String keyword, Pageable pageable);

}
