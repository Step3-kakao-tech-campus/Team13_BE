package com.theocean.fundering.domain.comment.controller;

import com.theocean.fundering.domain.comment.dto.CommentRequest;
import com.theocean.fundering.domain.comment.dto.CommentResponse;
import com.theocean.fundering.domain.comment.service.CommentService;
import com.theocean.fundering.global.jwt.userInfo.CustomUserDetails;
import com.theocean.fundering.global.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // (기능) 댓글 작성
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> createComment(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody @Valid CommentRequest.saveDTO commentRequest, @PathVariable long postId) {

        Long memberId = 1L;  // Long memberId = userDetails.getMember().getUserId();
        commentService.createComment(memberId, postId, commentRequest);

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    // (기능) 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> getComments(@PathVariable long postId,
                                         @RequestParam(required = false, defaultValue = "0") int lastRef,
                                         @RequestParam(required = false, defaultValue = "0") int lastOrder,
                                         @RequestParam(required = false, defaultValue = "5") int pageSize) {

        CommentResponse.findAllDTO response = commentService.getComments(postId, lastRef, lastOrder, pageSize);

        return ResponseEntity.ok(ApiUtils.success(response));
    }


    // (기능) 댓글 삭제
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable long postId, @PathVariable long commentId) {

        Long memberId = 1L;  // userDetails.getMember().getUserId();
        commentService.deleteComment(memberId, postId, commentId);

        return ResponseEntity.ok(ApiUtils.success(null));
    }
}