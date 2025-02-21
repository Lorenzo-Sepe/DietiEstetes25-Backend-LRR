package it.unina.dietiestates25.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ImmobileRequest {

    @NotBlank @Size(min = 3, max = 255)
    String indirizzo;

    @NotNull @Min(0)
    Double prezzo;

    @NotBlank @Size(min = 3, max = 255)
    String infoMutuo;

}
