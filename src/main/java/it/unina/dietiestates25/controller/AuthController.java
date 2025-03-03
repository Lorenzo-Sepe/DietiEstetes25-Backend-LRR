package it.unina.dietiestates25.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.ChangePasswordRequest;
import it.unina.dietiestates25.dto.request.SignInRequest;
import it.unina.dietiestates25.dto.request.SignUpRequest;
import it.unina.dietiestates25.dto.response.JwtAuthenticationResponse;
import it.unina.dietiestates25.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @Operation(
            summary = "LOGIN",
            description = "Method to sign in the user that exists the database to the application",
            tags = {"Auth"})
    @PostMapping("/pb/auth/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody @Valid SignInRequest request){
        return ResponseEntity.ok(authService.signin(request));
    }

    @Operation(
            summary = "Cambia password",
            description = "Metodo per cambiare la password dell'utente",
            tags = {"Auth"})
    @PostMapping("/auth/change_password")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MEMBER', 'AGENT')")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequest request){
        return ResponseEntity.ok(authService.changePassword(request.getOldPassword(), request.getNewPassword(),request.getConfirmPassword()));
    }

    @Operation(
            summary = "MODIFY USER AUTHORITY",
            description = "Method to modify the authority of the user in the database. Only the admin can do it",
            tags = {"Auth"})
    @PatchMapping("/auth/modify_user_authority") // modify user authority
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> modifyUserAuthority(@RequestParam int id, @RequestParam String auth){
        return ResponseEntity.ok(authService.modifyUserAuthority(id, auth));
    }

}
