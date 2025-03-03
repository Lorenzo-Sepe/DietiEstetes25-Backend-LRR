package it.unina.dietiestates25.dto.request.agenziaImmobiliare;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AnnuncioImmobiliareRequest {

    @NotBlank(message = "Il titolo è obbligatorio")
    @Size(min = 1, max = 255, message = "Il titolo deve avere tra 1 e 255 caratteri")
    private String titolo;

    @NotBlank(message = "La descrizione è obbligatoria")
    @Size(min = 1, max = 1000, message = "La descrizione deve avere tra 1 e 1000 caratteri")
    private String descrizione;

    @NotNull(message = "La data di pubblicazione è obbligatoria")
    private LocalDate dataPubblicazione;

    @NotNull(message = "L'agente associato all'annuncio è obbligatorio")
    private Integer agenteId; // Presupponendo che tu abbia un ID per l'agente

    @NotNull(message = "L'immobile è obbligatorio")
    private ImmobileRequest immobile; // DTO per l'immobile

    @NotNull(message = "Il contratto è obbligatorio")
    private ContrattoRequest contratto; // DTO per il contratto

    // Aggiungi ulteriori campi se necessario
}


@Getter
@Setter
class IndirizzoRequest {

    @NotBlank(message = "La via è obbligatoria")
    private String via;

    @NotBlank(message = "Il numero civico è obbligatorio")
    private String numeroCivico;

    @NotBlank(message = "La città è obbligatoria")
    private String citta;

    @NotBlank(message = "Il CAP è obbligatorio")
    private String cap;

    @NotBlank(message = "La provincia è obbligatoria")
    private String provincia;

    @NotBlank(message = "La nazione è obbligatoria")
    private String nazione;

    @NotNull(message = "La latitudine è obbligatoria")
    private Double latitudine;

    @NotNull(message = "La longitudine è obbligatoria")
    private Double longitudine;
}

@Getter
@Setter
class CaratteristicheAggiuntiveRequest {

    private boolean balconi = false;
    private boolean garage = false;
    private boolean postiAuto = false;
    private boolean giardino = false;
    private boolean ascensore = false;
    private boolean portiere = false;
    private boolean riscaldamentoCentralizzato = false;
    private boolean climatizzatori = false;
    private boolean pannelliSolari = false;
    private boolean cantina = false;
    private boolean soffitta = false;
}