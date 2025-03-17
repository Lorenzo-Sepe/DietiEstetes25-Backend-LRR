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

    @PostMapping("/annuncioImmobiliare")
    @Operation(
            summary = "AGGIUNGI UN ANNUNCIO IMMOBILIARE",
            description = "Metodo per aggiungere un nuovo agente immobiliare nel database",
            tags = {"Annuncio Immobiliare"})
    @PreAuthorize("hasAnyAuthority('AGENT')")
    ResponseEntity<String> creaAnnuncioImmobiliare(@ModelAttribute AnnuncioImmobiliareRequest request){

        return ResponseEntity.ok(annuncioImmobileService.creaAnnuncioImmobiliare(request));
    }

    @PostMapping("/pb/annuncioImmobiliare/cerca")
    public ResponseEntity<List<AnnuncioImmobiliareResponse>> cercaAnnunci(@RequestBody FiltroAnnuncio filtro) {

        return ResponseEntity.ok(annuncioImmobileService.cercaAnnunci(filtro));
    }

    @PostMapping("/annuncioImmobiliare/cercaByStaff")
    public ResponseEntity<List<AnnuncioImmobiliareResponse>> cercaAnnuniByStaff(@RequestBody FiltroAnnuncio filtro){

        return ResponseEntity.ok(annuncioImmobileService.cercaAnnunci(filtro));
    }

    private final RicercaAnnunciEffettuataService ricercaAnnunciEffettuataService;
    @PostMapping("/annuncioImmobiliare/cerca")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<List<AnnuncioImmobiliareResponse>> cercaAnnunciConAutentificazinr(@RequestBody FiltroAnnuncio filtro) {
        ricercaAnnunciEffettuataService.salvaRicercaAnnunciEffettuata(filtro);
        return ResponseEntity.ok(annuncioImmobileService.cercaAnnunci(filtro));
    }

    @GetMapping("/annuncioImmobiliare/storicoRicerche/{id}")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<FiltroAnnuncio> getRicerca(@PathVariable int id) {
        return ResponseEntity.ok(ricercaAnnunciEffettuataService.getFiltroRicerca(id));
    }

    //TODO AGGIUNGERE UNA DTO RESPONSE
    @GetMapping("/annuncioImmobiliare/storicoRicerche")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<List<RicercaAnnunciEffettuata>> getStoricoRicerche() {
        return ResponseEntity.ok(ricercaAnnunciEffettuataService.getStoricoRicerche());
    }


    @DeleteMapping("/annuncioImmobiliare/{id}")
    @PreAuthorize("hasAnyAuthority('AGENT', 'ADMIN')")
    public ResponseEntity<String> cancellaAnnuncioImmobiliare(@PathVariable int id) {

        return ResponseEntity.ok(annuncioImmobileService.cancellaAnnuncioImmobiliare(id));
    }

    @PatchMapping("/annuncioImmobiliare/{id}")
    @PreAuthorize("hasAnyAuthority('AGENT', 'ADMIN')")
    public ResponseEntity<String> modificaAnnuncioImmobiliare(@PathVariable int id, @ModelAttribute AnnuncioImmobiliareRequest request) {

        return ResponseEntity.ok(annuncioImmobileService.modificaAnnuncioImmobiliare(id, request));
    }
}
