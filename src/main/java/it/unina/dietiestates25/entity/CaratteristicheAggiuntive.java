package it.unina.dietiestates25.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    private boolean balconi = false;

    private boolean garage = false;

    private boolean postiAuto = false;

    private boolean giardino = false;

    private boolean ascensore = false;

    private boolean portiere = false;

    private boolean riscaldamentoCentralizzato = false;

    private boolean climatizzatori = false;

    private boolean pannelliSolari = false;

    private boolean cantina = false;

    private boolean soffitta = false;
}