package it.unina.dietiestates25.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.FiltroAnnuncioDTO;
import it.unina.dietiestates25.entity.RicercaAnnunciEffettuata;
import it.unina.dietiestates25.service.RicercaAnnunciEffettuataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class StoricoRicercaController {

    private final RicercaAnnunciEffettuataService ricercaAnnunciEffettuataService;

    @GetMapping(value = "/storicoRicerche")
    @Operation(
            summary = "OTTIENI STORICO RICERCHE",
            description = "Metodo per ottenere lo storico delle ricerche effettuate da un singolo utente",
            tags = {"Storico Ricerche"})
    public ResponseEntity<List<RicercaAnnunciEffettuata>> getStoricoRicerche() {
        return ResponseEntity.ok(ricercaAnnunciEffettuataService.getStoricoRicerche());
    }

    @GetMapping("/storicoRicerche/{id}")
    @Operation(
            summary = "OTTIENI STORICO RICERCHE DATO UTENTE",
            description = "Metodo per ottenere lo storico delle ricerche effettuate di un utente",
            tags = {"Storico Ricerche"})
    public ResponseEntity<FiltroAnnuncioDTO> getRicerca(@PathVariable int id ) {
        return ResponseEntity.ok(ricercaAnnunciEffettuataService.getFiltroRicerca(id));
    }
}
