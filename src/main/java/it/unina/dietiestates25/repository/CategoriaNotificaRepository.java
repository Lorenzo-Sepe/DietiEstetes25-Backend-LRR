package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.entity.CategoriaNotifica;
import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaNotificaRepository extends JpaRepository<CategoriaNotifica,Integer> {

    Optional<CategoriaNotifica> findByCategoriaName(CategoriaNotificaName categoriaName);
}
