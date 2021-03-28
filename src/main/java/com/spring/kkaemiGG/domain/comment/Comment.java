package com.spring.kkaemiGG.domain.comment;

import com.spring.kkaemiGG.domain.BaseTimeEntity;
import com.spring.kkaemiGG.domain.posts.Posts;
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
    private Boolean step;

    @Column(nullable = false)
    private Boolean deletion;

    @Builder
    public Comment(Posts posts, Comment parentComment, String content, String author, Boolean step, Boolean deletion) {
        this.posts = posts;
        this.parentComment = parentComment;
        this.content = content;
        this.author = author;
        this.step = step;
        this.deletion = deletion;
    }

    public void update(String content, String author) {
        this.content = content;
        this.author = author;
    }
}
