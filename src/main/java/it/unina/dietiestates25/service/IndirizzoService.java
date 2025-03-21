package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.ProvinciaRequest;
import it.unina.dietiestates25.entity.utilities.Point;
import it.unina.dietiestates25.utils.NearbyPlacesChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndirizzoService {
    private final NearbyPlacesChecker nearbyPlacesChecker;

    public List<String> getProvinceVicine(double lat, double lon, int raggio) {
        Point centro = new Point(lat, lon);
        return nearbyPlacesChecker.getProvinceVicine(centro, raggio);
    }
}
