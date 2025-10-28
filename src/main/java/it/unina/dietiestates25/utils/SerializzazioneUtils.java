package it.unina.dietiestates25.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unina.dietiestates25.dto.request.FiltroAnnuncioDTO;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @NoArgsConstructor
public class SerializzazioneUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String serializzaFiltroAnnuncio(FiltroAnnuncioDTO filtro) {
        try {
            return mapper.writeValueAsString(filtro);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static FiltroAnnuncioDTO deserializzaFiltroAnnuncio(String json) {
        try {
            return mapper.readValue(json, FiltroAnnuncioDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
