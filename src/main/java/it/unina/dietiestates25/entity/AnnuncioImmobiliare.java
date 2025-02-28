package it.unina.dietiestates25.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(nullable = false, length = 100)
    private String titolo;

    @Column(nullable=false,length = 1000)
    private String descrizione;

    private LocalDateTime dataPubblicazione;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User agente;

    @OneToOne
    private Immobile immobile;

    @OneToOne
    private Contratto contratto;

    @OneToMany(mappedBy = "annuncio")
    private List<Proposta> proposte; // Un annuncio può avere molte proposte
}