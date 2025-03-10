package it.unina.dietiestates25.entity;

import it.unina.dietiestates25.entity.enumeration.StatoProposta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


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

    private Double controproposta;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String cognome;

    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private StatoProposta stato;

    @ManyToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.LAZY)
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private Contatto contatto;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private AnnuncioImmobiliare annuncio; // Molte proposte possono appartenere a un solo annuncio

    private LocalDateTime dataDellaProposta;
}