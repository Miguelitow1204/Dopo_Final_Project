package Domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for class Jugador
 * @author MurilloRubiano
 */
public class JugadorTest {

    private Jugador jugador;

    @BeforeEach
    public void setUp() {
        jugador = new Jugador(new Posicion(100, 100), "Player", TipoPersonaje.ROJO);
    }

    @Test
    public void testPosicionInicial() {
        assertEquals(100, (int) jugador.getPosicion().getX());
        assertEquals(100, (int) jugador.getPosicion().getY());
    }

    @Test
    public void testMuerteInicialEnCero() {
        assertEquals(0, jugador.getMuertes());
    }

    @Test
    public void testMonedasInicialesEnCero() {
        assertEquals(0, jugador.getMonedasRecogidas());
    }

    @Test
    public void testPerderVidaIncrementaMuertes() {
        jugador.perderVida();
        assertEquals(1, jugador.getMuertes());
    }

    @Test
    public void testPerderVidaReiniciaMonedas() {
        Moneda moneda = new Moneda(new Posicion(100, 100));
        jugador.recogerMoneda(moneda);
        jugador.perderVida();
        assertEquals(0, jugador.getMonedasRecogidas());
    }

    @Test
    public void testPerderVidaReiniciaPosicion() {
        jugador.mover(1, 0);
        jugador.perderVida();
        assertEquals(100, (int) jugador.getPosicion().getX());
        assertEquals(100, (int) jugador.getPosicion().getY());
    }

    @Test
    public void testRecogerMoneda() {
        Moneda moneda = new Moneda(new Posicion(100, 100));
        jugador.recogerMoneda(moneda);
        assertEquals(1, jugador.getMonedasRecogidas());
        assertTrue(moneda.isRecogida());
    }

    @Test
    public void testHaRecogidoTodas() {
        Moneda m1 = new Moneda(new Posicion(100, 100));
        Moneda m2 = new Moneda(new Posicion(200, 200));
        jugador.recogerMoneda(m1);
        jugador.recogerMoneda(m2);
        assertTrue(jugador.haRecogidoTodas(2));
        assertFalse(jugador.haRecogidoTodas(3));
    }
}
