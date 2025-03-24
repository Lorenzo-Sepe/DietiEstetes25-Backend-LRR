package it.unina.dietiestates25.service;

import it.unina.dietiestates25.entity.enumeration.ClasseEnergetica;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClasseEnergeticaService {
    public ClasseEnergetica checkEnumClasseEnergeticaFromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Valore nullo o vuoto");
        }
        try {
            return ClasseEnergetica.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nessun valore dell'enumerazione ClasseEnergetica corrispondente a: " + value);
        }
    }
}
