package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.entity.Immobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface ImmobileRepository extends JpaRepository<Immobile, Integer> {


    @Query(value = "SELECT i FROM Immobile i " +
            "left join ContrattoAffitto ca on i.contratto.id=ca.id " +
            "left join ContrattoVendita cv on i.contratto.id=cv.id " +
            "WHERE i.id = :immobileId")
    Optional<Immobile> getImmobileById(int immobileId);
}
