package it.unina.dietiestates25.repository;

import it.unina.dietiestates25.entity.Authority;
import it.unina.dietiestates25.entity.enumeration.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Byte> {

    Optional<Authority> findByDefaultAuthorityTrue();
    // SELECT * FROM authority WHERE default_authority = true AND visible = true;

    Optional<Authority> findByAuthorityName(AuthorityName authorityName);
    // SELECT * FROM authority WHERE authority_name = 'MEMBER' AND visible = true;
}
