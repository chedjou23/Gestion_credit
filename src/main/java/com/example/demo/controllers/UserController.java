package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
  @Operation(
    summary = "Message de bienvenue pour l'user",
    description = "Retourne un message de salutation destiné à l'utilisateur avec le rôle USER.",
    responses = {
      @ApiResponse(responseCode = "200", description = "Message retourné avec succès"),
      @ApiResponse(responseCode = "403", description = "Accès refusé - l'utilisateur n'est pas un user")
    }
  )
  @GetMapping
  public ResponseEntity<String> sayHello(){
    return ResponseEntity.ok("Hi User");
  }
}
