package it.unina.dietiestates25.dto.response;
import it.unina.dietiestates25.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class JwtAuthenticationResponse {

    private int id;
    private String nomeVisualizzato;
    private String UrlFotoProfilo;
    private String email;
    private String authority;
    private String token;

    public static JwtAuthenticationResponse fromEntityToDto(User user,String token) {
       return JwtAuthenticationResponse.builder()
                .id(user.getId())
                .authority(user.getAuthority().getAuthorityName().name())
                .email(user.getEmail())
                .token(token)
               .nomeVisualizzato(user.getNomeVisualizzato())
               .UrlFotoProfilo(user.getUrlFotoProfilo())
                .build();
    }
}
