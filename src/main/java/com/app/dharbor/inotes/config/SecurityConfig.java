package com.app.dharbor.inotes.config;

import com.app.dharbor.inotes.common.Path;
import com.app.dharbor.inotes.config.filter.JwtTokenValidator;
import com.app.dharbor.inotes.domain.entity.RoleEnum;
import com.app.dharbor.inotes.security.CustomAccessDeniedHandler;
import com.app.dharbor.inotes.security.CustomAuthenticationEntryPoint;
import com.app.dharbor.inotes.service.implement.UserDetailsServiceImpl;
import com.app.dharbor.inotes.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   CustomAuthenticationEntryPoint authenticationEntryPoint,
                                                   CustomAccessDeniedHandler accessDeniedHandler) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    http.requestMatchers(HttpMethod.POST, Path.AUTH_ALL).permitAll();

                    http.requestMatchers(HttpMethod.GET, Path.NOTE_ID).hasAnyRole(
                            RoleEnum.ADMIN.name(), RoleEnum.USER.name());
                    http.requestMatchers(HttpMethod.GET, Path.NOTE).hasAnyRole(
                            RoleEnum.ADMIN.name(), RoleEnum.USER.name());
                    http.requestMatchers(HttpMethod.POST, Path.NOTE).hasAnyRole(
                            RoleEnum.ADMIN.name(), RoleEnum.USER.name());
                    http.requestMatchers(HttpMethod.PUT, Path.NOTE_ID).hasAnyRole(
                            RoleEnum.ADMIN.name(), RoleEnum.USER.name());
                    http.requestMatchers(HttpMethod.DELETE, Path.NOTE_ID).hasAnyRole(
                            RoleEnum.ADMIN.name(), RoleEnum.USER.name());
                    http.requestMatchers(HttpMethod.PATCH, Path.NOTE_ID).hasAnyRole(
                            RoleEnum.ADMIN.name(), RoleEnum.USER.name());

                    http.anyRequest().denyAll();
                })
                .exceptionHandling(exception -> {
                    exception
                            .authenticationEntryPoint(authenticationEntryPoint)
                            .accessDeniedHandler(accessDeniedHandler);
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

