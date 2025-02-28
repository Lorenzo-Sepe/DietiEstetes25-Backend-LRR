package it.unina.dietiestates25.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgenziaImmobiliareResponse {
    int id;
    String nomeAzienda;
    String ragioneSociale;
    String partitaIva;
    String fondatore;
}
