package com.spring.kkaemiGG.domain.view;

import com.spring.kkaemiGG.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViewRepository extends JpaRepository<View, Long> {

    Optional<View> findByIpAddressAndPost(String ipAddress, Post post);
}
