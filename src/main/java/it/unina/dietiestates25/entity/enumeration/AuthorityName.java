package it.unina.dietiestates25.entity.enumeration;

import lombok.ToString;

@ToString
public enum AuthorityName {

    MANAGER, //modifica annunci, crea agenti, crea manger
    MEMBER, // utente senza ruoli speciali
    AGENT // crea annunci, modifica annunci
}
