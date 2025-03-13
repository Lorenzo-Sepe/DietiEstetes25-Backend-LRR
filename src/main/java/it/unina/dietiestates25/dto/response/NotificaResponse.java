package it.unina.dietiestates25.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificaResponse {

    int id;

    private String contenuto;

    private LocalDateTime dataDiCreazione;

    private String mittente;

    private boolean isLetta;
}
