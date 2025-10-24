package it.unina.dietiestates25.service;

import it.unina.dietiestates25.utils.GeneratorePassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PasswordService {
    private final GeneratorePassword generatorePassword;
    private final PasswordEncoder passwordEncoder;


    public String generaPasswordDipendente() {
        return generatorePassword.generaPasswordDipendente();
    }

    public String cifrarePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean verificaPassword(String password, String passwordCifrata) {
        return passwordEncoder.matches(password, passwordCifrata);
    }

}
