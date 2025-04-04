package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
<<<<<<<< HEAD:src/main/java/it/unina/dietiestates25/dto/response/UserInfoReponse.java
public class UserInfoReponse {
========
public class UserPrivateInfoResponse {
>>>>>>>> 3ede9f3833fa7f2d8b953b2bf0a8ab5b8cadce8e:src/main/java/it/unina/dietiestates25/dto/response/UserPrivateInfoResponse.java
    private String UrlFotoProfilo;
    private String email;
    private String nomeVisualizzato;
    private String tipoAccount;
<<<<<<<< HEAD:src/main/java/it/unina/dietiestates25/dto/response/UserInfoReponse.java

    public static UserInfoReponse fromEntityToDto(User user) {
        return UserInfoReponse.builder()
========
    public static UserPrivateInfoResponse fromEntityToDto(User user) {
        return UserPrivateInfoResponse.builder()
>>>>>>>> 3ede9f3833fa7f2d8b953b2bf0a8ab5b8cadce8e:src/main/java/it/unina/dietiestates25/dto/response/UserPrivateInfoResponse.java
                .UrlFotoProfilo(user.getUrlFotoProfilo())
                .email(user.getEmail())
                .nomeVisualizzato(user.getNomeVisualizzato())
                .tipoAccount(user.getAuthority().getAuthorityName().name())
                .build();
    }
}
