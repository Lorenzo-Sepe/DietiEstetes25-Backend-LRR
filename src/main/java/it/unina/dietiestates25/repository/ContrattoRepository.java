package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.entity.Contratto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContrattoRepository extends JpaRepository<Contratto, Integer> {
}
