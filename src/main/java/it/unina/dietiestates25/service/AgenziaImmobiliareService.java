package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.AgenziaImmobiliareRequest;
import it.unina.dietiestates25.dto.response.AgenziaImmobiliareResponse;
import it.unina.dietiestates25.entity.AgenziaImmobiliare;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.exception.InternalServerErrorException;
import it.unina.dietiestates25.repository.AgenziaImmobiliareRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AgenziaImmobiliareService {
    private final AgenziaImmobiliareRepository agenziaImmobiliareRepository;
    private final AuthService authService;

    @Transactional
    public void createAgenzia(AgenziaImmobiliareRequest request) {

        User fondatore = authService.createAdmin(request);

        AgenziaImmobiliare agenziaImmobiliare = AgenziaImmobiliare.builder()
                .nomeAzienda(request.getNomeAgenzia())
                .partitaIva(request.getPartitaIva())
                .ragioneSociale(request.getRagioneSociale())
                .fondatore(fondatore)
                .build();
        try {
            agenziaImmobiliareRepository.save(agenziaImmobiliare);
        }catch (Exception e){
            throw new InternalServerErrorException("Non è stato possibile creare l'agenzia immobiliare " + e.getMessage());
        }
    }

    public List<AgenziaImmobiliareResponse> getAgenzie() {
        return agenziaImmobiliareRepository.getAllAgenzieImmobiliari();
    }



}
