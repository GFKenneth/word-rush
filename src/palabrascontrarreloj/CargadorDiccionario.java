package palabrascontrarreloj;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CargadorDiccionario {

    // URL
    private static final String URL_DICCIONARIO =
        "https://raw.githubusercontent.com/JorgeDuenasLerin/diccionario-espanol-txt/master/data/archive/2024-05-22/0_palabras_todas.txt";

    private static final String RUTA_LOCAL = "diccionario.txt";

    public static void cargar(Trie trie) throws IOException {
        java.io.File archivo = new java.io.File(RUTA_LOCAL);
        if (!archivo.exists()) {
            System.out.println("Descargando diccionario...");
            try (InputStream in = new URL(URL_DICCIONARIO).openStream()) {
                Files.copy(in, Paths.get(RUTA_LOCAL), StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("Diccionario guardado como '" + RUTA_LOCAL + "'.");
        } else {
            System.out.println("Cargando diccionario local...");
        }

        int conteo = 0;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(RUTA_LOCAL), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    trie.insertar(Trie.normalizarMayusculas(linea));
                    conteo++;
                }
            }
        }
        System.out.println("Diccionario cargado: " + conteo + " palabras.");
    }
}
