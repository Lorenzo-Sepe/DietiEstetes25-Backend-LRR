package it.unina.dietiestates25.entity;

import it.unina.dietiestates25.entity.common.CreationUpdate;
import it.unina.dietiestates25.entity.enumeration.ClasseEnergetica;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import jakarta.persistence.*;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "immobile")
    private List<ImmaginiImmobile> immagini;

    @Enumerated(EnumType.STRING)
    private TipologiaImmobile tipologiaImmobile;

    @Column(nullable = false)
    int metriQuadri;

    @Column(nullable = false)
    @Builder.Default
    int numeroDiPiani=0;

    @Column(nullable = false)
    @Builder.Default
    int numeroStanze = 0;

    @Column(nullable = false)
    @Builder.Default
    int numeroServizi= 0;

    @Enumerated(EnumType.STRING)
    private ClasseEnergetica classeEnergetica;

    @OneToOne(cascade = CascadeType.ALL)
    private Indirizzo indirizzo;

    @OneToOne(cascade = CascadeType.ALL)
    private CaratteristicheAggiuntive caratteristicheAggiuntive;
}
