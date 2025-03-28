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
    private boolean balconi = false;

    @Builder.Default
    private boolean garage = false;

    @Builder.Default
    private boolean postiAuto = false;

    @Builder.Default
    private boolean giardino = false;

    @Builder.Default
    private boolean ascensore = false;

    @Builder.Default
    private boolean portiere = false;

    @Builder.Default
    private boolean riscaldamentoCentralizzato = false;

    @Builder.Default
    private boolean climatizzatori = false;

    @Builder.Default
    private boolean pannelliSolari = false;

    @Builder.Default
    private boolean cantina = false;

    @Builder.Default
    private boolean soffitta = false;

    @Builder.Default
    @Column(columnDefinition = "TEXT")
    private String descrizioneAggiuntiva ="";

}