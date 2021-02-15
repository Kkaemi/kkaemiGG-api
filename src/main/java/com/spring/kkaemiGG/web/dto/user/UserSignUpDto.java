package com.spring.kkaemiGG.web.dto.user;

import com.spring.kkaemiGG.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignUpDto {

    private String nickname;
    private String email;
    private String password;

    @Builder
    public UserSignUpDto(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public User toEntity()   {
        return User.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .build();
    }
}
