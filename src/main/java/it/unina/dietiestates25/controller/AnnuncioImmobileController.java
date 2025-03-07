package it.unina.dietiestates25.controller;


import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.FiltroAnnuncio;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.AnnuncioImmobiliareRequest;
import it.unina.dietiestates25.dto.response.AnnuncioImmobiliareResponse;
import it.unina.dietiestates25.service.AnnuncioImmobileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class AnnuncioImmobileController {

    private final AnnuncioImmobileService annuncioImmobileService;

    @PostMapping("/annuncioImmobiliare")
    @Operation(
            summary = "Crea Annuncio Immobiliare",
            description = "Metodo per creare un nuovo annuncio immobiliare nel database a partire dai dati forniti",
            tags = {"Annuncio Immobile"})
    @PatchMapping("/auth/change_password")
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    ResponseEntity<String> creaAnnuncioImmobiliare(@ModelAttribute AnnuncioImmobiliareRequest request){

        return ResponseEntity.ok(annuncioImmobileService.creaAnnuncioImmobiliare(request));
    }

    @PostMapping("/pb/annuncioImmobiliare/cerca")
    @Operation(
            summary = "Cerca Annuncio Immobiliare",
            description = "Metodo per filtrare gli annunci immobiliari nel databse a partire dai dati forniti",
            tags = {"Annuncio Immobile"})
    public List<AnnuncioImmobiliareResponse> cercaAnnunci(@RequestBody FiltroAnnuncio filtro) {

        return annuncioImmobileService.cercaAnnunci(filtro);
    }

    @DeleteMapping("/annuncioImmobiliare/{id}")
    @Operation(
            summary = "Cancella Annuncio Immobiliare",
            description = "Metodo per rimuovere un annuncio immobiliare nel database",
            tags = {"Annuncio Immobile"})
    @PreAuthorize("hasAnyAuthority('AGENT', 'ADMIN')")
    public ResponseEntity<String> cancellaAnnuncioImmobiliare(@PathVariable int id) {
        annuncioImmobileService.cancellaAnnuncioImmobiliare(id);
        return new ResponseEntity<>("Annuncio eliminato con successo", HttpStatus.OK);
    }

    @PatchMapping("/annuncioImmobiliare/{id}")
    @Operation(
            summary = "Modifica Annuncio Immobiliare",
            description = "Metodo per modificare annuncio immobiliare nel database con i dati forniti",
            tags = {"Annuncio Immobile"})
    @PreAuthorize("hasAnyAuthority('AGENT', 'ADMIN')")
    public ResponseEntity<String> modificaAnnuncioImmobiliare(@PathVariable int id, @RequestBody AnnuncioImmobiliareRequest request) {
        annuncioImmobileService.modificaAnnuncioImmobiliare(id, request);
        return new ResponseEntity<>("Annuncio modificato con successo", HttpStatus.OK);
    }
}
