package com.theocean.fundering.domain.update.domain;

import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "updates")
public class Update {
    @Id
    @Column(name = "update_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long updateId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "update_order")
    private Integer updateOrder;

    public Update(Post post, User user, String title, String content) {
        this.post = post;
        this.user = user;
        this.title = title;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Update update)) return false;
        return Objects.equals(updateId, update.updateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(updateId);
    }
}