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

    @Column(nullable = false)
    private double prezzoProposta;

    @Column(nullable = false)
    private Double controproposta;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String cognome;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StatoProposta stato = StatoProposta.IN_TRATTAZIONE;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Contatto contatto;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AnnuncioImmobiliare annuncio; // Molte proposte possono appartenere a un solo annuncio
}