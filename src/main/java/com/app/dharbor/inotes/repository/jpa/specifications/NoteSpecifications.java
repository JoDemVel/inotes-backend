package com.app.dharbor.inotes.repository.jpa.specifications;

import com.app.dharbor.inotes.domain.entity.NoteEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class NoteSpecifications {
    public static Specification<NoteEntity> hasTitle(String title) {
        return (root, query, criteriaBuilder) ->
                title == null
                        ? null
                        : criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<NoteEntity> hasContent(String content) {
        return (root, query, criteriaBuilder) ->
                content == null
                        ? null
                        : criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), "%" + content.toLowerCase() + "%");
    }

    public static Specification<NoteEntity> hasTagName(String tagName) {
        return (root, query, criteriaBuilder) -> {
            if (tagName == null) return null;
            query.distinct(true);
            Join<Object, Object> tags = root.join("tags");
            return criteriaBuilder.like(criteriaBuilder.lower(tags.get("name")), "%" + tagName.toLowerCase() + "%");
        };
    }

    public static Specification<NoteEntity> belongsToUser(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("userId"), userId);
    }
}

