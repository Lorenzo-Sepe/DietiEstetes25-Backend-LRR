package it.unina.dietiestates25.entity.enumeration;

import lombok.Getter;

@Getter
public enum ClasseEnergetica {
    A_PLUS_PLUS("A++"),
    A_PLUS("A+"),
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    E("E"),
    F("F"),
    G("G"),
    NON_SPECIFICATA("Non specificata");

    private final String label;

    ClasseEnergetica(String label) {
        this.label = label;
    }


}