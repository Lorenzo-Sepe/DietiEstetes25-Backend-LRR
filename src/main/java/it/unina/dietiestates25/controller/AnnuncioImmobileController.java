package it.unina.dietiestates25.controller;


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
    @PreAuthorize("hasAnyAuthority('AGENT', 'ADMIN')")
    ResponseEntity<String> creaAnnuncioImmobiliare(@ModelAttribute AnnuncioImmobiliareRequest request){

        return ResponseEntity.ok(annuncioImmobileService.creaAnnuncioImmobiliare(request));
    }

    @PostMapping("/pb/annuncioImmobiliare/cerca")
    public ResponseEntity<List<AnnuncioImmobiliareResponse>> cercaAnnunci(@RequestBody FiltroAnnuncio filtro) {

        return ResponseEntity.ok(annuncioImmobileService.cercaAnnunci(filtro));
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
