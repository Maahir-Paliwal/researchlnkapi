package com.maahir.researchlnkapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maahir.researchlnkapi.dtos.users.UserDto;
import com.maahir.researchlnkapi.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public SecurityConfig(UserMapper userMapper, ObjectMapper objectMapper) {
        this.userMapper    = userMapper;
        this.objectMapper  = objectMapper;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                   CustomOAuth2UserService customOAuth2UserService) throws Exception {



        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login",
                                "/api/users/signup/password",
                                "/api/users/signup/orcid",
                                "/oauth2/authorization/orcid",
                                "/login/oauth2/code/orcid",
                                "/error")
                        .permitAll()
                        .anyRequest().authenticated()
                )

                //receives x-www-form-urlencoded
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(jsonSuccessHandler())
                        .failureHandler(jsonFailureHandler())
                        .permitAll()
                )

                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .defaultSuccessUrl("/", true)
                );

        return http.build();
    }

    private AuthenticationSuccessHandler jsonSuccessHandler() {
        return (req, res, auth) -> {
            CustomUserDetails cd = (CustomUserDetails) auth.getPrincipal();
            UserDto dto = userMapper.toDto(cd.getUserEntity());
            byte[] body = objectMapper.writeValueAsBytes(dto);

            res.setStatus(HttpStatus.OK.value());
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setCharacterEncoding(StandardCharsets.UTF_8.name());
            res.getOutputStream().write(body);
        };
    }

    private AuthenticationFailureHandler jsonFailureHandler() {
        return (req, res, ex) ->
                res.sendError(HttpStatus.UNAUTHORIZED.value(), "Bad credentials");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}


//right now, a user can log in via ORCID and have a null password, then if someone else chooses to login
//via username and password, it can be very easy to login to someone else's account
//to fix this, on the frontend, we will make sure that someone's password cannot be null