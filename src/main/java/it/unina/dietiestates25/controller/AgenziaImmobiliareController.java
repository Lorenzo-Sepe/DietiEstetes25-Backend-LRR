package it.unina.dietiestates25.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.NewAgentRequest;
import it.unina.dietiestates25.dto.request.AgenziaImmobiliareRequest;
import it.unina.dietiestates25.dto.response.AgenziaImmobiliareResponse;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.service.AgenziaImmobiliareService;
import it.unina.dietiestates25.service.AuthService;
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

    //regsitra la tua agenzia immobiliare
    @PostMapping("/pb/agenzia/")
    @Operation(
            summary = "ADD A NEW AGENCY",
            description = "Method to add a new agency to the database",
            tags = {"Agenzia"})
    public ResponseEntity<String> createAgenzia(@RequestBody AgenziaImmobiliareRequest request) {

        return new ResponseEntity<String>(agenziaImmobiliareService.createAgenzia(request),HttpStatus.CREATED);
    }

    //da cambiare in privato quando il server è funzionante
    @PostMapping("/v1/addAgent/")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "ADD A NEW AGENT",
            description = "Method to add a new agent to the database",
            tags = {"Agenzia"})
    public ResponseEntity<User> createAgente(@RequestBody NewAgentRequest request,
                                             @AuthenticationPrincipal UserDetails userDetails) {

        return new ResponseEntity<User>(authService.createAgente(request, userDetails), HttpStatus.CREATED);
    }

    //restituisce tutte le agenzie
    @GetMapping("/pb/agenzia/")
    @Operation(
            summary = "GET ALL AGENCIES",
            description = "Method to get all agencies from the database",
            tags = {"Agenzia"})
    public ResponseEntity<List<AgenziaImmobiliareResponse>> getAgenzie(@RequestParam Pageable pageable) {
        return ResponseEntity.ok(agenziaImmobiliareService.getAgenzie());
    }



}
