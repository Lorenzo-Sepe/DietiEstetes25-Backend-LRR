package it.unina.dietiestates25.entity;

import it.unina.dietiestates25.entity.common.CreationUpdate;
import it.unina.dietiestates25.entity.enumeration.TipoContratto;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class RicercaAnnunciEffettuata extends CreationUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private User utente;

    private TipologiaImmobile tipologiaImmobile;

    private TipoContratto tipologiaContratto;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> locality;

    private BigDecimal prezzoMin;
    private BigDecimal prezzoMax;

    @Column(columnDefinition = "TEXT", length = 65535)
    private String filtroUsatoJson;
}
