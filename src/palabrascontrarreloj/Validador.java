package palabrascontrarreloj;

public class Validador {

    /*
     * Verifica si una palabra puede formarse con las letras de la secuencia.
     * Reglas:
     *   - No distingue mayusculas de minusculas.
     *   - Las tildes son "libres": 'á' consume la 'a' de la secuencia.
     *   - Respeta la multiplicidad de letras en la secuencia.
     */
    public static boolean puedeFormarse(String secuencia, String palabra) {
        String secNorm = quitarTildes(Trie.normalizarMayusculas(secuencia));
        String palNorm = Trie.normalizarMayusculas(palabra);

        int[] disponibles = new int[26];
        for (int i = 0; i < secNorm.length(); i++) {
            char c = secNorm.charAt(i);
            if (c >= 'a' && c <= 'z') disponibles[c - 'a']++;
        }

        for (int i = 0; i < palNorm.length(); i++) {
            char base = quitarTilde(palNorm.charAt(i));
            if (base < 'a' || base > 'z') return false;
            int idx = base - 'a';
            if (disponibles[idx] <= 0) return false;
            disponibles[idx]--;
        }
        return true;
    }

    public static String quitarTildes(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) sb.append(quitarTilde(s.charAt(i)));
        return sb.toString();
    }

    public static char quitarTilde(char c) {
        switch (c) {
            case 'á': return 'a';
            case 'é': return 'e';
            case 'í': return 'i';
            case 'ó': return 'o';
            case 'ú': case 'ü': return 'u';
            case 'ñ': return 'n';
            default: return c;
        }
    }

    public static int calcularPuntuacion(String palabra) {
        int len = palabra.length();
        return len * len;
    }
}
