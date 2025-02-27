package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.AnnuncioImmobiliareRequest;
import it.unina.dietiestates25.entity.Contratto;
import it.unina.dietiestates25.entity.ContrattoAffitto;
import it.unina.dietiestates25.entity.Immobile;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.ContrattoRepository;
import it.unina.dietiestates25.repository.ImmobileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImmobileService {
    public final ImmobileRepository immobileRepository;

    @Transactional
    public Immobile createImmobile(AnnuncioImmobiliareRequest request) {
        Contratto contratto = ContrattoAffitto.builder()
                                .prezzo(request.getContratto().getPrezzo())
                                .build();

        Immobile immobile = null;
        return immobile;
    }

    public Immobile getImmobile(int immobileId) {
        return immobileRepository.findById(immobileId)
                .orElseThrow(() -> new ResourceNotFoundException("Immobile", "Id", immobileId));
    }
}
