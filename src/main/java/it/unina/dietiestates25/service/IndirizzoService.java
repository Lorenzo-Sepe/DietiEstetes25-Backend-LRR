package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.IndirizzoRequest;
import it.unina.dietiestates25.entity.Indirizzo;
import it.unina.dietiestates25.entity.enumeration.VicinoA;
import it.unina.dietiestates25.entity.utilities.Point;
import it.unina.dietiestates25.utils.NearbyPlacesChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class IndirizzoService {
    private final NearbyPlacesChecker nearbyPlacesChecker;

    public List<String> getProvinceVicine(double lat, double lon, double raggio) {
        Point centro = new Point(lat, lon);
        return nearbyPlacesChecker.getProvinceVicine(centro, raggio);
    }

    public Indirizzo createIndirizzoFromRequest(IndirizzoRequest request) {

        return Indirizzo.builder()
                .nazione(request.getNazione())
                .cap(request.getCap())
                .citta(request.getCitta())
                .provincia(request.getProvincia())
                .via(request.getVia())
                .numeroCivico(request.getNumeroCivico())
                .longitudine(request.getLongitudine())
                .latitudine(request.getLatitudine())
                .luoghiVicini(getPostiVicino(request.getLatitudine(), request.getLongitudine()))
                .build();
    }

    private Set<VicinoA> getPostiVicino(double latitudine, double longitudine) {
        return nearbyPlacesChecker.getPuntiInteresseVicini(latitudine, longitudine);
    }

    public void updateIndirizzo(IndirizzoRequest request, Indirizzo indirizzo) {
        indirizzo.setNazione(request.getNazione());
        indirizzo.setCap(request.getCap());
        indirizzo.setCitta(request.getCitta());
        indirizzo.setProvincia(request.getProvincia());
        indirizzo.setVia(request.getVia());
        indirizzo.setNumeroCivico(request.getNumeroCivico());
        indirizzo.setLongitudine(request.getLongitudine());
        indirizzo.setLatitudine(request.getLatitudine());
    }
}
