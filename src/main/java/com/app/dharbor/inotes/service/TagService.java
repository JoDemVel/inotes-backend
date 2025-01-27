package com.app.dharbor.inotes.service;

import com.app.dharbor.inotes.dto.TagDTO;

import java.util.List;

public interface TagService {
    TagDTO createTag(TagDTO tagDTO);
    List<TagDTO> findAllByUserId(Long userId);
    List<TagDTO> findAllByUserAuth();
    TagDTO deleteTag(Long id);
}
