package it.unina.dietiestates25;

import com.azure.storage.blob.BlobContainerClient;
import it.unina.dietiestates25.dto.request.agenzia_immobiliare.DipendenteRequest;
import it.unina.dietiestates25.dto.response.NewDipendeteResponse;
import it.unina.dietiestates25.entity.Authority;
import it.unina.dietiestates25.entity.DatiImpiegato;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.entity.enumeration.AuthorityName;
import it.unina.dietiestates25.repository.AuthorityRepository;
import it.unina.dietiestates25.repository.DatiImpiegatoRepository;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.service.CategoriaNotificaService;
import it.unina.dietiestates25.service.ImageUploaderService;
import it.unina.dietiestates25.service.PasswordService;
import it.unina.dietiestates25.service.UserService;
import it.unina.dietiestates25.utils.GeneratorePassword;
import it.unina.dietiestates25.utils.ImageContainerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestAddDipendente {

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DatiImpiegatoRepository datiImpiegatoRepository;

    @Mock
    private CategoriaNotificaService categoriaService;

    // Servizi reali che vogliamo usare
    private PasswordService passwordService;
    private ImageUploaderService imageUploaderService;

    private UserService userService;

    @BeforeEach
    void setUp() {

        // Oggetti che servono realmente durante il test
        passwordService = new PasswordService(new GeneratorePassword(), new BCryptPasswordEncoder());
        imageUploaderService = new ImageUploaderService(
                new ImageContainerUtil(Mockito.mock(BlobContainerClient.class)));

        // UserService istanziato manualmente passando sia i mock che i servizi reali
        userService = new UserService(
                authorityRepository,
                userRepository,
                datiImpiegatoRepository,
                passwordService, // reale
                imageUploaderService, // reale
                categoriaService);
    }

    /**
     * Test che copre le classi valide: DRN1 (Valida); DRC1 (Valida); DRR1(Valida);
     * AA1(Valida);
     */
    @Test
    void addDipedenteTestAddAgente() {

        DipendenteRequest dipendente = new DipendenteRequest("Roberto", "Spena", "AGENT");
        String dominioAgenzia = "RobyImmobili";

        when(userRepository.countByEmail("roberto.spena@robyimmobili.dietiestate.com")).thenReturn(0);

        Authority fakeAuthority = new Authority();
        fakeAuthority.setAuthorityName(AuthorityName.AGENT);
        when(authorityRepository.findByAuthorityName(AuthorityName.AGENT)).thenReturn(Optional.of(fakeAuthority));

        User fakeUser = new User();
        fakeUser.setId(1); // id fittizio
        DatiImpiegato fakeDati = new DatiImpiegato();
        when(userRepository.save(Mockito.any(User.class))).thenReturn(fakeUser);
        when(datiImpiegatoRepository.save(Mockito.any(DatiImpiegato.class))).thenReturn(fakeDati);

        NewDipendeteResponse response = userService.addDipendete(dipendente, dominioAgenzia);

        // Controllo email generata
        String emailPrevista = "roberto.spena@robyimmobili.dietiestate.com";
        assertEquals(emailPrevista, response.getUser().getEmail(), "Email non generata correttamente");

        // Controllo password generata
        String password = response.getPassword();
        assertEquals(12, password.length(), "La password deve essere lunga 12 caratteri");
        assertTrue(
                password.matches(".*[a-zA-Z].*"),
                "La stringa deve contenere almeno una lettera");
        assertTrue(
                password.matches(".*[!@#$%^&*()\\\\-_=+\\\\[\\\\]{}|;:,.<>?].*"),
                "La stringa deve contenere almeno un carattere speciale");
        assertTrue(
                password.matches(".*[0-9].*"),
                "La stringa deve contenere almeno una cifra");

        // Controllo nomeVisualizzato
        String nomeVisualizzato = response.getUser().getNomeVisualizzato();
        String nomeVisualizzatoPrevisto = "Agente Roberto S.";
        assertEquals(nomeVisualizzato, nomeVisualizzatoPrevisto);

        // Controllo foto profilo
        String fotoProfilo = response.getUser().getUrlFotoProfilo();
        assertTrue(fotoProfilo.matches("^https://dummyimage.com/.*"), "Url foto profilo non valido");
    }

    /**
     * Test che copre le classi valide: DRN1 (Valida); DRC1 (Valida); DRR2(Valida);
     * AA1(Valida);
     */
    @Test
    void addDipedenteTestAddManager() {

        DipendenteRequest dipendente = new DipendenteRequest("Raimondo", "Morosini", "MANAGER");
        String dominioAgenzia = "RaimondoImmobili";

        when(userRepository.countByEmail("raimondo.morosini@raimondoimmobili.dietiestate.com")).thenReturn(0);

        Authority fakeAuthority = new Authority();
        fakeAuthority.setAuthorityName(AuthorityName.MANAGER);
        when(authorityRepository.findByAuthorityName(AuthorityName.MANAGER)).thenReturn(Optional.of(fakeAuthority));

        User fakeUser = new User();
        fakeUser.setId(1); // id fittizio
        DatiImpiegato fakeDati = new DatiImpiegato();
        when(userRepository.save(Mockito.any(User.class))).thenReturn(fakeUser);
        when(datiImpiegatoRepository.save(Mockito.any(DatiImpiegato.class))).thenReturn(fakeDati);

        NewDipendeteResponse response = userService.addDipendete(dipendente, dominioAgenzia);

        // Controllo email generata
        String emailPrevista = "raimondo.morosini@raimondoimmobili.dietiestate.com";
        assertEquals(emailPrevista, response.getUser().getEmail(), "Email non generata correttamente");

        // Controllo password generata
        String password = response.getPassword();
        assertEquals(12, password.length(), "La password deve essere lunga 12 caratteri");
        assertTrue(
                password.matches(".*[a-zA-Z].*"),
                "La stringa deve contenere almeno una lettera");
        assertTrue(
                password.matches(".*[!@#$%^&*()\\\\-_=+\\\\[\\\\]{}|;:,.<>?].*"),
                "La stringa deve contenere almeno un carattere speciale");
        assertTrue(
                password.matches(".*[0-9].*"),
                "La stringa deve contenere almeno una cifra");

        // Controllo nomeVisualizzato
        String nomeVisualizzato = response.getUser().getNomeVisualizzato();
        String nomeVisualizzatoPrevisto = "Manager Raimondo M.";
        assertEquals(nomeVisualizzato, nomeVisualizzatoPrevisto);

        // Controllo foto profilo
        String fotoProfilo = response.getUser().getUrlFotoProfilo();
        assertTrue(fotoProfilo.matches("^https://dummyimage.com/.*"), "Url foto profilo non valido");
    }

    /**
     * Test per coprire la classe nome non valida: DRN1 (Non Valida); DRC1 (Valida);
     * DRR2(Valida); AA1(Valida);
     */
    @Test
    void addDipedenteTestNomeNonValido() {

        DipendenteRequest dipendente = new DipendenteRequest(null, "Sepe", "MANAGER");
        String dominioAgenzia = null;

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> userService.addDipendete(dipendente, dominioAgenzia));

        assertTrue(ex.getMessage().contains("Il parametro nome è vuoto."));
    }

    /**
     * Test per coprire la classe cognome non valida: DRN1 (Valida); DRC1 (Non
     * Valida); DRR2(Valida); AA1(Valida);
     */
    @Test
    void addDipedenteTestCognomeNonValido() {

        DipendenteRequest dipendente = new DipendenteRequest("Lorenzo", "", "MANAGER");
        String dominioAgenzia = "LorenzoImmobili";

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> userService.addDipendete(dipendente, dominioAgenzia));

        assertTrue(ex.getMessage().contains("Il parametro cognome è vuoto."));
    }

    /**
     * Test per coprire la classe ruolo non valida: DRN1 (Valida); DRC1 (Valida);
     * DRR2(Non Valida); AA1(Valida);
     */
    @Test
    void addDipedenteTestRuoloNonValido() {

        DipendenteRequest dipendente = new DipendenteRequest("", "Sepe", "ADMIN");
        String dominioAgenzia = "LorenzoImmobili";

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> userService.addDipendete(dipendente, dominioAgenzia));

        assertTrue(ex.getMessage().contains("Ruolo non valido:"));
    }

    /**
     * Test per coprire la classe ruolo non esistente: DRN1 (Valida); DRC1 (Valida);
     * DRR4(Non Valida); AA1(Valida);
     */
    @Test
    void addDipedenteTestRuoloInesistente() {

        DipendenteRequest dipendente = new DipendenteRequest("", "Sepe", "");
        String dominioAgenzia = "LorenzoImmobili";

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> userService.addDipendete(dipendente, dominioAgenzia));

        assertTrue(ex.getMessage().contains("Ruolo inesistente"));
    }

    /**
     * Test per coprire la classe aliasAgeniza non valida: DRN1 (Valida); DRC1
     * (Valida); DRR2(Valida); AA1(Non Valida);
     */
    @Test
    void addDipedenteTestDominioAgenziaNonValido() {

        DipendenteRequest dipendente = new DipendenteRequest("Lorenzo", "Sepe", "MANAGER");
        String dominioAgenzia = null;

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> userService.addDipendete(dipendente, dominioAgenzia));

        assertTrue(ex.getMessage().contains("Il parametro aliasAgenzia è vuoto."));
    }

}
