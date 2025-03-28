package it.unina.dietiestates25.strategy;

import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoPropostaRifiutata;

public class GeneratoreContenutoPropostaRifiutata implements GeneratoreContenutoNotifica<DatiContenutoPropostaRifiutata> {

    @Override
    public String generaContenuto(DatiContenutoPropostaRifiutata dati) {
        return "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }"
                + ".contenitore { max-width: 600px; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0px 0px 10px rgba(0,0,0,0.1); }"
                + "h1 { color: #cc0000; }"
                + "p { color: #555555; font-size: 16px; line-height: 1.5; }"
                + ".dettagli { background: #f9f9f9; padding: 10px; border-left: 4px solid #cc0000; margin-top: 10px; }"
                + ".pulsante { display: inline-block; background: #007BFF; color: #ffffff; padding: 10px 15px; text-decoration: none; border-radius: 5px; margin-top: 10px; }"
                + ".pulsante:hover { background: #0056b3; }"
                + ".card-immobile { border: 1px solid #ddd; border-radius: 8px; overflow: hidden; margin-top: 20px; }"
                + ".card-immobile img { width: 100%; height: auto; }"
                + ".card-immobile .info { padding: 15px; }"
                + " .descrizione { display: -webkit-box; -webkit-box-orient: vertical; overflow: hidden; -webkit-line-clamp: 3"
                + ".contatto { background: #ffcccc; padding: 15px; border-radius: 8px; text-align: center; margin-top: 20px; }"
                + ".contatto strong { color: #cc0000; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='contenitore'>"
                + "<h1>Proposta Rifiutata</h1>"
                + "<p>Ciao <strong>" + dati.getNomeDestinatario() + "</strong>,</p>"
                + "<p>Ci dispiace informarti che la tua proposta per l'immobile <strong>#" + dati.getTitoloAnnuncio() + "</strong> è stata rifiutata.</p>"

                // Sezione dettagli proposta rifiutata
                + "<div class='dettagli'>"
                + "<p><strong>Prezzo che hai proposto:</strong> €" + dati.getPrezzoProposto() + "</p>"
                + "<p><strong>Prezzo Rifiutato:</strong> €</p>"
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

                // Sezione per incentivare una nuova proposta
                + "<div class='contatto'>"
                + "<p><strong>Non arrenderti!</strong></p>"
                + "<p>Puoi sempre fare una nuova proposta per questo immobile. Visita la pagina dell'annuncio per ulteriori dettagli e per inviare una nuova offerta.</p>"
                + "<a href='" + dati.getUrlAnnuncioImmobile() + "' class='pulsante'>Visita l'Annuncio Immobiliare</a>"
                + "</div>"

                + "</div>"
                + "</body>"
                + "</html>";
    }



    @Override
    public CategoriaNotificaName getTipoNotifica() {
        return CategoriaNotificaName.PROPOSTA_RIFIUTATA;
    }

    @Override
    public String generaOggetto(DatiContenutoPropostaRifiutata dati) {
        return "La tua proposta per '" + dati.getTitoloAnnuncio() + "' è stata Rifiutata!";
    }
}





