package it.unina.dietiestates25.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.NewAgentRequest;
import it.unina.dietiestates25.dto.request.AgenziaImmobiliareRequest;
import it.unina.dietiestates25.dto.response.AgenziaImmobiliareResponse;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.service.AgenziaImmobiliareService;
import it.unina.dietiestates25.service.AuthService;
import it.unina.dietiestates25.utils.UserContex;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    @PostMapping("/pb/agenzia/")
    @Operation(
            summary = "AGGIUNGI UNA NUOVA AGENZIA",
            description = "Metodo per aggiungere una nuova agenzia immobiliare nel database",
            tags = {"Agenzia"})
    public ResponseEntity<String> createAgenzia(@RequestBody AgenziaImmobiliareRequest request) {
        return new ResponseEntity<>(agenziaImmobiliareService.createAgenzia(request), HttpStatus.CREATED);
    }

    // Aggiungi un nuovo agente immobiliare (da rendere privato quando il server è funzionante)
    @PostMapping("/v1/addAgent/")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "AGGIUNGI UN NUOVO AGENTE",
            description = "Metodo per aggiungere un nuovo agente immobiliare nel database",
            tags = {"Agenzia"})
    public ResponseEntity<User> createAgente(@RequestBody NewAgentRequest request) {

        return new ResponseEntity<>(authService.createAgente(request), HttpStatus.CREATED);
    }

    // Restituisce tutte le agenzie immobiliari presenti nel database
    @GetMapping("/pb/agenzia/")
    @Operation(
            summary = "OTTIENI TUTTE LE AGENZIE",
            description = "Metodo per ottenere l'elenco di tutte le agenzie immobiliari presenti nel database",
            tags = {"Agenzia"})
    public ResponseEntity<List<AgenziaImmobiliareResponse>> getAgenzie(@RequestParam Pageable pageable) {
        return ResponseEntity.ok(agenziaImmobiliareService.getAgenzie());
    }
}
