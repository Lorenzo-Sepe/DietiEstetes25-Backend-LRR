package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.PropostaRequest;
import it.unina.dietiestates25.dto.response.PropostaResponse;
import it.unina.dietiestates25.entity.AnnuncioImmobiliare;
import it.unina.dietiestates25.entity.Contatto;
import it.unina.dietiestates25.entity.Proposta;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.entity.enumeration.StatoProposta;
import it.unina.dietiestates25.exception.BadRequestException;
import it.unina.dietiestates25.exception.InternalServerErrorException;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.AnnuncioImmobiliareRepository;
import it.unina.dietiestates25.repository.PropostaRepository;
import it.unina.dietiestates25.utils.UserContex;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
    @RequiredArgsConstructor
    public class PropostaService {

    private static final String PROPOSTA_NON_TROVATA = "Proposta non trovata";

    final private PropostaRepository propostaRepository;
    final private AnnuncioImmobiliareRepository annuncioImmobiliareRepository;
    private final NotificaService notificaService;

    //----------------------------------GET-----------------------------------------------------------------------

        public List<PropostaResponse> getProposte(int idAnnuncio){
            List<Proposta> proposte = propostaRepository.findByAnnuncio_Id(idAnnuncio);

            return PropostaResponse.fromListEntityToDto(proposte);
        }


        //------------------------------------------------------------------------------------------------------------

        public void inviaProposta(PropostaRequest request) {
            User utente = UserContex.getUserCurrent();
            AnnuncioImmobiliare annuncio = annuncioImmobiliareRepository.findById(request.getAnnuncioId())
                    .orElseThrow(() -> new BadRequestException("Non esiste un annuncio con id " + request.getAnnuncioId()));
            Contatto contatto = Contatto.builder()
                    .tipo(request.getTipoContatto().toString())
                    .valore(request.getInformazioniContatto())
                    .build();
            Proposta proposta = Proposta.builder()
                    .annuncio(annuncio)
                    .cognome(request.getCognome())
                    .nome(request.getNome())
                    .contatto(contatto)
                    .stato(StatoProposta.IN_TRATTAZIONE)
                    .prezzoProposta(request.getPrezzo())
                    .build();
            try {
                propostaRepository.save(proposta);
                proposta.setUser(utente);
                propostaRepository.save(proposta);
            } catch (Exception e) {
                throw new InternalServerErrorException("Non è stato possibile inviare la proposta: ");
            }
        }

        public PropostaResponse inserisciPropostaManuale(PropostaRequest request) {
            AnnuncioImmobiliare annuncio = annuncioImmobiliareRepository.findById(request.getAnnuncioId())
                    .orElseThrow(() -> new BadRequestException("Non esiste un annuncio con id " + request.getAnnuncioId()));
            Contatto contatto = Contatto.builder()
                    .tipo(request.getTipoContatto().toString())
                    .valore(request.getInformazioniContatto())
                    .build();
            Proposta proposta = Proposta.builder()
                    .annuncio(annuncio)
                    .cognome(request.getCognome())
                    .nome(request.getNome())
                    .contatto(contatto)
                    .stato(StatoProposta.IN_TRATTAZIONE)
                    .prezzoProposta(request.getPrezzo())
                    .build();
            try {
                return PropostaResponse.fromEntityToDto(propostaRepository.save(proposta));

            } catch (Exception e) {
                throw new InternalServerErrorException("Non è stato possibile inviare la proposta: ");
            }
        }

        @Transactional
        public void aggiungiUnaControProposta(int propostaId, Double controproposta) {
            Proposta proposta = propostaRepository.findById(propostaId)
                    .orElseThrow(() -> new ResourceNotFoundException(PROPOSTA_NON_TROVATA, "id", propostaId));
            verificaProprietarioAnnuncio(proposta);

            if(proposta.getControproposta()!=null && proposta.getControproposta() != 0){
                throw new BadRequestException("La proposta ha già una controproposta");
            }

            if(controproposta < proposta.getPrezzoProposta()){
                throw new BadRequestException("Non puoi proporre una controproposta minore della proposta del proponente");
            }
            checkPropostaStatus(proposta);
            proposta.setControproposta(controproposta);
            proposta.setStato(StatoProposta.IN_TRATTAZIONE);
            propostaRepository.save(proposta);


            notificaService.inviaNotificaControproposta(proposta);
        }


        private static void verificaProprietarioAnnuncio(Proposta proposta) {
            int agenteProprietarioAnnuncioId= proposta.getAnnuncio().getAgente().getId();
            if(agenteProprietarioAnnuncioId != UserContex.getUserCurrent().getId()){
                throw new BadRequestException("Non sei autorizzato a fare una controproposta su questa proposta\n non sei il proprietario dell'annuncio");
            }
        }

        @Transactional
        public void accettaProposta(int propostaId) {
            Proposta proposta = propostaRepository.findById(propostaId)
                    .orElseThrow(() -> new ResourceNotFoundException(PROPOSTA_NON_TROVATA, "id", propostaId));
            verificaProprietarioAnnuncio(proposta);
            checkPropostaStatus(proposta);
            proposta.setStato(StatoProposta.ACCETTATO);
            propostaRepository.save(proposta);
            notificaService.inviaNotificaAccettazione(proposta);
        }

        @Transactional
        public void rifiutaProposta(int propostaId) {
            Proposta proposta = propostaRepository.findById(propostaId)
                    .orElseThrow(() -> new ResourceNotFoundException(PROPOSTA_NON_TROVATA, "id", propostaId));
            verificaProprietarioAnnuncio(proposta);
            checkPropostaStatus(proposta);
            proposta.setStato(StatoProposta.RIFIUTATO);
            propostaRepository.save(proposta);
            notificaService.inviaNotificaRifiuto(proposta);
        }

        private void checkPropostaStatus(Proposta proposta) {
            if (proposta.getStato() == StatoProposta.ACCETTATO || proposta.getStato() == StatoProposta.RIFIUTATO) {
                throw new BadRequestException("L'operazione non può essere eseguita perché la proposta è già stata accettata o rifiutata.");
            }
        }

    }