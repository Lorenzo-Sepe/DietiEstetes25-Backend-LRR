package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.Immobile;
import it.unina.dietiestates25.entity.Indirizzo;
import it.unina.dietiestates25.entity.CaratteristicheAggiuntive;
import it.unina.dietiestates25.entity.enumeration.ClasseEnergetica;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImmobileResponse {
    private int id;
    private TipologiaImmobile tipologiaImmobile;
    private int metriQuadri;
    private int numeroDiPiani;
    private int numeroStanze;
    private int numeroServizi;
    private ClasseEnergetica classeEnergetica;
    private IndirizzoResponse indirizzo;
    private CaratteristicheAggiuntiveResponse caratteristicheAggiuntive;

    public static ImmobileResponse fromImmobileToDto(Immobile immobile) {
        return ImmobileResponse.builder()
                .id(immobile.getId())
                .tipologiaImmobile(immobile.getTipologiaImmobile())
                .metriQuadri(immobile.getMetriQuadri())
                .numeroDiPiani(immobile.getNumeroDiPiani())
                .numeroStanze(immobile.getNumeroStanze())
                .numeroServizi(immobile.getNumeroServizi())
                .classeEnergetica(immobile.getClasseEnergetica())
                .indirizzo(IndirizzoResponse.fromIndirizzoToDto(immobile.getIndirizzo()))
                .caratteristicheAggiuntive(CaratteristicheAggiuntiveResponse.fromCaratteristicheToDto(immobile.getCaratteristicheAggiuntive()))
                .build();
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class IndirizzoResponse {
    private String via;
    private String numeroCivico;
    private String citta;
    private String cap;
    private String provincia;
    private String nazione;
    private Double latitudine;
    private Double longitudine;

    public static IndirizzoResponse fromIndirizzoToDto(Indirizzo indirizzo) {
        return IndirizzoResponse.builder()
                .via(indirizzo.getVia())
                .numeroCivico(indirizzo.getNumeroCivico())
                .citta(indirizzo.getCitta())
                .cap(indirizzo.getCap())
                .provincia(indirizzo.getProvincia())
                .nazione(indirizzo.getNazione())
                .latitudine(indirizzo.getLatitudine())
                .longitudine(indirizzo.getLongitudine())
                .build();
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class CaratteristicheAggiuntiveResponse {
    private boolean balconi;
    private boolean garage;
    private boolean postiAuto;
    private boolean giardino;
    private boolean ascensore;
    private boolean portiere;
    private boolean riscaldamentoCentralizzato;
    private boolean climatizzatori;
    private boolean pannelliSolari;
    private boolean cantina;
    private boolean soffitta;

    public static CaratteristicheAggiuntiveResponse fromCaratteristicheToDto(CaratteristicheAggiuntive caratteristiche) {
        return CaratteristicheAggiuntiveResponse.builder()
                .balconi(caratteristiche.isBalconi())
                .garage(caratteristiche.isGarage())
                .postiAuto(caratteristiche.isPostiAuto())
                .giardino(caratteristiche.isGiardino())
                .ascensore(caratteristiche.isAscensore())
                .portiere(caratteristiche.isPortiere())
                .riscaldamentoCentralizzato(caratteristiche.isRiscaldamentoCentralizzato())
                .climatizzatori(caratteristiche.isClimatizzatori())
                .pannelliSolari(caratteristiche.isPannelliSolari())
                .cantina(caratteristiche.isCantina())
                .soffitta(caratteristiche.isSoffitta())
                .build();
    }
} 