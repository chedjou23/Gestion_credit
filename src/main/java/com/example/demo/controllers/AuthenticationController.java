package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.JwtAuthenticationResponse;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.entities.User;
import com.example.demo.services.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  
  private final AuthenticationService authenticationService;

  @Operation(
    summary = "Créer un nouvel utilisateur", 
    description = "Permet de créer un utilisateur avec ses informations d'inscription.",
    responses = {
      @ApiResponse(responseCode = "200", description = "Utilisateur inscrit avec succès")
    }
  )
  @PostMapping("/signup")
  public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest){
    return ResponseEntity.ok(authenticationService.signup(signUpRequest));
  }
  
  @Operation(
    summary = "Se connecter", 
    description = "Permet à un utilisateur de se connecter et de recevoir un jeton JWT.",
    responses = {
      @ApiResponse(responseCode = "200", description = "Connexion réussie"),
      @ApiResponse(responseCode = "403", description = "Accès refusé")
    }
  )
  @PostMapping("/signin")
  public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest signInRequest){
    return ResponseEntity.ok(authenticationService.signin(signInRequest));
  }
  
  @Operation(
    summary = "Rafraîchir le token JWT", 
    description = "Renvoie un nouveau jeton d'accès à partir du refresh token.",
    responses = {
      @ApiResponse(responseCode = "200", description = "Token rafraîchi avec succès"),
      @ApiResponse(responseCode = "403", description = "Token invalide ou expiré")
    }
  )
  @PostMapping("/refresh")
  public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
    return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
  }
}
