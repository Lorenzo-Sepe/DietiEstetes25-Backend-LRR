package it.unina.dietiestates25.entity;

    import jakarta.persistence.*;
    import lombok.*;
    import jakarta.validation.constraints.*;
    import java.util.List;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
    @Entity
    public class DatiImpiegato {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Include
        private int id;

        @NotNull(message = "Il nome è obbligatorio")
        @Size(min = 1, max = 100, message = "Il nome deve avere tra 1 e 100 caratteri")
        private String nome;

        @NotNull(message = "Il cognome è obbligatorio")
        @Size(min = 1, max = 100, message = "Il cognome deve avere tra 1 e 100 caratteri")
        private String cognome;

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @NotNull(message = "L'utente è obbligatorio")
        private User user;

        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
        @JoinColumn(name = "dati_impiegato_id")
        private List<Contatto> contatti;
    }