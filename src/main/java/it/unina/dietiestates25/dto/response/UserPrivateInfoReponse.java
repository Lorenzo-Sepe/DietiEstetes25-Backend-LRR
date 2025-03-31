package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPrivateInfoReponse {
    private String UrlFotoProfilo;
    private String email;
    private String nomeVisualizzato;
    private String tipoAccount;
    public static UserPrivateInfoReponse fromEntityToDto(User user) {
        return UserPrivateInfoReponse.builder()
                .UrlFotoProfilo(user.getUrlFotoProfilo())
                .email(user.getEmail())
                .nomeVisualizzato(user.getNomeVisualizzato())
                .tipoAccount(user.getAuthority().getAuthorityName().toString())
                .build();
    }
}
