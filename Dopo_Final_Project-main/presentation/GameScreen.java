package presentation;

import Domain.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista principal del juego: render de mapa, entidades, HUD y mensajes.
 * 
 * @author (Murillo Rubiano)
 * @version 2.5
 */
public class GameScreen extends JPanel {

    private Nivel nivel;
    private int muertes;
    private int tiempoRestante;

    public static final int ANCHO = 800;
    public static final int ALTO = 600;
    public static final int HUD_ALTO = 45;

    private boolean mostrandoVictoria;
    private boolean mostrandoDerrota;
    private boolean mostrandoPausa;
    
    private boolean modoPvP = false;
    private boolean mostrandoVictoriaPvP = false;
    private String mensajeVictoriaPvP = "";
    
    private boolean mensajeGuardado = false;

    /**
     * Crea el panel de juego y configura propiedades de render.
     */
    public GameScreen() {
        setPreferredSize(new Dimension(ANCHO, ALTO));
        setDoubleBuffered(true);
        setFocusable(true);
        this.mostrandoVictoria = false;
        this.mostrandoDerrota = false;
        this.mostrandoPausa = false;
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
        dibujarParedes(g2, nivel.getParedes());
        dibujarZonaSegura(g2, nivel.getZonaInicio());
        dibujarZonaSegura(g2, nivel.getZonaMeta());
        if(nivel.getZonaIntermedia() != null){
            dibujarZonaSegura(g2, nivel.getZonaIntermedia());
        }
        dibujarMonedas(g2, nivel.getMonedas());
        dibujarFuentesVida(g2, nivel.getFuentesVida());
        dibujarBombas(g2, nivel.getBombas());
        dibujarEnemigos(g2, nivel.getEnemigos());
        dibujarJugador(g2, nivel.getJugador());
        if (modoPvP && nivel.getJugador2() != null) {
            dibujarJugador2(g2, nivel.getJugador2());
        }
        dibujarHUD(g2);

        if (mostrandoVictoria) {
            dibujarVictoria(g2);
        } else if (mostrandoDerrota) {
            dibujarDerrota(g2);
        } else if (mostrandoPausa) {
            dibujarPausa(g2);
        }
        
        if (mostrandoVictoriaPvP) {
            dibujarVictoriaPvP(g2);
        } else if (mostrandoVictoria) {
            dibujarVictoria(g2);
        } else if (mostrandoDerrota) {
            dibujarDerrota(g2);
        } else if (mostrandoPausa) {
            dibujarPausa(g2);
        }
        
        if (mensajeGuardado) {
            dibujarMensajeGuardado(g2);
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
        // Paredes sólidas visibles
        int grosor = 10;
        g.setColor(new Color(60, 60, 90));
        // Pared superior
        g.fillRect(0, HUD_ALTO, ANCHO, grosor);
        // Pared inferior
        g.fillRect(0, ALTO - grosor, ANCHO, grosor);
        // Pared izquierda
        g.fillRect(0, HUD_ALTO, grosor, ALTO - HUD_ALTO);
        // Pared derecha
        g.fillRect(ANCHO - grosor, HUD_ALTO, grosor, ALTO - HUD_ALTO);

        // Borde decorativo encima
        g.setColor(new Color(255, 200, 50));
        g.setStroke(new BasicStroke(2));
        g.drawRect(grosor, HUD_ALTO + grosor,
                ANCHO - grosor * 2 - 1, ALTO - HUD_ALTO - grosor * 2 - 1);
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
        } else if(zona.getTipo() == TipoZona.META){
            g.setColor(new Color(40, 160, 60, 160));
        } else {
            g.setColor(new Color(100, 180, 255, 160)); 
        }
        g.fillRect(x, y, zona.getAncho(), zona.getAlto());

        // Borde
        g.setColor(new Color(30, 100, 40));
        g.drawRect(x, y, zona.getAncho(), zona.getAlto());

        // Texto indicador
        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        g.setColor(new Color(20, 80, 30));
        String label = zona.getTipo() == TipoZona.INICIO ? "START" :
                       zona.getTipo() == TipoZona.META ? "GOAL" : "CHECK";
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
            if(enemigo.isActiva()){
                int x = (int) enemigo.getPosicion().getX();
                int y = (int) enemigo.getPosicion().getY();

                //Brillo
                g.setColor(new Color(50, 120, 255, 40));
                g.fillOval(x - 3, y - 3, enemigo.getAncho() + 6, enemigo.getAlto() + 6);

                //Circulo azul
                g.setColor(new Color(40, 100, 220));
                g.fillOval(x, y, enemigo.getAncho(), enemigo.getAlto());

                //Borde
                g.setColor(new Color(25, 60, 160));
                g.drawOval(x, y, enemigo.getAncho(), enemigo.getAlto());
            }
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

        // Indicadores visuales exclusivos del jugador verde
        if (jugador.getTipoPersonaje() == TipoPersonaje.VERDE) {
            if (jugador.isEscudoActivo()) {
                // Escudo activo: anillo verde brillante
                g.setColor(new Color(100, 255, 120, 180));
                g.setStroke(new BasicStroke(3));
                g.drawRect(x - 4, y - 4, w + 8, h + 8);
                g.setStroke(new BasicStroke(1));
            } else {
                // Escudo roto: borde rojo pulsante para indicar vulnerabilidad
                g.setColor(new Color(220, 50, 50, 200));
                g.setStroke(new BasicStroke(3));
                g.drawRect(x - 4, y - 4, w + 8, h + 8);
                // Icono de advertencia en la esquina superior derecha
                g.setStroke(new BasicStroke(1));
                g.setFont(new Font("Monospaced", Font.BOLD, 10));
                g.setColor(new Color(255, 80, 80));
                g.drawString("!", x + w + 2, y + 4);
            }
        }
    }
    
    /**
     * Dibuja el jugador 2 en modo PvP con borde distintivo.
     *
     * @param g contexto grafico.
     */
    private void dibujarJugador2(Graphics2D g, Jugador jugador2) {
        if (jugador2 == null) return;
        int x = (int) jugador2.getPosicion().getX();
        int y = (int) jugador2.getPosicion().getY();
        int w = jugador2.getAncho();
        int h = jugador2.getAlto();

        Color colorPrincipal;
        Color colorBorde;
        switch (jugador2.getTipoPersonaje()) {
            case AZUL:
                colorPrincipal = new Color(50, 120, 230);
                colorBorde = new Color(30, 70, 160);
                break;
            case VERDE:
                colorPrincipal = new Color(50, 190, 70);
                colorBorde = new Color(30, 120, 40);
                break;
            default:
                colorPrincipal = new Color(220, 50, 50);
                colorBorde = new Color(160, 30, 30);
                break;
        }

        g.setColor(colorPrincipal);
        g.fillRect(x, y, w, h);
        // Borde amarillo para distinguir P2
        g.setColor(new Color(255, 200, 50));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, w, h);
        g.setStroke(new BasicStroke(1));

        // P2 label
        g.setFont(new Font("Monospaced", Font.BOLD, 9));
        g.setColor(Color.WHITE);
        g.drawString("P2", x + 4, y + h - 4);
    }
    
    /**
     * Dibuja las fuentes de vida activas del nivel.
     *
     * @param g           contexto grafico.
     * @param fuentesVida fuentes del nivel.
     */
    private void dibujarFuentesVida(Graphics2D g, List<FuenteVida> fuentesVida) {
        for (FuenteVida fuente : fuentesVida) {
            if (fuente.isActiva()) {
                int x = (int) fuente.getPosicion().getX();
                int y = (int) fuente.getPosicion().getY();

                //Cruz verde
                g.setColor(new Color(50, 220, 80));
                g.fillRect(x + 5, y, 6, 16);
                g.fillRect(x, y + 5, 16, 6);

                //Borde
                g.setColor(new Color(30, 160, 50));
                g.drawRect(x + 5, y, 6, 16);
                g.drawRect(x, y + 5, 16, 6);
            }
        }
    }
    
    /**
     * Dibuja las bombas activas del nivel
     * 
     * @param g
     * @param bombas
     */
    private void dibujarBombas(Graphics2D g, List<Bomba> bombas){
        for(Bomba bomba : bombas){
            if(bomba.isActiva()){
                int x = (int) bomba.getPosicion().getX();
                int y = (int) bomba.getPosicion().getY();
            
                //Bomba negra
                g.setColor(new Color(30, 30, 30));
                g.fillOval(x, y + 4, 14, 12);
            
                //Mecha
                g.setColor(new Color(150, 100, 50));
                g.setStroke(new BasicStroke(2));
                g.drawLine(x + 7, y + 4, x + 11, y);
                g.setStroke(new BasicStroke(1));
            
                //Chispa
                g.setColor(new Color(255, 200, 50));
                g.fillOval(x + 10, y - 2, 5, 5);
            }
        }
    }

    /**
     * Dibuja la barra superior con nivel, muertes, monedas y tiempo.
     *
     * @param g contexto grafico.
     */
    private void dibujarHUD(Graphics2D g) {
        //Barra superior oscura
        g.setColor(new Color(30, 30, 45));
        g.fillRect(0, 0, ANCHO, HUD_ALTO);

        //Linea inferior del HUD
        g.setColor(new Color(255, 200, 50));
        g.fillRect(0, HUD_ALTO - 2, ANCHO, 2);

        g.setFont(new Font("Monospaced", Font.BOLD, 15));

        //Nivel
        g.setColor(new Color(100, 200, 255));
        g.drawString("LEVEL " + (nivel != null ? nivel.getNumero() : ""), 20, 28);

        //Muertes
        if(modoPvP && nivel.getJugador2() != null){
            int muertesTotales = nivel.getJugador().getMuertes() + nivel.getJugador2().getMuertes();
            g.setColor(new Color(255, 100, 100));
            g.drawString("DEATHS: " + muertesTotales, 180, 28);
        } else {
            g.setColor(new Color(255, 100, 100));
            g.drawString("DEATHS: " + muertes, 180, 28);
        }

        //Monedas
        if (nivel != null) {
            int total = nivel.getMonedas().size();
            int recogidas;
            if(modoPvP && nivel.getJugador2() != null){
                recogidas = nivel.getJugador().getMonedasRecogidas() + nivel.getJugador2().getMonedasRecogidas();
            } else {
                recogidas = nivel.getJugador().getMonedasRecogidas();
            }
            g.setColor(new Color(255, 215, 0));
            g.drawString("COINS: " + recogidas + "/" + total, 380, 28);
        }

        // Tiempo
        int min = tiempoRestante / 60;
        int seg = tiempoRestante % 60;
        g.setColor(Color.WHITE);
        g.drawString(String.format("TIME: %02d:%02d", min, seg), 600, 28);
    }

    /**
     * Dibuja un mensaje central superpuesto sobre el juego indicando la victoria
     * del nivel.
     *
     * @param g contexto grafico.
     */
    private void dibujarVictoria(Graphics2D g) {
        // Overlay
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, ANCHO, ALTO);

        // Caja central
        int boxW = 500;
        int boxH = 280;
        int boxX = (ANCHO - boxW) / 2;
        int boxY = (ALTO - boxH) / 2;

        g.setColor(new Color(20, 60, 20));
        g.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        g.setColor(new Color(50, 200, 80));
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(boxY, boxY, boxW, boxH, 20, 20);
        g.setStroke(new BasicStroke(1));

        // Title
        g.setFont(new Font("Monospaced", Font.BOLD, 36));
        g.setColor(new Color(50, 220, 80));
        String titulo = "YOU WIN!";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(titulo, (ANCHO - fm.stringWidth(titulo)) / 2, boxY + 70);

        // Subtitle level
        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
        g.setColor(Color.WHITE);
        String subtitle = "LEVEL COMPLETED!";
        fm = g.getFontMetrics();
        g.drawString(subtitle, (ANCHO - fm.stringWidth(subtitle)) / 2, boxY + 105);

        // Botones
        dibujarBotonOverlay(g, boxX + 20, boxY + 180, 140, 44, "↺ RETRY [R]", new Color(60, 60, 180));
        dibujarBotonOverlay(g, boxX + 180, boxY + 180, 140, 44, "▶ NEXT LEVEL [N]", new Color(50, 160, 50));
        dibujarBotonOverlay(g, boxX + 340, boxY + 180, 140, 44, "⌂ MENU [M]", new Color(160, 60, 60));
    }
    
    /**
     * Dibuja el overlay de victoria en modo PvP mostrando el ganador.
     *
     * @param g contexto grafico.
     */
    private void dibujarVictoriaPvP(Graphics2D g) {
        //Overlay oscuro
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, ANCHO, ALTO);

        //Caja central
        int boxW = 500, boxH = 280;
        int boxX = (ANCHO - boxW) / 2;
        int boxY = (ALTO - boxH) / 2;

        g.setColor(new Color(20, 20, 60));
        g.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        g.setColor(new Color(100, 100, 255));
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        g.setStroke(new BasicStroke(1));

        //Titulo
        g.setFont(new Font("Monospaced", Font.BOLD, 36));
        g.setColor(new Color(150, 150, 255));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(mensajeVictoriaPvP,
                (ANCHO - fm.stringWidth(mensajeVictoriaPvP)) / 2, boxY + 80);

        //Botones
        dibujarBotonOverlay(g, boxX + 20,  boxY + 160, 140, 44,
                            "PLAY AGAIN [R]",  new Color(60, 60, 180));
        dibujarBotonOverlay(g, boxX + 180, boxY + 160, 140, 44,
                            "SEL. LEVEL [L]",  new Color(50, 160, 50));
        dibujarBotonOverlay(g, boxX + 340, boxY + 160, 140, 44,
                            "MENU [M]",        new Color(160, 60, 60));
    }

    /**
     * Muestra el mensaje indicando que el timepo se acabó, mensaje de derrota
     * 
     * @param g
     */
    private void dibujarDerrota(Graphics2D g) {
        // Overlay
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, ANCHO, ALTO);

        // Caja central
        int boxW = 500;
        int boxH = 250;
        int boxX = (ANCHO - boxW) / 2;
        int boxY = (ALTO - boxH) / 2;

        g.setColor(new Color(60, 20, 20));
        g.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        g.setColor(new Color(200, 50, 50));
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        g.setStroke(new BasicStroke(1));

        // Title
        g.setFont(new Font("Monospaced", Font.BOLD, 36));
        g.setColor(new Color(220, 50, 50));
        String titulo = "YOU LOST!";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(titulo, (ANCHO - fm.stringWidth(titulo)) / 2, boxY + 70);

        // Subtitulo
        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
        g.setColor(Color.WHITE);
        String sub = "Time's up! Better luck next time.";
        fm = g.getFontMetrics();
        g.drawString(sub, (ANCHO - fm.stringWidth(sub)) / 2, boxY + 110);

        // Botones
        dibujarBotonOverlay(g, boxX + 80, boxY + 160, 140, 44,
                "↺ RETRY [R]", new Color(60, 60, 180));
        dibujarBotonOverlay(g, boxX + 280, boxY + 160, 140, 44,
                "⌂ MENU [M]", new Color(160, 60, 60));
    }

    /**
     * muestra la pantalla de pausa del juego
     * 
     * @param g
     */
    private void dibujarPausa(Graphics2D g) {
        // Overlay
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, ANCHO, ALTO);

        // Caja
        int boxW = 500;
        int boxH = 250;
        int boxX = (ANCHO - boxW) / 2;
        int boxY = (ALTO - boxH) / 2;

        g.setColor(new Color(20, 20, 60));
        g.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        g.setColor(new Color(100, 100, 255));
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);
        g.setStroke(new BasicStroke(1));

        // Titulo
        g.setFont(new Font("Monospaced", Font.BOLD, 36));
        g.setColor(new Color(150, 150, 255));
        String titulo = "⏸️ PAUSED";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(titulo, (ANCHO - fm.stringWidth(titulo)) / 2, boxY + 70);

        // Subtitulo
        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
        g.setColor(Color.WHITE);
        String sub = "Game is paused";
        fm = g.getFontMetrics();
        g.drawString(sub, (ANCHO - fm.stringWidth(sub)) / 2, boxY + 110);

        // Botones
        dibujarBotonOverlay(g, boxX + 50, boxY + 170, 160, 44,
                "▶ RESUME [esc]", new Color(60, 60, 180));
        dibujarBotonOverlay(g, boxX + 290, boxY + 170, 160, 44,
                "SAVE [S]", new Color(50, 160, 50));
    }

    /**
     * Dibuja los botones de Retry level, Next Level y Menu
     * 
     * 
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     * @param texto
     * @param color
     */
    private void dibujarBotonOverlay(Graphics2D g, int x, int y, int w, int h, String texto, Color color) {
        g.setColor(color);
        g.fillRoundRect(x, y, w, h, 10, 10);
        g.setColor(color.brighter());
        g.drawRoundRect(x, y, w, h, 10, 10);
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(texto, x + (w - fm.stringWidth(texto)) / 2, y + 28);
    }

    /**
     * Dibuja las paredes del nivel donde rebotan los enemigos azules
     * 
     * @param g
     * @param paredes
     */
    private void dibujarParedes(Graphics2D g, List<Wall> paredes) {
        for (Wall pared : paredes) {
            int x = (int) pared.getPosicion().getX();
            int y = (int) pared.getPosicion().getY();

            // Relleno gris oscuro como en el juego original
            g.setColor(new Color(100, 100, 120));
            g.fillRect(x, y, pared.getAncho(), pared.getAlto());

        }
    }
    
    /**
     * Dibuja el mensaje de confirmacion de la partida guardada
     * 
     * @param g
     */
    private void dibujarMensajeGuardado(Graphics2D g) {
        g.setColor(new Color(50, 160, 50, 200));
        g.fillRoundRect(250, 10, 300, 35, 10, 10);
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.setColor(Color.WHITE);
        g.drawString("Game saved!", 340, 33);
        mensajeGuardado = false;
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
     * Oculta el mensaje central en pantalla.
     */
    public void ocultarOverlay() {
        this.mostrandoVictoria = false;
        this.mostrandoDerrota = false;
        this.mostrandoPausa = false;
        this.mostrandoVictoriaPvP = false;
    }

    /**
     * Establece el estado de visctoria en true
     */
    public void mostrarVictoria() {
        this.mostrandoVictoria = true;
        this.mostrandoDerrota = false;
    }

    /**
     * Establece el estado de derrota en true
     */
    public void mostrarDerrota() {
        this.mostrandoDerrota = true;
        this.mostrandoVictoria = false;
    }

    public void mostrarPausa() {
        this.mostrandoPausa = true;
    }
    
    /**
     * Activa o desactiva el modo PvP en la vista.
     *
     * @param modoPvP true para activar PvP.
     */
    public void setModoPvP(boolean modoPvP) {
        this.modoPvP = modoPvP;
    }

    /**
     * Muestra el overlay de victoria en modo PvP indicando el ganador.
     *
     * @param ganador numero del jugador ganador (1 o 2).
     */
    public void mostrarVictoriaPvP(int ganador) {
        this.mensajeVictoriaPvP = "PLAYER " + ganador + " WINS!";
        this.mostrandoVictoriaPvP = true;
        this.mostrandoVictoria = false;
        this.mostrandoDerrota = false;
    }
    
    /**
     * Muestra un mensaje temporal de partida guardada.
     */
    public void mostrarMensajeGuardado() {
        this.mensajeGuardado = true;
    }
}