package Test;

import Domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para EnemigoLineal y EnemigoAcelerado.
 * @author MurilloRubiano
 */
public class EnemigoTest {

    private EnemigoLineal enemigoLineal;
    private EnemigoAcelerador enemigoAcelerado;

    @BeforeEach
    public void setUp() {
        enemigoLineal = new EnemigoLineal(
                new Posicion(100, 100), 14, 14, 2.0,
                new Posicion(50, 100), new Posicion(300, 100), true);
        enemigoAcelerado = new EnemigoAcelerador(
                new Posicion(100, 100), 14, 14, 2.0,
                new Posicion(50, 100), new Posicion(300, 100), true);
    }

    //ENEMIGO LINEAL

    @Test
    public void testEnemigoLinealPosicionInicial() {
        assertEquals(100, (int) enemigoLineal.getPosicion().getX());
        assertEquals(100, (int) enemigoLineal.getPosicion().getY());
    }

    @Test
    public void testEnemigoLinealSeMueve() {
        double posInicial = enemigoLineal.getPosicion().getX();
        enemigoLineal.actualizar();
        assertNotEquals(posInicial, enemigoLineal.getPosicion().getX());
    }

    @Test
    public void testEnemigoLinealRebota() {
        //Mover hasta el limite derecho
        for (int i = 0; i < 200; i++) {
            enemigoLineal.actualizar();
        }
        //Debe haber rebotado y estar dentro de los limites
        assertTrue(enemigoLineal.getPosicion().getX() >= 50);
        assertTrue(enemigoLineal.getPosicion().getX() <= 300);
    }

    @Test
    public void testEnemigoLinealDimensions() {
        assertEquals(14, enemigoLineal.getAncho());
        assertEquals(14, enemigoLineal.getAlto());
    }

    //ENEMIGO ACELERADOR

    @Test
    public void testEnemigoAceleradoEsMasRapido() {
        double posInicialLineal = enemigoLineal.getPosicion().getX();
        double posInicialAcelerado = enemigoAcelerado.getPosicion().getX();

        enemigoLineal.actualizar();
        enemigoAcelerado.actualizar();

        double distLineal = Math.abs(enemigoLineal.getPosicion().getX() - posInicialLineal);
        double distAcelerado = Math.abs(enemigoAcelerado.getPosicion().getX() - posInicialAcelerado);

        assertTrue(distAcelerado > distLineal);
    }

    @Test
    public void testEnemigoAceleradoVelocidadDoble() {
        double posInicialLineal = enemigoLineal.getPosicion().getX();
        double posInicialAcelerado = enemigoAcelerado.getPosicion().getX();

        enemigoLineal.actualizar();
        enemigoAcelerado.actualizar();

        double distLineal = Math.abs(enemigoLineal.getPosicion().getX() - posInicialLineal);
        double distAcelerado = Math.abs(enemigoAcelerado.getPosicion().getX() - posInicialAcelerado);

        assertEquals(distLineal * 2, distAcelerado, 0.01);
    }

    @Test
    public void testEnemigoAceleradoRebota() {
        for (int i = 0; i < 200; i++) {
            enemigoAcelerado.actualizar();
        }
        assertTrue(enemigoAcelerado.getPosicion().getX() >= 50);
        assertTrue(enemigoAcelerado.getPosicion().getX() <= 300);
    }

    @Test
    public void testEnemigoAceleradoEsInstanciaDeLineal() {
        assertTrue(enemigoAcelerado instanceof EnemigoLineal);
    }
}
