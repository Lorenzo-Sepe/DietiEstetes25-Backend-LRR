package it.unina.dietiestates25.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.AgenziaImmobiliareRequest;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.DipendenteRequest;
import it.unina.dietiestates25.dto.response.AgenziaImmobiliareResponse;
import it.unina.dietiestates25.dto.response.impiegato.DatiAgenziaImmobiliareResponse;
import it.unina.dietiestates25.service.AgenziaImmobiliareService;
import it.unina.dietiestates25.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class AgenziaImmobiliareController {
    private final AgenziaImmobiliareService agenziaImmobiliareService;
    private final AuthService authService;

    // Registra una nuova agenzia immobiliare
    @PostMapping("/pb/agenzia/" )
    @Operation(
            summary = "INSERISCI LA TUA AGENZIA IMMOBILIARE",
            description = "Metodo per aggiungere una nuova agenzia immobiliare nel database",
            tags = {"Agenzia"})
    public ResponseEntity<String> createAgenzia(@RequestBody AgenziaImmobiliareRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.TEXT_PLAIN)
                .body(agenziaImmobiliareService.createAgenzia(request));
    }

    @PostMapping("/agenzia/dipendete")
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(
            summary = "AGGIUNGI UN NUOVO AGENTE",
            description = "Metodo per aggiungere un nuovo agente immobiliare nel database",
            tags = {"Agenzia"})
    public ResponseEntity<String> createAgente(@ModelAttribute DipendenteRequest request) {

        return new ResponseEntity<>(agenziaImmobiliareService.aggiungiDipendete(request), HttpStatus.CREATED);
    }


    @GetMapping("/pb/agenzia/")
    @Operation(
            summary = "OTTIENI TUTTE LE AGENZIE",
            description = "Metodo per ottenere l'elenco di tutte le agenzie immobiliari presenti nel database",
            tags = {"Agenzia"})
    public ResponseEntity<List<AgenziaImmobiliareResponse>> getAgenzie(@ModelAttribute Pageable pageable) {
        return ResponseEntity.ok(agenziaImmobiliareService.getAgenzie());
    }

    @GetMapping("/pb/agenzia/email")
    @Operation(
            summary = "OTTIENI AGENZIA IMMOBIlIARE ASSOCIATA A UN DIPENDENTE TRAMITE EMAIL",
            description = "Metodo per ottenere un'agenzia immobiliare in base all'email",
            tags = {"Agenzia"})
    public ResponseEntity<DatiAgenziaImmobiliareResponse> getAgenziaByEmail(@RequestParam String email) {
            return ResponseEntity.ok(agenziaImmobiliareService.getAgenziaByEmailImpiegato(email));

    }

}
