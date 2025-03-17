package it.unina.dietiestates25.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotificaPromozionaleRequest {

    @NotBlank
    private String contenuto;

    CriteriDiRicercaUtenti criteridiRicerca;
}
