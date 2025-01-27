package com.app.dharbor.inotes.service.implement;

import com.app.dharbor.inotes.common.ErrorMessages;
import com.app.dharbor.inotes.domain.entity.TagEntity;
import com.app.dharbor.inotes.dto.TagDTO;
import com.app.dharbor.inotes.repository.data.TagRepository;
import com.app.dharbor.inotes.service.TagService;
import com.app.dharbor.inotes.service.mapper.TagMapper;
import com.app.dharbor.inotes.utils.UserInfo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final UserInfo userInfo;
    private final TagMapper tagMapper;

    public TagServiceImpl(TagRepository tagRepository, UserInfo userInfo, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.userInfo = userInfo;
        this.tagMapper = tagMapper;
    }

    @Override
    public TagDTO createTag(TagDTO tagDTO) {
        Long userId = userInfo.getAuthenticatedUser().getId();
        tagDTO.setUserId(userId);
        TagEntity tagEntity = tagMapper.toEntity(tagDTO);
        TagEntity savedTag = tagRepository.save(tagEntity);
        return tagMapper.toDTO(savedTag);
    }

    /**
     * Finds all tags associated with a specific user.
     *
     * @param userId The ID of the user whose tags are being retrieved.
     * @return A list of TagDTO objects representing the user's tags.
     */
    @Override
    public List<TagDTO> findAllByUserId(Long userId) {
        List<TagEntity> tags = tagRepository.findAllByUserId(userId);
        return tags.stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds all tags associated with the authenticated user.
     *
     * @return A list of TagDTO objects representing the authenticated user's tags.
     */
    @Override
    public List<TagDTO> findAllByUserAuth() {
        Long userId = userInfo.getAuthenticatedUser().getId();
        return findAllByUserId(userId);
    }

    @Override
    public TagDTO deleteTag(Long id) {
        TagEntity tagEntity = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ErrorMessages.TAG_NOT_EXIST, id)));

        tagRepository.delete(tagEntity);
        return tagMapper.toDTO(tagEntity);
    }
}
