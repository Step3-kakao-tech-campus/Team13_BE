package com.theocean.fundering.domain.post.domain;


import com.theocean.fundering.domain.account.domain.Account;
import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.post.dto.PostRequest;
import com.theocean.fundering.global.utils.AuditingFields;
import com.theocean.fundering.domain.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name="post")
public class Post extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    private Member writer;

    @ManyToOne
    private Celebrity celebrity;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Account account;

    @Column
    private String thumbnail; // 현재 임시로 String 클래스로 할당, 추후 s3와 연동할 때 리팩토링

    @Column @Min(1000)
    private int targetPrice;

    @Column @Min(0)
    private int participants;

    @Column @DateTimeFormat
    private LocalDateTime deadline;



    @Builder
    public Post(Long postId, Member writer, Celebrity celebrity, String title, String content, String thumbnail, int targetPrice, int participants, LocalDateTime deadline){
        this.postId = postId;
        this.writer = writer;
        this.celebrity = celebrity;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.targetPrice = targetPrice;
        this.participants = participants;
        this.deadline = deadline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return Objects.equals(postId, post.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId);
    }

    public void update(String title, String content, String thumbnail, int targetPrice, LocalDateTime deadline, LocalDateTime modifiedAt){
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.targetPrice = targetPrice;
        this.deadline = deadline;
        this.modifiedAt = modifiedAt;
    }
}
