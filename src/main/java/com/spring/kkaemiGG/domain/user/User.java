package com.spring.kkaemiGG.domain.user;

import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public static UserBuilder builder(
            String email,
            String nickname,
            Role role
    ) {
        Assert.hasText(email, "email must not be null, empty, or blank");
        Assert.hasText(nickname, "nickname must not be null, empty, or blank");
        Assert.isInstanceOf(Role.class, role);

        return new UserBuilder()
                .email(email)
                .nickname(nickname)
                .role(role);
    }

    public User update(String nickname) {
        this.nickname = nickname;
        return this;
    }
}
