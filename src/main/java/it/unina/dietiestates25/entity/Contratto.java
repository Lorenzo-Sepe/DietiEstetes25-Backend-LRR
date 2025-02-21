package it.unina.dietiestates25.entity;

import it.unina.dietiestates25.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Keeps the hierarchy correct
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Allows subclass instantiation
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public abstract class Contratto extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(nullable = false)
    private Double prezzo;

    // Constructor for subclasses to use
    protected Contratto(Double prezzo) {
        this.prezzo = prezzo;
    }
}
