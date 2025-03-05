package it.unina.dietiestates25.entity;

import it.unina.dietiestates25.entity.enumeration.TipoContratto;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ContrattoAffitto extends Contratto {

    private Double prezzoAffitto;
    private int tempoMinimo;
    private int tempoMassimo;
    private Double caparra;

    @Builder
    public ContrattoAffitto(Double prezzoAffitto, int tempoMinimo, int tempoMassimo, Double caparra) {
        super(TipoContratto.AFFITTO.toString());
        this.tempoMinimo = tempoMinimo;
        this.tempoMassimo = tempoMassimo;
        this.caparra = caparra;
    }
}