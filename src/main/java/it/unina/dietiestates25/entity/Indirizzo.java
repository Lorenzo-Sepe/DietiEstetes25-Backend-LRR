package it.unina.dietiestates25.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Indirizzo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @NotNull(message = "La via è obbligatoria")
    private String via;

    @NotNull(message = "Il numero civico è obbligatorio")
    private String numeroCivico;

    @NotNull(message = "La città è obbligatoria")
    private String citta;

    @NotNull(message = "Il CAP è obbligatorio")
    private String cap;

    @NotNull(message = "La provincia è obbligatoria")
    private String provincia;

    @NotNull(message = "La nazione è obbligatoria")
    private String nazione;

    @NotNull(message = "La latitudine è obbligatoria")
    private Double latitudine;

    @NotNull(message = "La longitudine è obbligatoria")
    private Double longitudine;
}