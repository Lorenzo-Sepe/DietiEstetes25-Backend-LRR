package it.unina.dietiestates25.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class CaratteristicheAggiuntive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(nullable = false)
    @Builder.Default
    private final boolean balconi = false;

    @Builder.Default
    private final boolean garage = false;

    @Builder.Default
    private final boolean postiAuto = false;

    @Builder.Default
    private final boolean giardino = false;

    @Builder.Default
    private final boolean ascensore = false;

    @Builder.Default
    private final boolean portiere = false;

    @Builder.Default
    private final boolean riscaldamentoCentralizzato = false;

    @Builder.Default
    private final boolean climatizzatori = false;

    @Builder.Default
    private final boolean pannelliSolari = false;

    @Builder.Default
    private final boolean cantina = false;

    @Builder.Default
    private final boolean soffitta = false;
}