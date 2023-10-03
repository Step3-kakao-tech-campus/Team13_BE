package com.theocean.fundering.domain.like.domain;


import com.theocean.fundering.domain.celebrity.domain.Celebrity;
import com.theocean.fundering.domain.user.domain.User;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CELEB_ID")
    private Celebrity celeb;


    @Builder
    public Like(User user, Celebrity celeb){
        this.user = user;
        this.celeb = celeb;
    }

}
