package it.unina.dietiestates25.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.PageableProposte;
import it.unina.dietiestates25.dto.request.PropostaRequest;
import it.unina.dietiestates25.dto.response.PropostaResponse;
import it.unina.dietiestates25.service.PropostaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class PropostaController {
    final PropostaService propostaService;

    @GetMapping("/pb/proposte/")
    public ResponseEntity<List<PropostaResponse>> getProposte(int idAnnuncio, PageableProposte pagebleRequest){
        return ResponseEntity.ok(propostaService.getProposte(idAnnuncio, pagebleRequest));
    }

    @PostMapping("/proposta")
    @PreAuthorize("hasAuthority('MEMBER')")
    @Operation(
            summary = "INVIA UNA PROPOSTA",
            description = "Metodo per inviare una proposta di acquisto o affitto di un immobile",
            tags = {"Proposta"})
    public void inviaProposta(@ModelAttribute PropostaRequest request) {
        propostaService.inviaProposta(request);
    }

    @PostMapping("/proposta/manuale")
    @PreAuthorize("hasAuthority('AGENT')")
    @Operation(
            summary = "INSERISCI UNA PROPOSTA MANUALE",
            description = "Metodo per inserire manualmente una proposta di acquisto o affitto di un immobile",
            tags = {"Proposta"})
    public void inserisciPropostaManuale(@ModelAttribute PropostaRequest request) {
        propostaService.inserisciPropostaManuale(request);
    }

    @PostMapping("/proposta/{id}/controproposta")
    @PreAuthorize("hasAuthority('AGENT')")
    @Operation(
            summary = "AGGIUNGI UNA CONTROPROPOSTA",
            description = "Metodo per aggiungere una controproposta a una proposta esistente",
            tags = {"Proposta"})
    public void aggiungiUnaControProposta(@PathVariable int id, @RequestParam Double controproposta) {
        propostaService.aggiungiUnaControProposta(id, controproposta);
    }

    @PostMapping("/proposta/{id}/accetta")
    @PreAuthorize("hasAuthority('AGENT')")
    @Operation(
            summary = "ACCETTA UNA PROPOSTA",
            description = "Metodo per accettare una proposta esistente",
            tags = {"Proposta"})
    public void accettaProposta(@PathVariable int id) {
        propostaService.accettaProposta(id);
    }

    @PostMapping("/proposta/{id}/rifiuta")
    @PreAuthorize("hasAuthority('AGENT')")
    @Operation(
            summary = "RIFIUTA UNA PROPOSTA",
            description = "Metodo per rifiutare una proposta esistente",
            tags = {"Proposta"})
    public void rifiutaProposta(@PathVariable int id) {
        propostaService.rifiutaProposta(id);
    }
}