package com.spring.kkaemiGG.dto;

import com.spring.kkaemiGG.entity.Member;
import com.spring.kkaemiGG.entity.Role;
import lombok.*;

@Data
@NoArgsConstructor
public class MemberDto {

    private String name;
    private String email;
    private String password;

    @Builder
    public MemberDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .role(Role.USER)
                .build();
    }
}
