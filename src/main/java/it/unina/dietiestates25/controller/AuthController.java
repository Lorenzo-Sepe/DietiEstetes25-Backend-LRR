package it.unina.dietiestates25.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.SignUpRequest;
import it.unina.dietiestates25.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "ADD A NEW USER",
            description = "Method to add a new user to the database with a sign in procedure and send an email to confirm the registration",
            tags = {"Auth"})
    @PostMapping("/pb/auth/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignUpRequest request){
        return ResponseEntity.ok(authService.signup(request));
    }
}
