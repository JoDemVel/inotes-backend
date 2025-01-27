package com.app.dharbor.inotes.repository.jpa.specifications;

import com.app.dharbor.inotes.domain.entity.NoteEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class NoteSpecifications {
    /**
     * Filters notes by title. Returns notes with a title containing the provided string.
     *
     * @param title The title or part of the title to filter notes by.
     * @return Specification to filter notes by title.
     */
    public static Specification<NoteEntity> hasTitle(String title) {
        return (root, query, criteriaBuilder) ->
                title == null
                        ? null
                        : criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    /**
     * Filters notes by content. Returns notes with content containing the provided string.
     *
     * @param content The content or part of the content to filter notes by.
     * @return Specification to filter notes by content.
     */
    public static Specification<NoteEntity> hasContent(String content) {
        return (root, query, criteriaBuilder) ->
                content == null
                        ? null
                        : criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), "%" + content.toLowerCase() + "%");
    }

    /**
     * Filters notes by tag name. Returns notes that have a tag with the provided name.
     *
     * @param tagName The tag name or part of the tag name to filter notes by.
     * @return Specification to filter notes by tag name.
     */
    public static Specification<NoteEntity> hasTagName(String tagName) {
        return (root, query, criteriaBuilder) -> {
            if (tagName == null) return null;
            query.distinct(true);
            Join<Object, Object> tags = root.join("tags");
            return criteriaBuilder.like(criteriaBuilder.lower(tags.get("name")), "%" + tagName.toLowerCase() + "%");
        };
    }

    /**
     * Filters notes by user ID. Returns notes belonging to the specified user.
     *
     * @param userId The user ID to filter notes by.
     * @return Specification to filter notes by user ID.
     */
    public static Specification<NoteEntity> belongsToUser(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("userId"), userId);
    }
}

