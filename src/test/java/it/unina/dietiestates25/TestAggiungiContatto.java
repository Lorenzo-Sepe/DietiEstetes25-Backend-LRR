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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestAggiungiContatto {

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
        assertEquals(1, contattiEsistenti.size());
        assertEquals("Cellulare", contattiEsistenti.get(0).getTipo());
        assertEquals("338469123", contattiEsistenti.get(0).getValore());

        // Controlli sulla DTO in risposta
        assertEquals(1, response.size());

        assertEquals("Cellulare", response.get(0).getTipo());
        assertEquals("338469123", response.get(0).getValore());
        assertEquals("338469123", response.get(0).getValore());
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
        assertEquals(2, contattiEsistenti.size());

        assertEquals("Cellulare", contattiEsistenti.get(0).getTipo());
        assertEquals("3338469123", contattiEsistenti.get(0).getValore());

        assertEquals("Email", contattiEsistenti.get(1).getTipo());
        assertEquals("roby@gmail.com", contattiEsistenti.get(1).getValore());

        // Controlli sulla DTO in risposta
        assertEquals(2, response.size());

        assertEquals("Cellulare", response.get(0).getTipo());
        assertEquals("3338469123", response.get(0).getValore());

        assertEquals("Email", response.get(1).getTipo());
        assertEquals("roby@gmail.com", response.get(1).getValore());
        assertEquals("roby@gmail.com", response.get(1).getValore());
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
        assertEquals(2, contattiEsistenti.size());

        assertEquals("Cellulare", contattiEsistenti.get(0).getTipo());
        assertEquals("3338469123", contattiEsistenti.get(0).getValore());

        assertEquals("Email", contattiEsistenti.get(1).getTipo());
        assertEquals("raimondo@gmail.com", contattiEsistenti.get(1).getValore());

        // Controlli sulla DTO in risposta
        assertEquals(2, response.size());

        assertEquals("Cellulare", response.get(0).getTipo());
        assertEquals("3338469123", response.get(0).getValore());

        assertEquals("Email", response.get(1).getTipo());
        assertEquals("raimondo@gmail.com", response.get(1).getValore());
        assertEquals("raimondo@gmail.com", response.get(1).getValore());

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
        assertEquals(1, contattiEsistenti.size());

        assertEquals("Cellulare", contattiEsistenti.get(0).getTipo());
        assertEquals("31520289137", contattiEsistenti.get(0).getValore());

        // Controlli sulla DTO in risposta
        assertEquals(1, response.size());

        assertEquals("Cellulare", response.get(0).getTipo());
        assertEquals("31520289137", response.get(0).getValore());
        assertEquals("31520289137", response.get(0).getValore());

    }
}
