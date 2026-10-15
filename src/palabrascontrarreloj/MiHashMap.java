package palabrascontrarreloj;

public class MiHashMap<K, V> implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private static class Nodo<K, V> implements java.io.Serializable {
        private static final long serialVersionUID = 2L;
        K llave;
        V valor;
        Nodo<K, V> siguiente;

        Nodo(K llave, V valor) {
            this.llave = llave;
            this.valor = valor;
        }
    }

    private static final int CAPACIDAD_INICIAL = 16;
    private static final float FACTOR_CARGA = 0.75f;

    @SuppressWarnings("unchecked")
    private Nodo<K, V>[] tabla;
    private int tamano;

    @SuppressWarnings("unchecked")
    public MiHashMap() {
        tabla = new Nodo[CAPACIDAD_INICIAL];
    }

    @SuppressWarnings("unchecked")
    public MiHashMap(int capacidadInicial) {
        tabla = new Nodo[capacidadInicial];
    }

    private int indice(K llave) {
        int h = llave.hashCode();
        h ^= (h >>> 16);
        return (h & 0x7FFFFFFF) % tabla.length;
    }

    public void put(K llave, V valor) {
        int i = indice(llave);
        Nodo<K, V> actual = tabla[i];
        while (actual != null) {
            if (actual.llave.equals(llave)) {
                actual.valor = valor;
                return;
            }
            actual = actual.siguiente;
        }
        Nodo<K, V> nuevo = new Nodo<>(llave, valor);
        nuevo.siguiente = tabla[i];
        tabla[i] = nuevo;
        tamano++;
        if ((float) tamano / tabla.length > FACTOR_CARGA) rehash();
    }

    public V get(K llave) {
        int i = indice(llave);
        Nodo<K, V> actual = tabla[i];
        while (actual != null) {
            if (actual.llave.equals(llave)) return actual.valor;
            actual = actual.siguiente;
        }
        return null;
    }

    public boolean containsKey(K llave) {
        return get(llave) != null;
    }

    public void remove(K llave) {
        int i = indice(llave);
        Nodo<K, V> actual = tabla[i];
        Nodo<K, V> anterior = null;
        while (actual != null) {
            if (actual.llave.equals(llave)) {
                if (anterior == null) tabla[i] = actual.siguiente;
                else anterior.siguiente = actual.siguiente;
                tamano--;
                return;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
    }

    public int size() { return tamano; }

    public boolean isEmpty() { return tamano == 0; }

    @SuppressWarnings("unchecked")
    public K[] llaves() {
        K[] resultado = (K[]) new Object[tamano];
        int pos = 0;
        for (int i = 0; i < tabla.length; i++) {
            Nodo<K, V> actual = tabla[i];
            while (actual != null) {
                resultado[pos++] = actual.llave;
                actual = actual.siguiente;
            }
        }
        return resultado;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        Nodo<K, V>[] vieja = tabla;
        tabla = new Nodo[vieja.length * 2];
        tamano = 0;
        for (int i = 0; i < vieja.length; i++) {
            Nodo<K, V> actual = vieja[i];
            while (actual != null) {
                put(actual.llave, actual.valor);
                actual = actual.siguiente;
            }
        }
    }
}
