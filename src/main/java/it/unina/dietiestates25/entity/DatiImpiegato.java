package it.unina.dietiestates25.entity;

    import jakarta.persistence.*;
    import lombok.*;
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

        @Column(nullable = false, length = 100)
        private String nome;

        @Column(nullable = false, length = 100)
        private String cognome;

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        private User user;

        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
        @JoinColumn(name = "dati_impiegato_id")
        private List<Contatto> contatti;
    }