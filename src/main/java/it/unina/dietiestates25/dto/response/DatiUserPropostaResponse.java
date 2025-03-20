package it.unina.dietiestates25.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatiUserPropostaResponse {
    String email;
    String nome;
    String cognome;
    ContattoResponse contatto;


}
