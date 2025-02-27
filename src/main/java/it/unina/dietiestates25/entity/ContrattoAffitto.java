package it.unina.dietiestates25.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ContrattoAffitto extends Contratto {

    private int tempoMinimo;
    private int tempoMassimo;
    private Double caparra;

    @Builder
    public ContrattoAffitto(Double prezzo, int tempoMinimo, int tempoMassimo, Double caparra) {
        super(prezzo);
        this.tempoMinimo = tempoMinimo;
        this.tempoMassimo = tempoMassimo;
        this.caparra = caparra;
    }
}