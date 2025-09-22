package it.unina.dietiestates25.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContattoRequest {

    String tipo;

    String valore;
}
