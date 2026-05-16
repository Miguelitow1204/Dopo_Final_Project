package Test;

import Domain.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for class Moneda
 * @author MurilloRubiano
 */
class MonedaTest {
	private Moneda moneda;

    @BeforeEach
    public void setUp() {
        moneda = new Moneda(new Posicion(100, 100));
    }

    @Test
    public void testMonedaInicialNoRecogida() {
        assertFalse(moneda.isRecogida());
        assertTrue(moneda.isActiva());
    }

    @Test
    public void testRecogerMoneda() {
        moneda.recoger();
        assertTrue(moneda.isRecogida());
        assertFalse(moneda.isActiva());
    }

    @Test
    public void testReiniciarMoneda() {
        moneda.recoger();
        moneda.reiniciar();
        assertFalse(moneda.isRecogida());
        assertTrue(moneda.isActiva());
    }

    @Test
    public void testMonedaTienePositions() {
        assertEquals(100, (int) moneda.getPosicion().getX());
        assertEquals(100, (int) moneda.getPosicion().getY());
    }
}
