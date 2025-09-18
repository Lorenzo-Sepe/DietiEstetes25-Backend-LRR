package it.unina.dietiestates25;

import it.unina.dietiestates25.repository.RicercaAnnunciEffettuataRepository;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.service.RicercaAnnunciEffettuataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CriteriDiRicercaTest {

    RicercaAnnunciEffettuataService ricercaAnnunciEffettuataService;
    @Mock
    RicercaAnnunciEffettuataRepository ricercaAnnunciEffettuataRepository;
    @Mock
    UserRepository userRepository;
    @BeforeEach
    void setup() {
        ricercaAnnunciEffettuataService = new RicercaAnnunciEffettuataService(ricercaAnnunciEffettuataRepository,userRepository);
    }
    @Test
    void NapoliShouldBeValid() {
        String city = "Napoli";
        assertTrue(ricercaAnnunciEffettuataService.isItalianCitty(city));

    }
}
