package it.unina.dietiestates25.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)


public class ImmaginiImmobile implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(nullable = true)
    private String url;

    private String descrizione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "immobile_id")
    private Immobile immobile;

    public String getUrl(){
        return Objects.requireNonNullElse(url, "");
    }
}