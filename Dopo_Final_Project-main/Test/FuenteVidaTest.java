package Test;

import Domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para FuenteVida.
 * @author MurilloRubiano
 */
public class FuenteVidaTest {

    private FuenteVida fuente;
    private Jugador jugador;

    @BeforeEach
    public void setUp() {
        fuente = new FuenteVida(new Posicion(100, 100));
        jugador = new Jugador(new Posicion(100, 100), "Player", TipoPersonaje.ROJO);
    }

    @Test
    public void testFuenteInicialActiva() {
        assertTrue(fuente.isActiva());
        assertFalse(fuente.isUsada());
    }

    @Test
    public void testActivarFuenteDesaparece() {
        fuente.activar(jugador);
        assertFalse(fuente.isActiva());
        assertTrue(fuente.isUsada());
    }

    @Test
    public void testFuenteNoSeActivaDosVeces() {
        fuente.activar(jugador);
        fuente.activar(jugador);
        assertTrue(fuente.isUsada());
    }

    @Test
    public void testFuenteVerdeRestaurasEscudo() {
        Jugador verde = new Jugador(new Posicion(100, 100), "P", TipoPersonaje.VERDE);
        verde.perderVida(); // pierde escudo
        assertFalse(verde.isEscudoActivo());
        fuente.activar(verde);
        assertTrue(verde.isEscudoActivo());
    }
}
