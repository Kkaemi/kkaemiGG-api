package com.spring.kkaemiGG.domain.user;

import com.spring.kkaemiGG.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Post> posts;

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

    public void writePost(Post post) {
        this.posts.add(post);
    }
}
