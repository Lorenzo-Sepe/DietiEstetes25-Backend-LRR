package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.entity.Immobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImmobileRepository extends JpaRepository<Immobile, Integer> {
}
