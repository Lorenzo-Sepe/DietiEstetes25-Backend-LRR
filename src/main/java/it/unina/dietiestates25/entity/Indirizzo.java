package it.unina.dietiestates25.entity;

import it.unina.dietiestates25.entity.enumeration.VicinoA;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Indirizzo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(nullable = false)
    private String via;

    @Column(nullable = false)
    private String numeroCivico;

    @Column(nullable = false)
    private String citta;

    @Column(nullable = false)
    private String cap;

    @Column(nullable = false)
    private String provincia;

    @Column(nullable = false)
    private String nazione;

    @Column(nullable = false)
    private Double latitudine;

    @Column(nullable = false)
    private Double longitudine;

    @ElementCollection(targetClass = VicinoA.class, fetch = FetchType.EAGER)
    private Set<VicinoA> luoghiVicini;
}