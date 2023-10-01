package com.theocean.fundering.admin.domain;

import lombok.*;

import jakarta.persistence.*;

import com.theocean.fundering.user.domain.User;
import com.theocean.fundering.post.domain.Post;
import com.theocean.fundering.account.domain.Account;

@Entity
@Table(name = "Admin")
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adminId")
    @EqualsAndHashCode.Include
    private Long id;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    // 게시물
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    // 계좌
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    // 생성자
    @Builder
    public Admin(User user, Post post, Account account) {
        this.user = user;
        this.post = post;
        this.account = account;
    }

}
