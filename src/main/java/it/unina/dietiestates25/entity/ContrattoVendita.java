package it.unina.dietiestates25.entity;

import it.unina.dietiestates25.entity.enumeration.TipoContratto;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ContrattoVendita extends Contratto {

    private Double prezzoVendita;
    private boolean mutuoEstinto;

    @Builder
    public ContrattoVendita(Double prezzoVendita, boolean mutuoEstinto) {
        super(TipoContratto.VENDITA.toString());
        this.mutuoEstinto = mutuoEstinto;
    }
}
