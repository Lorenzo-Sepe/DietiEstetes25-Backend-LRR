package it.unina.dietiestates25.strategy;

import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoPropostaRifiutata;

public class GeneratoreContenutoPropostaRifiutata implements GeneratoreContenutoNotifica<DatiContenutoPropostaRifiutata> {

    @Override
    public String generaContenuto(DatiContenutoPropostaRifiutata dati) {
        return "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9fbfd; padding: 20px; }"
                + ".contenitore { max-width: 600px; background: #ffffff; padding: 30px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.08); }"
                + "h1 { color: #c0392b; margin-bottom: 18px; font-size: 24px; }"
                + "p { color: #34495e; font-size: 15px; line-height: 1.6; margin: 8px 0; }"
                + ".dettagli { background: #fcf6f6; padding: 15px; border-left: 4px solid #e74c3c; margin: 15px 0; }"
                + ".card-immobile { border: 1px solid #e9eef6; border-radius: 10px; overflow: hidden; margin: 20px 0; background: #fff; }"
                + ".card-immobile img { width: 100%; height: 220px; object-fit: cover; }"
                + ".card-immobile .info { padding: 20px; }"
                + ".descrizione { display: -webkit-box; -webkit-box-orient: vertical; overflow: hidden; -webkit-line-clamp: 3; }"
                + ".contatto { background: #fff3f3; padding: 20px; border-radius: 10px; margin-top: 20px; }"
                + ".contatto strong { color: #c0392b; font-size: 15px; }"
                + ".icona { margin-right: 8px; color: #e74c3c; }"
                + ".pulsante { display: inline-block; background: #3498db; color: #ffffff; padding: 10px 15px; text-decoration: none; border-radius: 5px; margin-top: 10px; font-weight: bold; }"
                + ".pulsante:hover { background: #2980b9; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='contenitore'>"
                + "<h1>😞 Proposta Rifiutata</h1>"
                + "<p>Ciao <strong>" + dati.getNomeDestinatario() + "</strong>,</p>"
                + "<p>Siamo spiacenti, la tua proposta per l'immobile <strong>" + dati.getTitoloAnnuncio() + "</strong> non è stata accolta.</p>"

                // Sezione dettagli proposta rifiutata
                + "<div class='dettagli'>"
                + "<p><span class='icona'>💶</span><strong>Prezzo che hai proposto:</strong> €" + dati.getPrezzoProposto() + "</p>"
                + "<p><span class='icona'>❌</span><strong>Prezzo Rifiutato:</strong> €" + dati.getPrezzo() + "</p>"
                + "</div>"

                // Card dell'immobile
                + "<div class='card-immobile'>"
                + "<img src='" + dati.getUrlImmagineImmobile() + "' alt='Immagine Immobile' />"
                + "<div class='info'>"
                + "<p><span class='icona'>🏠</span><strong>Indirizzo:</strong> " + dati.getIndirizzoImmobile() + "</p>"
                + "<p><span class='icona'>📋</span><strong>Prezzo:</strong> €" + dati.getPrezzo() + "</p>"
                + "<p><span class='icona'>📋</span><strong>Descrizione:</strong> <span class='descrizione'>"
                + dati.getDescrizione()
                + "</span> <a href='" + dati.getUrlAnnuncioImmobile() + "' target='_blank' style='color: #3498db; text-decoration: none; font-weight: bold;'>Leggi tutto</a></p>"
                + "</div>"
                + "</div>"

                // Sezione per incentivare una nuova proposta
                + "<div class='contatto'>"
                + "<p><strong>Non demordere! 🔁</strong></p>"
                + "<p>Puoi sempre inviare una nuova proposta per questo immobile. Consulta nuovamente l'annuncio per modificare la tua offerta.</p>"
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
