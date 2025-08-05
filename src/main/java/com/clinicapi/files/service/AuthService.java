package com.clinicapi.files.service;

import com.clinicapi.files.dto.LoginRequestDto;
import com.clinicapi.files.dto.LoginResponseDto;
import com.clinicapi.files.dto.UserRegistrationDto;
import com.clinicapi.files.entity.User;
import com.clinicapi.files.model.Role;
import com.clinicapi.files.repository.UserRepository;
import com.clinicapi.files.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public User register(UserRegistrationDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está en uso.");
        }
        var user = User.builder()
                .nombreCompleto(request.getNombreCompleto())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER) // Default role
                .build();
        return userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas."));
        var jwtToken = jwtService.generateToken(user);
        return new LoginResponseDto(jwtToken);
    }
}