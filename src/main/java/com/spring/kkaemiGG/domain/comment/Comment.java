package com.spring.kkaemiGG.domain.comment;

import com.spring.kkaemiGG.domain.BaseTimeEntity;
import com.spring.kkaemiGG.domain.post.Post;
import com.spring.kkaemiGG.domain.user.User;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> childComments;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Long groupOrder;

    @Column(name = "deleted_at")
    private LocalDateTime deletedDate;

    public static CommentBuilder builder(
            User user,
            Post post,
            String content,
            Long groupOrder
    ) {
        Assert.hasText(content, "Content must not be null, empty, or blank");
        Assert.notNull(groupOrder, "Group order must not be null");
        Assert.notNull(post, "post must not be null");
        Assert.notNull(user, "user must not be null");

        return new CommentBuilder()
                .content(content)
                .groupOrder(groupOrder)
                .post(post)
                .user(user);
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }
}
