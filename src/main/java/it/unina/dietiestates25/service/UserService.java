package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.agenziaImmobiliare.DipendenteRequest;
import it.unina.dietiestates25.dto.response.NewDipendeteResponse;
import it.unina.dietiestates25.entity.DatiImpiegato;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.entity.enumeration.AuthorityName;
import it.unina.dietiestates25.repository.AuthorityRepository;
import it.unina.dietiestates25.repository.DatiImpiegatoRepository;
import it.unina.dietiestates25.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final DatiImpiegatoRepository datiImpiegatoRepository;
    private final PasswordService passwordService;

    public NewDipendeteResponse AddDipendete(DipendenteRequest request, String aliasAgenzia) {
        String email = generaEmailDipendente(request.getNome(), request.getCognome(), aliasAgenzia);
        String password = passwordService.generaPasswordDipendente();
        AuthorityName authorityName = request.getRuolo().equals("ADMIN") ? AuthorityName.ADMIN : AuthorityName.AGENT;

        User user = User.builder()
                .email(email)
                .username(email)
                .authority(authorityRepository.findByAuthorityName(authorityName).orElseThrow())
                .password(passwordService.cifrarePassword(password))
                .build();

        DatiImpiegato datiImpiegato= DatiImpiegato.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .user(user)
                .build();
        salvaImpiegato(user, datiImpiegato);

        return NewDipendeteResponse.builder().user(user).password(password).build();
    }

    private void salvaImpiegato(User user, DatiImpiegato datiImpiegato) {
        userRepository.save(user);
        datiImpiegatoRepository.save(datiImpiegato);
    }


    private String generaEmailDipendente(String nome, String cognome, String aliasAgenzia) {
        convalidaParametri(nome, cognome, aliasAgenzia);
        String emailBase = generaEmailBase(nome, cognome, aliasAgenzia);
        return risolviDuplicatiEmail(emailBase, nome, cognome, aliasAgenzia);
    }

    private void convalidaParametri(String nome, String cognome, String aliasAgenzia) {
        if (nome == null || nome.isBlank() ||
                cognome == null || cognome.isBlank() ||
                aliasAgenzia == null || aliasAgenzia.isBlank()) {

            throw new IllegalArgumentException(String.format(
                    "Per generare un'email i parametri non devono essere nulli:\n nome: %s\n cognome: %s\n aliasAgenzia: %s",
                    nome, cognome, aliasAgenzia
            ));
        }
    }

    private String generaEmailBase(String nome, String cognome, String aliasAgenzia) {
        return String.format("%s.%s@%s.dietiestate.com",
                nome.toLowerCase(),
                cognome.toLowerCase(),
                aliasAgenzia.toLowerCase());
    }

    private String risolviDuplicatiEmail(String emailBase, String nome, String cognome, String aliasAgenzia) {
        String email = emailBase;
        int contatore = 1;

        while (userRepository.countByEmail(email) > 0) {
            email = String.format("%s.%s%d@%s.dietiestate.com",
                    nome.toLowerCase(),
                    cognome.toLowerCase(),
                    ++contatore,
                    aliasAgenzia.toLowerCase());
        }

        return email;
    }


}
