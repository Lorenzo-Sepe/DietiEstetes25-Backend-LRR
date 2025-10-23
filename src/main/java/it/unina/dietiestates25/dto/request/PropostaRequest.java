package it.unina.dietiestates25.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data

public class PropostaRequest {
    @Schema(description = "ID dell'annuncio Immobiliare", required = true)
    private int annuncioId;

    @Schema(description = "Nome del proponente")
    private String nome;

    @Schema(description = "Cognome del proponente")
    private String cognome;

    @Schema(description = "Prezzo della proposta", example = "250000.00")
    private Double prezzo;

    @Schema(description = "Tipo di contatto (es. TELEFONO, EMAIL)")
    private TipoContatto tipoContatto;

    @Schema(description = "Informazioni di contatto (es. numero di telefono, email)")
    private String informazioniContatto;

    // Enum per i tipi di contatto
    public enum TipoContatto {
        TELEFONO,
        EMAIL,
        MESSAGGIO
    }
}

