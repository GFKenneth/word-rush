package palabrascontrarreloj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {

    private static final BufferedReader STDIN = new BufferedReader(
            new InputStreamReader(System.in, StandardCharsets.UTF_8));

    public static void main(String[] args) {
        System.out.println("Palabras Contrarreloj");

        Trie diccionario = new Trie();
        try {
            CargadorDiccionario.cargar(diccionario);
        } catch (IOException e) {
            System.out.println("Error al cargar el diccionario: " + e.getMessage());
            System.exit(1);
        }

        Estadisticas estadisticas = PersistenciaEstadisticas.cargar();

        boolean salir = false;
        while (!salir) {
            System.out.println("\n1. Nueva partida (secuencia aleatoria)");
            System.out.println("2. Nueva partida (tu propia secuencia)");
            System.out.println("3. Ver estadisticas globales");
            System.out.println("4. Salir");
            System.out.print("Opcion: ");
            String opcion = leer();
            switch (opcion) {
                case "1": nuevaPartida(diccionario, estadisticas, false); break;
                case "2": nuevaPartida(diccionario, estadisticas, true);  break;
                case "3":
                    System.out.println("\n--- ESTADISTICAS GLOBALES ---");
                    estadisticas.imprimirTodas();
                    break;
                case "4": salir = true; break;
                default: System.out.println("Opcion no reconocida.");
            }
        }

        PersistenciaEstadisticas.guardar(estadisticas);
        System.out.println("Estadisticas guardadas. Hasta luego.");
    }

    private static void nuevaPartida(Trie diccionario, Estadisticas estadisticas,
                                      boolean usuarioEligeSecuencia) {
        try {
            String secuencia;
            if (usuarioEligeSecuencia) {
                secuencia = GeneradorSecuencia.solicitarAlUsuario();
            } else {
                secuencia = GeneradorSecuencia.generarAleatoria();
                System.out.println("Secuencia generada: " + secuencia);
            }
            new Partida(secuencia, diccionario, estadisticas).jugar();
        } catch (IOException e) {
            System.out.println("Error durante la partida: " + e.getMessage());
        }
    }

    private static String leer() {
        try {
            String linea = STDIN.readLine();
            return linea == null ? "4" : linea.trim();
        } catch (IOException e) {
            return "4";
        }
    }
}
