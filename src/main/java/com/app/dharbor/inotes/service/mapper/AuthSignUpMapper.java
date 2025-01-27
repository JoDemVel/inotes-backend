package com.app.dharbor.inotes.service.mapper;

import com.app.dharbor.inotes.domain.entity.UserEntity;
import com.app.dharbor.inotes.dto.AuthSignUpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthSignUpMapper implements CustomMapper<AuthSignUpRequest, UserEntity>{
    @Override
    public AuthSignUpRequest toDTO(UserEntity userEntity) {
        List<String> roles = userEntity.getRoles()
                .stream()
                .map(roleEntity -> roleEntity.getRoleEnum().name())
                .collect(Collectors.toList());
        return new AuthSignUpRequest(
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getPassword_hash(),
                roles
        );
    }

    @Override
    public UserEntity toEntity(AuthSignUpRequest authSignUpRequest) {
        return new UserEntity().builder()
                .username(authSignUpRequest.getUsername())
                .email(authSignUpRequest.getEmail())
                .password_hash(authSignUpRequest.getPassword())
                .roles(null)
                .enabled(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .build();
    }
}
