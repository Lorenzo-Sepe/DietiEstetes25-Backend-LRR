package it.unina.dietiestates25.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.CategoriaNotificaRequest;
import it.unina.dietiestates25.dto.request.CategoriaNotificaRequest2;
import it.unina.dietiestates25.dto.response.DipendenteResponse;
import it.unina.dietiestates25.dto.response.SottoscrizioneNotificaResponse;
import it.unina.dietiestates25.dto.response.impiegato.DatiImpiegatoResponse;
import it.unina.dietiestates25.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {
    final UserService userService;

    //Modifica Sottoscrizioni
    @PatchMapping("/utente/sottoscrizioni")
    @Operation(summary = "Modifica le sottoscrizioni di un utente",
            description = "Modifica le sottoscrizioni di un utente",
            tags = {"Utente"}
    )
    public ResponseEntity<String> modificaSottoscrizioni(@RequestBody CategoriaNotificaRequest request) {

        return ResponseEntity.ok(userService.modificaSottoscrizioni(request));
    }

    //Modifica Sottoscrizioni
    @PatchMapping("/utente/sottoscrizioni2")
    @Operation(summary = "Modifica le sottoscrizioni di un utente",
            description = "Modifica le sottoscrizioni di un utente",
            tags = {"Utente"}
    )
    public ResponseEntity<String> modificaSottoscrizioni2(@RequestBody List<CategoriaNotificaRequest2> request) {

        return ResponseEntity.ok(userService.modificaSottoscrizioni(request));
    }

    @GetMapping("/pb/dipendente/{idDipendente}")
    @Operation(summary = "Recupera le informazioni di un dipendente",
            description = "Recupera le informazioni di un dipendente",
            tags = {"Utente"}
    )
    public ResponseEntity<DipendenteResponse> getDipendente(@PathVariable int idDipendente) {
        return ResponseEntity.ok(userService.getDipendente(idDipendente));
    }

    @GetMapping("/sottoscrizioni")
    @Operation(summary = "Recupera le sottoscrizioni dell'utente",
            description = "Recupera le sottoscrizione dell' utente",
            tags = {"Utente"}
    )
    public ResponseEntity<List<SottoscrizioneNotificaResponse>> getSottoscrizioni(){

        return ResponseEntity.ok(userService.getSottoscrizioni());
    }

    
    @GetMapping("/pb/impiegato/email")
    @Operation(
            summary = "OTTIENI I DATI DI UN AGENTE DEL SITO PER EMAIL",
            description = "Metodo per ottenere i dati di un agente in base all'email",
            tags = {"Impiegato"})
    public ResponseEntity<DatiImpiegatoResponse> getAgenziaByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getDatiDipendente(email));

    }
}
