package it.unina.dietiestates25.strategy;

import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoPropostaAccettata;
import it.unina.dietiestates25.entity.Contatto;

public class GeneratoreContenutoPropostaAccettata implements GeneratoreContenutoNotifica<DatiContenutoPropostaAccettata> {

    @Override
    public String generaContenuto(DatiContenutoPropostaAccettata dati) {
        StringBuilder contattiHtml = new StringBuilder();
        if(dati.getContattiAgente() != null && !dati.getContattiAgente().isEmpty()) {
            contattiHtml.append("<div class='lista-contatti'>");
            contattiHtml.append("<p><strong>Contatta l'agente:</strong></p>");
            contattiHtml.append("<ul style='list-style-type: none; padding: 0;'>");

            for(Contatto contatto : dati.getContattiAgente()) {
                contattiHtml.append("<li style='margin-bottom: 8px;'>");
                contattiHtml.append("<span style='background: #e0f7fa; padding: 5px 10px; border-radius: 15px; display: inline-block;'>");
                contattiHtml.append("<strong>").append(contatto.getTipo()).append(":</strong> ")
                        .append(contatto.getValore());
                contattiHtml.append("</span></li>");
            }

            contattiHtml.append("</ul></div>");
        }

        return "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9fbfd; padding: 20px; }"
                + ".contenitore { max-width: 600px; background: #ffffff; padding: 30px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.08); }"
                + "h1 { color: #2c3e50; margin-bottom: 25px; font-size: 26px; }"
                + "p { color: #34495e; font-size: 15px; line-height: 1.6; margin: 10px 0; }"
                + ".dettagli { background: #f8fafc; padding: 15px; border-left: 4px solid #3498db; margin: 15px 0; }"
                + ".card-immobile { border: 1px solid #e3e9f4; border-radius: 10px; overflow: hidden; margin: 20px 0; background: #fff; }"
                + ".card-immobile img { width: 100%; height: 220px; object-fit: cover; }"
                + ".card-immobile .info { padding: 20px; }"
                + ".descrizione { display: -webkit-box; -webkit-box-orient: vertical; overflow: hidden; -webkit-line-clamp: 3; }"
                + ".contatto { background: #e8f5e9; padding: 20px; border-radius: 10px; margin-top: 25px; }"
                + ".contatto strong { color: #1b5e20; font-size: 16px; }"
                + ".icona { margin-right: 8px; color: #3498db; }"
                + ".link-annuncio { display: inline-block; background: #3498db; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold; margin: 10px 0; }"
                + ".link-annuncio:hover { background: #2980b9; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='contenitore'>"
                + "<h1>🎉 Offerta Accettata!</h1>"
                + "<p>Ciao <strong>" + dati.getNomeDestinatario() + "</strong>,</p>"
                + "<p>La tua offerta per l'immobile <strong>" + dati.getTitoloAnnuncio() + "</strong> è stata accettata!</p>"


                // Sezione dettagli offerta
                + "<div class='dettagli'>"
                + "<p><span class='icona'>💶</span><strong>Prezzo proposto:</strong> €" + dati.getPrezzoProposto() + "</p>"
                + "<p><span class='icona'>✅</span><strong>Prezzo accettato:</strong> €" + dati.getPrezzo() + "</p>"
                + "</div>"

                // Card immobile
                + "<div class='card-immobile'>"
                + "<img src='" + dati.getUrlImmagineImmobile() + "' alt='Immobile'>"
                + "<div class='info'>"
                + "<p><span class='icona'>🏠</span><strong>Indirizzo:</strong> " + dati.getIndirizzoImmobile() + "</p>"
                + "<p><span class='icona'>📋</span><strong>Descrizione:</strong> <span class='descrizione'>"
                + dati.getDescrizione()
                + "</span> <a href='" + dati.getUrlAnnuncioImmobile() + "' target='_blank' style='color: #2ecc71; text-decoration: none; font-weight: bold;'>Vedi tutti i dettagli ›</a></p>"
                + "</div>"
                + "</div>"



                // Sezione contatti agente
                + "<div class='contatto'>"
                + "<p><strong>Congratulazioni " + dati.getNomeDestinatario() + "! 🎉</strong></p>"
                + "<p>Contatta ora l'agente <strong>" + dati.getNomeAgente() + "</strong> per finalizzare l'acquisto:</p>"
                + contattiHtml.toString()
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
