package presentation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Gestiona teclas presionadas para consulta continua durante el game loop.
 * Autores: MurilloRubiano con apoyo de Claude Opus 4.6.
 */
public class ControladorTeclado implements KeyListener {

    private Set<Integer> teclasPresionadas;

    /**
     * Inicializa el conjunto de teclas actualmente presionadas.
     */
    public ControladorTeclado() {
        this.teclasPresionadas = new HashSet<>();
    }

    /**
     * Registra una tecla cuando pasa a estado presionado.
     *
     * @param e evento de teclado recibido.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        teclasPresionadas.add(e.getKeyCode());
    }

    /**
     * Elimina una tecla del estado presionado al liberarse.
     *
     * @param e evento de teclado recibido.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        teclasPresionadas.remove(e.getKeyCode());
    }

    /**
     * Metodo no utilizado porque solo se trabaja con presion/liberacion.
     *
     * @param e evento de teclado.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // No se usa
    }

    /**
     * Consulta si una tecla sigue presionada en este frame.
     *
     * @param codigoTecla codigo KeyEvent.VK_* a evaluar.
     * @return true si la tecla permanece activa.
     */
    public boolean isTeclaPresionada(int codigoTecla) {
        return teclasPresionadas.contains(codigoTecla);
    }

    /**
     * Expone el conjunto actual de teclas presionadas.
     *
     * @return conjunto de codigos de tecla activos.
     */
    public Set<Integer> getTeclasPresionadas() {
        return teclasPresionadas;
    }
    
    /**
     * Limpia todas las teclas presionadas actualmente.
     */
    public void limpiarTeclas() {
        teclasPresionadas.clear();
    }
}
