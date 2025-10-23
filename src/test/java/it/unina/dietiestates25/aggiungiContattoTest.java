package it.unina.dietiestates25;

import it.unina.dietiestates25.dto.request.ContattoRequest;
import it.unina.dietiestates25.dto.response.ContattoResponse;
import it.unina.dietiestates25.entity.Contatto;
import it.unina.dietiestates25.entity.DatiImpiegato;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.repository.DatiImpiegatoRepository;
import it.unina.dietiestates25.service.DatiImpiegatoService;
import it.unina.dietiestates25.utils.UserContex;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class AggiungiContattoTest {

    @Mock
    private DatiImpiegatoRepository datiImpiegatoRepository;

    private MockedStatic<UserContex> mocked;

    private ContattoRequest request;

    private List<Contatto> contattiEsistenti;

    private DatiImpiegato datiImpiegato;

    @InjectMocks
    private DatiImpiegatoService datiImpiegatoService;

    @BeforeEach
    void setup() {

        mocked = Mockito.mockStatic(UserContex.class);

        request = ContattoRequest.builder().build();

        contattiEsistenti = new ArrayList<>();

        datiImpiegato = DatiImpiegato.builder()
                .contatti(contattiEsistenti)
                .build();

        User mockUser = new User();

        mocked.when(UserContex::getUserCurrent).thenReturn(mockUser);

        lenient().when(datiImpiegatoRepository.findDatiImpiegatoByUser(mockUser))
                .thenReturn(Optional.of(datiImpiegato));
    }

    @AfterEach
    void tearDown() {

        mocked.close();
    }

    /**
     * Test verifica l'eccezione in caso di UserCurrent non trovato.
     * Copertura 1-7
     */
    @Test
    void aggiungiContatto_ShouldThrowUnauthorizedException_WhenUserContextNotFound() {

        mocked.when(UserContex::getUserCurrent).thenReturn(null);

        assertThrows(it.unina.dietiestates25.exception.UnauthorizedException.class,
                () -> datiImpiegatoService.aggiungiContatto(request));
    }

    /**
     * verifica che venga lanciata una ResourceNotFoundException quando l'utente non
     * viene trovato nel database.
     * Copertura cammino 1-2-8
     */
    @Test
    void aggiungiContatto_ShouldThrowResourceNotFoundException_WhenUserNotFoundInDB() {

        User mockUser = new User();

        mocked.when(UserContex::getUserCurrent).thenReturn(mockUser);
        when(datiImpiegatoRepository.findDatiImpiegatoByUser(mockUser)).thenReturn(Optional.empty());

        assertThrows(it.unina.dietiestates25.exception.ResourceNotFoundException.class,
                () -> datiImpiegatoService.aggiungiContatto(request));

    }

    /**
     * Verifica che aggiunge un nuovo contatto tra i contatti già esistenti
     * dell'agente
     * Copertura cammino 1-2-3-4-5-6-5
     */
    @Test
    void aggiungiContatto_ShouldAddNewContactWithoutExistingContacts() {

        request.setTipo("Cellulare");
        request.setValore("338469123");

        List<ContattoResponse> response = datiImpiegatoService.aggiungiContatto(request);

        // Controlli sul nuovo stato dei contatti esistenti dell'agente
        assertTrue(contattiEsistenti.size() == 1);
        assertTrue(contattiEsistenti.get(0).getTipo().equals("Cellulare"));
        assertTrue(contattiEsistenti.get(0).getValore().equals("338469123"));

        // Controlli sulla DTO in risposta
        assertTrue(response.size() == 1);

        assertTrue(response.get(0).getTipo().equals("Cellulare"));
        assertTrue(response.get(0).getValore().equals("338469123"));
    }

    /**
     * Verica che aggiunge un nuovo contatto (come sorpa ma con almen un iterazione
     * tra i contati esistenti)
     * Copertura cammino 1-2-3-9-3-4-5-6-5-6-5
     */
    @Test
    void aggiungiContatto_ShouldAddNewConcatWithExistingContacts() {

        Contatto contatto = Contatto.builder()
                .tipo("Cellulare")
                .valore("3338469123")
                .build();

        contattiEsistenti.add(contatto);

        request.setTipo("Email");
        request.setValore("roby@gmail.com");

        List<ContattoResponse> response = datiImpiegatoService.aggiungiContatto(request);

        // Controlli sul nuovo stato dei contatti esistenti dell'agente
        assertTrue(contattiEsistenti.size() == 2);

        assertTrue(contattiEsistenti.get(0).getTipo().equals("Cellulare"));
        assertTrue(contattiEsistenti.get(0).getValore().equals("3338469123"));

        assertTrue(contattiEsistenti.get(1).getTipo().equals("Email"));
        assertTrue(contattiEsistenti.get(1).getValore().equals("roby@gmail.com"));

        // Controlli sulla DTO in risposta
        assertTrue(response.size() == 2);

        assertTrue(response.get(0).getTipo().equals("Cellulare"));
        assertTrue(response.get(0).getValore().equals("3338469123"));

        assertTrue(response.get(1).getTipo().equals("Email"));
        assertTrue(response.get(1).getValore().equals("roby@gmail.com"));
    }

    /**
     * verifica che si accorge che il contatto è un tipo già esistente e quindi deve
     * modificarne il valore non aggiungere uno nuovo.
     * Copertura cammino 1-2-3-9-3-9-10-5-6-5-6-5
     */
    @Test
    void aggiungiContatto_ShouldModifyExistingContact() {

        Contatto contatto1 = Contatto.builder()
                .tipo("Cellulare")
                .valore("3338469123")
                .build();

        Contatto contatto2 = Contatto.builder()
                .valore("roby@gmail.com")
                .tipo("Email")
                .build();

        contattiEsistenti.add(contatto1);
        contattiEsistenti.add(contatto2);

        request.setTipo("Email");
        request.setValore("raimondo@gmail.com");

        List<ContattoResponse> response = datiImpiegatoService.aggiungiContatto(request);

        // Controlli sul nuovo stato dei contatti esistenti dell'agente
        assertTrue(contattiEsistenti.size() == 2);

        assertTrue(contattiEsistenti.get(0).getTipo().equals("Cellulare"));
        assertTrue(contattiEsistenti.get(0).getValore().equals("3338469123"));

        assertTrue(contattiEsistenti.get(1).getTipo().equals("Email"));
        assertTrue(contattiEsistenti.get(1).getValore().equals("raimondo@gmail.com"));

        // Controlli sulla DTO in risposta
        assertTrue(response.size() == 2);

        assertTrue(response.get(0).getTipo().equals("Cellulare"));
        assertTrue(response.get(0).getValore().equals("3338469123"));

        assertTrue(response.get(1).getTipo().equals("Email"));
        assertTrue(response.get(1).getValore().equals("raimondo@gmail.com"));

    }

    /**
     * verifica che si accorge di moficare il valore di un contatto invece di
     * aggihgerlo uno nuovo. (Stessa verifica di sopra ma senza iterazione nel
     * ciclo)
     * Copertura cammino 1-2-3-9-10-5-6-5
     */
    @Test
    void aggiungiContatto_ShouldModifyExistingContactWithoutIteration() {

        Contatto contatto = Contatto.builder()
                .tipo("Cellulare")
                .valore("3338469123")
                .build();

        contattiEsistenti.add(contatto);

        request.setTipo("Cellulare");
        request.setValore("31520289137");

        List<ContattoResponse> response = datiImpiegatoService.aggiungiContatto(request);

        // Controlli sul nuovo stato dei contatti esistenti dell'agente
        assertTrue(contattiEsistenti.size() == 1);

        assertTrue(contattiEsistenti.get(0).getTipo().equals("Cellulare"));
        assertTrue(contattiEsistenti.get(0).getValore().equals("31520289137"));

        // Controlli sulla DTO in risposta
        assertTrue(response.size() == 1);

        assertTrue(response.get(0).getTipo().equals("Cellulare"));
        assertTrue(response.get(0).getValore().equals("31520289137"));

    }
}
