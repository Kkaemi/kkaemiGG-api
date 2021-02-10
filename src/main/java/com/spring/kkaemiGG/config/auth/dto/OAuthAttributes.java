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
    private String nickname;
    private final String email;
    private final String picture;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String nickname, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId,
                                      String userNameAttributeName,
                                      Map<String, Object> attributes) {

        return ofGoogle(userNameAttributeName, attributes);

    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {

        return OAuthAttributes.builder()
                .nickname(attributes.get("name").toString())
                .email(attributes.get("email").toString())
                .picture(attributes.get("picture").toString())
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();

    }

    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();
    }
}
