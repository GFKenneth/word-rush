package palabrascontrarreloj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class GeneradorSecuencia {

    private static final char[] LETRAS = {
        'a','b','c','d','e','f','g','h','i','j','k','l','m',
        'n','o','p','q','r','s','t','u','v','w','x','y','z'
    };

    // Pesos por frecuencia; letras raras (w, x, q, k) tienen menor probabilidad
    private static final int[] PESOS = {
        14, 2,  5,  6, 14, 2,  2,  1, 10, 1,  1,  5,  3,
         8, 9,  3,  1,  8,  8,  5,  5,  1,  1,  1,  2,  1
    };

    private static final int[] ACUMULADO;
    private static final int TOTAL;

    static {
        ACUMULADO = new int[PESOS.length];
        int suma = 0;
        for (int i = 0; i < PESOS.length; i++) {
            suma += PESOS[i];
            ACUMULADO[i] = suma;
        }
        TOTAL = suma;
    }

    public static String generarAleatoria() {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(9);
        for (int i = 0; i < 9; i++) sb.append(elegirLetra(rnd));
        return sb.toString().toUpperCase();
    }

    private static char elegirLetra(Random rnd) {
        int val = rnd.nextInt(TOTAL) + 1;
        for (int i = 0; i < ACUMULADO.length; i++) {
            if (val <= ACUMULADO[i]) return LETRAS[i];
        }
        return 'a';
    }

    public static String solicitarAlUsuario() throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        while (true) {
            System.out.print("Ingresa 9 letras: ");
            String entrada = br.readLine();
            if (entrada == null) continue;

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < entrada.length(); i++) {
                char c = Trie.normalizarMayusculas(String.valueOf(entrada.charAt(i))).charAt(0);
                if (c >= 'a' && c <= 'z') sb.append(c);
            }

            String limpia = sb.toString().toUpperCase();
            if (limpia.length() != 9) {
                System.out.println("  Se encontraron " + limpia.length()
                        + " letras validas. Se necesitan exactamente 9. Intenta de nuevo.");
                continue;
            }
            return limpia;
        }
    }
}
