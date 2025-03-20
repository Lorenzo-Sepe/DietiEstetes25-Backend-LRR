package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.Proposta;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropostaResponse {
    private int idProposta;
    private double prezzoProposta;
    private Double controproposta;
    private String stato;
    private DatiUserPropostaResponse datiProponente;


}