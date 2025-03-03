package it.unina.dietiestates25.utils;

import it.unina.dietiestates25.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserContex {

    public static User getUserCurrent() {
        Authentication autenticazione = SecurityContextHolder.getContext().getAuthentication();

        if (autenticazione != null && autenticazione.getPrincipal() instanceof UserDetails) {
            UserDetails dettagliUtente = (UserDetails) autenticazione.getPrincipal();

            if (dettagliUtente instanceof User) {
                return (User) dettagliUtente;
            }
        }
        return null; // Se non c'è alcun utente autenticato
    }
}
