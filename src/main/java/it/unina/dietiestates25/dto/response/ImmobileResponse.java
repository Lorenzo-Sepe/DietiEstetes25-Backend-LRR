package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.Immobile;
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

    public static ImmobileResponse fromEntityToDto(Immobile immobile){

        IndirizzoResponse indirizzoResponse = IndirizzoResponse.fromEntityToDto(immobile.getIndirizzo());
        CaratteristicheAggiuntiveResponse caratteristicheAggiuntiveResponse = CaratteristicheAggiuntiveResponse.fromEntityToDto(immobile.getCaratteristicheAggiuntive());
        List<ImmaginiImmobileResponse> immaginiImmobileResponse = ImmaginiImmobileResponse.fromEntityToDto(immobile.getImmagini());

        return ImmobileResponse.builder()
                .tipologiaImmobile(immobile.getTipologiaImmobile().toString())
                .metriQuadri(immobile.getMetriQuadri())
                .classeEnergetica(immobile.getClasseEnergetica().toString())
                .numeroServizi(immobile.getNumeroServizi())
                .numeroStanze(immobile.getNumeroStanze())
                .numeroDiPiani(immobile.getNumeroDiPiani())
                .indirizzo(indirizzoResponse)
                .caratteristicheAggiuntive(caratteristicheAggiuntiveResponse)
                .immagini(immaginiImmobileResponse)
                .build();
    }
}