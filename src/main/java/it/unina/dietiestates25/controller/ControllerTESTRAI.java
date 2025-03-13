package it.unina.dietiestates25.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.DipendenteRequest;
import it.unina.dietiestates25.dto.response.*;
import it.unina.dietiestates25.entity.*;
import it.unina.dietiestates25.entity.enumeration.TipoContratto;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import it.unina.dietiestates25.entity.enumeration.VicinoA;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.AnnuncioImmobiliareRepository;
import it.unina.dietiestates25.repository.DatiImpiegatoRepository;
import it.unina.dietiestates25.repository.RicercaAnnunciEffettuataRepository;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.service.AnnuncioImmobileService;
import it.unina.dietiestates25.service.JwtService;
import it.unina.dietiestates25.utils.ImageContainerUtil;
import it.unina.dietiestates25.utils.NearbyPlacesChecker;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
public class ControllerTESTRAI {

    private final JwtService jwtService;
    private final AnnuncioImmobileService annuncioImmobileService;
    private  final NearbyPlacesChecker nearbyPlacesChecker;
    private final DatiImpiegatoRepository datiImpiegatoRepository;

    @GetMapping("pb/test/geoapify2/{latitudine}/{longitudine}")
    public Set<VicinoA> verificaVicino2(@PathVariable double latitudine, @PathVariable double longitudine) {
        return nearbyPlacesChecker.getPuntiInteresseVicini(latitudine, longitudine);
    }
    private final UserRepository userRepository;

    private final String chiaveApi = "89dcc279975c4ca2bc1f39fb349bc4da";
    private final WebClient.Builder webClientBuilder;

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
        int pagina= 0;
        int dimensionePagina= 10;
        Pageable pageable = Pageable.ofSize(dimensionePagina).withPage(pagina);
        List<AnnuncioImmobiliare> annunci = annuncioImmobiliareRepository.findAll(pageable).toList();
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


                ContattoResponse contattoResponse = ContattoResponse.builder()
                        .tipo(proposta.getContatto().getTipo())
                        .valore(proposta.getContatto().getValore())
                        .build();

                DatiUserPropostaResponse datiProponente = DatiUserPropostaResponse.builder()
                        .nome(proposta.getNome())
                        .cognome(proposta.getCognome())
                        .contatto(contattoResponse)
                        .build();

                PropostaResponse propostaResponse = PropostaResponse.builder()
                        .prezzoProposta(proposta.getPrezzoProposta())
                        .controproposta(proposta.getPrezzoProposta())
                        .stato(proposta.getStato().toString())
                        .datiProponente(datiProponente)
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
            ContrattoResponse contrattoResponse = ContrattoResponse.builder().build();

            if(contratto instanceof ContrattoAffitto){

                ContrattoAffittoResponse contrattoAffittoResponse = ContrattoAffittoResponse.builder()
                        .caparra(((ContrattoAffitto) contratto).getCaparra())
                        .prezzoAffitto(((ContrattoAffitto) contratto).getPrezzoAffitto())
                        .tempoMinimo(((ContrattoAffitto) contratto).getTempoMinimo())
                        .tempoMassimo(((ContrattoAffitto) contratto).getTempoMassimo())
                        .build();

                        contrattoResponse.setContrattoAffittoResponse(contrattoAffittoResponse);
                        contrattoResponse.setTipoContratto("AFFITTO");

            }else if(contratto instanceof ContrattoVendita){

                ContrattoVenditaResponse contrattoVenditaResponse = ContrattoVenditaResponse.builder()
                        .mutuoEstinto(((ContrattoVendita) contratto).isMutuoEstinto())
                        .prezzoVendita(((ContrattoVendita) contratto).getPrezzoVendita())
                        .build();

                contrattoResponse.setTipoContratto("VENDITA");
                contrattoResponse.setContrattoVenditaResponse(contrattoVenditaResponse);
            }

            AnnuncioImmobiliareResponse annuncioResponse = AnnuncioImmobiliareResponse.builder()
                    .titolo(annuncio.getTitolo())
                    .descrizione(annuncio.getDescrizione())
                    .immobile(immobileResponse)
                    .proposte(proposteRespose)
                    .agente(agenteCreatoreAnnuncio)
                    .contratto(contrattoResponse)
                    .build();

            annunciResponse.add(annuncioResponse);
        }

        return annunciResponse;
    }


    private  final RicercaAnnunciEffettuataRepository ricercaAnnunciEffettuataRepository;
    @GetMapping("pb/test/ricerche")
    public List<Integer> trovaUtentiInteressati(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        List<Integer> utentiInteressati = new ArrayList<>();
        List<String> listLocalita = new ArrayList<>();
        listLocalita.add("Napoli");
        listLocalita.add("Roma");
        for (String localita : listLocalita) {
            List<RicercaAnnunciEffettuata> ricerche = ricercaAnnunciEffettuataRepository.findByTipologiaImmobileAndTipologiaContrattoAndLocalitaAndPrezzoMinLessThanEqualAndPrezzoMaxGreaterThanEqual(
                    TipologiaImmobile.APPARTAMENTO,
                    TipoContratto.AFFITTO,
                    localita,
                    min.multiply(BigDecimal.valueOf(0.9)),  // Range -10%
                    max.multiply(BigDecimal.valueOf(1.1))   // Range +10%
            );

            utentiInteressati.addAll(
                    ricerche.stream()
                            .map(r -> r.getUtente().getId())
                            .toList()
            );
        }
        String localita =listLocalita.getFirst();
        Specification<RicercaAnnunciEffettuata> spec = (root, query, criteriaBuilder) -> {
            if (localita == null || localita.isEmpty()) {
                return criteriaBuilder.conjunction(); // Restituisce sempre vero
            }
            return criteriaBuilder.isMember(localita, root.get("localita"));
        };

        List<RicercaAnnunciEffettuata> ricerche = ricercaAnnunciEffettuataRepository.findAll(spec);

        return utentiInteressati.stream().distinct().toList(); // Rimuoviamo i duplicati
    }

//addFakeRicerche
    @PostMapping("pb/test/ricerche")
    @Operation(summary = "Aggiungi ricerche fittizie")
    public String addFakeRicerche(@RequestParam int id) {
        User utente = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Utente non trovato", "id", id));
        List<String> listLocalita = new ArrayList<>();
        listLocalita.add("Napoli");
        listLocalita.add("Roma");


                RicercaAnnunciEffettuata ricerca = RicercaAnnunciEffettuata.builder()
                        .utente(utente)
                        .tipologiaImmobile(TipologiaImmobile.APPARTAMENTO)
                        .tipologiaContratto(TipoContratto.AFFITTO)
                        .localita(listLocalita)
                        .prezzoMin(BigDecimal.valueOf(500))
                        .prezzoMax(BigDecimal.valueOf(1000))
                        .build();

                    ricercaAnnunciEffettuataRepository.save(ricerca);


        return "Ricerche fittizie aggiunte";
    }

    @PostMapping("pb/test/getDatiImpiegatoByUserID/{id}")
    public DatiImpiegato getDatiImpiegatoByUserID(@PathVariable int id) {
        DatiImpiegato datiImpiegato=  datiImpiegatoRepository.findByUser_Id(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dati impiegato non trovati", "id", id));
        datiImpiegato.setUser(null);
        datiImpiegato.setContatti(null);
        return datiImpiegato;
    }

}
