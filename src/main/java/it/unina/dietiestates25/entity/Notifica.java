package it.unina.dietiestates25.entity;

import jakarta.persistence.*;
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

    @Column(nullable = false,columnDefinition = "TEXT")
    @Lob
    private String contenuto;

    @Column(nullable = false)
    private LocalDateTime dataCreazione;

    @Column(nullable = false)
    private String mittente;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private User destinatario;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoriaNotifica categoria;

    private boolean isLetta;
}