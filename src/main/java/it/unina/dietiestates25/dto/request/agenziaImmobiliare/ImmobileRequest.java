package it.unina.dietiestates25.dto.request.agenziaImmobiliare;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ImmobileRequest {

    @NotNull(message = "La tipologia di immobile è obbligatoria")
    private String tipologiaImmobile; // Potresti usare un enum o una stringa

    @Min(value = 0, message = "I metri quadri non possono essere negativi")
    @NotNull(message = "I metri quadri sono obbligatori")
    private int metriQuadri;

    @Min(value = 0, message = "Il numero di piani non può essere negativo")
    private int numeroDiPiani = 0;

    @Min(value = 0, message = "Il numero di stanze non può essere un numero negativo")
    private int numeroStanze = 0;

    @Min(value = 0, message = "Il numero di bagni non può essere un numero negativo")
    private int numeroServizi = 0;

    @NotNull(message = "La classe energetica è obbligatoria")
    private String classeEnergetica; // Potresti usare un enum o una stringa

    @NotNull(message = "L'indirizzo è obbligatorio")
    private IndirizzoRequest indirizzo; // DTO per l'indirizzo

    private CaratteristicheAggiuntiveRequest caratteristicheAggiuntive; // DTO per le caratteristiche aggiuntive

    private List<String> immagini; // Lista di URL delle immagini
}

