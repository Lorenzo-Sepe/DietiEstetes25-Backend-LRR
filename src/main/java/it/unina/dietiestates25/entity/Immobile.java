package it.unina.dietiestates25.entity;

import it.unina.dietiestates25.entity.common.CreationUpdate;
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
    private TipologiaImmobile TipologiaImmobile;

    @Min(value = 0, message = "I metri quadri non possono essere negativi")
    @NotNull(message = "I metri quadri sono obbligatori")
    int metriQuadri;

    @Min(value = 0, message = "Il numero di piani non puo essere negativo")
    int numeroDiPiani=0;

    @Min(value = 0, message = "Il numero di stanze non puo' essere un numero negativo")
    int numeroStanze= 0;

    @Min(value = 0, message = "Il numero di bagni non puo' essere un numero negativo")
    int numeroServizi= 0;

    @Enumerated(EnumType.STRING)
    private ClasseEnergetica classeEnergetica;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Indirizzo indirizzo;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CaratteristicheAggiuntive caratteristicheAggiuntive;



}
