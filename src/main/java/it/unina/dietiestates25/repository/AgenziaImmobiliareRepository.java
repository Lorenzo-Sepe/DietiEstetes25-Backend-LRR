package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.dto.response.AgenziaImmobiliareResponse;
import it.unina.dietiestates25.entity.AgenziaImmobiliare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}