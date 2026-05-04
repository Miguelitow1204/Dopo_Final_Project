package presentation;

import Domain.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Vista principal del juego: render de mapa, entidades, HUD y mensajes.
 * Autores: MurilloRubiano con apoyo de Claude Opus 4.6.
 */
public class GameScreen extends JPanel {

    private Nivel nivel;
    private int muertes;
    private int tiempoRestante;
    private String mensaje;
    private boolean mostrandoMensaje;

    public static final int ANCHO = 800;
    public static final int ALTO = 600;
    private static final int HUD_ALTO = 45;

    /**
     * Crea el panel de juego y configura propiedades de render.
     */
    public GameScreen() {
        setPreferredSize(new Dimension(ANCHO, ALTO));
        setDoubleBuffered(true);
        setFocusable(true);
        this.mensaje = "";
        this.mostrandoMensaje = false;
    }

    /**
     * Dibuja un frame completo del juego.
     *
     * @param g contexto grafico de Swing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo oscuro base
        g2.setColor(new Color(25, 25, 38));
        g2.fillRect(0, 0, ANCHO, ALTO);

        if (nivel == null)
            return;

        dibujarFondo(g2);
        dibujarZonaSegura(g2, nivel.getZonaInicio());
        dibujarZonaSegura(g2, nivel.getZonaMeta());
        dibujarMonedas(g2, nivel.getMonedas());
        dibujarEnemigos(g2, nivel.getEnemigos());
        dibujarJugador(g2, nivel.getJugador());
        dibujarHUD(g2);

        if (mostrandoMensaje) {
            dibujarMensajeCentral(g2, mensaje);
        }
    }

    /**
     * Dibuja el fondo cuadriculado y el borde del area jugable.
     *
     * @param g contexto grafico.
     */
    private void dibujarFondo(Graphics2D g) {
        // Cuadricula estilo The World's Hardest Game
        int tamCelda = 30;
        for (int x = 0; x < ANCHO; x += tamCelda) {
            for (int y = HUD_ALTO; y < ALTO; y += tamCelda) {
                if ((x / tamCelda + y / tamCelda) % 2 == 0) {
                    g.setColor(new Color(235, 235, 240));
                } else {
                    g.setColor(new Color(250, 250, 252));
                }
                g.fillRect(x, y, tamCelda, tamCelda);
            }
        }
        // Borde del area de juego
        g.setColor(new Color(60, 60, 80));
        g.setStroke(new BasicStroke(2));
        g.drawRect(0, HUD_ALTO, ANCHO - 1, ALTO - HUD_ALTO - 1);
        g.setStroke(new BasicStroke(1));
    }

    /**
     * Dibuja una zona segura (inicio o meta) con su etiqueta.
     *
     * @param g    contexto grafico.
     * @param zona zona a dibujar.
     */
    private void dibujarZonaSegura(Graphics2D g, ZonaSegura zona) {
        if (zona == null)
            return;
        int x = (int) zona.getPosicion().getX();
        int y = (int) zona.getPosicion().getY();

        if (zona.getTipo() == TipoZona.INICIO) {
            g.setColor(new Color(130, 220, 130, 160));
        } else {
            g.setColor(new Color(40, 160, 60, 160));
        }
        g.fillRect(x, y, zona.getAncho(), zona.getAlto());

        // Borde
        g.setColor(new Color(30, 100, 40));
        g.drawRect(x, y, zona.getAncho(), zona.getAlto());

        // Texto indicador
        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        g.setColor(new Color(20, 80, 30));
        String label = zona.getTipo() == TipoZona.INICIO ? "START" : "GOAL";
        FontMetrics fm = g.getFontMetrics();
        int tx = x + (zona.getAncho() - fm.stringWidth(label)) / 2;
        int ty = y + zona.getAlto() - 8;
        g.drawString(label, tx, ty);
    }

    /**
     * Dibuja las monedas activas del nivel.
     *
     * @param g       contexto grafico.
     * @param monedas monedas del nivel.
     */
    private void dibujarMonedas(Graphics2D g, List<Moneda> monedas) {
        for (Moneda moneda : monedas) {
            if (moneda.isActiva()) {
                int x = (int) moneda.getPosicion().getX();
                int y = (int) moneda.getPosicion().getY();

                // Brillo exterior
                g.setColor(new Color(255, 230, 80, 60));
                g.fillOval(x - 3, y - 3, moneda.getAncho() + 6, moneda.getAlto() + 6);

                // Moneda
                g.setColor(new Color(255, 215, 0));
                g.fillOval(x, y, moneda.getAncho(), moneda.getAlto());

                // Borde
                g.setColor(new Color(200, 160, 20));
                g.drawOval(x, y, moneda.getAncho(), moneda.getAlto());
            }
        }
    }

    /**
     * Dibuja todos los enemigos visibles del nivel.
     *
     * @param g        contexto grafico.
     * @param enemigos enemigos a renderizar.
     */
    private void dibujarEnemigos(Graphics2D g, List<Enemigo> enemigos) {
        for (Enemigo enemigo : enemigos) {
            int x = (int) enemigo.getPosicion().getX();
            int y = (int) enemigo.getPosicion().getY();

            // Brillo
            g.setColor(new Color(50, 120, 255, 40));
            g.fillOval(x - 3, y - 3, enemigo.getAncho() + 6, enemigo.getAlto() + 6);

            // Circulo azul
            g.setColor(new Color(40, 100, 220));
            g.fillOval(x, y, enemigo.getAncho(), enemigo.getAlto());

            // Borde
            g.setColor(new Color(25, 60, 160));
            g.drawOval(x, y, enemigo.getAncho(), enemigo.getAlto());
        }
    }

    /**
     * Dibuja el avatar del jugador segun su tipo seleccionado.
     *
     * @param g       contexto grafico.
     * @param jugador jugador activo.
     */
    private void dibujarJugador(Graphics2D g, Jugador jugador) {
        int x = (int) jugador.getPosicion().getX();
        int y = (int) jugador.getPosicion().getY();
        int w = jugador.getAncho();
        int h = jugador.getAlto();

        // Color segun tipo de personaje
        Color colorPrincipal;
        Color colorBorde;
        switch (jugador.getTipoPersonaje()) {
            case AZUL:
                colorPrincipal = new Color(50, 120, 230);
                colorBorde = new Color(30, 70, 160);
                break;
            case VERDE:
                colorPrincipal = new Color(50, 190, 70);
                colorBorde = new Color(30, 120, 40);
                break;
            default: // ROJO
                colorPrincipal = new Color(220, 50, 50);
                colorBorde = new Color(160, 30, 30);
                break;
        }

        // Cuadrado del jugador
        g.setColor(colorPrincipal);
        g.fillRect(x, y, w, h);
        g.setColor(colorBorde);
        g.drawRect(x, y, w, h);

        // Cuadradito interior decorativo (estilo original)
        g.setColor(new Color(255, 255, 255, 80));
        g.fillRect(x + 4, y + 4, w - 8, h - 8);
    }

    /**
     * Dibuja la barra superior con nivel, muertes, monedas y tiempo.
     *
     * @param g contexto grafico.
     */
    private void dibujarHUD(Graphics2D g) {
        // Barra superior oscura
        g.setColor(new Color(30, 30, 45));
        g.fillRect(0, 0, ANCHO, HUD_ALTO);

        // Linea inferior del HUD
        g.setColor(new Color(255, 200, 50));
        g.fillRect(0, HUD_ALTO - 2, ANCHO, 2);

        g.setFont(new Font("Monospaced", Font.BOLD, 15));

        // Nivel
        g.setColor(new Color(100, 200, 255));
        g.drawString("LEVEL " + (nivel != null ? nivel.getNumero() : ""), 20, 28);

        // Muertes
        g.setColor(new Color(255, 100, 100));
        g.drawString("DEATHS: " + muertes, 180, 28);

        // Monedas
        if (nivel != null) {
            Jugador j = nivel.getJugador();
            int total = nivel.getMonedas().size();
            g.setColor(new Color(255, 215, 0));
            g.drawString("COINS: " + j.getMonedasRecogidas() + "/" + total, 380, 28);
        }

        // Tiempo
        int min = tiempoRestante / 60;
        int seg = tiempoRestante % 60;
        g.setColor(Color.WHITE);
        g.drawString(String.format("TIME: %02d:%02d", min, seg), 600, 28);
    }

    /**
     * Dibuja un mensaje central superpuesto sobre el juego.
     *
     * @param g     contexto grafico.
     * @param texto mensaje principal a mostrar.
     */
    private void dibujarMensajeCentral(Graphics2D g, String texto) {
        // Overlay
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, ANCHO, ALTO);

        // Caja central
        int boxW = 420, boxH = 120;
        int boxX = (ANCHO - boxW) / 2, boxY = (ALTO - boxH) / 2;
        g.setColor(new Color(30, 30, 50));
        g.fillRoundRect(boxX, boxY, boxW, boxH, 16, 16);
        g.setColor(new Color(255, 200, 50));
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(boxX, boxY, boxW, boxH, 16, 16);
        g.setStroke(new BasicStroke(1));

        // Texto principal
        g.setFont(new Font("Monospaced", Font.BOLD, 26));
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int tw = fm.stringWidth(texto);
        g.drawString(texto, (ANCHO - tw) / 2, boxY + 50);

        // Subtexto
        g.setFont(new Font("Monospaced", Font.PLAIN, 13));
        g.setColor(new Color(180, 180, 200));
        String sub = "Press ENTER to continue  |  ESC for menu";
        int sw = g.getFontMetrics().stringWidth(sub);
        g.drawString(sub, (ANCHO - sw) / 2, boxY + 85);
    }

    // Metodos publicos

    /**
     * Define el nivel actualmente mostrado.
     *
     * @param nivel nivel activo.
     */
    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    /**
     * Define el total de muertes mostrado en el HUD.
     *
     * @param muertes total de muertes.
     */
    public void setMuertes(int muertes) {
        this.muertes = muertes;
    }

    /**
     * Define el tiempo restante mostrado en el HUD.
     *
     * @param tiempoRestante segundos restantes.
     */
    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    /**
     * Establece y muestra un mensaje superpuesto.
     *
     * @param mensaje texto del mensaje.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
        this.mostrandoMensaje = true;
    }

    /**
     * Oculta el mensaje central en pantalla.
     */
    public void ocultarMensaje() {
        this.mostrandoMensaje = false;
    }
}
