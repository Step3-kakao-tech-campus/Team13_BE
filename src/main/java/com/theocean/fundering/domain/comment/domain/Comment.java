package com.theocean.fundering.domain.comment.domain;

import com.theocean.fundering.global.utils.AuditingFields;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

/*
 로그인한 사용자는 댓글 작성 부분에 입력을 하여 댓글을 작성할 수 있다.
 또한 댓글 옆에 댓글 작성 버튼을 클릭하여 대댓글을 작성할 수 있다.
 대댓글에 대한 대댓글은 허용되지 않는다.
*/

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "comment")
@SQLDelete(sql = "UPDATE comment SET is_deleted = true WHERE comment_id = ?")
public class Comment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private Long writerId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private int ref;  // 댓글 조회시 분류를 위한 그룹핑 - 대댓글은 원댓글의 필드값을 따라가고, 원댓글의 경우 자신의 PK값을 갖는다

    @Column(nullable = false)
    private int refOrder;  // 같은 그룹내에서 댓글 순서 - 생성순, 원댓글은 0

    @Column(nullable = false)
    private int depth;  // 화면에 표시되는 들여쓰기 수준 - 원댓글 0부터 시작

    @Builder
    public Comment(Long writerId, Long postId, String content) {
        this.writerId = writerId;
        this.postId = postId;
        this.content = content;
        this.isDeleted = false;
    }

    public void updateCommentProperties(int ref, int refOrder, int depth) {
        this.ref = ref;
        this.refOrder = refOrder;
        this.depth = depth;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return Objects.equals(commentId, comment.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }
}
