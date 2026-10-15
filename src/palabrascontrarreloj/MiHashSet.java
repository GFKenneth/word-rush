package palabrascontrarreloj;

public class MiHashSet<E> implements java.io.Serializable {

    private static final long serialVersionUID = 3L;

    private final MiHashMap<E, Boolean> mapa;

    public MiHashSet() {
        mapa = new MiHashMap<>();
    }

    public void add(E elemento) {
        mapa.put(elemento, Boolean.TRUE);
    }

    public boolean contains(E elemento) {
        return mapa.containsKey(elemento);
    }

    public void remove(E elemento) {
        mapa.remove(elemento);
    }

    public int size() { return mapa.size(); }

    public boolean isEmpty() { return mapa.isEmpty(); }

    public E[] elementos() { return mapa.llaves(); }
}
