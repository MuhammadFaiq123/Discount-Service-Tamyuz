package com.assignment.tamyuz.discount.service.app.config.security;

import com.assignment.tamyuz.discount.service.app.exception.BadRequestException;
import com.assignment.tamyuz.discount.service.persistence.entity.Role;
import com.assignment.tamyuz.discount.service.persistence.entity.User;
import com.assignment.tamyuz.discount.service.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_success() {
        LoginRequest request = new LoginRequest("user@example.com", "pass123");

        User user = User.builder()
                .email("user@example.com")
                .password("encodedPass")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user.getEmail(), user.getRole().name())).thenReturn("jwtToken123");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwtToken123", response.accessToken());
    }

    @Test
    void login_userNotFound_throwsBadRequest() {
        LoginRequest request = new LoginRequest("unknown@example.com", "pass123");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> authService.login(request));

        assertEquals("Invalid email or password", ex.getMessage());
    }

    @Test
    void login_passwordMismatch_throwsBadRequest() {
        LoginRequest request = new LoginRequest("user@example.com", "wrongPass");

        User user = User.builder()
                .email("user@example.com")
                .password("encodedPass")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> authService.login(request));

        assertEquals("Invalid email or password", ex.getMessage());
    }
}

