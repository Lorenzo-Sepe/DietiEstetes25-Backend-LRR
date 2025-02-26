package it.unina.dietiestates25.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class JwtAuthenticationResponse {

    private int id;
    private String username;
    private String email;
    private String authority;
    private String token;
}
