package it.unina.dietiestates25.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class Notifica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @NotNull(message = "Il contenuto della notifica è obbligatorio")
    private String contenuto;

    @NotNull(message = "La data di creazione è obbligatoria")
    private LocalDateTime dataCreazione;

    @NotNull(message = "Il mittente è obbligatorio")
    private String mittente;

    @NotNull(message = "Il destinatario è obbligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    private User destinatario;
}