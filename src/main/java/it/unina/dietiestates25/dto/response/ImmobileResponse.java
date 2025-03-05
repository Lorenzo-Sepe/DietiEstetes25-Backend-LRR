package it.unina.dietiestates25.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImmobileResponse {
    private String tipologiaImmobile;
    private int metriQuadri;
    private String classeEnergetica;
    private int numeroServizi;
    private int numeroStanze;
    private int numeroDiPiani;
    private IndirizzoResponse indirizzo;
    private CaratteristicheAggiuntiveResponse caratteristicheAggiuntive;
    private List<ImmaginiImmobileResponse> immagini;
}