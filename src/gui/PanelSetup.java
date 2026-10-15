package gui;

import palabrascontrarreloj.GeneradorSecuencia;
import palabrascontrarreloj.Trie;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class PanelSetup extends JPanel {

    private final VentanaPrincipal ventana;
    private final PanelJuego       panelJuego;

    private JRadioButton rbAleatoria;
    private JRadioButton rbManual;
    private JTextField   campoSecuencia;
    private JLabel       lblPreview;

    public PanelSetup(VentanaPrincipal ventana, PanelJuego panelJuego) {
        this.ventana    = ventana;
        this.panelJuego = panelJuego;
        setBackground(Colores.FONDO);
        setLayout(new GridBagLayout());
        construir();
    }

    private void construir() {
        JPanel contenedor = new JPanel();
        contenedor.setBackground(Colores.PANEL);
        contenedor.setBorder(crearBorde("Nueva Partida"));
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setPreferredSize(new Dimension(480, 420));

        JLabel titulo = new JLabel("PALABRAS CONTRARRELOJ", SwingConstants.CENTER);
        titulo.setFont(Colores.FUENTE_TITULO);
        titulo.setForeground(Colores.ORO);
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 4, 0));

        JLabel subtitulo = new JLabel("Forma la mayor cantidad de palabras en 60 segundos", SwingConstants.CENTER);
        subtitulo.setFont(Colores.FUENTE_NORMAL);
        subtitulo.setForeground(Colores.TEXTO_DIM);
        subtitulo.setAlignmentX(CENTER_ALIGNMENT);

        // Opciones de secuencia
        JPanel panelOpciones = new JPanel();
        panelOpciones.setBackground(Colores.PANEL);
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        panelOpciones.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Colores.BORDE),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)));
        panelOpciones.setAlignmentX(CENTER_ALIGNMENT);
        panelOpciones.setMaximumSize(new Dimension(400, 200));

        rbAleatoria = new JRadioButton("Secuencia aleatoria");
        rbManual    = new JRadioButton("Ingresar mi propia secuencia");
        estilizarRadio(rbAleatoria);
        estilizarRadio(rbManual);
        rbAleatoria.setSelected(true);

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbAleatoria);
        grupo.add(rbManual);

        // Campo para secuencia manual
        JPanel filaManual = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filaManual.setBackground(Colores.PANEL);
        JLabel lblCampo = new JLabel("9 letras:");
        lblCampo.setFont(Colores.FUENTE_NORMAL);
        lblCampo.setForeground(Colores.TEXTO_DIM);
        campoSecuencia = new JTextField(10);
        estilizarCampo(campoSecuencia);
        campoSecuencia.setEnabled(false);
        filaManual.add(lblCampo);
        filaManual.add(campoSecuencia);

        // Preview de la secuencia generada
        lblPreview = new JLabel("Secuencia: (se generará al iniciar)", SwingConstants.CENTER);
        lblPreview.setFont(Colores.FUENTE_SECUENCIA);
        lblPreview.setForeground(Colores.ORO);
        lblPreview.setAlignmentX(CENTER_ALIGNMENT);

        // Botón generar preview
        JButton btnPreview = crearBoton("Generar vista previa", Colores.BOTON);
        btnPreview.setAlignmentX(CENTER_ALIGNMENT);
        btnPreview.addActionListener(e -> generarPreview());

        rbAleatoria.addActionListener(e -> {
            campoSecuencia.setEnabled(false);
            lblPreview.setText("Secuencia: (a generar..)");
            lblPreview.setForeground(Colores.ORO);
        });
        rbManual.addActionListener(e -> {
            campoSecuencia.setEnabled(true);
            campoSecuencia.requestFocus();
            lblPreview.setText("");
        });
        campoSecuencia.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { actualizarPreviewManual(); }
        });

        panelOpciones.add(rbAleatoria);
        panelOpciones.add(Box.createVerticalStrut(6));
        panelOpciones.add(rbManual);
        panelOpciones.add(Box.createVerticalStrut(6));
        panelOpciones.add(filaManual);

        // Botón iniciar
        JButton btnIniciar = crearBoton("INICIAR PARTIDA", Colores.VERDE);
        btnIniciar.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnIniciar.setAlignmentX(CENTER_ALIGNMENT);
        btnIniciar.setMaximumSize(new Dimension(240, 44));
        btnIniciar.addActionListener(e -> iniciarPartida());

        contenedor.add(titulo);
        contenedor.add(subtitulo);
        contenedor.add(Box.createVerticalStrut(20));
        contenedor.add(panelOpciones);
        contenedor.add(Box.createVerticalStrut(16));
        contenedor.add(btnPreview);
        contenedor.add(Box.createVerticalStrut(12));
        contenedor.add(lblPreview);
        contenedor.add(Box.createVerticalStrut(20));
        contenedor.add(btnIniciar);
        contenedor.add(Box.createVerticalStrut(20));

        add(contenedor);
    }

    private void generarPreview() {
        if (rbAleatoria.isSelected()) {
            String sec = GeneradorSecuencia.generarAleatoria();
            lblPreview.setText(sec);
            lblPreview.setForeground(Colores.ORO);
        }
    }

    private void actualizarPreviewManual() {
        String raw = campoSecuencia.getText();
        String limpia = raw.replaceAll("[^a-zA-Z]", "").toUpperCase();
        if (limpia.length() == 9) {
            lblPreview.setText(limpia);
            lblPreview.setForeground(Colores.VERDE);
        } else {
            lblPreview.setText("(" + limpia.length() + "/9 letras)");
            lblPreview.setForeground(Colores.TEXTO_DIM);
        }
    }

    private void iniciarPartida() {
        String secuencia;
        if (rbAleatoria.isSelected()) {
            secuencia = GeneradorSecuencia.generarAleatoria();
        } else {
            String raw = campoSecuencia.getText();
            String limpia = raw.replaceAll("[^a-zA-Z]", "").toUpperCase();
            if (limpia.length() != 9) {
                JOptionPane.showMessageDialog(this,
                    "La secuencia debe tener exactamente 9 letras.\nSe encontraron: " + limpia.length(),
                    "Secuencia inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            secuencia = limpia;
        }
        panelJuego.iniciarPartida(secuencia);
        ventana.mostrarJuego();
    }

    public void reiniciar() {
        rbAleatoria.setSelected(true);
        campoSecuencia.setEnabled(false);
        campoSecuencia.setText("");
        lblPreview.setText("Secuencia: (a generar..)");
        lblPreview.setForeground(Colores.ORO);
    }

    private void estilizarRadio(JRadioButton rb) {
        rb.setFont(Colores.FUENTE_NORMAL);
        rb.setForeground(Colores.TEXTO);
        rb.setBackground(Colores.PANEL);
    }

    private void estilizarCampo(JTextField tf) {
        tf.setBackground(Colores.PANEL_CLARO);
        tf.setForeground(Colores.TEXTO);
        tf.setCaretColor(Colores.ORO);
        tf.setFont(new Font("Monospaced", Font.BOLD, 15));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Colores.BORDE),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(Colores.FUENTE_BOTON);
        btn.setBackground(color.darker());
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(color); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(color.darker()); }
        });
        return btn;
    }

    private Border crearBorde(String titulo) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Colores.BORDE, 2),
                titulo,
                TitledBorder.CENTER, TitledBorder.TOP,
                Colores.FUENTE_SUBTITULO, Colores.ORO),
            BorderFactory.createEmptyBorder(8, 16, 16, 16));
    }
}
