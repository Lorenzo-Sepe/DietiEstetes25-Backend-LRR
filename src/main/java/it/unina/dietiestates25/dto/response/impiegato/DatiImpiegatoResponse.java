package it.unina.dietiestates25.dto.response.impiegato;

import it.unina.dietiestates25.entity.Contatto;
import it.unina.dietiestates25.entity.DatiImpiegato;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DatiImpiegatoResponse {
    private String email;
    private String nome;
    private String cognome;
    private List<Contatto> contatti;
    private String ruolo;

    public static DatiImpiegatoResponse fromEntityToDto(DatiImpiegato entity){
        return DatiImpiegatoResponse.builder()
                .email(entity.getUser().getEmail())
                .nome(entity.getNome())
                .cognome(entity.getCognome())
                .contatti(entity.getContatti())
                .ruolo(entity.getUser().getAuthority().getAuthorityName().name())
                .build();
    }
}
