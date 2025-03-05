package it.unina.dietiestates25.controller;


import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.PropostaRequest;
import it.unina.dietiestates25.service.PropostaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class PropostaController {
    final PropostaService propostaService;

    @PostMapping("/proposta")
    @PreAuthorize("hasAuthority('MEMBER')")
    @Operation(
            summary = "INVIA UNA PROPOSTA",
            description = "Metodo per inviare una proposta di acquisto o affitto di un immobile",
            tags = {"Proposta"})
    public void inviaProposta(@ModelAttribute PropostaRequest request) {
        propostaService.inviaProposta(request);
    }

}
