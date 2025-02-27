package it.unina.dietiestates25.entity;

import it.unina.dietiestates25.entity.common.CreationUpdate;
import it.unina.dietiestates25.entity.enumeration.ClasseEnergetica;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Immobile extends CreationUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "immobile")
    private List<ImmaginiImmobile> immagini;

    @NotNull(message = "La tipologia di immobile è obbligatorio")
    @Enumerated(EnumType.STRING)
    private TipologiaImmobile tipologiaImmobile;

    @Column(nullable = false)
    int metriQuadri;

    @Column(nullable = false)
    int numeroDiPiani=0;

    @Column(nullable = false)
    int numeroStanze= 0;

    @Column(nullable = false)
    int numeroServizi= 0;

    @Enumerated(EnumType.STRING)
    private ClasseEnergetica classeEnergetica;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Indirizzo indirizzo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CaratteristicheAggiuntive caratteristicheAggiuntive;
}
