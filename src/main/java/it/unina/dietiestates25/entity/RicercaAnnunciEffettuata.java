package it.unina.dietiestates25.entity;

import it.unina.dietiestates25.entity.enumeration.TipoContratto;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class RicercaAnnunciEffettuata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    private User utente;

    private TipologiaImmobile tipologiaImmobile;
    private TipoContratto tipologiaContratto;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> localita;
    private BigDecimal prezzoMin;
    private BigDecimal prezzoMax;
    private LocalDateTime dataRicerca;
}
