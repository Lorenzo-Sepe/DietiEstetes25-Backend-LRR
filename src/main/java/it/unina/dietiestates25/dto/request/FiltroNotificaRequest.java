package it.unina.dietiestates25.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FiltroNotificaRequest {

    private int numeroPagina;

    private int numeroDiElementiPerPagina;

    private boolean isOrdinatiPerDataDesc;

    private int idCategoria;
}
