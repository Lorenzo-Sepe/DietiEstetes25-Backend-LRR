package it.unina.dietiestates25.utils;

import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.entity.enumeration.AuthorityName;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserContex {

    private UserContex() {
        throw new UnsupportedOperationException("Questa classe non può essere istanziata.");
    }

    public static User getUserCurrent() {
        Authentication autenticazione = SecurityContextHolder.getContext().getAuthentication();

        if (autenticazione != null && autenticazione.getPrincipal() instanceof UserDetails dettagliUtente
                && dettagliUtente instanceof User user) {
            return user;
        }
        return null;
    }

    public static AuthorityName getRoleCurrent() {
        User user = getUserCurrent();
        if (user != null) {
            return user.getAuthority().getAuthorityName();
        }
        return null;
    }
}
