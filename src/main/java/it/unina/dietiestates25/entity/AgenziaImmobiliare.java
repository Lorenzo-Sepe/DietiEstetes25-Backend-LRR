package it.unina.dietiestates25.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class AgenziaImmobiliare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(nullable = false, length = 100)
    private String nomeAzienda;

    @Column(nullable = false, length = 80)
    private String ragioneSociale;

    @Column(nullable = false, length = 20, unique = true)
    private String dominio;

    private String partitaIva;

    @OneToOne(cascade = CascadeType.ALL)
    private User fondatore;

    @OneToMany( cascade = CascadeType.ALL)
    private Set<User> dipendenti;

    public void addDipendente(User dipendente) {
        if (this.dipendenti == null) {
            this.dipendenti = new HashSet<>();
        }
        this.dipendenti.add(dipendente);
    }

}