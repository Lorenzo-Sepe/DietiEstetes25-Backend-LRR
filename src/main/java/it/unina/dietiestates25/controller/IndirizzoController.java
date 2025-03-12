package it.unina.dietiestates25.controller;

import it.unina.dietiestates25.dto.request.ProvinciaRequest;
import it.unina.dietiestates25.service.IndirizzoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @RequiredArgsConstructor
public class IndirizzoController {
    private final IndirizzoService indirizzoService;

    @GetMapping("/pb/province/")
    public ResponseEntity<List<String>> getProvinceVicine(@RequestBody @Valid ProvinciaRequest request) {
        return ResponseEntity.ok(indirizzoService.getProvinceVicine(request));
    }

}
