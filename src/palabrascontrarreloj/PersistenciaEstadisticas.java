package palabrascontrarreloj;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PersistenciaEstadisticas {

    private static final String RUTA = "estadisticas.dat";

    public static Estadisticas cargar() {
        java.io.File archivo = new java.io.File(RUTA);
        if (!archivo.exists()) return new Estadisticas();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RUTA))) {
            return (Estadisticas) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se pudo cargar estadisticas.dat: " + e.getMessage());
            System.out.println("Se inicia con estadisticas vacias.");
            return new Estadisticas();
        }
    }

    public static void guardar(Estadisticas estadisticas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA))) {
            oos.writeObject(estadisticas);
        } catch (IOException e) {
            System.out.println("Error al guardar estadisticas: " + e.getMessage());
        }
    }
}
