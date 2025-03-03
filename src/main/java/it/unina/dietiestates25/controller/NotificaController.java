package it.unina.dietiestates25.controller;

import it.unina.dietiestates25.dto.request.NotificaPromozionaleRequest;
import it.unina.dietiestates25.dto.request.FiltroNotificaRequest;
import it.unina.dietiestates25.dto.response.NotificaResponse;
import it.unina.dietiestates25.service.NotificaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class NotificaController {

    private final NotificaService notificaService;

    @PostMapping("/notifica/")
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<String> inviaNotificaPromozionale(@RequestBody @Valid NotificaPromozionaleRequest request){

        return notificaService.inviaNotificaPromozionale(request);
    }


    @GetMapping("/numeroNotifica")
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<Integer> getNumeroAllNotifiche(){

        return ResponseEntity.ok(notificaService.getNumeroAllNotifiche());
    }

    @GetMapping("/notifiche")
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<List<NotificaResponse>> getNotifiche(@RequestParam int page, @RequestParam int numeroPerPage, boolean discedente){


        FiltroNotificaRequest request = FiltroNotificaRequest.builder()
                .numeroPagina(page)
                .numeroDiElementiPerPagina(numeroPerPage)
                .isOrdinatiPerDataDesc(discedente)
                .build();

        return ResponseEntity.ok(notificaService.getAllNotifiche(request));
    }

    @GetMapping("/numeroNotificheByCategoria")
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<Integer> getNumeroNotiifcaByCategoria(@RequestParam int idCategoria){

        return ResponseEntity.ok(notificaService.getNumeroNotificheByCategoria(idCategoria));
    }

    @GetMapping("/notificheByCategoria")
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<List<NotificaResponse>> getNotificheByCategoria(@RequestParam int page, @RequestParam int numeroPerPage, @RequestParam boolean discedente, @RequestParam int idCategoria){

        FiltroNotificaRequest request = FiltroNotificaRequest.builder()
                .numeroPagina(page)
                .numeroDiElementiPerPagina(numeroPerPage)
                .isOrdinatiPerDataDesc(discedente)
                .idCategoria(idCategoria)
                .build();

        return ResponseEntity.ok(notificaService.getNotificheByCategoria(request));
    }
}
