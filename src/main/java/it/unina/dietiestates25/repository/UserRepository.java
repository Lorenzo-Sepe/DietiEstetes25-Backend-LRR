package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    int countByEmail(String email);

    Optional<User> findByEmail(String email);
}
