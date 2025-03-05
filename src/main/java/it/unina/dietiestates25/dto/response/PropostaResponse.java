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
    private String nome;
    private String cognome;
    private StatoProposta stato;
    private int userId;
    private int contattoId;
    private int annuncioId;
}