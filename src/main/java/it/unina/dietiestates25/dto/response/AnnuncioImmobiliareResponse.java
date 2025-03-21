package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.AnnuncioImmobiliare;
import it.unina.dietiestates25.entity.Contratto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnuncioImmobiliareResponse {

    private int id;
    private String titolo;
    private String descrizione;
    private DipendenteResponse agente;
    private ImmobileResponse immobile;
    private ContrattoResponse contratto;
    private List<PropostaResponse> proposte;

}

