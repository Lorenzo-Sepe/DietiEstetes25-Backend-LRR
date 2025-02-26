package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.ImmobileRequest;
import it.unina.dietiestates25.entity.Contratto;
import it.unina.dietiestates25.entity.ContrattoVendita;
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
    public final ContrattoRepository contrattoRepository;

    @Transactional
    public Immobile createImmobile(ImmobileRequest request) {
        Contratto contratto = ContrattoVendita.builder()
                                .prezzo(request.getPrezzo())
                                .infoMutuo(request.getInfoMutuo())
                                .build();

        Immobile immobile= Immobile.builder()
                .indirizzo(request.getIndirizzo())
                .contratto(contratto)
                .build();
        immobileRepository.save(immobile);
        return immobile;
    }

    public Immobile getImmobile(int immobileId) {
        return immobileRepository.findById(immobileId)
                .orElseThrow(() -> new ResourceNotFoundException("Immobile", "Id", immobileId));
    }
}
