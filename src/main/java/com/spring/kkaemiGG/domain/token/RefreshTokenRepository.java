package com.spring.kkaemiGG.domain.token;

import com.spring.kkaemiGG.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("SELECT r " +
            "FROM RefreshToken r " +
            "WHERE r.user = :user")
    Optional<RefreshToken> findByUser(@Param("user") User user);
}
