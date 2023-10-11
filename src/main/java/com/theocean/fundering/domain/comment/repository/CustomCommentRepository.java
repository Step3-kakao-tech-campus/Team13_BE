package com.theocean.fundering.domain.comment.repository;

import com.theocean.fundering.domain.comment.domain.Comment;

import java.util.List;

public interface CustomCommentRepository {
    List<Comment> findCommentsByPostIdAndCursor(Long postId, Long commentOrderCursor, int limit);
}
