package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.enumeration.StatoProposta;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropostaResponse {
    private double prezzoProposta;
    private Double controproposta;
    private String stato;
    private UserResponse utente;
    private ContattoResponse contatto;
}