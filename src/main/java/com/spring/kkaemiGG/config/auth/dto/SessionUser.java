package com.spring.kkaemiGG.config.auth.dto;

import com.spring.kkaemiGG.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private final Long id;
    private final String email;
    private final String nickname;

    public SessionUser(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}
