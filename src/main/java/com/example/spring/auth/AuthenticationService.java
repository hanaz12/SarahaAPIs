package com.example.spring.auth;


import com.example.spring.Exceptions.UserNotFoundException;
import com.example.spring.config.JwtService;
import com.example.spring.token.Token;
import com.example.spring.token.TokenRepository;
import com.example.spring.token.TokenType;
import com.example.spring.Enums.Role;
import com.example.spring.Model.User;
import com.example.spring.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        System.out.println("Iam in register method");
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new UserNotFoundException("Invalid username or password");
        }

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Invalid username or password"));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserToken(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    public AuthenticationResponse changePassword(ChangePasswordRequest request) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("old password is Wrong ");
        }
       user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        revokeAllUserToken(user);
        String accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, accessToken);
        return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }
    public void revokeAllUserToken(User user){
        var validUserToken = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserToken.isEmpty()) return;
        validUserToken.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserToken);
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final String authHeader= request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader==null || !authHeader.startsWith("Bearer "))
        {
            return;
        }
        refreshToken= authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail !=null){
            var user=this.userRepository.findByUsername(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken,user)){
                var accessToken = jwtService.generateToken(user);
                revokeAllUserToken(user);
                saveUserToken(user,accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
             }
        }
    }
}
