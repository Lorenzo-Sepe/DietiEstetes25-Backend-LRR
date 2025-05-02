package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {
    private String urlFotoProfilo;
    private String email;
    private String nomeVisualizzato;
    private String tipoAccount;

    public static UserInfoResponse fromEntityToDto(User user) {
        return UserInfoResponse.builder()
                .urlFotoProfilo(user.getUrlFotoProfilo())
                .email(user.getEmail())
                .nomeVisualizzato(user.getNomeVisualizzato())
                .tipoAccount(user.getAuthority().getAuthorityName().name())
                .build();
    }
}
