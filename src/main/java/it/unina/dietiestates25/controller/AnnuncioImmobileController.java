package it.unina.dietiestates25.controller;


import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.FiltroAnnuncio;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.AnnuncioImmobiliareRequest;
import it.unina.dietiestates25.dto.response.AnnuncioImmobiliareResponse;
import it.unina.dietiestates25.entity.RicercaAnnunciEffettuata;
import it.unina.dietiestates25.service.AnnuncioImmobileService;
import it.unina.dietiestates25.service.RicercaAnnunciEffettuataService;
import lombok.RequiredArgsConstructor;
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
    private final RicercaAnnunciEffettuataService ricercaAnnunciEffettuataService;

    @PostMapping("/annuncioImmobiliare")
    @Operation(
            summary = "AGGIUNGI UN ANNUNCIO IMMOBILIARE",
            description = "Metodo per aggiungere un nuovo annuncio immobiliare nel database",
            tags = {"Annuncio Immobiliare"})
    @PreAuthorize("hasAnyAuthority('AGENT')")
    ResponseEntity<String> creaAnnuncioImmobiliare(@ModelAttribute AnnuncioImmobiliareRequest request){

        return ResponseEntity.ok(annuncioImmobileService.creaAnnuncioImmobiliare(request));
    }

    
    @PostMapping("/pb/annuncioImmobiliare/cerca")
    @Operation(
            summary = "CERCA ANNUNCI IMMOBILIARI",
            description = "Metodo per ottenere la lista di annunci immobiliari dal database",
            tags = {"Annuncio Immobiliare"})
    public ResponseEntity<List<AnnuncioImmobiliareResponse>> cercaAnnunci(@RequestBody FiltroAnnuncio filtro) {

        return ResponseEntity.ok(annuncioImmobileService.cercaAnnunci(filtro));
    }

    @PostMapping ("/pb/annuncioImmobiliare/numero")
    @Operation(
            summary = "OTTIENI NUMERO ANNUNCI IMMOBILIARI",
            description = "Metodo per ottenere il numero di annunci immobiliari dal database a partire da filtri",
            tags = {"Annuncio Immobiliare"})
    public ResponseEntity<Long> getNumeroAnnunci(@RequestBody FiltroAnnuncio filtro){

        return ResponseEntity.ok(annuncioImmobileService.getNumeroAnnunci(filtro));
    }

    @PostMapping("/annuncioImmobiliare/cercaByStaff")
    @Operation(
            summary = "CERCA ANNUNCI IMMOBILIARI STAFF",
            description = "Metodo per ottenere la lista di annunci immobiliari dal database esclusivo per Impiegati",
            tags = {"Annuncio Immobiliare"})
    public ResponseEntity<List<AnnuncioImmobiliareResponse>> cercaAnnunciByStaff(@RequestBody FiltroAnnuncio filtro){

        return ResponseEntity.ok(annuncioImmobileService.cercaAnnunci(filtro));
    }

    @GetMapping("/annuncioImmobiliare/numeroByStaff")
    @Operation(
            summary = "OTTIENI NUMERO ANNUNCI IMMOBILIARI STAFF",
            description = "Metodo per ottenere il numero di annunci immobiliari dal database a partire da filtri esclusivo per Impiegati",
            tags = {"Annuncio Immobiliare"})
    public ResponseEntity<Long> getNumeroAnnunciByStaff(){

        return ResponseEntity.ok(annuncioImmobileService.getNumeroAnnunci(null));
    }

    @PostMapping("/annuncioImmobiliare/cerca")
    @Operation(
            summary = "CERCA ANNUNCI IMMOBILIARI CON AUTENTICAZIONE",
            description = "Metodo per ottenere la lista di annunci immobiliari dal database con autenticazione",
            tags = {"Annuncio Immobiliare"})
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<List<AnnuncioImmobiliareResponse>> cercaAnnunciConAutenticazione(@RequestBody FiltroAnnuncio filtro) {
        ricercaAnnunciEffettuataService.salvaRicercaAnnunciEffettuata(filtro);
        return ResponseEntity.ok(annuncioImmobileService.cercaAnnunci(filtro));
    }

    @GetMapping("/annuncioImmobiliare/storicoRicerche/{id}")
    @Operation(
            summary = "OTTIENI STORICO RICERCHE DATO UTENTE",
            description = "Metodo per ottenere lo storico delle ricerche effettuate di un utente",
            tags = {"Annuncio Immobiliare"})
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<FiltroAnnuncio> getRicerca(@PathVariable int id) {
        return ResponseEntity.ok(ricercaAnnunciEffettuataService.getFiltroRicerca(id));
    }

    //TODO AGGIUNGERE UNA DTO RESPONSE
    @GetMapping("/annuncioImmobiliare/storicoRicerche")
    @Operation(
            summary = "OTTIENI STORICO RICERCHE",
            description = "Metodo per ottenere lo storico delle ricerche effettuate",
            tags = {"Annuncio Immobiliare"})
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<List<RicercaAnnunciEffettuata>> getStoricoRicerche() {
        return ResponseEntity.ok(ricercaAnnunciEffettuataService.getStoricoRicerche());
    }


    @DeleteMapping("/annuncioImmobiliare/{id}")
    @Operation(
            summary = "CANCELLA ANNUNCIO IMMOBILIARE",
            description = "Metodo per cancellare un annuncio immobiliare dal database",
            tags = {"Annuncio Immobiliare"})
    @PreAuthorize("hasAnyAuthority('AGENT', 'MANAGER')")
    public ResponseEntity<String> cancellaAnnuncioImmobiliare(@PathVariable int id) {

        return ResponseEntity.ok(annuncioImmobileService.cancellaAnnuncioImmobiliare(id));
    }

    @PatchMapping("/annuncioImmobiliare/{id}")
    @Operation(
            summary = "MODIFICA ANNUNCIO IMMOBILIARE",
            description = "Metodo per modificare un annuncio immobiliare nel database",
            tags = {"Annuncio Immobiliare"})
    @PreAuthorize("hasAnyAuthority('AGENT', 'MANAGER')")
    public ResponseEntity<String> modificaAnnuncioImmobiliare(@PathVariable int id, @ModelAttribute AnnuncioImmobiliareRequest request) {

        return ResponseEntity.ok(annuncioImmobileService.modificaAnnuncioImmobiliare(id, request));
    }

    @GetMapping("/pb/annuncioImmobiliare/{id}")
    @Operation(
            summary = "OTTIENI ANNUNCIO IMMOBILIARE DA ID",
            description = "Metodo per ottenere un annuncio immobiliare dal database",
            tags = {"Annuncio Immobiliare"})
    public ResponseEntity<AnnuncioImmobiliareResponse> getAnnuncioImmobiliare(@PathVariable int id) {
        return ResponseEntity.ok(annuncioImmobileService.getAnnuncioImmobiliare(id));
    }
}
