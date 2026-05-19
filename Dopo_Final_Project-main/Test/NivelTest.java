package Test;
import Domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class Nivel
 * @author MurilloRubiano
 */
public class NivelTest {

    private Nivel nivel;
    private Jugador jugador;

    @BeforeEach
    public void setUp() {
        nivel = new Nivel(1, 60);
        jugador = new Jugador(new Posicion(50, 270), "Player", TipoPersonaje.ROJO);
        nivel.setJugador(jugador);
        nivel.setZonaInicio(new ZonaSegura(
                new Posicion(0, 150), 150, 270, TipoZona.INICIO));
        nivel.setZonaMeta(new ZonaSegura(
                new Posicion(650, 150), 150, 270, TipoZona.META));
        nivel.agregarMoneda(new Moneda(new Posicion(300, 200)));
        nivel.agregarMoneda(new Moneda(new Posicion(400, 200)));
    }

    @Test
    public void testNivelNoCompletadoSinMonedas() {
        assertFalse(nivel.estaCompleto());
    }

    @Test
    public void testNivelNoCompletadoConMonedasPeroSinLlegar() {
        jugador.recogerMoneda(nivel.getMonedas().get(0));
        jugador.recogerMoneda(nivel.getMonedas().get(1));
        assertFalse(nivel.estaCompleto());
    }

    @Test
    public void testNivelCompletadoConMonedasYEnMeta() {
        jugador.recogerMoneda(nivel.getMonedas().get(0));
        jugador.recogerMoneda(nivel.getMonedas().get(1));
        jugador.getPosicion().setX(700);
        jugador.getPosicion().setY(270);
        assertTrue(nivel.estaCompleto());
    }

    @Test
    public void testReiniciarNivelReseteoMonedas() {
        jugador.recogerMoneda(nivel.getMonedas().get(0));
        nivel.reiniciar();
        assertFalse(nivel.getMonedas().get(0).isRecogida());
        assertEquals(0, jugador.getMonedasRecogidas());
    }

    @Test
    public void testReiniciarNivelReiniciaPosicion() {
        jugador.mover(1, 0);
        nivel.reiniciar();
        assertEquals(50, (int) jugador.getPosicion().getX());
        assertEquals(270, (int) jugador.getPosicion().getY());
    }

    @Test
    public void testTiempoLimite() {
        assertEquals(60, nivel.getTiempoLimite());
    }
    
    //TESTS PVP
    @Test
    public void testNivelIniciaEnModoNormal() {
        assertFalse(nivel.isModoPvP());
    }

    @Test
    public void testActivarModoPvP() {
        nivel.setModoPvP(true);
        assertTrue(nivel.isModoPvP());
    }

    @Test
    public void testGanadorPvPNinguno() {
        Jugador jugador2 = new Jugador(new Posicion(700, 270), "P2", TipoPersonaje.AZUL);
        nivel.setJugador2(jugador2);
        nivel.setModoPvP(true);
        assertEquals(0, nivel.ganadorPvP());
    }

    @Test
    public void testGanadorPvPJugador1() {
        Jugador jugador2 = new Jugador(new Posicion(700, 270), "P2", TipoPersonaje.AZUL);
        nivel.setJugador2(jugador2);
        nivel.setModoPvP(true);

        // Ambos recogen todas las monedas entre los dos
        jugador.recogerMoneda(nivel.getMonedas().get(0));
        jugador2.recogerMoneda(nivel.getMonedas().get(1));

        // J1 llega a la meta
        jugador.getPosicion().setX(700);
        jugador.getPosicion().setY(270);

        assertEquals(1, nivel.ganadorPvP());
    }

    @Test
    public void testGanadorPvPJugador2() {
        Jugador jugador2 = new Jugador(new Posicion(700, 270), "P2", TipoPersonaje.AZUL);
        nivel.setJugador2(jugador2);
        nivel.setModoPvP(true);

        // Ambos recogen todas las monedas entre los dos
        jugador.recogerMoneda(nivel.getMonedas().get(0));
        jugador2.recogerMoneda(nivel.getMonedas().get(1));

        // J2 llega a la zona inicio (su meta)
        jugador2.getPosicion().setX(50);
        jugador2.getPosicion().setY(270);

        assertEquals(2, nivel.ganadorPvP());
    }
}
