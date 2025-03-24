package it.unina.dietiestates25.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SottoscrizioneNotificaResponse {

    String nomeCategoria;

    String descrizione;

    int idCategoria;

    boolean attivo;
}