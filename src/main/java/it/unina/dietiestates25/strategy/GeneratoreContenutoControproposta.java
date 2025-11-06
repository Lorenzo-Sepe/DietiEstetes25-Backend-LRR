package it.unina.dietiestates25.strategy;

import it.unina.dietiestates25.entity.Contatto;
import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoControproposta;

public class GeneratoreContenutoControproposta implements GeneratoreContenutoNotifica<DatiContenutoControproposta> {

    @Override
    public String generaContenuto(DatiContenutoControproposta dati) {
        StringBuilder contattiHtml = new StringBuilder();
        if (dati.getContattiAgente() != null && !dati.getContattiAgente().isEmpty()) {
            contattiHtml.append("<div class='lista-contatti'>");
            contattiHtml.append("<p><strong>Contatta l'agente:</strong></p>");
            contattiHtml.append("<ul style='list-style-type: none; padding: 0;'>");

            for (Contatto contatto : dati.getContattiAgente()) {
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
                + ".contatto { background: #fff3e0; padding: 20px; border-radius: 10px; margin-top: 25px; }"
                + ".contatto strong { color: #e65100; font-size: 16px; }"
                + ".icona { margin-right: 8px; color: #3498db; }"
                + ".pulsante { display: inline-block; background: #3498db; color: #fff; padding: 10px 18px; border-radius: 8px; text-decoration: none; margin-top: 15px; transition: background 0.3s; }"
                + ".pulsante:hover { background: #2c80c9; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='contenitore'>"
                + "<h1>💬 Controproposta Ricevuta</h1>"
                + "<p>Ciao <strong>" + dati.getNomeDestinatario() + "</strong>,</p>"
                + "<p>Hai ricevuto una <strong>controproposta</strong> per la tua offerta sull'immobile <strong>" + dati.getTitoloAnnuncio() + "</strong>.</p>"

                // Sezione dettagli controproposta
                + "<div class='dettagli'>"
                + "<p><span class='icona'>💶</span><strong>Prezzo proposto da te:</strong> €" + dati.getPrezzoProposto() + "</p>"
                + "<p><span class='icona'>📈</span><strong>Controproposta dell'agente:</strong> €" + dati.getPrezzoControproposta() + "</p>"
                + "</div>"

                // Card immobile
                + "<div class='card-immobile'>"
                + "<img src='" + dati.getUrlImmagineImmobile() + "' alt='Immobile'>"
                + "<div class='info'>"
                + "<p><span class='icona'>🏠</span><strong>Indirizzo:</strong> " + dati.getIndirizzoImmobile() + "</p>"
                + "<p><span class='icona'>📋</span><strong>Descrizione:</strong> <span class='descrizione'>" + dati.getDescrizione() + "</span></p>"
                + "</div>"
                + "</div>"

                // Sezione contatti agente
                + "<div class='contatto'>"
                + "<p><strong>" + dati.getNomeDestinatario() + ", l'agente <span style='color:#bf360c;'>" + dati.getNomeAgente() + "</span> ti ha inviato una controproposta!</strong></p>"
                + "<p>Contattalo per discutere i dettagli e valutare la migliore soluzione per te:</p>"
                + contattiHtml.toString()
                + "<a href='" + dati.getUrlProfiloAgente() + "' class='pulsante'>💼 Visita il Profilo dell'Agente</a>"
                + "</div>"

                + "</div>"
                + "</body>"
                + "</html>";
    }

    @Override
    public CategoriaNotificaName getTipoNotifica() {
        return CategoriaNotificaName.CONTROPROPOSTA;
    }

    @Override
    public String generaOggetto(DatiContenutoControproposta dati) {
        return "Hai ricevuto una controproposta per '" + dati.getTitoloAnnuncio() + "'!";
    }
}
