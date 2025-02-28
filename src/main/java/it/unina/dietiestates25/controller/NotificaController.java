package it.unina.dietiestates25.controller;

import it.unina.dietiestates25.dto.request.NotificaPromozionaleRequest;
import it.unina.dietiestates25.service.NotificaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class NotificaController {

    private final NotificaService notificaService;

    @PostMapping("/pb/notifica/")
    public ResponseEntity<String> inviaNotificaPromozionale(@RequestBody @Valid NotificaPromozionaleRequest request){

        return notificaService.inviaNotificaPromozionale(request);
    }

    @GetMapping("/pb/notifica")
    public ResponseEntity<Integer> getNumeroAllNotifiche(){

        return notificaService.getNumeroAllNotifiche();
    }
}
