package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.CategoriaNotifica;
import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class SottoscrizioneNotificaResponse {

    String nomeCategoria;

    String descrizione;

    int idCategoria;

    boolean attivo;

    public static List<SottoscrizioneNotificaResponse> fromEntityToDto(List<CategoriaNotifica> categorieDisattivate){

        List<SottoscrizioneNotificaResponse> sottoscrizioni = new ArrayList<>();

        for (CategoriaNotifica categoria : categorieDisattivate) {
            SottoscrizioneNotificaResponse sottoscrizione = SottoscrizioneNotificaResponse.builder()
                    .nomeCategoria(categoria.getCategoriaName().name())
                    .descrizione(categoria.getDescrizione())
                    .idCategoria(categoria.getId())
                    .attivo(false)
                    .build();
            sottoscrizioni.add(sottoscrizione);
        }

        return sottoscrizioni;
    }
}