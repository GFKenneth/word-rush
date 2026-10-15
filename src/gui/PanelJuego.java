package gui;

import palabrascontrarreloj.Estadisticas;
import palabrascontrarreloj.MiHashMap;
import palabrascontrarreloj.MiHashSet;
import palabrascontrarreloj.PersistenciaEstadisticas;
import palabrascontrarreloj.Trie;
import palabrascontrarreloj.Validador;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class PanelJuego extends JPanel {

    private static final int DURACION = 60;

    private final VentanaPrincipal ventana;

    // Referencias al diccionario y estadísticas se inyectan desde Main
    private Trie         diccionario;
    private Estadisticas estadisticas;

    // Estado de la partida en curso
    private String           secuencia;
    private MiHashSet<String> usadas;
    private int              puntuacion;
    private boolean          partidaActiva;

    // UI
    private JLabel    lblSecuencia;
    private JLabel    lblCronometro;
    private JLabel    lblPuntuacion;
    private JTextArea areaLog;
    private JTextField campoEntrada;
    private JButton   btnEnviar;
    private JPanel    panelResultados;

    // Hilo del cronómetro
    private Thread hiloReloj;

    public PanelJuego(VentanaPrincipal ventana) {
        this.ventana = ventana;
        setBackground(Colores.FONDO);
        setLayout(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        construirUI();
    }

    public void setDiccionario(Trie diccionario) { this.diccionario = diccionario; }
    public void setEstadisticas(Estadisticas e)  { this.estadisticas = e; }

    // ----------------------------------------------------------------
    //  Construcción de la UI
    // ----------------------------------------------------------------

    private void construirUI() {
        // Barra norte: título + secuencia + botón
        JPanel norte = new JPanel(new BorderLayout(10, 0));
        norte.setBackground(Colores.PANEL);
        norte.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Colores.ORO),
            BorderFactory.createEmptyBorder(8, 14, 8, 14)));

        JLabel lblTitulo = new JLabel("PALABRAS CONTRARRELOJ");
        lblTitulo.setFont(Colores.FUENTE_SUBTITULO);
        lblTitulo.setForeground(Colores.ORO);

        lblSecuencia = new JLabel("—");
        lblSecuencia.setFont(Colores.FUENTE_SECUENCIA);
        lblSecuencia.setForeground(Colores.TEXTO);
        lblSecuencia.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnNueva = crearBoton("Nueva partida", Colores.ROJO);
        btnNueva.addActionListener(e -> {
            detenerReloj();
            PersistenciaEstadisticas.guardar(estadisticas);
            ventana.mostrarSetup();
        });

        norte.add(lblTitulo,   BorderLayout.WEST);
        norte.add(lblSecuencia,BorderLayout.CENTER);
        norte.add(btnNueva,    BorderLayout.EAST);

        // Panel izquierdo: cronómetro + log
        JPanel izquierda = new JPanel(new BorderLayout(0, 6));
        izquierda.setBackground(Colores.FONDO);

        // Cronómetro grande
        JPanel panelReloj = new JPanel(new BorderLayout());
        panelReloj.setBackground(Colores.PANEL);
        panelReloj.setBorder(crearBorde("Tiempo"));
        lblCronometro = new JLabel("60", SwingConstants.CENTER);
        lblCronometro.setFont(Colores.FUENTE_CRONOMETRO);
        lblCronometro.setForeground(Colores.VERDE);
        lblCronometro.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        panelReloj.add(lblCronometro, BorderLayout.CENTER);

        // Área de log de palabras
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setBackground(Colores.FONDO);
        areaLog.setForeground(Colores.TEXTO);
        areaLog.setFont(Colores.FUENTE_MONO);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(crearBorde("Palabras ingresadas"));
        scrollLog.setBackground(Colores.FONDO);

        izquierda.add(panelReloj, BorderLayout.NORTH);
        izquierda.add(scrollLog,  BorderLayout.CENTER);

        // Panel derecho: puntuación + panel de resultados (se muestra al terminar)
        JPanel derecha = new JPanel(new BorderLayout(0, 6));
        derecha.setBackground(Colores.FONDO);
        derecha.setPreferredSize(new Dimension(300, 0));

        // Puntuación
        JPanel panelPuntos = new JPanel(new BorderLayout());
        panelPuntos.setBackground(Colores.PANEL);
        panelPuntos.setBorder(crearBorde("Puntuación"));
        lblPuntuacion = new JLabel("0 pts", SwingConstants.CENTER);
        lblPuntuacion.setFont(new Font("Serif", Font.BOLD, 32));
        lblPuntuacion.setForeground(Colores.ORO);
        lblPuntuacion.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelPuntos.add(lblPuntuacion, BorderLayout.CENTER);

        // Panel de resultados (oculto hasta el final)
        panelResultados = new JPanel();
        panelResultados.setBackground(Colores.PANEL);
        panelResultados.setBorder(crearBorde("Resultados"));
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        panelResultados.setVisible(false);

        derecha.add(panelPuntos,    BorderLayout.NORTH);
        derecha.add(panelResultados,BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, izquierda, derecha);
        split.setBackground(Colores.FONDO);
        split.setDividerLocation(660);
        split.setDividerSize(4);

        // Panel sur: entrada de palabras
        JPanel sur = new JPanel(new BorderLayout(8, 0));
        sur.setBackground(Colores.PANEL);
        sur.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, Colores.BORDE),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)));

        JLabel lblIngresa = new JLabel("Ingresa una palabra:");
        lblIngresa.setFont(Colores.FUENTE_SUBTITULO);
        lblIngresa.setForeground(Colores.ORO);

        campoEntrada = new JTextField();
        campoEntrada.setBackground(Colores.PANEL_CLARO);
        campoEntrada.setForeground(Colores.TEXTO);
        campoEntrada.setCaretColor(Colores.ORO);
        campoEntrada.setFont(new Font("Monospaced", Font.PLAIN, 16));
        campoEntrada.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Colores.BORDE),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        campoEntrada.setEnabled(false);
        campoEntrada.addActionListener(e -> enviarPalabra());

        btnEnviar = crearBoton("Enviar", Colores.AZUL);
        btnEnviar.setEnabled(false);
        btnEnviar.addActionListener(e -> enviarPalabra());

        sur.add(lblIngresa,  BorderLayout.WEST);
        sur.add(campoEntrada,BorderLayout.CENTER);
        sur.add(btnEnviar,   BorderLayout.EAST);

        add(norte, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
        add(sur,   BorderLayout.SOUTH);
    }

    // ----------------------------------------------------------------
    //  Iniciar partida
    // ----------------------------------------------------------------

    public void iniciarPartida(String sec) {
        this.secuencia   = sec;
        this.usadas      = new MiHashSet<>();
        this.puntuacion  = 0;
        this.partidaActiva = true;

        lblSecuencia.setText(sec);
        lblSecuencia.setForeground(Colores.TEXTO);
        lblCronometro.setText("60");
        lblCronometro.setForeground(Colores.VERDE);
        lblPuntuacion.setText("0 pts");
        areaLog.setText("");
        panelResultados.setVisible(false);
        campoEntrada.setEnabled(true);
        campoEntrada.setText("");
        btnEnviar.setEnabled(true);

        iniciarReloj();
    }

    public void enfocarEntrada() {
        campoEntrada.requestFocusInWindow();
    }

    // ----------------------------------------------------------------
    //  Cronómetro
    // ----------------------------------------------------------------

    private void iniciarReloj() {
        detenerReloj();
        hiloReloj = new Thread(() -> {
            long fin = System.currentTimeMillis() + DURACION * 1000L;
            while (true) {
                long restante = fin - System.currentTimeMillis();
                if (restante <= 0) {
                    SwingUtilities.invokeLater(() -> terminarPartida());
                    break;
                }
                int seg = (int) ((restante + 999) / 1000);
                SwingUtilities.invokeLater(() -> actualizarCronometro(seg));
                try { Thread.sleep(200); } catch (InterruptedException e) { break; }
            }
        });
        hiloReloj.setDaemon(true);
        hiloReloj.start();
    }

    private void detenerReloj() {
        if (hiloReloj != null && hiloReloj.isAlive()) hiloReloj.interrupt();
    }

    private void actualizarCronometro(int seg) {
        lblCronometro.setText(String.valueOf(seg));
        if (seg <= 10)      lblCronometro.setForeground(Colores.ROJO);
        else if (seg <= 20) lblCronometro.setForeground(Colores.AMARILLO);
        else                lblCronometro.setForeground(Colores.VERDE);
    }

    // ----------------------------------------------------------------
    //  Procesar palabra
    // ----------------------------------------------------------------

    private void enviarPalabra() {
        if (!partidaActiva) return;
        String raw = campoEntrada.getText().trim();
        campoEntrada.setText("");
        if (raw.isEmpty()) return;

        String clave = Trie.normalizarMayusculas(raw);

        if (usadas.contains(clave)) {
            log("[repetida]        " + raw + "  →  0 pts", Colores.TEXTO_DIM);
            return;
        }
        if (!Validador.puedeFormarse(secuencia, raw)) {
            log("[letras inválidas] " + raw + "  →  0 pts", Colores.ROJO);
            return;
        }
        if (!diccionario.contiene(clave)) {
            log("[no en diccionario] " + raw + "  →  0 pts", Colores.ROJO);
            return;
        }

        int pts = Validador.calcularPuntuacion(clave);
        puntuacion += pts;
        usadas.add(clave);
        log("[+] " + raw + "  →  +" + pts + " pts", Colores.VERDE);
        lblPuntuacion.setText(puntuacion + " pts");
    }

    private void log(String mensaje, Color color) {
        // Append con color usando HTML es complejo en JTextArea; usamos prefijo y color de fondo uniforme
        // Para colores por línea usaríamos JTextPane; aquí mantenemos JTextArea simple
        areaLog.append(mensaje + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    // ----------------------------------------------------------------
    //  Fin de partida
    // ----------------------------------------------------------------

    private void terminarPartida() {
        partidaActiva = false;
        campoEntrada.setEnabled(false);
        btnEnviar.setEnabled(false);
        lblCronometro.setText("0");
        lblCronometro.setForeground(Colores.ROJO);
        areaLog.append("\n¡Tiempo agotado!\n");

        String fecha = java.time.LocalDateTime.now().toString().substring(0, 16).replace('T', ' ');
        estadisticas.registrar(secuencia, puntuacion, fecha);
        PersistenciaEstadisticas.guardar(estadisticas);

        mostrarResultados();
    }

    private void mostrarResultados() {
        panelResultados.removeAll();
        panelResultados.setVisible(true);

        JLabel lblFinal = new JLabel("Puntuación final: " + puntuacion + " pts", SwingConstants.CENTER);
        lblFinal.setFont(Colores.FUENTE_SUBTITULO);
        lblFinal.setForeground(Colores.ORO);
        lblFinal.setAlignmentX(CENTER_ALIGNMENT);
        lblFinal.setBorder(BorderFactory.createEmptyBorder(10, 0, 6, 0));

        JLabel lblTop = new JLabel("Top 3 — " + secuencia, SwingConstants.CENTER);
        lblTop.setFont(Colores.FUENTE_PEQUENA);
        lblTop.setForeground(Colores.TEXTO_DIM);
        lblTop.setAlignmentX(CENTER_ALIGNMENT);

        panelResultados.add(lblFinal);
        panelResultados.add(lblTop);
        panelResultados.add(Box.createVerticalStrut(6));

        Estadisticas.Registro[] top = estadisticas.obtenerTop(secuencia);
        if (top != null) {
            int pos = 1;
            for (Estadisticas.Registro r : top) {
                if (r == null) continue;
                JLabel lbl = new JLabel(pos + ".  " + r.toString(), SwingConstants.CENTER);
                lbl.setFont(Colores.FUENTE_NORMAL);
                lbl.setForeground(pos == 1 ? Colores.ORO : Colores.TEXTO);
                lbl.setAlignmentX(CENTER_ALIGNMENT);
                panelResultados.add(lbl);
                panelResultados.add(Box.createVerticalStrut(2));
                pos++;
            }
        }

        panelResultados.add(Box.createVerticalStrut(16));

        JButton btnNueva = crearBoton("Nueva Partida", Colores.VERDE);
        btnNueva.setAlignmentX(CENTER_ALIGNMENT);
        btnNueva.addActionListener(e -> {
            ventana.mostrarSetup();
        });
        panelResultados.add(btnNueva);

        panelResultados.revalidate();
        panelResultados.repaint();
    }

    // ----------------------------------------------------------------
    //  Helpers UI
    // ----------------------------------------------------------------

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(Colores.FUENTE_BOTON);
        btn.setBackground(color.darker());
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color.darker()); }
        });
        return btn;
    }

    private Border crearBorde(String titulo) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Colores.BORDE, 1),
                titulo, TitledBorder.LEFT, TitledBorder.TOP,
                Colores.FUENTE_PEQUENA, Colores.TEXTO_DIM),
            BorderFactory.createEmptyBorder(4, 4, 4, 4));
    }
}
