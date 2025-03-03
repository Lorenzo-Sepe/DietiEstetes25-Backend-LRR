package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.agenziaImmobiliare.AgenziaImmobiliareRequest;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.DipendenteRequest;
import it.unina.dietiestates25.dto.response.AgenziaImmobiliareResponse;
import it.unina.dietiestates25.dto.response.NewDipendeteResponse;
import it.unina.dietiestates25.entity.AgenziaImmobiliare;
import it.unina.dietiestates25.exception.InternalServerErrorException;
import it.unina.dietiestates25.repository.AgenziaImmobiliareRepository;
import it.unina.dietiestates25.repository.AuthorityRepository;
import it.unina.dietiestates25.repository.DatiImpiegatoRepository;
import it.unina.dietiestates25.repository.UserRepository;
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
        DipendenteRequest dipendenteRequest = DipendenteRequest.builder()
                .nome(request.getNomeFondatore())
                .cognome(request.getCognomeFondatore())
                .ruolo("ADMIN").build();
        NewDipendeteResponse fondatoreResponse = userService.AddDipendete(dipendenteRequest,request.getDominio());

        AgenziaImmobiliare agenziaImmobiliare = AgenziaImmobiliare.builder()
                .nomeAzienda(request.getNomeAgenzia())
                .partitaIva(request.getPartitaIva())
                .ragioneSociale(request.getRagioneSociale())
                .dominio(request.getDominio())
                .fondatore(fondatoreResponse.getUser())
                .build();

        try {
        agenziaImmobiliareRepository.save(agenziaImmobiliare);

        }catch (Exception e){
            throw new InternalServerErrorException("Non è stato possibile creare l'agenzia immobiliare " + e.getMessage());
        }
        String response= "Agenzia creata con successo. Le credenziali del fondatore sono:\n"+fondatoreResponse.getUser().getEmail() +"\n"+ fondatoreResponse.getPassword();
        System.out.println(response);
        return response;
    }

    public List<AgenziaImmobiliareResponse> getAgenzie() {
        return agenziaImmobiliareRepository.getAllAgenzieImmobiliari();
    }



}
