package Domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * unit test for class GestorColisiones.
 * @author MurilloRubiano
 */
public class GestorColisionesTest {

    private GestorColisiones gestor;
    private Jugador jugador;

    @BeforeEach
    public void setUp() {
        gestor = new GestorColisiones();
        jugador = new Jugador(new Posicion(100, 100), "Player", TipoPersonaje.ROJO);
    }

    @Test
    public void testColisionJugadorEnemigo() {
        EnemigoLineal enemigo = new EnemigoLineal(
                new Posicion(100, 100), 14, 14, 2,
                new Posicion(50, 100), new Posicion(200, 100), true);
        assertTrue(gestor.jugadorTocaEnemigo(jugador, enemigo));
    }

    @Test
    public void testSinColisionJugadorEnemigo() {
        EnemigoLineal enemigo = new EnemigoLineal(
                new Posicion(400, 400), 14, 14, 2,
                new Posicion(300, 400), new Posicion(500, 400), true);
        assertFalse(gestor.jugadorTocaEnemigo(jugador, enemigo));
    }

    @Test
    public void testColisionJugadorMoneda() {
        Moneda moneda = new Moneda(new Posicion(100, 100));
        assertTrue(gestor.jugadorTocaMoneda(jugador, moneda));
    }

    @Test
    public void testSinColisionJugadorMoneda() {
        Moneda moneda = new Moneda(new Posicion(400, 400));
        assertFalse(gestor.jugadorTocaMoneda(jugador, moneda));
    }

    @Test
    public void testVerificarColisionesReiniciaMonedas() {
        Nivel nivel = new Nivel(1, 60);
        nivel.setJugador(jugador);
        nivel.setZonaInicio(new ZonaSegura(
                new Posicion(0, 0), 100, 100, TipoZona.INICIO));
        nivel.setZonaMeta(new ZonaSegura(
                new Posicion(500, 500), 100, 100, TipoZona.META));

        Moneda moneda = new Moneda(new Posicion(100, 100));
        jugador.recogerMoneda(moneda);
        assertEquals(1, jugador.getMonedasRecogidas());

        EnemigoLineal enemigo = new EnemigoLineal(
                new Posicion(100, 100), 14, 14, 2,
                new Posicion(50, 100), new Posicion(200, 100), true);
        nivel.getEnemigos().add(enemigo);
        nivel.getMonedas().add(moneda);

        gestor.verificarColisionesNivel(nivel);
        assertEquals(0, jugador.getMonedasRecogidas());
    }
}
