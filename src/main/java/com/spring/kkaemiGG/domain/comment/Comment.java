package com.spring.kkaemiGG.domain.comment;

import com.spring.kkaemiGG.domain.BaseTimeEntity;
import com.spring.kkaemiGG.domain.post.Post;
import com.spring.kkaemiGG.domain.user.User;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
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
    @JoinColumn(name = "GROUP_ID", nullable = false)
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<Comment> childComments;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Long groupOrder;

    public static CommentBuilder builder(
            String content,
            Long groupOrder
    ) {
        Assert.hasText(content, "Content must not be null, empty, or blank");
        Assert.notNull(groupOrder, "Group order must not be null");

        return new CommentBuilder()
                .content(content)
                .groupOrder(groupOrder);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public void changeOrder(Long groupOrder) {
        this.groupOrder = groupOrder;
    }

    public void addChildComment(Comment childComment) {
        // 자기 자신이 최상위 부모 댓글이 아닐 경우 자식 댓글을 추가하지 않음
        // depth 2 유지
        boolean isParentComment = this.id.equals(parentComment.id);
        if (!isParentComment) {
            return;
        }

        this.getChildComments().add(childComment);
    }

    public void update(String content) {
        this.content = content;
    }
}
