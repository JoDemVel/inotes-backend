package com.app.dharbor.inotes.service.mapper;

public interface CustomMapper<DTO, E> {
    DTO toDTO(E e);
    E toEntity(DTO dto);
}
