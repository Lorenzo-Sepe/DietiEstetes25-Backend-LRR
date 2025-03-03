package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewDipendeteResponse {
    private User user;
    private String password;
}
