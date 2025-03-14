package it.unina.dietiestates25.controller;

import it.unina.dietiestates25.dto.request.CategoriaNotificaRequest;
import it.unina.dietiestates25.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {
    final UserService userService;

    //Modifica Sottoscrizioni
    @PatchMapping("/utente/sottoscrizioni")
    public void modificaSottoscrizioni(CategoriaNotificaRequest request) {
        userService.modificaSottoscrizioni(request);
    }
}
