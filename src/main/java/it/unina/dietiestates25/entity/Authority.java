package it.unina.dietiestates25.entity;

import it.unina.dietiestates25.entity.common.CreationUpdate;
import it.unina.dietiestates25.entity.enumeration.AuthorityName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Authority extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private byte id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, length = 9, nullable = false)
    private AuthorityName authorityName;

    private boolean defaultAuthority;

}
