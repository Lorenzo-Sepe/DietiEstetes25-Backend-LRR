package it.unina.dietiestates25;

import it.unina.dietiestates25.dto.request.CriteriDiRicercaUtenti;
import it.unina.dietiestates25.entity.enumeration.TipoContratto;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import it.unina.dietiestates25.repository.RicercaAnnunciEffettuataRepository;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.service.IndirizzoService;
import it.unina.dietiestates25.service.RicercaAnnunciEffettuataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CriteriDiRicercaTest {

    RicercaAnnunciEffettuataService ricercaAnnunciEffettuataService;
    @Mock
    RicercaAnnunciEffettuataRepository mockRicercaAnnunciEffettuataRepository;
    @Mock
    UserRepository MockUserRepository;
    @Mock
    IndirizzoService mockIndirizzoService;

    CriteriDiRicercaUtenti request;

    @BeforeEach
    void setup() {
        ricercaAnnunciEffettuataService = new RicercaAnnunciEffettuataService(mockRicercaAnnunciEffettuataRepository,MockUserRepository,mockIndirizzoService);
        request = CriteriDiRicercaUtenti.builder()
                .intervalloGiorniStoricoRicerca(10)
                .tipoDiContrattoDiInteresse(TipoContratto.AFFITTO)
                .tipologiaDiImmobileDiInteresse(TipologiaImmobile.APPARTAMENTO)
                .budgetMin(BigDecimal.valueOf(100))
                .budgetMax(BigDecimal.valueOf(600))
                .areaDiInteresse("Napoli")
                .build();
    }


    @AfterEach
    void verifyRepositorySave() {
        verify(mockRicercaAnnunciEffettuataRepository).trovaUtentiPerCriteri(
                eq(request.getBudgetMin()),
                eq(request.getBudgetMax()),
                eq(request.getAreaDiInteresse()),
                eq(request.getTipoDiContrattoDiInteresse()),
                eq(request.getTipologiaDiImmobileDiInteresse()),
                any(LocalDateTime.class)  // qualsiasi valore va bene
        );


    }


    /**
     * Test di tutte le classi valide
     *
     */

    @Test
    void validRequestShouldNotChangeAnything() {
        ricercaAnnunciEffettuataService.utentiInteressati(request);
        assertTrue(request.getAreaDiInteresse().equals("Napoli"));
        assertTrue(request.getBudgetMin().equals(BigDecimal.valueOf(100)));
        assertTrue(request.getBudgetMax().equals(BigDecimal.valueOf(600)));
        assertTrue(request.getTipologiaDiImmobileDiInteresse().equals(TipologiaImmobile.APPARTAMENTO));
        assertTrue(request.getTipoDiContrattoDiInteresse().equals(TipoContratto.AFFITTO));

    }

    /**
     * test con tutti i parametri null non deve modificare nulla tranne i giorni che deve settarli a 7
     */

    @Test
    void allNullParametersShouldSetDaysTo7() {
        request.setAreaDiInteresse(null);
        request.setBudgetMin(null);
        request.setBudgetMax(null);
        request.setTipologiaDiImmobileDiInteresse(null);
        request.setTipoDiContrattoDiInteresse(null);
        request.setIntervalloGiorniStoricoRicerca(0);
        ricercaAnnunciEffettuataService.utentiInteressati(request);
        assertTrue(request.getAreaDiInteresse()==null);
        assertTrue(request.getBudgetMin()==null);
        assertTrue(request.getBudgetMax()==null);
        assertTrue(request.getTipologiaDiImmobileDiInteresse()==null);
        assertTrue(request.getTipoDiContrattoDiInteresse()==null);
        assertTrue(request.getIntervalloGiorniStoricoRicerca()==7);

    }

    /**
     * test con area di intersse Italia non deve modificare nulla tranne l'area di interesse che deve settarla a null
     */
    @Test
    void areaDiInteresseItaliaShouldSetNullAreaDiInteresse() {
        request.setAreaDiInteresse("Italia");
        ricercaAnnunciEffettuataService.utentiInteressati(request);
        assertTrue(request.getAreaDiInteresse()==null);
    }

    /**
     * test che controlla che se inserisco un budget max minore del budget min me li scambia
     */
    @Test
    void budgetMaxLessThanBudgetMinShouldSwapThem() {
        request.setBudgetMin(BigDecimal.valueOf(600));
        request.setBudgetMax(BigDecimal.valueOf(100));
        ricercaAnnunciEffettuataService.utentiInteressati(request);
        assertTrue(request.getBudgetMin().equals(BigDecimal.valueOf(100)));
        assertTrue(request.getBudgetMax().equals(BigDecimal.valueOf(600)));
    }


    /**
     * BATTERIA di test che controlls che la request da in input a utentiInteressati modifica i dati dei criteri di ricerca qualora non sono validi o incompleti
     *
     */
    /**
     * Primo test per vedere se con la request tutta corretta non mi modifica nulla
     */


    /**
     * Test che controlla che se inerisco una citta che non esiste mi setti a null il campo area di interesse della request
     */
    @Test
    void invalidCityShouldSetNullAreaDiInteresse() {
        request.setAreaDiInteresse("Città Inesistente");
        ricercaAnnunciEffettuataService.utentiInteressati(request);
        assertTrue(request.getAreaDiInteresse()==null);
    }

    /**
     * test che controlla che se inserisco un budget min negativo me lo setta a null
     */
    @Test
    void negativeBudgetMinShouldSetNullBudgetMin() {
        request.setBudgetMin(BigDecimal.valueOf(-100));
        ricercaAnnunciEffettuataService.utentiInteressati(request);

        assertTrue(request.getBudgetMin()==null);
    }
    /**
     * test che controlla che se inserisco un budget max negativo me lo setta a null
     */
    @Test
    void negativeBudgetMaxShouldSetNullBudgetMax() {
        request.setBudgetMax(BigDecimal.valueOf(-100));
        ricercaAnnunciEffettuataService.utentiInteressati(request);
        assertTrue(request.getBudgetMax()==null);
    }

    /**
     * test che controlla se inserisco un intervallo di giorni negativo me lo setta a 7
     */
    @Test
    void deltaDaysLessThanZeroShouldSet7() {
        request.setIntervalloGiorniStoricoRicerca(0);
        ricercaAnnunciEffettuataService.utentiInteressati(request);
        assertTrue(request.getIntervalloGiorniStoricoRicerca()==7);

    }


}
