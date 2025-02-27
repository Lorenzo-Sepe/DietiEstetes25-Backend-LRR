package it.unina.dietiestates25.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ContrattoVendita extends Contratto {

    private boolean mutuoEstinto;

    // Constructor matching superclass
    @Builder
    public ContrattoVendita(Double prezzo, boolean mutuoEstinto) {
        super(prezzo);
        this.mutuoEstinto = mutuoEstinto;
    }
}
