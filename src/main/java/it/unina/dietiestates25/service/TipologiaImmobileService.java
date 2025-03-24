package it.unina.dietiestates25.service;

import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TipologiaImmobileService {

    public TipologiaImmobile checkEnumTipologiaImmobileFromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Valore nullo o vuoto");
        }
        try {
            return TipologiaImmobile.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nessun valore dell'enumerazione TipologiaImmobile corrispondente a: " + value);
        }
    }
}
