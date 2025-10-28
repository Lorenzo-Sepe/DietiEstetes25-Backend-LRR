package it.unina.dietiestates25.dto.response.impiegato;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CredenzialiDefaultResponse {

    private String email;
    private String password;
}
