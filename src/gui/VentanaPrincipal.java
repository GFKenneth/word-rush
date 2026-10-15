package gui;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private static final String CARTA_SETUP = "setup";
    private static final String CARTA_JUEGO = "juego";

    private final CardLayout cardLayout;
    private final JPanel     panelRaiz;
    private final PanelSetup panelSetup;
    private final PanelJuego panelJuego;

    public VentanaPrincipal() {
        super("Palabras Contrarreloj");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 680);
        setMinimumSize(new Dimension(820, 560));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panelRaiz  = new JPanel(cardLayout);
        panelRaiz.setBackground(Colores.FONDO);

        panelJuego = new PanelJuego(this);
        panelSetup = new PanelSetup(this, panelJuego);

        panelRaiz.add(panelSetup, CARTA_SETUP);
        panelRaiz.add(panelJuego, CARTA_JUEGO);

        add(panelRaiz);
        mostrarSetup();
    }

    public void mostrarSetup() {
        panelSetup.reiniciar();
        cardLayout.show(panelRaiz, CARTA_SETUP);
    }

    public PanelJuego getPanelJuego() { return panelJuego; }

    public void mostrarJuego() {
        cardLayout.show(panelRaiz, CARTA_JUEGO);
        panelJuego.enfocarEntrada();
    }
}
