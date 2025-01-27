package com.app.dharbor.inotes.service.implement;

import com.app.dharbor.inotes.common.ErrorMessages;
import com.app.dharbor.inotes.common.Messages;
import com.app.dharbor.inotes.domain.entity.RoleEntity;
import com.app.dharbor.inotes.domain.entity.UserEntity;
import com.app.dharbor.inotes.dto.AuthSignUpRequest;
import com.app.dharbor.inotes.dto.AuthLoginRequest;
import com.app.dharbor.inotes.dto.AuthResponse;
import com.app.dharbor.inotes.exception.EmailAlreadyExistsException;
import com.app.dharbor.inotes.repository.data.RoleRepository;
import com.app.dharbor.inotes.repository.data.UserRepository;
import com.app.dharbor.inotes.service.mapper.AuthSignUpMapper;
import com.app.dharbor.inotes.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthSignUpMapper authSignUpMapper;

    /**
     * Loads user details by username (email) for authentication.
     *
     * @param username The email of the user to load.
     * @return UserDetails object containing user information and authorities.
     * @throws UsernameNotFoundException if the user with the given email is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(ErrorMessages.USER_NOT_EXIST, username)));

        List<SimpleGrantedAuthority> authorities = userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getName())))
                .collect(Collectors.toList());

        userEntity.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name())));

        return new User(
                userEntity.getEmail(),
                userEntity.getPasswordHash(),
                userEntity.isEnabled(),
                userEntity.isAccountNonExpired(),
                userEntity.isCredentialsNonExpired(),
                userEntity.isAccountNonLocked(),
                authorities
        );
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param authLoginRequest The login request containing email and password.
     * @return AuthResponse object containing JWT token and user details.
     */
    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        Authentication authentication = this.authenticate(
                authLoginRequest.getEmail(), authLoginRequest.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);

        return AuthResponse.builder()
                .email(authLoginRequest.getEmail())
                .message(Messages.USER_LOGGED_SUCCESSFULLY)
                .jwt(accessToken)
                .status(200)
                .build();
    }

    /**
     * Creates a new user account and assigns roles.
     *
     * @param authSignUpRequest The signup request containing user details and roles.
     * @return AuthResponse object containing JWT token and user details.
     * @throws EmailAlreadyExistsException if the email already exists in the system.
     */
    public AuthResponse createUser(AuthSignUpRequest authSignUpRequest) {
        validateRoles(authSignUpRequest.getRoles());

        authSignUpRequest.setPassword(passwordEncoder.encode(authSignUpRequest.getPassword()));
        UserEntity userEntity = authSignUpMapper.toEntity(authSignUpRequest);

        if (userRepository.existsByEmail(authSignUpRequest.getEmail())) {
            throw new EmailAlreadyExistsException(
                    String.format(ErrorMessages.EMAIL_ALREADY_EXISTS, authSignUpRequest.getEmail()));
        }

        Set<RoleEntity> roleSet = new HashSet<>(roleRepository.findRoleByRoleEnumIn(authSignUpRequest.getRoles()));
        userEntity.setRoles(roleSet);

        UserEntity savedUser = userRepository.save(userEntity);
        Authentication authentication = createAuthentication(savedUser);

        String accessToken = jwtUtils.createToken(authentication);

        return AuthResponse.builder()
                .email(savedUser.getEmail())
                .message(Messages.USER_CREATED_SUCCESSFULLY)
                .jwt(accessToken)
                .status(201)
                .build();
    }

    /**
     * Creates an authentication object for a user.
     *
     * @param userEntity The user entity to create authentication for.
     * @return Authentication object containing user's authorities.
     */
    private Authentication createAuthentication(UserEntity userEntity) {
        List<SimpleGrantedAuthority> authorities = userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getName())))
                .collect(Collectors.toList());

        userEntity.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name())));

        return new UsernamePasswordAuthenticationToken(
                userEntity.getEmail(),
                userEntity.getPasswordHash(),
                authorities
        );
    }

    private void validateRoles(List<String> roles) {
        if (roleRepository.findRoleByRoleEnumIn(roles).isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.ROLE_NOT_EXIST);
        }
    }

    /**
     * Authenticates a user by verifying email and password.
     *
     * @param email    The email of the user to authenticate.
     * @param password The password to verify.
     * @return Authentication object containing user's details and authorities.
     * @throws UsernameNotFoundException if the user is not found.
     * @throws DisabledException if the user's account is disabled.
     * @throws LockedException if the user's account is locked.
     * @throws BadCredentialsException if the password is incorrect.
     */
    public Authentication authenticate(String email, String password) {
        UserDetails userDetails = loadUserByUsername(email);

        if(userDetails == null){
            throw new UsernameNotFoundException(String.format(ErrorMessages.USER_NOT_EXIST, email));
        }

        if(!userDetails.isEnabled()) {
            throw new DisabledException(String.format(ErrorMessages.USER_IS_DISABLED, email));
        }

        if(!userDetails.isAccountNonLocked()){
            throw new LockedException(String.format(ErrorMessages.USER_IS_BLOCKED, email));
        }


        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException(ErrorMessages.INVALID_PASSWORD);
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                password,
                userDetails.getAuthorities()
        );
    }
}
