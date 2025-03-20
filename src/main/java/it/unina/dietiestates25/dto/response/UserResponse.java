package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {

    String email;

    String username;

    String urlFotoProfilo;

    public UserResponse fromEntityToDto(User user){
        return UserResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .urlFotoProfilo(user.getUrlFotoProfilo())
                .build();
    }
}
