package it.unina.dietiestates25.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.AgenziaImmobiliareRequest;
import it.unina.dietiestates25.dto.response.AgenziaImmobiliareResponse;
import it.unina.dietiestates25.service.AgenziaImmobiliareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class AgenziaImmobiliareController {

    private final AgenziaImmobiliareService agenziaImmobiliareService;
    //regsitra la tua agenzia immobiliare
    @PostMapping("/pb/agenzia/")
    @Operation(
            summary = "ADD A NEW AGENCY",
            description = "Method to add a new agency to the database",
            tags = {"Agenzia"})
    public ResponseEntity<String> createAgenzia(@RequestBody AgenziaImmobiliareRequest request) {
        agenziaImmobiliareService.createAgenzia(request);
        return ResponseEntity.ok("Agenzia registrata con successo fai accesso con le credenziali di default: email, admin");
    }

    //restituisce tutte le agenzie
    @GetMapping("/pb/agenzia/")
    @Operation(
            summary = "GET ALL AGENCIES",
            description = "Method to get all agencies from the database",
            tags = {"Agenzia"})
    public ResponseEntity<List<AgenziaImmobiliareResponse>> getAgenzie() {
        return ResponseEntity.ok(agenziaImmobiliareService.getAgenzie());
    }


}
