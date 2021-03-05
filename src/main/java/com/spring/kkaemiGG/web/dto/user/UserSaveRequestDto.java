package com.spring.kkaemiGG.web.dto.user;

import com.spring.kkaemiGG.domain.user.Role;
import com.spring.kkaemiGG.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSaveRequestDto {

    private String nickname;
    private String email;
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    @Builder
    public UserSaveRequestDto(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public User toEntity()   {
        return User.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .picture("/img/image_not_found.png")
                .role(Role.USER)
                .build();
    }
}
