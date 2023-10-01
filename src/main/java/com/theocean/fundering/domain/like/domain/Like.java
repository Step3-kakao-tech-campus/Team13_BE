package com.theocean.fundering.domain.like.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="like")
public class Like {

    @Id
    @Column(name = "LIKE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "CELEBRITY_ID")
    private Long celebId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Celebrity celeb;


    @Builder
    public Like(User user, Celebrity celeb){
        this.user = user;
        this.celeb = celeb;
    }

}
