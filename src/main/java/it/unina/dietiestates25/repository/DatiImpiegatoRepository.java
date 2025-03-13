package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.entity.DatiImpiegato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DatiImpiegatoRepository extends JpaRepository<DatiImpiegato, Integer> {
    Optional<DatiImpiegato> findByUser_Id(int id);
}
