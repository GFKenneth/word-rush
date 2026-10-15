package palabrascontrarreloj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

public class Partida {

    private static final int DURACION_SEGUNDOS = 60;

    private final String secuencia;
    private final Trie diccionario;
    private final Estadisticas estadisticas;

    private final MiHashSet<String> usadas = new MiHashSet<>();
    private final MiHashMap<String, Integer> historial = new MiHashMap<>();
    private int puntuacionTotal = 0;

    private final AtomicBoolean tiempoAgotado = new AtomicBoolean(false);

    public Partida(String secuencia, Trie diccionario, Estadisticas estadisticas) {
        this.secuencia = secuencia;
        this.diccionario = diccionario;
        this.estadisticas = estadisticas;
    }

    public void jugar() throws IOException {
        System.out.println("\nSecuencia: " + secuencia.toUpperCase());
        System.out.println("Tienes 60 segundos. Escribe palabras y presiona Enter.");
        System.out.println("Puntuacion = longitud^2 por palabra.\n");

        Thread cronometro = new Thread(this::correrCronometro);
        cronometro.setDaemon(true);
        cronometro.start();

        BufferedReader lector = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));

        while (!tiempoAgotado.get()) {
            String linea;
            try {
                linea = lector.readLine();
            } catch (IOException e) {
                break;
            }
            if (linea == null || tiempoAgotado.get()) break;
            procesarPalabra(linea.trim());
        }

        mostrarResultados();
    }

    private void correrCronometro() {
        long fin = System.currentTimeMillis() + DURACION_SEGUNDOS * 1000L;
        try {
            while (true) {
                long restante = fin - System.currentTimeMillis();
                if (restante <= 0) break;
                long seg = (restante + 999) / 1000;
                if (seg % 10 == 0 || seg <= 10) {
                    System.out.println("\n  [Tiempo restante: " + seg + " seg]");
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException ignored) {}
        tiempoAgotado.set(true);
        System.out.println("\n¡Tiempo agotado!");
    }

    private void procesarPalabra(String raw) {
        if (raw.isEmpty()) return;

        String clave = Trie.normalizarMayusculas(raw);

        if (usadas.contains(clave)) {
            System.out.println("  [repetida] \"" + raw + "\" -> 0 pts");
            return;
        }

        if (!Validador.puedeFormarse(secuencia, raw)) {
            System.out.println("  [letras invalidas] \"" + raw + "\" -> 0 pts");
            return;
        }

        if (!diccionario.contiene(clave)) {
            System.out.println("  [no en diccionario] \"" + raw + "\" -> 0 pts");
            return;
        }

        int pts = Validador.calcularPuntuacion(clave);
        puntuacionTotal += pts;
        usadas.add(clave);
        historial.put(raw, pts);
        System.out.println("  [valida] \"" + raw + "\" -> +" + pts + " pts  (total: " + puntuacionTotal + ")");
    }

    private void mostrarResultados() {
        System.out.println("\n--- RESULTADOS ---");
        System.out.println("Secuencia: " + secuencia.toUpperCase());
        System.out.println("Puntuacion total: " + puntuacionTotal + " pts");
        System.out.println("\nTop 3 de esta secuencia:");
        String fecha = java.time.LocalDateTime.now().toString().substring(0, 16).replace('T', ' ');
        estadisticas.registrar(secuencia.toUpperCase(), puntuacionTotal, fecha);
        estadisticas.imprimirTop(secuencia.toUpperCase());
    }

    public int getPuntuacionTotal() { return puntuacionTotal; }
}
