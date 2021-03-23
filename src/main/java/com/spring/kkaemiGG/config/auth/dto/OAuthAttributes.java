package com.spring.kkaemiGG.config.auth.dto;

import com.spring.kkaemiGG.domain.user.Role;
import com.spring.kkaemiGG.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String nickname;
    private final String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String nickname, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
    }

    public static OAuthAttributes of(String registrationId,
                                      String userNameAttributeName,
                                      Map<String, Object> attributes) {

        return ofGoogle(userNameAttributeName, attributes);

    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {

        String sub = attributes.get(userNameAttributeName).toString();

        return OAuthAttributes.builder()
                .email(sub + "@google.com")
                .nickname("kkaemi" + sub)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();

    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .role(Role.USER)
                .build();
    }
}
