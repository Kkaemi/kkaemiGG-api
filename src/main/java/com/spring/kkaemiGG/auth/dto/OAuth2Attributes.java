package com.spring.kkaemiGG.auth.dto;

import com.spring.kkaemiGG.domain.user.Role;
import com.spring.kkaemiGG.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class OAuth2Attributes {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String nickname;
    private final String email;

    public static OAuth2Attributes of(
            String registrationId,
            String userNameAttributeName,
            Map<String, Object> attributes
    ) {
        if (registrationId.equalsIgnoreCase("naver")) {
            return ofNaver("id", attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuth2Attributes ofGoogle(
            String userNameAttributeName,
            Map<String, Object> attributes
    ) {
        return new OAuth2Attributes(
                attributes,
                userNameAttributeName,
                (String) attributes.get("name"),
                (String) attributes.get("email")
        );
    }

    private static OAuth2Attributes ofNaver(
            String userNameAttributeName,
            Map<String, Object> attributes
    ) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return new OAuth2Attributes(
                response,
                userNameAttributeName,
                (String) response.get("nickname"),
                (String) response.get("email")
        );
    }

    public User toEntity() {
        return User.builder(
                email,
                nickname,
                Role.USER
        ).build();
    }
}
