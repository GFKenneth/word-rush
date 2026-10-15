package palabrascontrarreloj;

public class Trie {

    private final NodoTrie raiz;

    public Trie() {
        raiz = new NodoTrie();
    }

    public void insertar(String palabra) {
        NodoTrie actual = raiz;
        for (int i = 0; i < palabra.length(); i++) {
            char c = palabra.charAt(i);
            int idx = NodoTrie.indice(c);
            if (idx == -1) return;
            if (actual.hijos[idx] == null) {
                actual.hijos[idx] = new NodoTrie();
            }
            actual = actual.hijos[idx];
        }
        actual.esPalabra = true;
    }

    public boolean contiene(String palabra) {
        String norm = normalizarMayusculas(palabra);
        NodoTrie actual = raiz;
        for (int i = 0; i < norm.length(); i++) {
            char c = norm.charAt(i);
            int idx = NodoTrie.indice(c);
            if (idx == -1) return false;
            if (actual.hijos[idx] == null) return false;
            actual = actual.hijos[idx];
        }
        return actual.esPalabra;
    }

    public static String normalizarMayusculas(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case 'A': sb.append('a'); break;
                case 'B': sb.append('b'); break;
                case 'C': sb.append('c'); break;
                case 'D': sb.append('d'); break;
                case 'E': sb.append('e'); break;
                case 'F': sb.append('f'); break;
                case 'G': sb.append('g'); break;
                case 'H': sb.append('h'); break;
                case 'I': sb.append('i'); break;
                case 'J': sb.append('j'); break;
                case 'K': sb.append('k'); break;
                case 'L': sb.append('l'); break;
                case 'M': sb.append('m'); break;
                case 'N': sb.append('n'); break;
                case 'O': sb.append('o'); break;
                case 'P': sb.append('p'); break;
                case 'Q': sb.append('q'); break;
                case 'R': sb.append('r'); break;
                case 'S': sb.append('s'); break;
                case 'T': sb.append('t'); break;
                case 'U': sb.append('u'); break;
                case 'V': sb.append('v'); break;
                case 'W': sb.append('w'); break;
                case 'X': sb.append('x'); break;
                case 'Y': sb.append('y'); break;
                case 'Z': sb.append('z'); break;
                case 'Á': sb.append('á'); break;
                case 'É': sb.append('é'); break;
                case 'Í': sb.append('í'); break;
                case 'Ó': sb.append('ó'); break;
                case 'Ú': sb.append('ú'); break;
                case 'Ü': sb.append('ü'); break;
                case 'Ñ': sb.append('ñ'); break;
                default:  sb.append(c);   break;
            }
        }
        return sb.toString();
    }
}
