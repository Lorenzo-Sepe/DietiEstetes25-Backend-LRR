package it.unina.dietiestates25.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class AnnuncioImmobiliare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @NotNull(message = "Il titolo è obbligatorio")
    @Size(min = 1, max = 255, message = "Il titolo deve avere tra 1 e 255 caratteri")
    private String titolo;

    @NotNull(message = "La descrizione è obbligatoria")
    @Size(min = 1, max = 1000, message = "La descrizione deve avere tra 1 e 1000 caratteri")
    private String descrizione;

    @NotNull(message = "La data di pubblicazione è obbligatoria")
    private LocalDate dataPubblicazione;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull(message = "L'agenta associato all'annuncio e è obbligatorio")
    private User agente;

    @NotNull(message="L' immobile è obbligatorio")
    @OneToOne
    private Immobile immobile;

    @NotNull(message="Il contratto è obbligatorio")
    @OneToOne
    private Contratto contratto;

    @OneToMany(mappedBy = "annuncio")
    private List<Proposta> proposte; // Un annuncio può avere molte proposte
}