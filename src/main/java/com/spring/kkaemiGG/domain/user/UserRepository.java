package com.spring.kkaemiGG.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u " +
            "FROM User u " +
            "LEFT JOIN FETCH u.posts " +
            "WHERE u.id = :userId")
    Optional<User> fetchPostsById(@Param("userId") Long userId);

    Optional<User> findByEmail(String email);
}
