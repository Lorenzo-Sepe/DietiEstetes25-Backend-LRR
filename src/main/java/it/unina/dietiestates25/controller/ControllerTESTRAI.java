package it.unina.dietiestates25.controller;

import it.unina.dietiestates25.dto.request.agenziaImmobiliare.DipendenteRequest;
import it.unina.dietiestates25.dto.response.*;
import it.unina.dietiestates25.entity.*;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.AnnuncioImmobiliareRepository;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.service.AnnuncioImmobileService;
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
    private final AnnuncioImmobileService annuncioImmobileService;


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

    @Transactional
    @GetMapping("pb/test/immobili")
    public List<AnnuncioImmobiliareResponse> getAllImmobileResponses() {
        List<AnnuncioImmobiliare> annunci = annuncioImmobiliareRepository.findAll();
        List<AnnuncioImmobiliareResponse> annunciResponse= new ArrayList<>();

        for(AnnuncioImmobiliare annuncio: annunci){

            Indirizzo indirizzoImmobile = annuncio.getImmobile().getIndirizzo();
            CaratteristicheAggiuntive caratteristicheAggiuntive = annuncio.getImmobile().getCaratteristicheAggiuntive();
            List<ImmaginiImmobile> immaginiImmobile = annuncio.getImmobile().getImmagini();
            List<Proposta> proposte = annuncio.getProposte();

            IndirizzoResponse indirizzoResponse = IndirizzoResponse.builder()
                    .via(indirizzoImmobile.getVia())
                    .numeroCivico(indirizzoImmobile.getNumeroCivico())
                    .citta(indirizzoImmobile.getCitta())
                    .cap(indirizzoImmobile.getCap())
                    .provincia(indirizzoImmobile.getCap())
                    .nazione(indirizzoImmobile.getNazione())
                    .longitudine(indirizzoImmobile.getLongitudine())
                    .latitudine(indirizzoImmobile.getLatitudine())
                    .build();

            CaratteristicheAggiuntiveResponse caratteristicheAggiuntiveResponse = CaratteristicheAggiuntiveResponse.builder()
                    .balconi(caratteristicheAggiuntive.isBalconi())
                    .garage(caratteristicheAggiuntive.isGarage())
                    .postiAuto(caratteristicheAggiuntive.isPostiAuto())
                    .giardino(caratteristicheAggiuntive.isGiardino())
                    .ascensore(caratteristicheAggiuntive.isAscensore())
                    .portiere(caratteristicheAggiuntive.isPortiere())
                    .riscaldamentoCentralizzato(caratteristicheAggiuntive.isRiscaldamentoCentralizzato())
                    .climatizzatori(caratteristicheAggiuntive.isClimatizzatori())
                    .pannelliSolari(caratteristicheAggiuntive.isPannelliSolari())
                    .cantina(caratteristicheAggiuntive.isCantina())
                    .soffitta(caratteristicheAggiuntive.isSoffitta())
                    .build();

            List<ImmaginiImmobileResponse> immaginiImmobileResponse = new ArrayList<>();
            for(ImmaginiImmobile immagine : immaginiImmobile){

                ImmaginiImmobileResponse immagineResponse = ImmaginiImmobileResponse.builder()
                        .url(immagine.getUrl())
                        .descrizione(immagine.getDescrizione())
                        .build();

                immaginiImmobileResponse.add(immagineResponse);
            }

            ImmobileResponse immobileResponse = ImmobileResponse.builder()
                    .tipologiaImmobile(annuncio.getImmobile().getTipologiaImmobile().toString())
                    .metriQuadri(annuncio.getImmobile().getMetriQuadri())
                    .classeEnergetica(annuncio.getImmobile().getClasseEnergetica().toString())
                    .numeroServizi(annuncio.getImmobile().getNumeroServizi())
                    .numeroStanze(annuncio.getImmobile().getNumeroStanze())
                    .numeroDiPiani(annuncio.getImmobile().getNumeroDiPiani())
                    .indirizzo(indirizzoResponse)
                    .caratteristicheAggiuntive(caratteristicheAggiuntiveResponse)
                    .immagini(immaginiImmobileResponse)
                    .build();

            List<PropostaResponse> proposteRespose = new ArrayList<>();
            for(Proposta proposta : proposte){

                UserResponse userReponse = UserResponse.builder()
                        .email(proposta.getUser().getEmail())
                        .username(proposta.getUser().getUsername())
                        .urlFotoProfilo(proposta.getUser().getUrlFotoProfilo())
                        .build();

                ContattoResponse contattoResponse = ContattoResponse.builder()
                        .tipo(proposta.getContatto().getTipo())
                        .valore(proposta.getContatto().getValore())
                        .build();

                PropostaResponse propostaResponse = PropostaResponse.builder()
                        .prezzoProposta(proposta.getPrezzoProposta())
                        .controproposta(proposta.getPrezzoProposta())
                        .stato(proposta.getStato().toString())
                        .utente(userReponse)
                        .contatto(contattoResponse)
                        .build();

                proposteRespose.add(propostaResponse);
            }

            UserResponse agenteCreatoreAnnuncio = UserResponse.builder()
                    .email(annuncio.getAgente().getEmail())
                    .username(annuncio.getAgente().getUsername())
                    .urlFotoProfilo(annuncio.getAgente().getUrlFotoProfilo())
                    .build();

            Contratto contratto = annuncio.getContratto();

            if(contratto.getTipoContratto().equals("AFFITTO")){



            }else{


            }

            AnnuncioImmobiliareResponse annuncioResponse = AnnuncioImmobiliareResponse.builder()
                    .titolo(annuncio.getTitolo())
                    .descrizione(annuncio.getDescrizione())
                    .immobile(immobileResponse)
                    .proposte(proposteRespose)
                    .agente(agenteCreatoreAnnuncio)
                    .contratto(annuncio.getContratto())
                    .build();

            annunciResponse.add(annuncioResponse);
        }

        int puntoBreak;

        return annunciResponse;
    }
}
