package Test;
import Domain.*;

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
    
    @Test
    public void testRojoVelocidadNormal() {
        assertEquals(3, jugador.getVelocidad());
    }

    @Test
    public void testRojoTamanioNormal() {
        assertEquals(20, jugador.getAncho());
        assertEquals(20, jugador.getAlto());
    }

    @Test
    public void testRojoMuereAlPrimerGolpe() {
        assertTrue(jugador.perderVida());
    }
    
    @Test
    public void testAzulVelocidadRapida() {
        Jugador azul = new Jugador(new Posicion(100, 100), "P", TipoPersonaje.AZUL);
        assertEquals(5, azul.getVelocidad());
    }

    @Test
    public void testAzulTamanioGrande() {
        Jugador azul = new Jugador(new Posicion(100, 100), "P", TipoPersonaje.AZUL);
        assertEquals(26, azul.getAncho());
        assertEquals(26, azul.getAlto());
    }

    @Test
    public void testAzulMuereAlPrimerGolpe() {
        Jugador azul = new Jugador(new Posicion(100, 100), "P", TipoPersonaje.AZUL);
        assertTrue(azul.perderVida());
    }
    
    @Test
    public void testVerdeIniciaConEscudo() {
        Jugador verde = new Jugador(new Posicion(100, 100), "P", TipoPersonaje.VERDE);
        assertTrue(verde.isEscudoActivo());
    }

    @Test
    public void testVerdePrimerGolpeNoMuere() {
        Jugador verde = new Jugador(new Posicion(100, 100), "P", TipoPersonaje.VERDE);
        assertFalse(verde.perderVida());
    }

    @Test
    public void testVerdePrimerGolpePierdeEscudo() {
        Jugador verde = new Jugador(new Posicion(100, 100), "P", TipoPersonaje.VERDE);
        verde.perderVida();
        assertFalse(verde.isEscudoActivo());
    }

    @Test
    public void testVerdePrimerGolpeReduceVelocidad() {
        Jugador verde = new Jugador(new Posicion(100, 100), "P", TipoPersonaje.VERDE);
        int velocidadOriginal = verde.getVelocidad();
        verde.perderVida();
        assertTrue(verde.getVelocidad() < velocidadOriginal);
    }

    @Test
    public void testVerdeSegundoGolpeMuere() {
        Jugador verde = new Jugador(new Posicion(100, 100), "P", TipoPersonaje.VERDE);
        verde.perderVida(); // primer golpe - pierde escudo
        // forzar fin de invulnerabilidad
        for (int i = 0; i < 100; i++) verde.actualizarInvulnerabilidad();
        assertTrue(verde.perderVida()); // segundo golpe - muere
    }

    @Test
    public void testVerdeRecuperaEscudoAlMorir() {
        Jugador verde = new Jugador(new Posicion(100, 100), "P", TipoPersonaje.VERDE);
        verde.perderVida();
        for (int i = 0; i < 100; i++) verde.actualizarInvulnerabilidad();
        verde.perderVida();
        assertTrue(verde.isEscudoActivo());
    }

    @Test
    public void testVerdeRecuperaVelocidadAlMorir() {
        Jugador verde = new Jugador(new Posicion(100, 100), "P", TipoPersonaje.VERDE);
        int velocidadOriginal = verde.getVelocidad();
        verde.perderVida();
        for (int i = 0; i < 100; i++) verde.actualizarInvulnerabilidad();
        verde.perderVida();
        assertEquals(velocidadOriginal, verde.getVelocidad());
    }
}
