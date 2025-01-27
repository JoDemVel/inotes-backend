package com.app.dharbor.inotes.service.implement;

import com.app.dharbor.inotes.common.ErrorMessages;
import com.app.dharbor.inotes.common.Messages;
import com.app.dharbor.inotes.domain.entity.RoleEntity;
import com.app.dharbor.inotes.domain.entity.UserEntity;
import com.app.dharbor.inotes.dto.AuthSignUpRequest;
import com.app.dharbor.inotes.dto.AuthLoginRequest;
import com.app.dharbor.inotes.dto.AuthResponse;
import com.app.dharbor.inotes.repository.data.RoleRepository;
import com.app.dharbor.inotes.repository.data.UserRepository;
import com.app.dharbor.inotes.service.mapper.AuthSignUpMapper;
import com.app.dharbor.inotes.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(ErrorMessages.USER_NOT_EXIST, username)));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        user.getRoles()
                .forEach(role -> authorities
                        .add(new SimpleGrantedAuthority("ROLE_"+role.getRoleEnum().name())));

        user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
        return new User(
                user.getEmail(),
                user.getPassword_hash(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                authorities
        );
    }

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest){
        String email = authLoginRequest.email();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(email, Messages.USER_LOGGED_SUCCESSFULLY, accessToken, 200);
    }

    public AuthResponse createUser(AuthSignUpRequest authSignUpRequest){
        authSignUpRequest.setPassword(passwordEncoder.encode(authSignUpRequest.getPassword()));
        UserEntity userEntity = authSignUpMapper.toEntity(authSignUpRequest);

        List<String> roleRequest = authSignUpRequest.getRoles();
        Set<RoleEntity> roleSet = new HashSet<>(roleRepository.findRoleByRoleEnumIn(roleRequest));

        if(roleSet.isEmpty()){
            throw new IllegalArgumentException(ErrorMessages.ROLE_NOT_EXIST);
        }

        userEntity.setRoles(roleSet);

        UserEntity savedUser = userRepository.save(userEntity);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        savedUser.getRoles()
                .forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleEnum().name())));

        savedUser.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(), savedUser.getPassword_hash(), authorities);

        String accessToken = jwtUtils.createToken(authentication);
        return new AuthResponse(
                savedUser.getUsername(), Messages.USER_LOGGED_SUCCESSFULLY, accessToken, 201
        );
    }

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

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
}
