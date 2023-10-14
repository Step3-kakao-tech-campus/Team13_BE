package com.theocean.fundering.domain.news.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.news.domain.News;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.theocean.fundering.domain.news.domain.QNews.news;

@RequiredArgsConstructor
@Repository
public class CustomNewsRepositoryImpl implements CustomNewsRepository {

    private final JPAQueryFactory queryFactory;

    public List<News> getNewsList(long postId, long cursor, int pageSize) {
        BooleanExpression whereCondition = news.postId.eq(postId);
        if (cursor > 0) {
            whereCondition = whereCondition.and(news.newsId.lt(cursor));
        }

        return queryFactory.selectFrom(news)
                .where(whereCondition)
                .orderBy(news.newsId.desc())
                .limit(pageSize)
                .fetch();
    }
}