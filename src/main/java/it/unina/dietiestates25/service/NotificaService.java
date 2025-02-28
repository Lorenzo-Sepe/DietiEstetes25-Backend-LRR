package it.unina.dietiestates25.service;

import com.nimbusds.openid.connect.sdk.federation.trust.marks.TrustMarkClaimsSet;
import it.unina.dietiestates25.dto.request.NotificaPromozionaleRequest;
import it.unina.dietiestates25.dto.response.NotificaResponse;
import it.unina.dietiestates25.entity.Notifica;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.repository.NotificaRepository;
import it.unina.dietiestates25.utils.UserContex;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificaService {

    private final NotificaRepository notificaRepository;

    private final EntityManager entityManager;

    public ResponseEntity<String> inviaNotificaPromozionale(NotificaPromozionaleRequest request){

        String mittente = "Qui Raimondo scoprirà cme ottenere il mittente";

        int count = 0;

        Notifica notifica = Notifica.builder().contenuto(request.getContenuto())
                .dataCreazione(LocalDateTime.now())
                .mittente(getNomeAzenziaImmobiliare(mittente))
                .contenuto(request.getContenuto())
                .build();

        List<User> destinatari = getDestinatariNotifica(request.getAreaDiInteresse(), request.getTipoDiContrattoDiInteresse(), request.getTipologiaDiImmobileDiInteresse());

        for(User destinatario : destinatari){

            try{

                notifica.setDestinatario(destinatario);

                notificaRepository.save(notifica);

                count++;

            }catch(Exception e){

            }
        }

        return ResponseEntity.ok("numero di notifiche inviate: " + count);
    }

    //TODO Da implementare
    private String getNomeAzenziaImmobiliare(String mittente){return "mittente@agenzia.com";}

    private List<User> getDestinatariNotifica(@NotBlank String areaDiInteresse, String tipoDiContrattoDiInteresse, String tipologiaDiImmobileDiInteresse){

        User utente = entityManager.getReference(User.class, 2);
        User utente2 = entityManager.getReference(User.class, 3);

        List<User> destinatari = new ArrayList<>();

        destinatari.add(utente);
        destinatari.add(utente2);

        return destinatari;
    }

    public ResponseEntity<Integer> getNumeroAllNotifiche(){

        User userCurrent = UserContex.getUserCurrent();
        User raimondo = entityManager.find(User.class, 2);
        return ResponseEntity.ok(notificaRepository.countByDestinatario(raimondo));
    }

    public ResponseEntity<List<NotificaResponse>> getAllNotifiche(Pageable page){

        User userCurrent = UserContex.getUserCurrent();

        return null;
    }
}
