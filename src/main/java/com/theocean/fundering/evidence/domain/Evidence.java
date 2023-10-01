package com.theocean.fundering.evidence.domain;

import com.theocean.fundering.withdrawal.domain.Withdrawal;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "evidence")
public class Evidence {
    @Id
    @Column(name = "evidence_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evidenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "withdrawal_id")
    private Withdrawal withdrawal;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "evidence_image")
    private String evidenceImage;

    @Builder
    public Evidence(Long postId, Long userId, String evidenceImage) {
        this.postId = postId;
        this.userId = userId;
        this.evidenceImage = evidenceImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Evidence evidence)) return false;
        return Objects.equals(evidenceId, evidence.evidenceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(evidenceId);
    }
}
