package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.Indirizzo;
import it.unina.dietiestates25.entity.enumeration.VicinoA;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndirizzoResponse {
    private String via;
    private String numeroCivico;
    private String citta;
    private String cap;
    private String provincia;
    private String nazione;
    private Double latitudine;
    private Double longitudine;
    private Set<VicinoA> vicinoA;

    public static IndirizzoResponse fromEntityToDto(Indirizzo indirizzoImmobile){

        return IndirizzoResponse.builder()
                .via(indirizzoImmobile.getVia())
                .numeroCivico(indirizzoImmobile.getNumeroCivico())
                .citta(indirizzoImmobile.getCitta())
                .cap(indirizzoImmobile.getCap())
                .provincia(indirizzoImmobile.getProvincia())
                .nazione(indirizzoImmobile.getNazione())
                .longitudine(indirizzoImmobile.getLongitudine())
                .latitudine(indirizzoImmobile.getLatitudine())
                .vicinoA(indirizzoImmobile.getLuoghiVicini())
                .build();
    }
}