package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.entity.AnnuncioImmobiliare;
import it.unina.dietiestates25.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AnnuncioImmobiliareRepository extends JpaRepository<AnnuncioImmobiliare, Integer>, JpaSpecificationExecutor<AnnuncioImmobiliare> {

    List<AnnuncioImmobiliare> findByAgente(User agente, Pageable pageable);

    List<AnnuncioImmobiliare> findByAgente(User agente);

    List<AnnuncioImmobiliare> findByAgenteIn(Collection<User> agentes, Pageable pageable);

    List<AnnuncioImmobiliare> findByAgenteIn(Collection<User> agentes);

    long countByAgente(User agente);

    long countByAgenteIn(Collection<User> agentes);


    }
