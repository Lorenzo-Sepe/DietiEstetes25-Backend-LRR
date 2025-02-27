package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.entity.Immobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImmobileRepository extends JpaRepository<Immobile, Integer> {



}
