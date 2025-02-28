package it.unina.dietiestates25.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificaPromozionaleRequest {

    @NotBlank
    private String contenuto;

    @NotBlank
    private String areaDiInteresse;

    private String tipoDiContrattoDiInteresse;

    private String tipologiaDiImmobileDiInteresse;

}
