package com.assignment.tamyuz.discount.service.app.controller;

import com.assignment.tamyuz.discount.service.app.config.security.AuthService;
import com.assignment.tamyuz.discount.service.app.config.security.LoginRequest;
import com.assignment.tamyuz.discount.service.app.config.security.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Login",
            description = "Authenticate user and return JWT token"
    )
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }
}