package it.unina.dietiestates25.entity;

public enum ClasseEnergetica {
    A_PLUS_PLUS("A++"),
    A_PLUS("A+"),
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    E("E"),
    F("F"),
    G("G");

    private final String label;

    ClasseEnergetica(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}