package it.unina.dietiestates25.service;



import it.unina.dietiestates25.dto.request.*;
import it.unina.dietiestates25.entity.*;
import it.unina.dietiestates25.entity.enumeration.ClasseEnergetica;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import it.unina.dietiestates25.repository.AnnuncioImmobiliareRepository;
import it.unina.dietiestates25.utils.UserContex;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnuncioImmobileService {

    private final AnnuncioImmobiliareRepository annuncioImmobiliareRepository;

    @Transactional
    public String creaAnnuncioImmobiliare(AnnuncioImmobiliareRequest request){

        User agenteImmobliare = UserContex.getUserCurrent();

        Immobile immobile = getImmobileByRequest(request.getImmobile());

        Contratto contratto = getContrattoFromRequest(request.getContratto());

        AnnuncioImmobiliare annuncioImmobiliare = AnnuncioImmobiliare.builder()
                .immobile(immobile)
                .contratto(contratto)
                .agente(agenteImmobliare)
                .dataPubblicazione(LocalDateTime.now())
                .descrizione(request.getDescrizione())
                .titolo(request.getTitolo())
                .proposte(new ArrayList<>())
                .build();

        annuncioImmobiliareRepository.save(annuncioImmobiliare);

        return "Annuncio creato con successo";
    }

    private Immobile getImmobileByRequest(ImmobileRequest request){

        Immobile immobile = Immobile.builder()
                .tipologiaImmobile(getEnumTipologiaImmobileFromString(request.getTipologiaImmobile()))
                .metriQuadri(request.getMetriQuadri())
                .classeEnergetica(getEnumClasseEnergeticaFromString(request.getClasseEnergetica()))
                .numeroServizi(request.getNumeroServizi())
                .numeroStanze(request.getNumeroStanze())
                .numeroDiPiani(request.getNumeroDiPiani())
                .indirizzo(getIndirizzoFromRequest(request.getIndirizzo()))
                .caratteristicheAggiuntive(getCaratteristicheAggiuntiveFromRequest(request.getCaratteristicheAggiuntive()))
                .build();

        immobile.setImmagini(getListaImmaginiFromRequest(request.getImmagini(),immobile));

        return immobile;
    }

    private TipologiaImmobile getEnumTipologiaImmobileFromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Valore nullo o vuoto");
        }
        try {
            return TipologiaImmobile.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nessun valore dell'enumerazione TipologiaImmobile corrispondente a: " + value);
        }
    }

    private ClasseEnergetica getEnumClasseEnergeticaFromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Valore nullo o vuoto");
        }
        try {
            return ClasseEnergetica.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nessun valore dell'enumerazione ClasseEnergetica corrispondente a: " + value);
        }
    }

    private Indirizzo getIndirizzoFromRequest(IndirizzoRequest request){

        Indirizzo indirizzo = Indirizzo.builder()
                .nazione(request.getNazione())
                .cap(request.getCap())
                .citta(request.getCitta())
                .provincia(request.getProvincia())
                .via(request.getVia())
                .numeroCivico(request.getNumeroCivico())
                .longitudine(request.getLongitudine())
                .latitudine(request.getLatitudine())
                .build();

        return indirizzo;
    }

    private CaratteristicheAggiuntive getCaratteristicheAggiuntiveFromRequest(CaratteristicheAggiuntiveRequest request){

        CaratteristicheAggiuntive caratteristicheAggiuntive = CaratteristicheAggiuntive.builder()
                .ascensore(request.isAscensore())
                .balconi(request.isBalconi())
                .cantina(request.isCantina())
                .climatizzatori(request.isClimatizzatori())
                .garage(request.isGarage())
                .pannelliSolari(request.isPannelliSolari())
                .portiere(request.isPortiere())
                .giardino(request.isGiardino())
                .riscaldamentoCentralizzato(request.isRiscaldamentoCentralizzato())
                .soffitta(request.isSoffitta())
                .postiAuto(request.isPostiAuto())
                .build();

        return caratteristicheAggiuntive;
    }

    private Contratto getContrattoFromRequest(ContrattoRequest request){

        Contratto contratto;

        if(request.getDatiAffittoRequest() != null)
            contratto = getContrattoAffittoFromRequest(request);

        else
            contratto = getContrattoVenditaFromRequest(request);

        return contratto;
    }

    private ContrattoAffitto getContrattoAffittoFromRequest(ContrattoRequest request){

        DatiAffittoRequest datiAffittoRequest = request.getDatiAffittoRequest();

        ContrattoAffitto contrattoAffitto = ContrattoAffitto.builder()
                .caparra(datiAffittoRequest.getCaparra())
                .tempoMinimo(datiAffittoRequest.getTempoMinimo())
                .tempoMassimo(datiAffittoRequest.getTempoMassimo())
                .prezzo(request.getPrezzo())
                .build();

        return contrattoAffitto;
    }

    private ContrattoVendita getContrattoVenditaFromRequest(ContrattoRequest request){

        DatiVenditaRequest datiVenditaRequest = request.getDatiVenditaRequest();

        ContrattoVendita contrattoVendita = ContrattoVendita.builder()
                .mutuoEstinto(datiVenditaRequest.isMutuoEstinto())
                .prezzo(request.getPrezzo())
                .build();

        return contrattoVendita;
    }

    // TODO da implementare
    private List<ImmaginiImmobile> getListaImmaginiFromRequest(List<String> immagini,Immobile immobile){

        List<ImmaginiImmobile> immaginiImmobili = new ArrayList<>();

        ImmaginiImmobile img1 = ImmaginiImmobile.builder()
                .url("ulr1.it")
                .descrizione("Prima immagine dell'annuncio")
                .immobile(immobile)
                .build();
        ImmaginiImmobile img2 = ImmaginiImmobile.builder()
                .url("ulr2.it")
                .descrizione("seconda immagine dell'annuncio")
                .immobile(immobile)
                .build();

        immaginiImmobili.add(img1);
        immaginiImmobili.add(img2);

        return immaginiImmobili;
    }
}
