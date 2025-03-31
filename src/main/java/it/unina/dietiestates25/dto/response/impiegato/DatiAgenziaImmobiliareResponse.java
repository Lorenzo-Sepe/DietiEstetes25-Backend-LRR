package it.unina.dietiestates25.dto.response.impiegato;

import it.unina.dietiestates25.entity.AgenziaImmobiliare;
import it.unina.dietiestates25.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class DatiAgenziaImmobiliareResponse {
    private String nomeAzienda;
    private String ragioneSociale;
    private String partitaIva;
    private String fondatore;
    private Set<String> dipendenti;

    public static DatiAgenziaImmobiliareResponse fromEntityToDto(AgenziaImmobiliare entity) {
        return DatiAgenziaImmobiliareResponse.builder()
                .nomeAzienda(entity.getNomeAzienda())
                .ragioneSociale(entity.getRagioneSociale())
                .partitaIva(entity.getPartitaIva())
                .fondatore(entity.getFondatore().getEmail())
                .dipendenti(entity.getDipendenti().stream()
                        .map(User::getEmail) // Mappa ogni User alla sua email
                        .collect(Collectors.toSet())) // Colleziona in un Set
                .build();
    }
}
