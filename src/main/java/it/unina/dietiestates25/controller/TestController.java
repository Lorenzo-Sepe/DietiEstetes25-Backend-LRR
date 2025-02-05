package it.unina.dietiestates25.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class TestController {

    

    @GetMapping("/test")
    public String test() {
        return "Hello World";
    }

}
