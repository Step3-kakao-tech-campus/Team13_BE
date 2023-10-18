package com.theocean.fundering.domain.comment.dto;

import com.theocean.fundering.domain.comment.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;


public class CommentResponse {

    // 전체 댓글 조회 DTO
    @Getter
    public static class findAllDTO {
        private final List<commentDTO> comments;
        private final Integer refCursor;
        private final Integer orderCursor;
        private final boolean isLastPage;

        public findAllDTO(List<commentDTO> comments, Integer refCursor, Integer orderCursor, boolean isLastPage) {
            this.comments = comments;
            this.refCursor = refCursor;
            this.orderCursor = orderCursor;
            this.isLastPage = isLastPage;
        }

        public boolean getIsLastPage() {
            return isLastPage;
        }
    }

    // findAllDTO의 내부에 들어갈 댓글 정보 DTO
    @Getter
    public static class commentDTO {
        private final Long commentId;
        private final Long writerId;
        private final String writerName;
        private final String writerProfile;
        private final String content;
        private final int ref;
        private final int refOrder;
        private final int depth;
        private final boolean isDeleted;
        private final long createdAt;

        public commentDTO(Comment comment, String writerName, String writerProfile) {
            this.commentId = comment.getCommentId();
            this.writerId = comment.getWriterId();
            this.writerName = writerName;
            this.writerProfile = writerProfile;
            this.content = comment.getContent();
            this.ref = comment.getRef();
            this.refOrder = comment.getRefOrder();
            this.depth = comment.getDepth();
            this.isDeleted = comment.isDeleted();
            this.createdAt = toEpochSecond(comment.getCreatedAt());
        }

        private long toEpochSecond(LocalDateTime localDateTime) {
            return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        }
        public boolean getIsDeleted() {
            return isDeleted;
        }
    }
}