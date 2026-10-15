package palabrascontrarreloj;

public class NodoTrie {

    static final int TAMANO = 33;

    NodoTrie[] hijos;
    boolean esPalabra;

    public NodoTrie() {
        hijos = new NodoTrie[TAMANO];
        esPalabra = false;
    }

    // Índices 0-25: a-z | 26-31: á,é,í,ó,ú,ü | 32: ñ
    public static int indice(char c) {
        switch (c) {
            case 'á': return 26;
            case 'é': return 27;
            case 'í': return 28;
            case 'ó': return 29;
            case 'ú': return 30;
            case 'ü': return 31;
            case 'ñ': return 32;
            default:
                if (c >= 'a' && c <= 'z') return c - 'a';
                return -1;
        }
    }
}
