package it.unina.dietiestates25.controller;

import it.unina.dietiestates25.dto.request.AgenziaImmobiliareRequest;
import it.unina.dietiestates25.entity.AgenziaImmobiliare;
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
    //regstra la tua agenzia immobiliare
    @PostMapping("/pb/agenzia/")
    public ResponseEntity<String> createAgenzia(@RequestBody AgenziaImmobiliareRequest request) {
        agenziaImmobiliareService.createAgenzia(request);
        return ResponseEntity.ok("Agenzia registrata con successo fai accesso con le credenziali di default: email, admin");
    }
    //restituiesce tutte le agenzie
    @GetMapping("/pb/agenzia/")
    public ResponseEntity<List<AgenziaImmobiliare>> getAgenzia() {
        return ResponseEntity.ok(agenziaImmobiliareService.FindAllAgenzie());
    }


}
