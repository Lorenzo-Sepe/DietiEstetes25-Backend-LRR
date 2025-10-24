package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.DatiAffittoRequest;
import it.unina.dietiestates25.dto.request.DatiVenditaRequest;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.ContrattoRequest;
import it.unina.dietiestates25.entity.AnnuncioImmobiliare;
import it.unina.dietiestates25.entity.Contratto;
import it.unina.dietiestates25.entity.ContrattoAffitto;
import it.unina.dietiestates25.entity.ContrattoVendita;
import it.unina.dietiestates25.entity.enumeration.TipoContratto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContrattoService {

    public void updateContratto(ContrattoRequest request, AnnuncioImmobiliare annuncio){

        if(request.getTipoDiContratto().equals(TipoContratto.AFFITTO.toString())){
            annuncio.setContratto(new ContrattoAffitto());
            updateContrattoAffitto(request.getDatiAffittoRequest(),(ContrattoAffitto)annuncio.getContratto());
        }

        else{
            annuncio.setContratto(new ContrattoVendita());
            updateContrattoVendita(request.getDatiVenditaRequest(), (ContrattoVendita)annuncio.getContratto());
        }
    }

    private void updateContrattoAffitto(DatiAffittoRequest request, ContrattoAffitto contratto){
        contratto.setCaparra(request.getCaparra());
        contratto.setTempoMinimo(request.getTempoMinimo());
        contratto.setTempoMassimo(request.getTempoMassimo());
        contratto.setPrezzoAffitto(request.getPrezzo());
        contratto.setTipoContratto(TipoContratto.AFFITTO.toString());
    }

    private void updateContrattoVendita(DatiVenditaRequest request, ContrattoVendita contratto){
        contratto.setMutuoEstinto(request.isMutuoEstinto());
        contratto.setPrezzoVendita(request.getPrezzo());
        contratto.setTipoContratto(TipoContratto.VENDITA.toString());
    }

    public Contratto createContrattoFromRequest(ContrattoRequest request) {
        Contratto contratto;

        if (request.getTipoDiContratto().equals(TipoContratto.AFFITTO.toString())) {
            contratto = new ContrattoAffitto();
            ((ContrattoAffitto) contratto).setCaparra(request.getDatiAffittoRequest().getCaparra());
            ((ContrattoAffitto) contratto).setTempoMinimo(request.getDatiAffittoRequest().getTempoMinimo());
            ((ContrattoAffitto) contratto).setTempoMassimo(request.getDatiAffittoRequest().getTempoMassimo());
            ((ContrattoAffitto) contratto).setPrezzoAffitto(request.getDatiAffittoRequest().getPrezzo());
        } else {
            contratto = new ContrattoVendita();
            ((ContrattoVendita) contratto).setMutuoEstinto(request.getDatiVenditaRequest().isMutuoEstinto());
            ((ContrattoVendita) contratto).setPrezzoVendita(request.getDatiVenditaRequest().getPrezzo());
        }

        contratto.setTipoContratto(request.getTipoDiContratto());
        return contratto;
    }
}
