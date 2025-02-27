package it.unina.dietiestates25.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class Contatto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @NotNull(message = "Il tipo di contatto è obbligatorio")
    @Size(min = 1, max = 50, message = "Il tipo di contatto deve avere tra 1 e 50 caratteri")
    private String tipo;

    @NotNull(message = "Il valore del contatto è obbligatorio")
    @Size(min = 1, max = 255, message = "Il valore del contatto deve avere tra 1 e 255 caratteri")
    private String valore;
}