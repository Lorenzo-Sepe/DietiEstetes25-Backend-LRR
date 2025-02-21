package it.unina.dietiestates25.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ContrattoAffitto extends Contratto {

    private String rateizzazione;

    @Builder
    public ContrattoAffitto(Double prezzo, String rateizzazione) {
        super(prezzo);
        this.rateizzazione = rateizzazione;
    }
}
