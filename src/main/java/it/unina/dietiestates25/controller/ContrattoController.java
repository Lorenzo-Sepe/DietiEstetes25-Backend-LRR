package it.unina.dietiestates25.controller;

import it.unina.dietiestates25.service.ContrattoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class ContrattoController {

    private final ContrattoService contrattoService;



}
