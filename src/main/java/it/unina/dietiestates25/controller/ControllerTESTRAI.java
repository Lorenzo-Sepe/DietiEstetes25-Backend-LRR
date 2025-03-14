package it.unina.dietiestates25.controller;

import it.unina.dietiestates25.dto.request.NotificaPromozionaleRequest;
import it.unina.dietiestates25.dto.response.JwtAuthenticationResponse;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.service.JwtService;
import it.unina.dietiestates25.service.RicercaAnnunciEffettuataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ControllerTESTRAI {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RicercaAnnunciEffettuataService ricercaAnnunciEffettuataService;

    //getToken for testing member
    @GetMapping("pb/test/getToken")
    public Object getToken(@RequestParam int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato", "id", id));


        String authority = user.getAuthority().getAuthorityName().name();
        String jwt = jwtService.generateToken(user);

     return     JwtAuthenticationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authority(authority)
                .token(jwt)
                .build();
    }

    @PostMapping("pb/test/getUtentiInteressati")
    public List<User> getUtentiInteressati(@RequestBody NotificaPromozionaleRequest request) {
        return ricercaAnnunciEffettuataService.UtentiInteressati(request);
    }
}
