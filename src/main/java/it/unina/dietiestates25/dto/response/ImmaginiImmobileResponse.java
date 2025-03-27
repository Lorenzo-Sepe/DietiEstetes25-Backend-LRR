package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.ImmaginiImmobile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class ImmaginiImmobileResponse {
    private String url;

    private String descrizione;

    public static List<ImmaginiImmobileResponse> fromEntityToDto(List<ImmaginiImmobile> immaginiImmobile) {
        List<ImmaginiImmobileResponse> immaginiImmobileResponse = new ArrayList<>();

        for(ImmaginiImmobile immagine : immaginiImmobile){

            ImmaginiImmobileResponse immagineResponse = ImmaginiImmobileResponse.builder()
                    .url(immagine.getUrl())
                    .descrizione(immagine.getDescrizione())
                    .build();

            immaginiImmobileResponse.add(immagineResponse);
        }

        return immaginiImmobileResponse;
    }
}
