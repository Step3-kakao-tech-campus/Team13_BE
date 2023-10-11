package com.theocean.fundering.domain.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.comment.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.theocean.fundering.domain.comment.domain.QComment.comment;

@RequiredArgsConstructor
@Repository
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findCommentsByPostIdAndCursor(Long postId, Long commentOrderCursor, int limit) {
        return queryFactory.selectFrom(comment)
                .where(comment.postId.eq(postId)
                        .and(comment.commentOrder.gt(commentOrderCursor)))
                .orderBy(
                        comment.parentCommentOrder.asc(),
                        comment.commentOrder.asc()
                )
                .limit(limit)
                .fetch();
    }
}
