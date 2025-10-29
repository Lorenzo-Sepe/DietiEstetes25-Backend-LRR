package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.Proposta;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    public static List<PropostaResponse> fromListEntityToDto(List<Proposta> proposte){

        List<PropostaResponse> proposteResponse = new ArrayList<>();

        for(Proposta proposta : proposte){
            PropostaResponse propostaResponse = PropostaResponse.builder()
                    .idProposta(proposta.getId())
                    .prezzoProposta(proposta.getPrezzoProposta())
                    .controproposta(proposta.getControproposta())
                    .stato(proposta.getStato().toString())
                    .datiProponente(DatiUserPropostaResponse.fromEntityToDto(proposta))
                    .build();
            proposteResponse.add(propostaResponse);
        }

        return proposteResponse;
    }

    public static PropostaResponse fromEntityToDto(Proposta proposta){

        return PropostaResponse.builder()
                    .idProposta(proposta.getId())
                    .prezzoProposta(proposta.getPrezzoProposta())
                    .controproposta(proposta.getControproposta())
                    .stato(proposta.getStato().toString())
                    .datiProponente(DatiUserPropostaResponse.fromEntityToDto(proposta))
                    .build();
    }
}