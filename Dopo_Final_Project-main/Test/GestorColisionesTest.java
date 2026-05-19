package Test;

import Domain.*;

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
        nivel.agregarEnemigo(enemigo);
        nivel.agregarMoneda(moneda);

        gestor.verificarColisionesNivel(nivel);
        assertEquals(0, jugador.getMonedasRecogidas());
    }
    
    //TESTS PVP

    @Test
    public void testJugadoresColisionan() {
        Jugador jugador2 = new Jugador(new Posicion(100, 100), "P2", TipoPersonaje.AZUL);
        assertTrue(gestor.jugadoresColisionan(jugador, jugador2));
    }

    @Test
    public void testJugadoresNoColisionan() {
        Jugador jugador2 = new Jugador(new Posicion(400, 400), "P2", TipoPersonaje.AZUL);
        assertFalse(gestor.jugadoresColisionan(jugador, jugador2));
    }

    @Test
    public void testColisionPvPJ1ConEnemigoReiniciaMonedas() {
        Nivel nivel = new Nivel(1, 60);
        nivel.setModoPvP(true);
        nivel.setJugador(jugador);
        Jugador jugador2 = new Jugador(new Posicion(400, 400), "P2", TipoPersonaje.AZUL);
        nivel.setJugador2(jugador2);
        nivel.setZonaInicio(new ZonaSegura(
                new Posicion(0, 0), 100, 100, TipoZona.INICIO));
        nivel.setZonaMeta(new ZonaSegura(
                new Posicion(500, 500), 100, 100, TipoZona.META));

        Moneda moneda = new Moneda(new Posicion(300, 300));
        jugador2.recogerMoneda(moneda);
        nivel.agregarMoneda(moneda);

        EnemigoLineal enemigo = new EnemigoLineal(
                new Posicion(100, 100), 14, 14, 2,
                new Posicion(50, 100), new Posicion(200, 100), true);
        nivel.agregarEnemigo(enemigo);

        gestor.verificarColisionesNivelPvP(nivel);

        assertEquals(0, jugador2.getMonedasRecogidas());
    }

    @Test
    public void testColisionEntrePvPAmbosReinician() {
        Nivel nivel = new Nivel(1, 60);
        nivel.setModoPvP(true);
        nivel.setJugador(jugador);
        Jugador jugador2 = new Jugador(new Posicion(100, 100), "P2", TipoPersonaje.AZUL);
        nivel.setJugador2(jugador2);
        nivel.setZonaInicio(new ZonaSegura(
                new Posicion(0, 0), 100, 100, TipoZona.INICIO));
        nivel.setZonaMeta(new ZonaSegura(
                new Posicion(500, 500), 100, 100, TipoZona.META));

        Moneda moneda1 = new Moneda(new Posicion(300, 300));
        Moneda moneda2 = new Moneda(new Posicion(350, 300));
        jugador.recogerMoneda(moneda1);
        jugador2.recogerMoneda(moneda2);
        nivel.agregarMoneda(moneda1);
        nivel.agregarMoneda(moneda2);

        gestor.verificarColisionesNivelPvP(nivel);

        assertEquals(0, jugador.getMonedasRecogidas());
        assertEquals(0, jugador2.getMonedasRecogidas());
    }
}
