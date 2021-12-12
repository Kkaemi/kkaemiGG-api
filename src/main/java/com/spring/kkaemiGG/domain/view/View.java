package com.spring.kkaemiGG.domain.view;

import com.spring.kkaemiGG.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VIEW_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private LocalDateTime lastViewed;

    @Column(nullable = false)
    private Long count;

    public static ViewBuilder builder(
            Post post,
            String ipAddress,
            LocalDateTime lastViewed,
            Long count
    ) {
        Assert.notNull(post, "Post must not be null");
        Assert.hasText(ipAddress, "IP Address must not be null, empty, or blank");
        Assert.notNull(lastViewed, "Last Viewed must not be null");
        Assert.notNull(count, "Count must not be null");

        return new ViewBuilder()
                .post(post)
                .ipAddress(ipAddress)
                .lastViewed(lastViewed)
                .count(count);
    }

    public void hitCount(LocalDateTime now) {
        this.count += 1;
        this.lastViewed = now;
    }
}
