package com.theocean.fundering.domain.celebrity.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.theocean.fundering.domain.celebrity.dto.CelebFundingResponseDTO;
import com.theocean.fundering.domain.celebrity.dto.CelebListResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.theocean.fundering.domain.celebrity.domain.QCelebrity.*;
import static com.theocean.fundering.domain.post.domain.QPost.post;

@RequiredArgsConstructor
@Slf4j
public class CelebRepositoryImpl implements CelebRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<CelebFundingResponseDTO> findAllPosting(Long celebId, Long postId, Pageable pageable) {
        List<CelebFundingResponseDTO> contents = queryFactory
                .select(Projections.constructor(CelebFundingResponseDTO.class,
                        post.postId,
                        post.writer.userId,
                        post.writer.nickname,
                        post.celebrity.celebId,
                        post.celebrity.celebName,
                        post.title,
                        post.content,
                        post.participants,
                        post.targetPrice))
                .from(post)
                .where(eqPostCelebId(celebId), ltPostId(postId))
                .orderBy(post.postId.desc())
                .limit(pageable.getPageSize())
                .fetch();
        boolean hasNext = false;
        if(contents.size() > pageable.getPageSize()){
            hasNext = true;
        }
        return new SliceImpl<>(contents, pageable, hasNext);
    }

    @Override
    public Slice<CelebListResponseDTO> findAllCeleb(Long celebId, Pageable pageable) {
        List<CelebListResponseDTO> contents = queryFactory
                .select(Projections.constructor(CelebListResponseDTO.class,
                        celebrity.celebId,
                        celebrity.celebName,
                        celebrity.celebGender,
                        celebrity.celebType,
                        celebrity.celebGroup,
                        celebrity.profileImage))
                .from(celebrity)
                .where(ltCelebId(celebId))
                .orderBy(celebrity.celebId.desc())
                .limit(pageable.getPageSize())
                .fetch();
        boolean hasNext = false;
        if(contents.size() > pageable.getPageSize()){
            hasNext = true;
        }
        return new SliceImpl<>(contents, pageable, hasNext);
    }


    private BooleanExpression eqPostCelebId(Long celebId){
        return post.celebrity.celebId.eq(celebId);
    }

    private BooleanExpression ltPostId(Long cursorId){
        if (cursorId == null){
            return null;
        }
        return post.postId.lt(cursorId);
    }

    private BooleanExpression ltCelebId(Long cursorId){
        if (cursorId == null){
            return null;
        }
        return celebrity.celebId.lt(cursorId);
    }
}
