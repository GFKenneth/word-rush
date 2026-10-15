package gui;

import palabrascontrarreloj.CargadorDiccionario;
import palabrascontrarreloj.Estadisticas;
import palabrascontrarreloj.PersistenciaEstadisticas;
import palabrascontrarreloj.Trie;

import javax.swing.*;
import java.io.IOException;

public class MainGUI {

    public static void main(String[] args) {
        // Cargar diccionario y estadísticas antes de mostrar la ventana
        Trie diccionario = new Trie();
        try {
            CargadorDiccionario.cargar(diccionario);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Error al cargar el diccionario:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        Estadisticas estadisticas = PersistenciaEstadisticas.cargar();

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            // Inyectar dependencias en el panel de juego
            ventana.getPanelJuego().setDiccionario(diccionario);
            ventana.getPanelJuego().setEstadisticas(estadisticas);
            ventana.setVisible(true);
        });
    }
}
