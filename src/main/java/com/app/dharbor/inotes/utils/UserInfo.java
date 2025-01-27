package com.app.dharbor.inotes.utils;

import com.app.dharbor.inotes.common.ErrorMessages;
import com.app.dharbor.inotes.domain.entity.UserEntity;
import com.app.dharbor.inotes.repository.data.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserInfo {
    private final UserRepository userRepository;

    public UserInfo(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
            throw new IllegalStateException(ErrorMessages.USER_NOT_AUTHENTICATED);
        }

        return authentication.getName();
    }

    public UserEntity getAuthenticatedUser() {
        String email = getAuthenticatedEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException(ErrorMessages.USER_NOT_EXIST));
    }
}
