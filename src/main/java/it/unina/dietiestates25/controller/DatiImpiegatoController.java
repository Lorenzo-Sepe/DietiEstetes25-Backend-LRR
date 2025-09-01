package it.unina.dietiestates25.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.ContattoRequest;
import it.unina.dietiestates25.dto.response.ContattoResponse;
import it.unina.dietiestates25.service.DatiImpiegatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DatiImpiegatoController {

    private final DatiImpiegatoService datiImpiegatoService;

    @PostMapping("agenzia/dipendente/contatto")
    @Operation(
            summary = "AGGIUNGE UN CONTATTO",
            description = "Metodo per aggiungere o modifcare un contatto di un agente",
            tags = {"Agenzia"})
    public ResponseEntity<List<ContattoResponse>> nuovoContatto(@RequestBody ContattoRequest request){

        return ResponseEntity.ok(datiImpiegatoService.aggiungiContatto(request));
    }
}
