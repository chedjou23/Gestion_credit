package com.example.demo.services.impl;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.JwtAuthenticationResponse;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.JWTService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  private final JWTService jwtService;

  public User signup(SignUpRequest signUpRequest){
    User user = new User();

    user.setEmail(signUpRequest.getEmail());
    user.setFirstname(signUpRequest.getFirstName());
    user.setSecondname(signUpRequest.getLastName());
    user.setRole(Role.USER);
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

    return userRepository.save(user);
  }

  public JwtAuthenticationResponse signin(SignInRequest signInRequest){
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));

    var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

    var jwt = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

    JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

    jwtAuthenticationResponse.setToken(jwt);
    jwtAuthenticationResponse.setRefreshToken(refreshToken);

    return jwtAuthenticationResponse;
  }

  public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
    String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
    User user = userRepository.findByEmail(userEmail).orElseThrow();

    if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
      var jwt = jwtService.generateToken(user);

      JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

      jwtAuthenticationResponse.setToken(jwt);
      jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

      return jwtAuthenticationResponse;
    }

    return null;
  }
}
