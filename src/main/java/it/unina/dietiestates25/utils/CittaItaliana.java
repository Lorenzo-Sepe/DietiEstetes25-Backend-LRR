package it.unina.dietiestates25.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CittaItaliana {
    private String denominazione_ita;
    private String cap;
    private String sigla_provincia;
    private String denominazione_provincia;
    private String latitudine;
    private String longitudine;
}
