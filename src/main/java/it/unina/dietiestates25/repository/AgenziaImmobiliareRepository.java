package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.entity.AgenziaImmobiliare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenziaImmobiliareRepository extends JpaRepository<AgenziaImmobiliare, Integer> {
}