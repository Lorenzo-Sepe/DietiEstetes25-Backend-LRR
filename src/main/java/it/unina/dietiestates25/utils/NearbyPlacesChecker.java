package it.unina.dietiestates25.utils;

import it.unina.dietiestates25.entity.enumeration.VicinoA;
import it.unina.dietiestates25.entity.utilities.Point;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Component
@RequiredArgsConstructor
public class NearbyPlacesChecker {

    private static final String FEATURES_KEY = "features";

    private final WebClient.Builder webClientBuilder;
    @Value("${application.security.geo-api.secret-key}")
    private String chiaveApi;

    // URL base per Geoapify
    @Value("${application.security.geo-api.base-url}")
    private String urlBase;

    public List<String> getProvinceVicine(Point centro, double raggio) {
        List<String> provinceVicine = new ArrayList<>();

        try {
            // Leggi il file GeoJSON
            String geojsonContent = Files.readString(Paths.get("src/main/resources/limits_IT_provinces.geojson"));
            JSONObject geojson = new JSONObject(geojsonContent);
            JSONArray features = geojson.getJSONArray(FEATURES_KEY);

            // Itera su tutte le province
            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject geometry = feature.getJSONObject("geometry");
                String type = geometry.getString("type");

                // Ottieni le coordinate della provincia
                JSONArray coordinates;
                if (type.equals("Polygon")) {
                    coordinates = geometry.getJSONArray("coordinates").getJSONArray(0);
                } else { // MultiPolygon
                    coordinates = geometry.getJSONArray("coordinates").getJSONArray(0).getJSONArray(0);
                }

                // Controlla se almeno un punto della provincia è nel cerchio
                boolean isInside = false;
                for (int j = 0; j < coordinates.length() && !isInside; j++) {
                    JSONArray point = coordinates.getJSONArray(j);
                    double provinceLon = point.getDouble(0);
                    double provinceLat = point.getDouble(1);

                    // Calcola la distanza usando la formula di Haversine
                    double distance = haversineDistance(centro.getLatitudine(), centro.getLongitudine(), provinceLat, provinceLon);
                    if (distance <= raggio) {
                        isInside = true;
                    }
                }

                // Se la provincia è nel cerchio, aggiungi il suo nome
                if (isInside) {
                    String nomeProvincia = feature.getJSONObject("properties").getString("name");
                    provinceVicine.add(nomeProvincia);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return provinceVicine;
    }

    // Metodo per calcolare la distanza tra due punti usando la formula di Haversine
    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // Raggio della Terra in km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }

    public Set<VicinoA> getPuntiInteresseVicini( double latitudine,  double longitudine) {
        Set<VicinoA> indicatori = new HashSet<>();

        // Mappa delle categorie con corrispondente valore enum
        Map<VicinoA, String> categorie = Map.of(
                VicinoA.SCUOLE, "education.school",
                VicinoA.PARCHEGGI, "leisure.park",
                VicinoA.TRASPORTO_PUBBLICO, "public_transport"
        );

        WebClient webClient = webClientBuilder.build();

        for (Map.Entry<VicinoA, String> voce : categorie.entrySet()) {
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
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block(); // Chiamata sincrona


            if (risposta != null && risposta.containsKey(FEATURES_KEY) &&
                    !((List<?>) risposta.get(FEATURES_KEY)).isEmpty()) {
                indicatori.add(voce.getKey());
            }
        }

        return indicatori;
    }
}
