package com.example.demo.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
  @Operation(
    summary = "Message de bienvenue pour l'admin",
    description = "Retourne un message de salutation destiné à l'utilisateur avec le rôle ADMIN.",
    responses = {
      @ApiResponse(responseCode = "200", description = "Message retourné avec succès"),
      @ApiResponse(responseCode = "403", description = "Accès refusé - l'utilisateur n'est pas un admin")
    }
  )
  @GetMapping
  public ResponseEntity<String> sayHello(){
    return ResponseEntity.ok("Hi Admin");
  }
  
}
