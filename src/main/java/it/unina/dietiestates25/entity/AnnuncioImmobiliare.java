package it.unina.dietiestates25.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User agente;

    @OneToOne(cascade = CascadeType.ALL)
    private Immobile immobile;

    @OneToOne(cascade = CascadeType.ALL)
    private Contratto contratto;

    @OneToMany(mappedBy = "annuncio", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proposta> proposte;

    public String getUrl() {
        return "/annuncio/" + id;
    }
}