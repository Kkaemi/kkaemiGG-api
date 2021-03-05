package com.spring.kkaemiGG.web.dto.user;

import com.spring.kkaemiGG.domain.user.Role;
import com.spring.kkaemiGG.domain.user.User;
import lombok.Getter;

@Getter
public class UsersResponseDto {

    private final Long id;
    private final String nickname;
    private final String email;
    private final String password;
    private final String picture;
    private final Role role;

    public UsersResponseDto(User entity) {
        this.id = entity.getId();
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
        this.password = entity.getPassword();
        this.picture = entity.getPicture();
        this.role = entity.getRole();
    }
}
