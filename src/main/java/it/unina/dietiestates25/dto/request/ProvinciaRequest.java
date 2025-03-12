package it.unina.dietiestates25.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProvinciaRequest {
    @NotNull(message = "La latitudine è obbligatoria")
    private Double latitudine;
    @NotNull(message = "La longitudine è obbligatoria")
    private Double longitudine;
    @NotNull @Min(value = 1,message = "Il raggio deve essere espresso in kilometri e maggiore di 1")
    private Double raggio;
}
