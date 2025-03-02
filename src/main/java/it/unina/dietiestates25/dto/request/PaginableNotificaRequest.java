package it.unina.dietiestates25.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginableNotificaRequest {

    private int numeroPagina;

    private int numeroDiElementiPerPagina;

    private boolean isOrdinatiPerDataDesc;
}
