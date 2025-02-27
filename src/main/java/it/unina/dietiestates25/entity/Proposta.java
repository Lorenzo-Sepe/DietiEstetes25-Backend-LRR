package it.unina.dietiestates25.entity;

import it.unina.dietiestates25.entity.enumeration.StatoProposta;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class Proposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @NotNull(message = "Il prezzo della proposta è obbligatorio")
    @Positive(message = "Il prezzo della proposta deve essere maggiore di 0")
    private double prezzoProposta;

    @Positive(message = "Il prezzo della contro proposta deve essere maggiore di 0")
    private Double controproposta;

    @NotNull(message = "Il nome è obbligatorio")
    @Size(min = 1, max = 100, message = "Il nome deve avere tra 1 e 100 caratteri")
    private String nome;

    @NotNull(message = "Il cognome è obbligatorio")
    @Size(min = 1, max = 100, message = "Il cognome deve avere tra 1 e 100 caratteri")
    private String cognome;

    @NotNull(message = "Lo stato è obbligatorio")
    @Enumerated(EnumType.STRING)
    private StatoProposta stato = StatoProposta.IN_TRATTAZIONE;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Contatto contatto;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AnnuncioImmobiliare annuncio; // Molte proposte possono appartenere a un solo annuncio
}