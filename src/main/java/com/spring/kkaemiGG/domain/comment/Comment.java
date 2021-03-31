package com.spring.kkaemiGG.domain.comment;

import com.spring.kkaemiGG.domain.BaseTimeEntity;
import com.spring.kkaemiGG.domain.posts.Posts;
import com.spring.kkaemiGG.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    private Posts posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> childComments = new ArrayList<>();

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private Integer groupOrder;

    @Column(nullable = false)
    private Boolean deletion;

    @Builder
    public Comment(String content, String author, Integer groupOrder, Boolean deletion) {
        this.content = content;
        this.author = author;
        this.groupOrder = groupOrder;
        this.deletion = deletion;
    }

    public void setDeletion(Boolean deletion) {
        this.deletion = deletion;
    }

    public void setGroupOrder(Integer groupOrder) {
        this.groupOrder = groupOrder;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
        if (this.equals(parentComment)) {
            return;
        }
        parentComment.getChildComments().add(this);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
        posts.getComments().add(this);
    }

    public void addTargetNickname(String nickname) {
        this.content = "<p class='text-success'>@" + nickname + "</p>" + this.content;
    }
}
