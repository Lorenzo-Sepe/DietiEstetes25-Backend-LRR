package it.unina.dietiestates25.utils;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class NearbyPlacesChecker {

    private final WebClient.Builder webClientBuilder;
    private final String chiaveApi="89dcc279975c4ca2bc1f39fb349bc4da";



    public List<String> getpuntiInteresseVicini(@PathVariable double latitudine, @PathVariable double longitudine) {
        List<String> indicatori = new ArrayList<>();

        // URL base per Geoapify
        String urlBase = "https://api.geoapify.com/v2/places?categories={categoria}&filter=circle:{longitudine},{latitudine},500&apiKey={apiKey}";

        // Categorie da verificare
        Map<String, String> categorie = Map.of(
                "Vicino a scuole", "education.school",
                "Vicino a parchi", "leisure.park",
                "Vicino a trasporto pubblico", "public_transport"
        );

        WebClient webClient = webClientBuilder.build();

        for (Map.Entry<String, String> voce : categorie.entrySet()) {
            String url = urlBase.replace("{categoria}", voce.getValue())
                    .replace("{latitudine}", String.valueOf(latitudine))
                    .replace("{longitudine}", String.valueOf(longitudine))
                    .replace("{apiKey}", chiaveApi);

            Map<String, Object> risposta = webClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            clientResponse.createException().flatMap(error -> {
                                throw new RuntimeException("Errore nella richiesta a Geoapify: " + error.getMessage());
                            })
                    )
                    .bodyToMono(Map.class)
                    .block(); // Chiamata sincrona

            if (risposta != null && risposta.containsKey("features") && ((List<?>) risposta.get("features")).size() > 0) {
                indicatori.add(voce.getKey());
            }
        }

        return indicatori;
    }
}