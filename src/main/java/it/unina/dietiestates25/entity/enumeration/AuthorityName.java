package it.unina.dietiestates25.entity.enumeration;

import lombok.ToString;

@ToString
public enum AuthorityName {

    ADMIN, //modifica annunci, crea agenti, crea admin
    MEMBER, // utente senza ruoli speciali
    AGENT // crea annunci, modifica annunci
}
