package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.ContattoRequest;
import it.unina.dietiestates25.dto.response.ContattoResponse;
import it.unina.dietiestates25.entity.Contatto;
import it.unina.dietiestates25.entity.DatiImpiegato;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.exception.UnauthorizedException;
import it.unina.dietiestates25.repository.DatiImpiegatoRepository;
import it.unina.dietiestates25.utils.UserContex;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DatiImpiegatoService {

    private final DatiImpiegatoRepository datiImpiegatoRepository;

    public List<ContattoResponse> aggiungiContatto(ContattoRequest request){

        User userCurrent = UserContex.getUserCurrent();

        if(userCurrent != null){

            DatiImpiegato datiImpiegato = datiImpiegatoRepository.findDatiImpiegatoByUser(userCurrent).orElseThrow(
                    () -> new ResourceNotFoundException(UserContex.getUserCurrent().getEmail(),"Utente non trovato. Assicurati di essere autenticato correttamente."));

            List<Contatto> contattiEsistenti = datiImpiegato.getContatti();

            int indexContattoEsistente = getIndexContattiFromTipo(contattiEsistenti,request.getTipo());

            //Aggiungi nuovo contatto
            if(indexContattoEsistente == -1){

                Contatto nuovoContatto = Contatto.builder()
                        .tipo(request.getTipo())
                        .valore(request.getValore())
                        .build();

                contattiEsistenti.add(nuovoContatto);

            }else{ //Tipo di contatto già esistente, modifica solo il valore

                contattiEsistenti.get(indexContattoEsistente).setValore(request.getValore());
            }

            datiImpiegatoRepository.save(datiImpiegato);

            List<ContattoResponse> responses = new ArrayList<>();

            for(Contatto contatto : contattiEsistenti){

                responses.add(ContattoResponse.fromEntityToDto(contatto));
            }

            return responses;
        }

        throw new UnauthorizedException("Utente non autenticato.");
    }

    private int getIndexContattiFromTipo(List<Contatto> contatti, String tipo){

        int indexContattoEsistente = -1;

        for(int i=0; i<contatti.size(); i++){

            if(contatti.get(i).getTipo().equals(tipo)){

                indexContattoEsistente = i;
                break;
            }
        }

        return indexContattoEsistente;
    }
}
