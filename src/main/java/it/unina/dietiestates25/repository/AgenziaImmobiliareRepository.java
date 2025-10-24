package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.dto.response.AgenziaImmobiliareResponse;
import it.unina.dietiestates25.entity.AgenziaImmobiliare;
import it.unina.dietiestates25.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgenziaImmobiliareRepository extends JpaRepository<AgenziaImmobiliare, Integer> {

    @Query("SELECT new it.unina.dietiestates25.dto.response.AgenziaImmobiliareResponse(" +
            "a.id," +
            "a.nomeAzienda, " +
            "a.ragioneSociale, " +
            "a.partitaIva,  " +
            "u.email" +
            ") FROM AgenziaImmobiliare a " +
            "join User u on a.fondatore.id=u.id")
    List<AgenziaImmobiliareResponse> getAllAgenzieImmobiliari();

    Optional<AgenziaImmobiliare> findAgenziaImmobiliareByDipendentiContains(User dipendente);


    @Query("SELECT a FROM AgenziaImmobiliare a JOIN a.dipendenti d WHERE d.email = :email")
    Optional<AgenziaImmobiliare> findByDipendenteEmail(@Param("email") String email);

    boolean existsByDominio(String dominio);

    boolean existsByRagioneSociale(String ragioneSociale);

    boolean existsByPartitaIva(String partitaIva);
}