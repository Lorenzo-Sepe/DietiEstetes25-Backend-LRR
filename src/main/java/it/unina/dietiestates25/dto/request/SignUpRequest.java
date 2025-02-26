package it.unina.dietiestates25.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(

        @NotBlank(message = "Username cannot be null or blank")
        @Size(max = 20, min = 3, message = "Username must be between 3 and 20 characters")
        String username,
        @NotBlank(message = "Email cannot be null or blank")
        @Email(message = "Email not well-formed")
        String email,
        @NotBlank(message = "Password cannot be null or blank")
        @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$£%^&+=!]).*$",
                message = "Password must contain at least one digit, one lowercase, one uppercase letter and one special character")
        String password
) {
}
