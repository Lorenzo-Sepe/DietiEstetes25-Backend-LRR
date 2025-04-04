package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.agenziaImmobiliare.AgenziaImmobiliareRequest;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.DipendenteRequest;
import it.unina.dietiestates25.dto.response.AgenziaImmobiliareResponse;
import it.unina.dietiestates25.dto.response.NewDipendeteResponse;
import it.unina.dietiestates25.dto.response.impiegato.DatiAgenziaImmobiliareResponse;
import it.unina.dietiestates25.entity.AgenziaImmobiliare;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.exception.InternalServerErrorException;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.AgenziaImmobiliareRepository;
import it.unina.dietiestates25.repository.AuthorityRepository;
import it.unina.dietiestates25.repository.DatiImpiegatoRepository;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.utils.GenericMail;
import it.unina.dietiestates25.utils.Msg;
import it.unina.dietiestates25.utils.UserContex;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AgenziaImmobiliareService {
    private final AgenziaImmobiliareRepository agenziaImmobiliareRepository;
    private final  UserService userService;
    private final EmailService emailService;

    @Transactional
    public String createAgenzia(AgenziaImmobiliareRequest request) {
        DipendenteRequest dipendenteRequest = DipendenteRequest.creaFondatoreAgenzia(request);
        NewDipendeteResponse fondatoreResponse = userService.addDipendete(dipendenteRequest, request.getDominio());

        AgenziaImmobiliare agenziaImmobiliare = buildAgenziaImmobiliare(request, fondatoreResponse);
        agenziaImmobiliare.addDipendente(fondatoreResponse.getUser());
        saveAgenzia(agenziaImmobiliare);
        GenericMail mail = new GenericMail(
            Msg.MAIL_SIGNUP_SUBJECT,
            String.format(Msg.MAIL_SIGNUP_BODY, fondatoreResponse.getUser().getEmail(), fondatoreResponse.getPassword()),
            request.getEmailFondatore()
        );
        emailService.sendVerificationMail(mail);
        String response = Msg.AGENCY_CREATION_SUCCESS;
        log.info(response);
        return response;
    }

    private AgenziaImmobiliare buildAgenziaImmobiliare(AgenziaImmobiliareRequest request, NewDipendeteResponse fondatoreResponse) {
        if (agenziaImmobiliareRepository.existsByDominio(request.getDominio())) {
            throw new IllegalArgumentException("Il dominio selezionato non e' disponibile");
        }
        if (agenziaImmobiliareRepository.existsByRagioneSociale(request.getRagioneSociale())) {
            throw new IllegalArgumentException("La ragione sociale selezionata non e' disponibile");
        }
        if (agenziaImmobiliareRepository.existsByPartitaIva(request.getPartitaIva())) {
            throw new IllegalArgumentException("La partita IVA inserita non e' disponibile");
        }
        return AgenziaImmobiliare.builder()
                .nomeAzienda(request.getNomeAgenzia())
                .partitaIva(request.getPartitaIva())
                .ragioneSociale(request.getRagioneSociale())
                .dominio(request.getDominio())
                .fondatore(fondatoreResponse.getUser ())
                .build();
    }

    private void saveAgenzia(AgenziaImmobiliare agenziaImmobiliare) {
        try {
            agenziaImmobiliareRepository.save(agenziaImmobiliare);
        } catch (Exception e) {
            throw new InternalServerErrorException("Non è stato possibile creare l'agenzia immobiliare: " + e.getMessage());
        }
    }


    public List<AgenziaImmobiliareResponse> getAgenzie() {
        return agenziaImmobiliareRepository.getAllAgenzieImmobiliari();
    }


    @Transactional
    public String aggiungiDipendete(DipendenteRequest request) {
        User user = UserContex.getUserCurrent();
        AgenziaImmobiliare agenziaImmobiliare =agenziaImmobiliareRepository.findAgenziaImmobiliareByDipendentiContains(user)
                .orElseThrow(() -> new IllegalArgumentException("L'utente non è un dipendente di nessuna agenzia immobiliare"));

        NewDipendeteResponse newDipendeteResponse = userService.addDipendete(request, agenziaImmobiliare.getDominio());
        agenziaImmobiliare.addDipendente(newDipendeteResponse.getUser());

        String email = newDipendeteResponse.getUser().getEmail();
        String password = newDipendeteResponse.getPassword();
        String response = String.format("Dipendente aggiunto con successo. Le credenziali del dipendete sono:%n%s%n%s", email, password);
        log.info(response);
        return response;
    }

    public DatiAgenziaImmobiliareResponse getAgenziaByEmailImpiegato(String email) {
        AgenziaImmobiliare agenzia =agenziaImmobiliareRepository.findByDipendenteEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Agenzia Immobiliare","email",email));
             return DatiAgenziaImmobiliareResponse.fromEntityToDto(agenzia);
    }
}
