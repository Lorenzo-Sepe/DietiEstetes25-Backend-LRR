package it.unina.dietiestates25.controller;

import it.unina.dietiestates25.dto.request.NotificaPromozionaleRequest;
import it.unina.dietiestates25.dto.request.PaginableNotificaRequest;
import it.unina.dietiestates25.dto.response.NotificaResponse;
import it.unina.dietiestates25.service.NotificaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class NotificaController {

    private final NotificaService notificaService;

    @PostMapping("/pb/notifica/")
    public ResponseEntity<String> inviaNotificaPromozionale(@RequestBody @Valid NotificaPromozionaleRequest request){

        return notificaService.inviaNotificaPromozionale(request);
    }

    @GetMapping("/pb/numeroNotifica")
    public ResponseEntity<Integer> getNumeroAllNotifiche(){

        return ResponseEntity.ok(notificaService.getNumeroAllNotifiche());
    }

    @GetMapping("/pb/notifiche")
    public ResponseEntity<List<NotificaResponse>> getNotifiche(@RequestParam int page, @RequestParam int numeroPerPage, boolean discedente){


        PaginableNotificaRequest request = PaginableNotificaRequest.builder()
                .numeroPagina(page)
                .numeroDiElementiPerPagina(numeroPerPage)
                .isOrdinatiPerDataDesc(discedente)
                .build();

        return ResponseEntity.ok(notificaService.getAllNotifiche(request));
    }
}
