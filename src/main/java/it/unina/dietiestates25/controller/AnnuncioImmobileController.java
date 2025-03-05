package it.unina.dietiestates25.controller;


import it.unina.dietiestates25.dto.request.FiltroAnnuncio;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.AnnuncioImmobiliareRequest;
import it.unina.dietiestates25.dto.response.AnnuncioImmobiliareResponse;
import it.unina.dietiestates25.entity.AnnuncioImmobiliare;
import it.unina.dietiestates25.service.AnnuncioImmobileService;
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
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    ResponseEntity<String> creaAnnuncioImmobiliare(@RequestBody AnnuncioImmobiliareRequest request){

        return ResponseEntity.ok(annuncioImmobileService.creaAnnuncioImmobiliare(request));
    }

    /*@GetMapping("/pb/annuncioImmobiliare")
    List<AnnuncioImmobiliareResponse> GetAllAnnunci(){
        return (annuncioImmobileService.cercaAnnunci());
    }*/

    @PostMapping("/pb/annuncioImmobiliare/cerca")
    public List<AnnuncioImmobiliare> cercaAnnunci(@RequestBody FiltroAnnuncio filtro) {
        return annuncioImmobileService.cercaAnnunci(filtro);
    }
}
