package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.entity.CategoriaNotifica;
import it.unina.dietiestates25.entity.Notifica;
import it.unina.dietiestates25.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificaRepository extends JpaRepository<Notifica,Integer> {

    List<Notifica> findAllByDestinatario(User destinatario, Pageable pageable);

    List<Notifica> findAllByDestinatarioAndCategoria(User destinatario, CategoriaNotifica categoria, Pageable pageable);

    Integer countByDestinatario(User destinatario);

    Integer countByDestinatarioAndCategoria(User destinatario, CategoriaNotifica categoriaNotifica);
}
