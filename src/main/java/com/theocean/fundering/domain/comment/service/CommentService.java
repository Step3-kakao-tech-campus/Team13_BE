package com.theocean.fundering.domain.comment.service;

import com.theocean.fundering.domain.comment.domain.Comment;
import com.theocean.fundering.domain.comment.repository.CustomCommentRepositoryImpl;
import com.theocean.fundering.domain.member.domain.Member;
import com.theocean.fundering.domain.comment.dto.CommentRequest;
import com.theocean.fundering.domain.comment.dto.CommentResponse;
import com.theocean.fundering.domain.comment.repository.CommentRepository;
import com.theocean.fundering.domain.member.repository.MemberRepository;
import com.theocean.fundering.domain.post.repository.PostRepository;
import com.theocean.fundering.global.errors.exception.Exception400;
import com.theocean.fundering.global.errors.exception.Exception403;
import com.theocean.fundering.global.errors.exception.Exception404;
import com.theocean.fundering.global.errors.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CustomCommentRepositoryImpl customCommentRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private static final int REPLY_LIMIT = 30;


    // (기능) 댓글 작성
    @Transactional
    public void createComment(Long memberId, Long postId, CommentRequest.saveDTO request) {

        validateMemberAndPost(memberId, postId);

        Integer ref = request.getRef();
        String content = request.getContent();

        Comment newComment = buildBaseComment(memberId, postId, content);

        if (ref != null) {
            createReplyComment(postId, ref, newComment);
        } else {
            createRootComment(postId, newComment);
        }
    }

    private void validateMemberAndPost(Long memberId, Long postId) {
        if (!memberRepository.existsById(memberId)) {
            throw new Exception404("존재하지 않는 회원입니다: " + memberId);
        }

        if (!postRepository.existsById(postId)) {
            throw new Exception404("해당 게시글을 찾을 수 없습니다: " + postId);
        }
    }

    private Comment buildBaseComment(Long memberId, Long postId, String content) {
        return Comment.builder()
                .writerId(memberId)
                .postId(postId)
                .content(content)
                .build();
    }

    private void createReplyComment(Long postId, Integer ref, Comment newComment) {

        Comment parentComment = commentRepository.getParentComment(postId, ref)
                .orElseThrow(() -> new Exception400("원댓글을 찾을 수 없습니다."));

        // 원댓글이 대댓글인 경우 댓글을 작성할 수 없다
        if (parentComment.getDepth() > 0) {
            throw new Exception400("대댓글에는 댓글을 달 수 없습니다.");
        }

        // 대댓글 수가 제한을 초과한 경우 댓글을 작성할 수 없다
        int replyCount = commentRepository.countReplies(postId, ref) - 1;
        if (replyCount >= REPLY_LIMIT) {
            throw new Exception400("더 이상 대댓글을 달 수 없습니다.");
        }

        // order, depth, group 업데이트
        int order = replyCount + 1;
        int depth = parentComment.getDepth() + 1;

        newComment.updateCommentProperties(ref, order, depth);

        commentRepository.save(newComment);
    }

    private void createRootComment(Long postId, Comment newComment) {
        int ref = commentRepository.findMaxGroup(postId) + 1;
        int order = 0;
        int depth = 0;

        newComment.updateCommentProperties(ref, order, depth);

        commentRepository.save(newComment);
    }



    // (기능) 댓글 목록 조회 - 컨트롤러로 findAllDTO 리턴
    public CommentResponse.findAllDTO getComments(long postId, int lastGroup, int lastOrder, int pageSize) {

        // 1. 게시글 존재 여부 판별
        validatePostExistence(postId);

        // 2. 댓글 조회 - postId가 일치하는 댓글 중 lastCommentId보다 PK값이 큰 댓글들을 pageSize+1개 가져온다
        List<Comment> comments;
        try {
            comments = customCommentRepository.getCommentList(postId, lastGroup, lastOrder, pageSize+1);
        } catch(Exception e) {
            throw new Exception500("댓글 조회 도중 문제가 발생했습니다.");
        }


        // 3. findAllDTO의 isLastPage - pageSize와 댓글 수가 일치할 때를 대비해 위에서 하나 더 조회한 상태이다
        boolean isLast = comments.size() <= pageSize;


        // 4. 마지막 페이지가 아닐 때는 pageSize만큼의 댓글만 사용한다
        if (!isLast) {
            comments = comments.subList(0, pageSize);
        }


        // 5. findAllDTO의 comments
        List<CommentResponse.commentDTO> commentsDTOs = convertToCommentDTOs(comments);


        // 6. findAllDTO의 groupCursor, orderCursor
        Integer groupCursor = null;
        Integer orderCursor = null;

        if (!comments.isEmpty()) {
            Comment lastComment = comments.get(comments.size() - 1);
            groupCursor = lastComment.getRef();
            orderCursor = lastComment.getRefOrder();
        }

        return new CommentResponse.findAllDTO(commentsDTOs, groupCursor, orderCursor, isLast);
    }

    private CommentResponse.commentDTO createCommentsDTO(Comment comment) {
        Member writer = memberRepository.findById(comment.getWriterId())
                .orElseThrow(() -> new Exception404("존재하지 않는 회원입니다: " + comment.getWriterId()));

        return new CommentResponse.commentDTO(
                comment,
                writer.getNickname(),
                writer.getProfileImage()
        );
    }

    private void validatePostExistence(long postId) {
        if (!postRepository.existsById(postId)) {
            throw new Exception404("해당 게시글을 찾을 수 없습니다: " + postId);
        }
    }

    private List<CommentResponse.commentDTO> convertToCommentDTOs(List<Comment> comments) {

        return comments.stream()
                .map(this::createCommentsDTO)
                .collect(Collectors.toList());
    }



    // (기능) 댓글 삭제
    @Transactional
    public void deleteComment(Long memberId, Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new Exception404("존재하지 않는 댓글입니다: " + commentId));

        // 1. 게시글 존재 여부 확인
        if (!postRepository.existsById(postId)) {
            throw new Exception404("해당 게시글을 찾을 수 없습니다: " + postId);
        }

        // 2. 권한 확인
        if (!comment.getWriterId().equals(memberId)) {
            throw new Exception403("댓글 삭제 권한이 없습니다.");
        }

        try {
            commentRepository.delete(comment);
        } catch (Exception e) {
            throw new Exception500("댓글 삭제처리 도중 문제가 발생했습니다.");
        }
    }
}