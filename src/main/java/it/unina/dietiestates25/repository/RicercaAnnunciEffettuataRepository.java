package it.unina.dietiestates25.repository;


import it.unina.dietiestates25.entity.RicercaAnnunciEffettuata;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.entity.enumeration.TipoContratto;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RicercaAnnunciEffettuataRepository extends JpaRepository<RicercaAnnunciEffettuata, Integer>, JpaSpecificationExecutor<RicercaAnnunciEffettuata> {

    @Query("SELECT r FROM RicercaAnnunciEffettuata r WHERE " +
            "r.tipologiaImmobile = :tipologiaImmobile " +
            "AND r.tipologiaContratto = :tipologiaContratto " +
            "AND :localita MEMBER OF r.localita " +
            "AND r.prezzoMin <= :prezzoMax " +
            "AND r.prezzoMax >= :prezzoMin")
    List<RicercaAnnunciEffettuata> findByTipologiaImmobileAndTipologiaContrattoAndLocalitaAndPrezzoMinLessThanEqualAndPrezzoMaxGreaterThanEqual(
            @Param("tipologiaImmobile") TipologiaImmobile tipologiaImmobile,
            @Param("tipologiaContratto") TipoContratto tipologiaContratto,
            @Param("localita") String localita,
            @Param("prezzoMax") BigDecimal prezzoMax,
            @Param("prezzoMin") BigDecimal prezzoMin);
    @Query("SELECT DISTINCT r.utente FROM RicercaAnnunciEffettuata r WHERE :localita MEMBER OF r.localita")
    List<User> findUtentiByLocalita(@Param("localita") String localita);

    List<RicercaAnnunciEffettuata> findByUtente(User user);
}
