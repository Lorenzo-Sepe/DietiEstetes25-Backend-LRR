package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.Proposta;
import lombok.Builder;
import lombok.Data;
import it.unina.dietiestates25.entity.DatiImpiegato;

@Data
@Builder
public class DatiUserPropostaResponse {
    String email;
    String nome;
    String cognome;
    ContattoResponse contatto;

    public static DatiUserPropostaResponse fromEntityToDto(Proposta proposta){
        return DatiUserPropostaResponse.builder()
                .email(proposta.getUser()==null?null:proposta.getUser().getEmail())
                .nome(proposta.getNome())
                .cognome(proposta.getCognome())
                .contatto(ContattoResponse.fromEntityToDto(proposta.getContatto()))
                .build();
    }

}
