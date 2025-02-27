package it.unina.dietiestates25.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    @Size(min = 1, max = 100, message = "Il nome dell'azienda non può essere vuoto e deve avere al massimo 100 caratteri")
    private String nomeAzienda;

    @Size(min = 1, max = 80, message = "La ragione sociale non può essere vuota e deve avere al massimo 80 caratteri")
    private String ragioneSociale;

    @Pattern(regexp = "\\d{11}", message = "La partita IVA deve essere composta esattamente da 11 numeri")
    private String partitaIva;

    @NotNull(message = "Il fondatore è obbligatorio")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User fondatore;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> dipendenti;   


}