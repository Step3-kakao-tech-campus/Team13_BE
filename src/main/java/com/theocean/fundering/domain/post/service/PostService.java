package com.theocean.fundering.domain.post.service;


import com.querydsl.core.BooleanBuilder;
import com.theocean.fundering.domain.post.domain.Post;
import com.theocean.fundering.domain.post.domain.QPost;
import com.theocean.fundering.domain.post.dto.PostRequest;
import com.theocean.fundering.domain.post.dto.PostResponse;
import com.theocean.fundering.domain.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Transactional
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
//  private final MemberRepository memberRepository;
//  private final CelebRepository celebRepository;

    /**
     * Member와 Celebrity 도메인의 작업이 완료되어야 구현 가능
     */
    @Transactional
    public void writePost(PostRequest.PostWriteDTO postWriteDTO){
        // Member writer = memberRepository.findById(postWriteDTO.getWriterId());
        // Celebrity celebrity = celebRepository.findById(postWriteDTO.getCelebId());
        // postRepository.save(postWriteDTO.toEntity(writer, celebrity));
    }

    public PostResponse.FindByPostIdDTO findByPostId(Long postId){
        Post postPS = postRepository.findByPostId(postId).orElseThrow();
        return new PostResponse.FindByPostIdDTO(postPS);

    }

    public List<PostResponse.FindAllDTO> findAll(Long postId){
        var postList = postRepository.findAll(postId);
        var checkForNext = postRepository.findAll(postList.get(-1).getPostId() + 1);
        if (checkForNext == null)
            postList.get(-1).setLast(true);
        return postList;
    }

    public List<PostResponse.FindAllDTO> findAllByWriterId(Long postId, Long writerId){
        var postList = postRepository.findAllByWriterId(postId, writerId);
        var checkForNext = postRepository.findAllByWriterId(postList.get(-1).getPostId() + 1, writerId);
        if (checkForNext == null)
            postList.get(-1).setLast(true);
        return postList;
    }
    @Transactional
    public Long editPost(Long postId, PostRequest.PostEditDTO postEditDTO){
        Post postPS = postRepository.findByPostId(postId).orElseThrow();
        postPS.update(postEditDTO);

        return postId;
    }

    public void deletePost(Long postId){
        postRepository.deleteByPostId(postId);
    }

}
