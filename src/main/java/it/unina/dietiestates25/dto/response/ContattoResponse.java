package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.Contatto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ContattoResponse {

    String tipo;
    String valore;

    public static ContattoResponse fromEntityToDto(Contatto contatto){
        return ContattoResponse.builder()
                .tipo(contatto.getTipo())
                .valore(contatto.getValore())
                .build();
    }
}
