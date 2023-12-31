package com.theocean.fundering.domain.post.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "heart",
        uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "member_id"}))
@IdClass(Heart.PK.class)
@Entity
public class Heart {
    @Id
    @Column(name = "post_id", insertable = false, updatable = false)
    private Long postId;

    @Id
    @Column(name = "member_id", insertable = false, updatable = false)
    private Long memberId;


    //Heart 관계의 유일성을 위한 복합키 설정
    public static class PK implements Serializable {
        Long postId;
        Long memberId;
    }
}
