package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.CategoriaNotificaRequest;
import it.unina.dietiestates25.dto.request.CategoriaNotificaRequest2;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.DipendenteRequest;
import it.unina.dietiestates25.dto.response.DipendenteResponse;
import it.unina.dietiestates25.dto.response.NewDipendeteResponse;
import it.unina.dietiestates25.dto.response.SottoscrizioneNotificaResponse;
import it.unina.dietiestates25.dto.response.UserInfoResponse;
import it.unina.dietiestates25.dto.response.impiegato.DatiImpiegatoResponse;
import it.unina.dietiestates25.entity.CategoriaNotifica;
import it.unina.dietiestates25.entity.DatiImpiegato;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.entity.enumeration.AuthorityName;
import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.AuthorityRepository;
import it.unina.dietiestates25.repository.DatiImpiegatoRepository;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.utils.UserContex;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class UserService {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final DatiImpiegatoRepository datiImpiegatoRepository;
    private final PasswordService passwordService;
    private final ImageUploaderService imageUploaderService;
    private final CategoriaNotificaService categoriaService;


    public NewDipendeteResponse addDipendete(DipendenteRequest request, String aliasAgenzia) {
        String email = generaEmailDipendente(request.getNome(), request.getCognome(), aliasAgenzia);
        String password = passwordService.generaPasswordDipendente();
        AuthorityName authorityName = request.getRuolo().equals("MANAGER") ? AuthorityName.MANAGER : AuthorityName.AGENT;

        //Agente Nome C.
        String nomeVisualizzato = "agente " + request.getNome() + " " + request.getCognome().substring(0, 1).toUpperCase() + ".";
        String fotoProfilo = imageUploaderService.getDefaultAvatar(nomeVisualizzato);
        User user = User.builder()
                .email(email)
                .urlFotoProfilo(fotoProfilo)
                .nomeVisualizzato(nomeVisualizzato)
                .authority(authorityRepository.findByAuthorityName(authorityName).orElseThrow())
                .password(passwordService.cifrarePassword(password))
                .build();

        DatiImpiegato datiImpiegato= DatiImpiegato.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .user(user)
                .build();
        int idUser = salvaImpiegato(user, datiImpiegato);
        user.setId(idUser);
        return NewDipendeteResponse.builder()
                .user(user)
                .password(password)
                .build();
    }

    private Integer salvaImpiegato(User user, DatiImpiegato datiImpiegato) {
        Integer idUser=userRepository.save(user).getId();
        datiImpiegatoRepository.save(datiImpiegato);
        return idUser;
    }

    private String generaEmailDipendente(String nome, String cognome, String aliasAgenzia) {
        convalidaParametri(nome, cognome, aliasAgenzia);
        String emailBase = generaEmailBase(nome, cognome, aliasAgenzia);
        return risolviDuplicatiEmail(emailBase, nome, cognome, aliasAgenzia);
    }

    private void convalidaParametri(String nome, String cognome, String aliasAgenzia) {
        if (nome == null || nome.isBlank() ||
                cognome == null || cognome.isBlank() ||
                aliasAgenzia == null || aliasAgenzia.isBlank()) {

            throw new IllegalArgumentException(String.format(
                    "Per generare un'email i parametri non devono essere nulli:\n nome: %s\n cognome: %s\n aliasAgenzia: %s",
                    nome, cognome, aliasAgenzia
            ));
        }
    }

    private String generaEmailBase(String nome, String cognome, String aliasAgenzia) {
        return String.format("%s.%s@%s.dietiestate.com",
                nome.toLowerCase(),
                cognome.toLowerCase(),
                aliasAgenzia.toLowerCase());
    }

    private String risolviDuplicatiEmail(String emailBase, String nome, String cognome, String aliasAgenzia) {
        String email = emailBase;
        int contatore = 1;

        while (userRepository.countByEmail(email) > 0) {
            email = String.format("%s.%s%d@%s.dietiestate.com",
                    nome.toLowerCase(),
                    cognome.toLowerCase(),
                    ++contatore,
                    aliasAgenzia.toLowerCase());
        }

        return email;
    }

    public String modificaSottoscrizioni(CategoriaNotificaRequest request) {

        User user = UserContex.getUserCurrent();

        List<CategoriaNotifica> categorieDisattivate = getCategorieDisattivate(request);

        assert user != null;
        user.setCategorieDisattivate(categorieDisattivate);

        userRepository.save(user);

        return "Sottoscrizione modificata con successo";
    }

    private List<CategoriaNotifica> getCategorieDisattivate(CategoriaNotificaRequest request) {

        List<CategoriaNotifica> categorieDisattivate = new ArrayList<>();

        if(!request.isAttivoPromozioni())
            categorieDisattivate.add(categoriaService.getCategoriaNotifica(CategoriaNotificaName.PROMOZIONI));
        if(!request.isAttivoCategoriaPropostaRifiutata())
            categorieDisattivate.add(categoriaService.getCategoriaNotifica(CategoriaNotificaName.PROPOSTA_RIFIUTATA));
        if(!request.isAttivoCategoriaPropostaAccettata())
            categorieDisattivate.add(categoriaService.getCategoriaNotifica(CategoriaNotificaName.PROPOSTA_ACCETTATA));
        if(!request.isAttivoCategoriaControproposta())
            categorieDisattivate.add(categoriaService.getCategoriaNotifica(CategoriaNotificaName.CONTROPROPOSTA));
        if(!request.isAttivoCategoriaOpportunityImmobile())
            categorieDisattivate.add(categoriaService.getCategoriaNotifica(CategoriaNotificaName.OPPORTUNITA_IMMOBILE));

        return categorieDisattivate;
    }

    public DipendenteResponse getDipendente(int idDipendente) {
        DatiImpiegato dipendente = datiImpiegatoRepository.findByUser_Id(idDipendente).orElseThrow();
        return DipendenteResponse.fromEntityToDto(dipendente);
    }

    public List<SottoscrizioneNotificaResponse> getSottoscrizioni(){

        User userCurrentContex = UserContex.getUserCurrent();

        User userCurrent = userRepository.findByEmail(userCurrentContex.getEmail())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Utente", "email", userCurrentContex.getEmail())
                );

        List<CategoriaNotifica>  categorieDisattivate = userCurrent.getCategorieDisattivate();

        return getSottoScrizioniResponse(categorieDisattivate);
    }

    private List<SottoscrizioneNotificaResponse> getSottoScrizioniResponse(List<CategoriaNotifica> categorieDisattivate){

        List<SottoscrizioneNotificaResponse> sottoscrizioni = new ArrayList<>();

        for(CategoriaNotificaName categoria : CategoriaNotificaName.values()){

            CategoriaNotifica categoriaNotifica = categoriaService.getCategoriaNotifica(categoria);

            if(contieneCategoria(categoria,categorieDisattivate)){

                SottoscrizioneNotificaResponse sottoscrizioneNotificaResponse = SottoscrizioneNotificaResponse.builder()
                        .nomeCategoria(categoriaNotifica.getCategoriaName().toString())
                        .descrizione(categoriaNotifica.getDescrizione())
                        .attivo(false)
                        .idCategoria(categoriaNotifica.getId())
                        .build();
                sottoscrizioni.add(sottoscrizioneNotificaResponse);

            }else{

                SottoscrizioneNotificaResponse sottoscrizioneNotificaResponse = SottoscrizioneNotificaResponse.builder()
                        .nomeCategoria(categoriaNotifica.getCategoriaName().toString())
                        .descrizione(categoriaNotifica.getDescrizione())
                        .attivo(true)
                        .idCategoria(categoriaNotifica.getId())
                        .build();
                sottoscrizioni.add(sottoscrizioneNotificaResponse);
            }
        }

        return sottoscrizioni;
    }

    private boolean contieneCategoria(CategoriaNotificaName categoria,List<CategoriaNotifica> categorie) {

        for(CategoriaNotifica c : categorie){

            if(c.getCategoriaName() == categoria){

                return true;
            }
        }

        return false;
    }

    public String modificaSottoscrizioni(List<CategoriaNotificaRequest2> request){

        User user = UserContex.getUserCurrent();

        List<CategoriaNotifica> categorieDisattivate = getCategorieDisattivate(request);

        user.setCategorieDisattivate(categorieDisattivate);

        userRepository.save(user);

        return "Sottoscrizione modificata con successo";
    }

    private List<CategoriaNotifica> getCategorieDisattivate(List<CategoriaNotificaRequest2> request){

        List<CategoriaNotifica> categorieDisattivate = new ArrayList<>();

        for(CategoriaNotificaRequest2 categoriaRequest : request){

            if(!categoriaRequest.isAttivo()){

                CategoriaNotifica categoriaNotifica = categoriaService.getCategoriaNotifica(categoriaRequest.getIdCategoria());
                categorieDisattivate.add(categoriaNotifica);

            }
        }

        return categorieDisattivate;
    }

    public DatiImpiegatoResponse getDatiDipendente(String email) {
        DatiImpiegato datiImpiegato =datiImpiegatoRepository.findByUser_Email(email)
                .orElseThrow(() -> new ResourceNotFoundException("Dati impiegato","email",email));

        return DatiImpiegatoResponse.fromEntityToDto(datiImpiegato);
    }

    @Transactional
    public UserInfoResponse getInfoUtente(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utente","email",email));
        //String nomeVisualizzato = user.getNomeVisualizzato();
        //String urlFotoProfilo = imageUploaderService.getDefaultAvatar(nomeVisualizzato);
        //user.setUrlFotoProfilo(urlFotoProfilo);
        //userRepository.save(user);
        return UserInfoResponse.fromEntityToDto(user);
    }

}
