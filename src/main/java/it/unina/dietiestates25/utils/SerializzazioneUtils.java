package it.unina.dietiestates25.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unina.dietiestates25.dto.request.FiltroAnnuncioDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SerializzazioneUtils {
    private SerializzazioneUtils(){
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String serializzaFiltroAnnuncio(FiltroAnnuncioDTO filtro) {
        try {
            return mapper.writeValueAsString(filtro);
        } catch (Exception e) {
            log.error("Errore durante la serializzazione del filtro annuncio: {}", e.getMessage());
            return null;
        }
    }

    public static FiltroAnnuncioDTO deserializzaFiltroAnnuncio(String json) {
        try {
            return mapper.readValue(json, FiltroAnnuncioDTO.class);
        } catch (Exception e) {
            log.error("Errore durante la deserializzazione del filtro annuncio: {}", e.getMessage());
            return null;
        }
    }
}
