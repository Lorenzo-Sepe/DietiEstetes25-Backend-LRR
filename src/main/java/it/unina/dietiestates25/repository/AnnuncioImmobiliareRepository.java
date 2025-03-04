package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.entity.AnnuncioImmobiliare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnuncioImmobiliareRepository extends JpaRepository<AnnuncioImmobiliare, Integer>, JpaSpecificationExecutor<AnnuncioImmobiliare> {

}
