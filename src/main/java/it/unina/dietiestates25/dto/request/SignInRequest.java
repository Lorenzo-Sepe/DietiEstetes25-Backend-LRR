package it.unina.dietiestates25.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "Username or email cannot be null or blank")
        String usernameOrEmail,
        @NotBlank(message = "Password cannot be null or blank")
        String password
) {
}