package it.unina.dietiestates25.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.ProvinciaRequest;
import it.unina.dietiestates25.service.IndirizzoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @RequiredArgsConstructor
public class IndirizzoController {
    private final IndirizzoService indirizzoService;

    @GetMapping("/pb/province/{lat}/{lon}/{raggio}")
    @Operation(
            summary = "OTTIENI PROVINCE VICINE",
            description = "Metodo per ricevere le province vicine a una posizione specifica",
            tags = {"Indirizzo"})
    public ResponseEntity<List<String>> getProvinceVicine(@PathVariable @NotNull double lat,
                                                          @PathVariable @NotNull double lon,
                                                          @PathVariable @NotNull @Min(1) int raggio) {
        return ResponseEntity.ok(indirizzoService.getProvinceVicine(lat,lon,raggio));
    }

}
