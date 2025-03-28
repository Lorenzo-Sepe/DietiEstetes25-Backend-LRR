package it.unina.dietiestates25.strategy;

import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoPropostaAccettata;

public class GeneratoreContenutoPropostaAccettata implements GeneratoreContenutoNotifica<DatiContenutoPropostaAccettata> {

    @Override
    public String generaContenuto(DatiContenutoPropostaAccettata dati) {
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
                + "<h1>Offerta Accettata!</h1>"
                + "<p>Ciao <strong>" + dati.getNomeDestinatario() + "</strong>,</p>"
                + "<p>La tua offerta per l'immobile <strong>#" + dati.getTitoloAnnuncio() + "</strong> è stata accettata!</p>"

                // Sezione dettagli offerta accettata
                + "<div class='dettagli'>"
                + "<p><strong>Prezzo che hai proposto:</strong> €" + dati.getPrezzoProposto() + "</p>"
                + "<p><strong>Prezzo accettato:</strong> €</p>"
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

                // Sezione per incentivare il contatto
                + "<div class='contatto'>"
                + "<p><strong>Congratulazioni!</strong></p>"
                + "<p>Se sei ancora interessato a questo immobile, ti consigliamo di contattare subito l'agente <strong>" + dati.getNomeAgente() + "</strong> per ulteriori dettagli.</p>"
                + "<a href='" + dati.getUrlProfiloAgente() + "' class='pulsante'>Visita il Profilo dell'Agente</a>"
                + "</div>"

                + "</div>"
                + "</body>"
                + "</html>";
    }

    @Override
    public CategoriaNotificaName getTipoNotifica() {
        return CategoriaNotificaName.PROPOSTA_ACCETTATA;
    }

    @Override
    public String generaOggetto(DatiContenutoPropostaAccettata dati) {
        return "La tua proposta per '" + dati.getTitoloAnnuncio() + "' è stata accettata!";
    }

}
