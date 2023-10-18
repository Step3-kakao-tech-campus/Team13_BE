package com.theocean.fundering.domain.comment.repository;

import com.theocean.fundering.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.postId = :postId AND c.ref = :group AND c.refOrder = 0")
    Optional<Comment> getParentComment(@Param("postId") Long postId, @Param("group") Integer group);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.postId = :postId AND c.ref = :group")
    int countReplies(@Param("postId") Long postId, @Param("group") Integer group);

    @Query("SELECT COALESCE(MAX(c.ref), 0) FROM Comment c WHERE c.postId = :postId")
    int findMaxGroup(@Param("postId") Long postId);
}
