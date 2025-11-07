package it.unina.dietiestates25.strategy;

import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoImmobile;

public class GeneratoreContenutoImmobile implements GeneratoreContenutoNotifica<DatiContenutoImmobile> {

    @Override
    public String generaContenuto(DatiContenutoImmobile dati) {
        return "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9fbfd; padding: 20px; }"
                + ".contenitore { max-width: 600px; background: #ffffff; padding: 30px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.08); margin: 0 auto; }"
                + "h1 { color: #2ecc71; margin-bottom: 20px; font-size: 26px; border-bottom: 2px solid #f0f0f0; padding-bottom: 10px; }"
                + "p { color: #34495e; font-size: 15px; line-height: 1.6; margin: 12px 0; }"
                + ".dettagli { background: #f8fafc; padding: 18px; border-left: 4px solid #2ecc71; margin: 20px 0; border-radius: 0 8px 8px 0; }"
                + ".card-immobile { border: 1px solid #e3e9f4; border-radius: 10px; overflow: hidden; margin: 25px 0; background: #fff; transition: transform 0.2s ease; }"
                + ".card-immobile:hover { transform: translateY(-3px); }"
                + ".card-immobile img { width: 100%; height: 220px; object-fit: cover; border-bottom: 1px solid #eeeeee; }"
                + ".card-immobile .info { padding: 20px; }"
                + ".descrizione { display: -webkit-box; -webkit-box-orient: vertical; overflow: hidden; -webkit-line-clamp: 3; color: #555; font-size: 14px; margin-top: 8px; }"
                + ".contatto { background: #e8f5e9; padding: 25px; border-radius: 10px; margin-top: 25px; text-align: center; }"
                + ".contatto strong { color: #27ae60; font-size: 17px; }"
                + ".pulsante { display: inline-block; background: #2ecc71; color: white; padding: 12px 30px; text-decoration: none; border-radius: 25px; font-weight: bold; margin-top: 15px; transition: all 0.3s ease; box-shadow: 0 4px 6px rgba(46, 204, 113, 0.2); }"
                + ".pulsante:hover { background: #27ae60; transform: translateY(-2px); box-shadow: 0 6px 8px rgba(46, 204, 113, 0.3); }"
                + ".icona { margin-right: 10px; color: #2ecc71; font-size: 18px; vertical-align: middle; }"
                + ".attrazione { color: #e67e22; font-weight: bold; margin: 15px 0; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='contenitore'>"
                + "<h1>🏡 Opportunità Immobiliare Eccezionale!</h1>"
                + "<p>Ciao <strong>" + dati.getNomeDestinatario() + "</strong>,</p>"
                + "<p class='attrazione'>🌟 Abbiamo individuato un immobile perfettamente in linea con le tue ricerche!</p>"

                // Sezione dettagli immobile
                + "<div class='dettagli'>"
                + "<p><span class='icona'>📍</span><strong>Indirizzo:</strong> " + dati.getIndirizzoImmobile() + "</p>"
                + "<p><span class='icona'>💼</span><strong>Contratto:</strong> " + dati.getTipoContratto() + "</p>"
                + "<p><span class='icona'>🏢</span><strong>Tipologia:</strong> " + dati.getTipoImmobile() + "</p>"
                + "<p><span class='icona'>💶</span><strong>Prezzo:</strong> €" + String.format("%,d", dati.getPrezzo()) + "</p>"
                + "</div>"

                // Card dell'immobile
                + "<div class='card-immobile'>"
                + "<img src='" + dati.getUrlImmagineImmobile() + "' alt='Immobile selezionato'>"
                + "<div class='info'>"
                + "<p><span class='icona'>📝</span><strong>Descrizione:</strong> <span class='descrizione'>"
                + dati.getDescrizione() + "</span></p>"
                + "<p style='margin-top: 15px; text-align: center;'>"
                + "<a href='" + dati.getUrlAnnuncioImmobile() + "' style='color: #2ecc71; font-weight: bold; text-decoration: none;'>Vedi tutti i dettagli ›</a>"
                + "</p>"
                + "</div>"
                + "</div>"

                // Sezione call-to-action
                + "<div class='contatto'>"
                + "<p><span style='font-size: 20px;'>⏳</span><br><strong>Questa offerta potrebbe non durare a lungo!</strong></p>"
                + "<p>Non perdere l'occasione di acquisire questo immobile<br>"
                + "Clicca qui sotto per inviare immediatamente una proposta</p>"
                + "<a href='" + dati.getUrlAnnuncioImmobile() + "' target='_blank' class='pulsante'>"
                + "📩 Invia Proposta Ora"
                + "</a>"
                + "</div>"

                + "</div>"
                + "</body>"
                + "</html>";
    }

    @Override
    public CategoriaNotificaName getTipoNotifica() {
        return CategoriaNotificaName.OPPORTUNITA_IMMOBILE;
    }

    @Override
    public String generaOggetto(DatiContenutoImmobile dati) {
        return "🔥 " + dati.getTipoImmobile() + " da €" + String.format("%,d", dati.getPrezzo())
                + " a " + dati.getIndirizzoImmobile().split(",")[0];
    }
}
