package palabrascontrarreloj;

import java.io.Serializable;

public class Estadisticas implements Serializable {

    private static final long serialVersionUID = 1L;

    public static class Registro implements Serializable, Comparable<Registro> {
        private static final long serialVersionUID = 2L;
        public final int puntuacion;
        public final String fecha;

        public Registro(int puntuacion, String fecha) {
            this.puntuacion = puntuacion;
            this.fecha = fecha;
        }

        @Override
        public int compareTo(Registro otro) {
            return Integer.compare(otro.puntuacion, this.puntuacion);
        }

        @Override
        public String toString() {
            return puntuacion + " pts  [" + fecha + "]";
        }
    }

    private final MiHashMap<String, Registro[]> datos;

    public Estadisticas() {
        datos = new MiHashMap<>();
    }

    public void registrar(String secuencia, int puntuacion, String fecha) {
        Registro nuevo = new Registro(puntuacion, fecha);
        Registro[] actual = datos.get(secuencia);
        if (actual == null) {
            actual = new Registro[]{nuevo, null, null};
        } else {
            actual = insertarOrdenado(actual, nuevo);
        }
        datos.put(secuencia, actual);
    }

    private Registro[] insertarOrdenado(Registro[] arr, Registro nuevo) {
        Registro[] tmp = new Registro[]{arr[0], arr[1], arr[2], nuevo};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3 - i; j++) {
                Registro a = tmp[j];
                Registro b = tmp[j + 1];
                if (a == null || (b != null && b.puntuacion > a.puntuacion)) {
                    tmp[j] = b;
                    tmp[j + 1] = a;
                }
            }
        }
        return new Registro[]{tmp[0], tmp[1], tmp[2]};
    }

    public Registro[] obtenerTop(String secuencia) {
        return datos.get(secuencia);
    }

    public String[] secuencias() {
        return datos.llaves();
    }

    public void imprimirTodas() {
        String[] secs = secuencias();
        if (secs.length == 0) {
            System.out.println("  (sin estadisticas guardadas)");
            return;
        }
        for (String sec : secs) {
            System.out.println("  Secuencia: " + sec);
            Registro[] top = datos.get(sec);
            int pos = 1;
            for (Registro r : top) {
                if (r != null) {
                    System.out.println("    " + pos + ". " + r);
                    pos++;
                }
            }
        }
    }

    public void imprimirTop(String secuencia) {
        Registro[] top = datos.get(secuencia);
        if (top == null) {
            System.out.println("  No hay puntuaciones registradas para esta secuencia.");
            return;
        }
        int pos = 1;
        for (Registro r : top) {
            if (r != null) {
                System.out.println("  " + pos + ". " + r);
                pos++;
            }
        }
    }
}
