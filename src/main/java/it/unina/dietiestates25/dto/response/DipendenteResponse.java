package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.Contatto;
import it.unina.dietiestates25.entity.DatiImpiegato;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DipendenteResponse {
    private String nome;
    private String cognome;
    private String email;
    private List<Contatto> contatti;
    private String urlFotoProfilo;

    public static DipendenteResponse fromEntityToDto(DatiImpiegato dipendente){
        return DipendenteResponse.builder()
                .nome(dipendente.getNome())
                .cognome(dipendente.getCognome())
                .email(dipendente.getUser().getEmail())
                .contatti(dipendente.getContatti())
                .urlFotoProfilo(dipendente.getUser().getUrlFotoProfilo())
                .build();
    }
}
