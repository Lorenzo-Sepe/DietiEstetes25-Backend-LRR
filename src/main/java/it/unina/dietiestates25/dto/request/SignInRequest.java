package it.unina.dietiestates25.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "email cannot be null or blank")
        String Email,
        @NotBlank(message = "Password cannot be null or blank")
        String password
) {
}