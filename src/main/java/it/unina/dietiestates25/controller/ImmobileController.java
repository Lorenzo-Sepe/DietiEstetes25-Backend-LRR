package it.unina.dietiestates25.controller;

import it.unina.dietiestates25.dto.request.ImmobileRequest;
import it.unina.dietiestates25.entity.Immobile;
import it.unina.dietiestates25.service.ImmobileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class ImmobileController {
    private final ImmobileService immobileService;

    @PostMapping("/pb/immobile/")
    public ResponseEntity<Immobile> createImmobile(
            @RequestBody @Valid ImmobileRequest request
            ) {
        return new ResponseEntity<>(immobileService.createImmobile(request), HttpStatus.CREATED);
    }

    @GetMapping("/pb/immobile/{immobileId}")
    public ResponseEntity<Immobile> getImmobile(
            @PathVariable int immobileId
            ) {
        return new ResponseEntity<>(immobileService.getImmobile(immobileId), HttpStatus.OK);
    }




}
