package it.unina.dietiestates25.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.CriteriDiRicercaUtenti;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.AnnuncioImmobiliareRequest;
import it.unina.dietiestates25.dto.response.JwtAuthenticationResponse;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.exception.InternalServerErrorException;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.service.JwtService;
import it.unina.dietiestates25.service.PasswordService;
import it.unina.dietiestates25.service.RicercaAnnunciEffettuataService;
import it.unina.dietiestates25.utils.UserContex;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ControllerTESTRAI {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RicercaAnnunciEffettuataService ricercaAnnunciEffettuataService;
    private final PasswordService passwordService;


    //getToken for testing member
    @GetMapping("/pb/test/getToken")
    @Operation(
            summary = "GET TOKEN",
            description = "Metodo per ottenere un token JWT per l'utente con ID specificato",
            tags = {"TESTS"})
    public Object getToken(@RequestParam int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato", "id", id));


        String jwt = jwtService.generateToken(user);

     return     JwtAuthenticationResponse.fromEntityToDto(user,jwt);
    }

    @PostMapping("pb/test/getUtentiInteressati")
    @Operation(
            summary = "OTTIENI UTENTI INTERESSATI",
            description = "Metodo per ottenere gli utenti interessati a un annuncio",
            tags = {"Tests"})
    public List<User> getUtentiInteressati(@RequestBody CriteriDiRicercaUtenti request) {
        return ricercaAnnunciEffettuataService.UtentiInteressati(request);
    }
    //test che dato il token di autenticazione ti restituisce user
    @GetMapping("test/getUser")
    @Operation(
            summary = "GET USER",
            description = "Metodo per ottenere l'utente autenticato",
            tags = {"Tests"})
    @PreAuthorize("hasAnyAuthority('AGENT', 'MANAGER', 'MEMBER')")
    public User getUser() {
        User user= UserContex.getUserCurrent();
        user.setPassword(null);
        return user;
    }

    @PatchMapping("pb/test/updatePassword")
    @Operation(
            summary = "AGGIORNA PASSWORD",
            description = "Metodo per aggiornare la password di un utente",
            tags = {"Tests"})
    public String updatePassword(@RequestParam int id, @RequestParam String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato", "id", id));
        user.setPassword(passwordService.cifrarePassword(newPassword));
        try {
        userRepository.save(user);

        }catch (Exception e){
            //trow new exception
            throw new InternalServerErrorException("Errore nel salvataggio della password");
        }
        String messaggio = "Password aggiornata con successo per l'utente: " + user.getNomeVisualizzato();
        messaggio+="\nEmail: " + user.getEmail();
        messaggio += "\nNuova password: " + newPassword;
        return messaggio;
    }

    //test dto get
    @PostMapping("pb/test/annuncioImmobiliare")
    @Operation(
            summary = "GET ANNUNCIO IMMOBILIARE",
            description = "Metodo per ottenere un annuncio immobiliare attraverso il DTO",
            tags = {"Tests"})
    public AnnuncioImmobiliareRequest getDtoCreazioneAnnuncio(@ModelAttribute AnnuncioImmobiliareRequest request) {
        return request;
    }



}
