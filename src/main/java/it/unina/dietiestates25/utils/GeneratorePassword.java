package it.unina.dietiestates25.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class GeneratorePassword {

    private static final int LUNGHEZZA_PASSWORD = 12;
    private static final String LETTERE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMERI = "0123456789";
    private static final String CARATTERI_SPECIALI = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final String TUTTI_I_CARATTERI = LETTERE + NUMERI + CARATTERI_SPECIALI;

    private final SecureRandom casuale = new SecureRandom();

    public String generaPasswordDipendente() {
        StringBuilder password = new StringBuilder(LUNGHEZZA_PASSWORD);

        password.append(LETTERE.charAt(casuale.nextInt(LETTERE.length())));
        password.append(NUMERI.charAt(casuale.nextInt(NUMERI.length())));
        password.append(CARATTERI_SPECIALI.charAt(casuale.nextInt(CARATTERI_SPECIALI.length())));

        // Riempi il resto della password con caratteri casuali
        for (int i = 3; i < LUNGHEZZA_PASSWORD; i++) {
            password.append(TUTTI_I_CARATTERI.charAt(casuale.nextInt(TUTTI_I_CARATTERI.length())));
        }

        // Mescola i caratteri per rendere la password più casuale
        return mescolaStringa(password.toString());
    }

    private String mescolaStringa(String input) {
        char[] caratteri = input.toCharArray();
        for (int i = caratteri.length - 1; i > 0; i--) {
            int j = casuale.nextInt(i + 1);
            // Scambia caratteri[i] con caratteri[j]
            char temp = caratteri[i];
            caratteri[i] = caratteri[j];
            caratteri[j] = temp;
        }
        return new String(caratteri);
    }

    public static void main(String[] args) {
        GeneratorePassword generatore = new GeneratorePassword();
        String password = generatore.generaPasswordDipendente();
        System.out.println("Password generata: " + password);
    }
}