package com.app.dharbor.inotes.service.mapper;

import com.app.dharbor.inotes.domain.entity.TagEntity;
import com.app.dharbor.inotes.dto.TagDTO;
import org.springframework.stereotype.Component;

@Component
public class TagMapper implements CustomMapper<TagDTO, TagEntity>{
    @Override
    public TagDTO toDTO(TagEntity tagEntity) {
        return new TagDTO(tagEntity.getId(), tagEntity.getName(), tagEntity.getUserId());
    }

    @Override
    public TagEntity toEntity(TagDTO tagDTO) {
        return new TagEntity().builder()
                .id(tagDTO.getId())
                .name(tagDTO.getName())
                .userId(tagDTO.getUserId())
                .build();
    }
}
