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
import it.unina.dietiestates25.utils.UserContex;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AgenziaImmobiliareService {
    private final AgenziaImmobiliareRepository agenziaImmobiliareRepository;
    private final AuthorityRepository authorityRepository;
    private  final DatiImpiegatoRepository datiImpiegatoRepository;
    private final UserRepository userRepository;
    private final  UserService userService;

    @Transactional
    public String createAgenzia(AgenziaImmobiliareRequest request) {
        DipendenteRequest dipendenteRequest = CreaFondatoreAgenzia(request);
        NewDipendeteResponse fondatoreResponse = userService.addDipendete(dipendenteRequest, request.getDominio());

        AgenziaImmobiliare agenziaImmobiliare = buildAgenziaImmobiliare(request, fondatoreResponse);
        agenziaImmobiliare.addDipendente(fondatoreResponse.getUser());
        saveAgenzia(agenziaImmobiliare);


        String response = String.format("Agenzia creata con successo. Le credenziali del fondatore sono:%n%s%n%s", fondatoreResponse.getUser().getEmail(), fondatoreResponse.getPassword());
        System.out.println(response);
        return response;
    }

    private DipendenteRequest CreaFondatoreAgenzia(AgenziaImmobiliareRequest request) {
        return DipendenteRequest.builder()
                .nome(request.getNomeFondatore())
                .cognome(request.getCognomeFondatore())
                .ruolo("ADMIN")
                .build();
    }

    private AgenziaImmobiliare buildAgenziaImmobiliare(AgenziaImmobiliareRequest request, NewDipendeteResponse fondatoreResponse) {
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
        System.out.println(response);
        return response;
    }

    public DatiAgenziaImmobiliareResponse getAgenziaByEmailImpiegato(String email) {
        AgenziaImmobiliare agenzia =agenziaImmobiliareRepository.findByDipendenteEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Agenzia Immobiliare","email",email));
             return DatiAgenziaImmobiliareResponse.fromEntityToDto(agenzia);
    }
}
