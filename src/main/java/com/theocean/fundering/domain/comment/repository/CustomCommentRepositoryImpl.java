package com.theocean.fundering.domain.comment.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.comment.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.theocean.fundering.domain.comment.domain.QComment.comment;

@RequiredArgsConstructor
@Repository
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    public List<Comment> getCommentList(Long postId, int lastRef, int lastOrder, int pageSize) {
        BooleanExpression whereCondition = comment.postId.eq(postId)
                .andAnyOf(
                        comment.ref.goe(lastRef).and(comment.refOrder.gt(lastOrder)),
                        comment.ref.gt(lastRef)
                );

        return queryFactory.selectFrom(comment)
                .where(whereCondition)
                .orderBy(comment.ref.asc(), comment.refOrder.asc())
                .limit(pageSize)
                .fetch();
    }
}
