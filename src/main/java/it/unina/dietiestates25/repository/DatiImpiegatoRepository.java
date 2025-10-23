package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.entity.DatiImpiegato;
import it.unina.dietiestates25.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DatiImpiegatoRepository extends JpaRepository<DatiImpiegato, Integer> {

    Optional<DatiImpiegato> findByUser_Id(int id);

    Optional<DatiImpiegato> findByUser_Email(String email);

    Optional<DatiImpiegato> findDatiImpiegatoByUser(User agente);
}
