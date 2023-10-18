package com.theocean.fundering.domain.post.repository;

import com.theocean.fundering.domain.post.dto.PostResponse;

import java.util.List;

public interface PostQuerydslRepository {
    List<PostResponse.FindAllDTO> findAll(Long postId);

    List<PostResponse.FindAllDTO> findAllByWriterId(Long postId, Long writerId);
}
