package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.entity.AnnuncioImmobiliare;
import it.unina.dietiestates25.entity.Proposta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropostaRepository extends JpaRepository<Proposta, Integer> {
    List<Proposta> findByAnnuncio(AnnuncioImmobiliare annuncio, Pageable pageable);
}
