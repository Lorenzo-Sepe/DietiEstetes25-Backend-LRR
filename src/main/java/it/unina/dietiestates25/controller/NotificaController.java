package it.unina.dietiestates25.controller;

import io.swagger.v3.oas.annotations.Operation;
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

    @PostMapping("/notifica")
    @Operation(
            summary = "Invia Notifica Promozionale",
            description = "Metodo per creare un nuovo notifica promozionale e inviarla a chi è interessato",
            tags = {"Notifiche"})
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    public ResponseEntity<String> inviaNotificaPromozionale(@RequestBody @Valid NotificaPromozionaleRequest request){

        return notificaService.inviaNotificaPromozionale(request);
    }


    @GetMapping("/numeroNotifica")
    @Operation(
            summary = "Ricevi Numero Notifiche",
            description = "Metodo per contare il numero di notifiche presenti nel database",
            tags = {"Notifiche"})
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<Integer> getNumeroAllNotifiche(){

        return ResponseEntity.ok(notificaService.getNumeroAllNotifiche());
    }


    @GetMapping("/notifiche")
    @Operation(
            summary = "Ricevi Notifiche",
            description = "Metodo per ottenere tutte le notifiche presenti nel database",
            tags = {"Notifiche"})
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
    @Operation(
            summary = "Numero Notifiche per Categoria",
            description = "Metodo per contare il numero di notifiche presenti nel database per una determinata categoria",
            tags = {"Notifiche"})
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<Integer> getNumeroNotificaByCategoria(@RequestParam int idCategoria){

        return ResponseEntity.ok(notificaService.getNumeroNotificheByCategoria(idCategoria));
    }

    @GetMapping("/notificheByCategoria")
    @Operation(
            summary = "Notifiche per Categoria",
            description = "Metodo per ottenere tutte le notifiche presenti nel database per una determinata categoria",
            tags = {"Notifiche"})
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

    @PatchMapping("/checkNotifica")
    @Operation(
            summary = "Set Visualizzazione Notifica",
            description = "Metodo per settare a true il campo letto della notifica",
            tags = {"Notifiche"})
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<String> setTrueVisualizzazioneNotifica(@RequestParam int idNotifica){

       return ResponseEntity.ok(notificaService.setTrueVisualizzazioneNotifica(idNotifica));
    }
}
