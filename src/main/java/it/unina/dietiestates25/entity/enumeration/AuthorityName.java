package it.unina.dietiestates25.entity.enumeration;

import lombok.ToString;

@ToString
public enum AuthorityName {

    ADMIN,
    MEMBER, // scrive i commenti, vota i post, segnala i commenti altrui
    AGENT, // scrive i post
    GUEST // di default all'iscrizione finché non conferma email
}
