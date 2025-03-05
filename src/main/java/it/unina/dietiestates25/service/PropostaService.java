package it.unina.dietiestates25.service;

    import it.unina.dietiestates25.dto.request.ControPropostaRequest;
    import it.unina.dietiestates25.dto.request.PropostaRequest;
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
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class PropostaService {
        final private PropostaRepository propostaRepository;
        final private AnnuncioImmobiliareRepository annuncioImmobiliareRepository;

        public void inviaProposta(PropostaRequest request) {
            User utente = UserContex.getUserCurrent();
            AnnuncioImmobiliare annuncio = annuncioImmobiliareRepository.findById(request.getAnnuncioId())
                    .orElseThrow(() -> new BadRequestException("Non esiste un annuncio con id " + request.getAnnuncioId()));
            Contatto contatto = Contatto.builder()
                    .tipo(request.getTipoContatto().toString())
                    .valore(request.getInformazioniContatto())
                    .build();
            Proposta proposta = Proposta.builder()
                    .user(utente)
                    .annuncio(annuncio)
                    .cognome(request.getCognome())
                    .nome(request.getNome())
                    .contatto(contatto)
                    .stato(StatoProposta.IN_TRATTAZIONE)
                    .prezzoProposta(request.getPrezzo())
                    .build();
            try {
                propostaRepository.save(proposta);
            } catch (Exception e) {
                throw new InternalServerErrorException("Non è stato possibile inviare la proposta: ");
            }
        }

        public void inserisciPropostaManuale(PropostaRequest request) {
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
            } catch (Exception e) {
                throw new InternalServerErrorException("Non è stato possibile inviare la proposta: ");
            }
        }

        public void aggiungiUnaControProposta(ControPropostaRequest request) {
            Proposta proposta = propostaRepository.findById(request.getPropostaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Proposta non trovata", "id", request.getPropostaId()));
            if(proposta.getControproposta()!=null || proposta.getControproposta() != 0){
                throw new BadRequestException("La proposta ha già una controproposta");
            }
            checkPropostaStatus(proposta);
            proposta.setControproposta(request.getPrezzo());
            proposta.setStato(StatoProposta.IN_TRATTAZIONE);
            propostaRepository.save(proposta);
        }

        public void accettaProposta(int propostaId) {
            Proposta proposta = propostaRepository.findById(propostaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Proposta non trovata", "id", propostaId));
            checkPropostaStatus(proposta);
            proposta.setStato(StatoProposta.ACCETTATO);
            propostaRepository.save(proposta);
        }

        public void rifiutaProposta(int propostaId) {
            Proposta proposta = propostaRepository.findById(propostaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Proposta non trovata", "id", propostaId));
            checkPropostaStatus(proposta);
            proposta.setStato(StatoProposta.RIFIUTATO);
            propostaRepository.save(proposta);
        }

        private void checkPropostaStatus(Proposta proposta) {
            if (proposta.getStato() == StatoProposta.ACCETTATO || proposta.getStato() == StatoProposta.RIFIUTATO) {
                throw new BadRequestException("L'operazione non può essere eseguita perché la proposta è già stata accettata o rifiutata.");
            }
        }
    }