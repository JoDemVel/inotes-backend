package com.app.dharbor.inotes.repository.data;

import com.app.dharbor.inotes.domain.entity.TagEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagRepository extends CrudRepository<TagEntity, Long> {
    List<TagEntity> findAllById(Iterable<Long> ids);
    List<TagEntity> findAllByUserId(Long userId);
}
