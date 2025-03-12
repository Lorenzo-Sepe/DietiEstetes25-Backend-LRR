package it.unina.dietiestates25.service;

import it.unina.dietiestates25.utils.GeneratorePassword;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PasswordService {
    final private GeneratorePassword generatorePassword;
    final private PasswordEncoder passwordEncoder;


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
