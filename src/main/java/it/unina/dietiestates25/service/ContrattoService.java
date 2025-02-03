package it.unina.dietiestates25.service;

import it.unina.dietiestates25.repository.ContrattoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContrattoService {
    private final ContrattoRepository contrattoRepository;

}
