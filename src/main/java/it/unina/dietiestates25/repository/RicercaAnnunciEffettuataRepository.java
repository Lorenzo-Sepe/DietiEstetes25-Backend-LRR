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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RicercaAnnunciEffettuataRepository extends JpaRepository<RicercaAnnunciEffettuata, Integer>, JpaSpecificationExecutor<RicercaAnnunciEffettuata> {


    List<RicercaAnnunciEffettuata> findByUtente(User user);


    @Query("SELECT r.utente FROM RicercaAnnunciEffettuata r " +
            "WHERE (:prezzoMin IS NULL OR r.prezzoMin >= :prezzoMin) " +
            "AND (:prezzoMax IS NULL OR r.prezzoMax <= :prezzoMax) " +
            "AND (:luogo IS NULL OR :luogo MEMBER OF r.localita) " +
            "AND (:tipologiaContratto IS NULL OR r.tipologiaContratto = :tipologiaContratto) " +
            "AND (:tipologiaImmobile IS NULL OR r.tipologiaImmobile = :tipologiaImmobile) " +
            "AND r.updatedAt >= :dataSettimana")
    List<User> trovaUtentiPerCriteri(@Param("prezzoMin") BigDecimal prezzoMin,
                                     @Param("prezzoMax") BigDecimal prezzoMax,
                                     @Param("luogo") String luogo,
                                     @Param("tipologiaContratto") TipoContratto tipologiaContratto,
                                     @Param("tipologiaImmobile") TipologiaImmobile tipologiaImmobile,
                                     @Param("dataSettimana") LocalDateTime dataSettimana);

    Optional<RicercaAnnunciEffettuata> findFirstByUtenteOrderByUpdatedAtDesc(User user);

}
