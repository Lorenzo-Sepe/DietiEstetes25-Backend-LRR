package it.unina.dietiestates25.controller;

import it.unina.dietiestates25.dto.request.agenziaImmobiliare.DipendenteRequest;
import it.unina.dietiestates25.dto.response.AnnuncioImmobiliareResponse;
import it.unina.dietiestates25.dto.response.JwtAuthenticationResponse;
import it.unina.dietiestates25.entity.AnnuncioImmobiliare;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.AnnuncioImmobiliareRepository;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.service.JwtService;
import it.unina.dietiestates25.utils.ImageContainerUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class ControllerTESTRAI {

    private final String chiaveApi = "89dcc279975c4ca2bc1f39fb349bc4da";
    private final WebClient.Builder webClientBuilder;
    private final UserRepository userRepository;
    private final JwtService jwtService;


    @GetMapping("pb/test/geoapify/{latitudine}/{longitudine}")
    public List<String> verificaVicino(@PathVariable double latitudine, @PathVariable double longitudine) {
        List<String> indicatori = new ArrayList<>();

        // URL base per Geoapify
        String urlBase = "https://api.geoapify.com/v2/places?categories={categoria}&filter=circle:{longitudine},{latitudine},500&apiKey={apiKey}";

        // Categorie da verificare
        Map<String, String> categorie = Map.of(
                "Vicino a scuole", "education.school",
                "Vicino a parchi", "leisure.park",
                "Vicino a trasporto pubblico", "public_transport"
        );

        WebClient webClient = webClientBuilder.build();

        for (Map.Entry<String, String> voce : categorie.entrySet()) {
            String url = urlBase.replace("{categoria}", voce.getValue())
                    .replace("{latitudine}", String.valueOf(latitudine))
                    .replace("{longitudine}", String.valueOf(longitudine))
                    .replace("{apiKey}", chiaveApi);

            Map<String, Object> risposta = webClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            clientResponse.createException().flatMap(error -> {
                                throw new RuntimeException("Errore nella richiesta a Geoapify: " + error.getMessage());
                            })
                    )
                    .bodyToMono(Map.class)
                    .block(); // Chiamata sincrona

            if (risposta != null && risposta.containsKey("features") && ((List<?>) risposta.get("features")).size() > 0) {
                indicatori.add(voce.getKey());
            }
        }

        return indicatori;
    }


    private final ImageContainerUtil imageContainerUtil;

    @Deprecated
    @PostMapping("pb/test/upload")
    public ResponseEntity<String> uploadImage(@ModelAttribute MultipartFile file ) {
        String nomePath = "test.png";
        String imageUrl = imageContainerUtil.uploadImage(file, nomePath);
        return ResponseEntity.ok(imageUrl);
    }

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

    @PostMapping("pb/test/ripetiStringa")
    public ResponseEntity<String> ripetiStringa(@RequestBody String stringa) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1; i++) {
            sb.append(stringa);
        }
        return ResponseEntity.ok(sb.toString());

    }

    @PostMapping("pb/test/dtoconfoto")
    public ResponseEntity<String> dtoConFoto(@ModelAttribute DipendenteRequest request) {
        String nome= request.getNome();
        String cognome= request.getCognome();
        MultipartFile foto= request.getFotoProfilo();
       String link="nessun link";
        if(foto!=null){
        link=imageContainerUtil.uploadImage(foto, nome + cognome + ".png");
       }
        return ResponseEntity.ok("user.toString() \nlink: "+link);
    }


    private final AnnuncioImmobiliareRepository annuncioImmobiliareRepository;
    private ModelMapper modelMapper;

    @Transactional
    @GetMapping("pb/test/immobili")
    public List<AnnuncioImmobiliareResponse> getAllImmobileResponses() {
        List<AnnuncioImmobiliare> annunci = annuncioImmobiliareRepository.findAll();
        List<AnnuncioImmobiliareResponse> annunciResponse= new ArrayList<>();
        for(AnnuncioImmobiliare annuncio: annunci){
            AnnuncioImmobiliareResponse annuncioResponse = modelMapper.map(annuncio, AnnuncioImmobiliareResponse.class);
            annunciResponse.add(annuncioResponse);
        }

        return annunciResponse;
    }
}
