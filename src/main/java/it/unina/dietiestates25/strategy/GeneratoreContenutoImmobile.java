package it.unina.dietiestates25.strategy;

import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoImmobile;

public class GeneratoreContenutoImmobile implements GeneratoreContenutoNotifica<DatiContenutoImmobile> {

    @Override
    public String generaContenuto(DatiContenutoImmobile dati) {
        return "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }"
                + ".contenitore { max-width: 600px; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0px 0px 10px rgba(0,0,0,0.1); }"
                + "h1 { color: #333366; }"
                + "p { color: #555555; font-size: 16px; line-height: 1.5; }"
                + ".dettagli { background: #f9f9f9; padding: 10px; border-left: 4px solid #333366; margin-top: 10px; }"
                + ".pulsante { display: inline-block; background: #007BFF; color: #ffffff; padding: 10px 15px; text-decoration: none; border-radius: 5px; margin-top: 10px; }"
                + ".pulsante:hover { background: #0056b3; }"
                + ".card-immobile { border: 1px solid #ddd; border-radius: 8px; overflow: hidden; margin-top: 20px; }"
                + ".card-immobile img { width: 100%; height: auto; }"
                + ".card-immobile .info { padding: 15px; }"
                + " .descrizione { display: -webkit-box; -webkit-box-orient: vertical; overflow: hidden; -webkit-line-clamp: 3"
                + ".contatto { background: #ccffcc; padding: 15px; border-radius: 8px; text-align: center; margin-top: 20px; }"
                + ".contatto strong { color: #005500; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='contenitore'>"
                + "<h1>Immobile Suggerito per Te!</h1>"
                + "<p>Ciao <strong>" + dati.getNomeDestinatario() + "</strong>,</p>"
                + "<p>Abbiamo trovato un immobile che potrebbe interessarti in base alle tue ultime ricerche.</p>"

                // Sezione dettagli immobile
                + "<div class='dettagli'>"
                + "<p><strong>Luogo:</strong> " + dati.getIndirizzoImmobile() + "</p>"
                + "<p><strong>Tipo di contratto:</strong> " + dati.getTipoContratto() + "</p>"
                + "<p><strong>Tipo di immobile:</strong> " + dati.getTipoImmobile() + "</p>"
                + "<p><strong>Prezzo:</strong> €" + dati.getPrezzo() + "</p>"
                + "</div>"

                // Card dell'immobile
                + "<div class='card-immobile'>"
                + "<img src='" + dati.getUrlImmagineImmobile() + "' alt='Immagine Immobile' />"
                + "<div class='info'>"
                + "<p><strong>Indirizzo:</strong> " + dati.getIndirizzoImmobile() + "</p>"
                + "<p><strong>Prezzo:</strong> €" + dati.getPrezzo() + "</p>"
                + "<p><strong>Descrizione:</strong> " + dati.getDescrizione() + "</p>"
                + "</div>"
                + "</div>"

                // Sezione per incentivare a fare una proposta
                + "<div class='contatto'>"
                + "<p><strong>Non perdere questa opportunità!</strong></p>"
                + "<p>Se sei interessato a questo immobile, ti incoraggiamo a fare una proposta.</p>"
                + "<a href='" + dati.getUrlAnnuncioImmobile() + "' class='pulsante'>Fai una Proposta</a>"
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
        return "Nuova Opportunità: a " + dati.getIndirizzoImmobile() + " per €" + dati.getPrezzo();
    }
}
