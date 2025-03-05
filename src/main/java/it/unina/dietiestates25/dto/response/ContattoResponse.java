package it.unina.dietiestates25.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ContattoResponse {

    String tipo;

    String valore;
}
