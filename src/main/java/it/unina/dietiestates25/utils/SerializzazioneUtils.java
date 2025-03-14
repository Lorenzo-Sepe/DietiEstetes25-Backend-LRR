package it.unina.dietiestates25.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unina.dietiestates25.dto.request.FiltroAnnuncio;

public class SerializzazioneUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String serializzaFiltroAnnuncio(FiltroAnnuncio filtro) {
        try {
            return mapper.writeValueAsString(filtro);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static FiltroAnnuncio deserializzaFiltroAnnuncio(String json) {
        try {
            return mapper.readValue(json, FiltroAnnuncio.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
