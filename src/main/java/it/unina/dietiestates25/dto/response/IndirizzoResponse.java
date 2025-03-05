package it.unina.dietiestates25.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndirizzoResponse {
    private int id;
    private String via;
    private String numeroCivico;
    private String citta;
    private String cap;
    private String provincia;
    private String nazione;
    private Double latitudine;
    private Double longitudine;
}