package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.AgenziaImmobiliareRequest;
import it.unina.dietiestates25.dto.response.AgenziaImmobiliareResponse;
import it.unina.dietiestates25.entity.AgenziaImmobiliare;
import it.unina.dietiestates25.entity.Authority;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.entity.enumeration.AuthorityName;
import it.unina.dietiestates25.exception.InternalServerErrorException;
import it.unina.dietiestates25.repository.AgenziaImmobiliareRepository;
import it.unina.dietiestates25.repository.AuthorityRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AgenziaImmobiliareService {
    private final AgenziaImmobiliareRepository agenziaImmobiliareRepository;
    private final AuthorityRepository authorityRepository;

    @Transactional
    public void createAgenzia(AgenziaImmobiliareRequest request) {
        Authority authority = Authority.builder()
                .authorityName(AuthorityName.ADMIN)
                .build();
        authorityRepository.save(authority);
        User fondatore = User.builder()
                .email(request.getEmailFondatore())
                .username(request.getEmailFondatore())
                .authority(authority)
                .password("admin")
                .build();
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
