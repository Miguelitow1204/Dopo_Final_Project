package Test;

import Domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para Bomba.
 * @author MurilloRubiano
 */
public class BombaTest {

    private Bomba bomba;
    private Jugador jugador;

    @BeforeEach
    public void setUp() {
        bomba = new Bomba(new Posicion(100, 100));
        jugador = new Jugador(new Posicion(100, 100), "Player", TipoPersonaje.ROJO);
    }

    @Test
    public void testBombaInicialActiva() {
        assertTrue(bomba.isActiva());
        assertFalse(bomba.isExplotada());
    }

    @Test
    public void testBombaExplotaAlActivarse() {
        bomba.explotar();
        assertTrue(bomba.isExplotada());
        assertFalse(bomba.isActiva());
    }

    @Test
    public void testBombaNoExplotaDosVeces() {
        bomba.explotar();
        bomba.explotar();
        assertTrue(bomba.isExplotada());
    }

    @Test
    public void testBombaReiniciaCorrectamente() {
        bomba.explotar();
        bomba.reiniciar();
        assertFalse(bomba.isExplotada());
        assertTrue(bomba.isActiva());
    }

    @Test
    public void testBombaColisionaConJugador() {
        assertTrue(bomba.colisionaCon(jugador));
    }

    @Test
    public void testBombaNoColisionaLejos() {
        Jugador jugadorLejos = new Jugador(new Posicion(400, 400), "P", TipoPersonaje.ROJO);
        assertFalse(bomba.colisionaCon(jugadorLejos));
    }
}
