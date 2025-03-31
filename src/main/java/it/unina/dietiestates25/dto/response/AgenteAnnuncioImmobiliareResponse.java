package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AgenteAnnuncioImmobiliareResponse {

    String email;


    String urlFotoProfilo;

    public AgenteAnnuncioImmobiliareResponse fromEntityToDto(User user){
        return AgenteAnnuncioImmobiliareResponse.builder()
                .email(user.getEmail())
                .urlFotoProfilo(user.getUrlFotoProfilo())
                .build();
    }
}
