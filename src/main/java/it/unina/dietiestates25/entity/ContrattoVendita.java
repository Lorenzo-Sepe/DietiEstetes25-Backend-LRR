package it.unina.dietiestates25.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ContrattoVendita extends Contratto {

    private String infoMutuo;

    // Constructor matching superclass
    @Builder
    public ContrattoVendita(Double prezzo, String infoMutuo) {
        super(prezzo);
        this.infoMutuo = infoMutuo;
    }
}
