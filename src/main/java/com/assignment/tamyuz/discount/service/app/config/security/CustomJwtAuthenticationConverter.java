package com.assignment.tamyuz.discount.service.app.config.security;

import com.assignment.tamyuz.discount.service.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private final UserRepository userRepository;

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {

        // Extract roles from JWT claim
        List<String> roles = jwt.getClaimAsStringList("roles");
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        //  email from JWT subject
        String email = jwt.getSubject();

        // Fetch full User entity
        // User user = userRepository.findByEmail(principal);

        return new JwtAuthenticationToken(jwt, authorities, email);
    }
}
