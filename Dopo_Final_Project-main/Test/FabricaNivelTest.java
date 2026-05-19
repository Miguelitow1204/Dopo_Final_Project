package Test;
import Domain.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase FabricaNivel.
 * @author MurilloRubiano
 */
public class FabricaNivelTest {

    @Test
    public void testCargarNivel1Exitoso() {
        Nivel nivel = FabricaNivel.cargar(1, TipoPersonaje.ROJO);
        assertNotNull(nivel);
    }

    @Test
    public void testNivel1TieneNumeroCorreecto() {
        Nivel nivel = FabricaNivel.cargar(1, TipoPersonaje.ROJO);
        assertEquals(1, nivel.getNumero());
    }

    @Test
    public void testNivel1TieneTiempoLimite() {
        Nivel nivel = FabricaNivel.cargar(1, TipoPersonaje.ROJO);
        assertTrue(nivel.getTiempoLimite() > 0);
    }

    @Test
    public void testNivel1TieneMonedas() {
        Nivel nivel = FabricaNivel.cargar(1, TipoPersonaje.ROJO);
        assertFalse(nivel.getMonedas().isEmpty());
    }

    @Test
    public void testNivel1TieneEnemigos() {
        Nivel nivel = FabricaNivel.cargar(1, TipoPersonaje.ROJO);
        assertFalse(nivel.getEnemigos().isEmpty());
    }

    @Test
    public void testNivel1TieneZonaInicio() {
        Nivel nivel = FabricaNivel.cargar(1, TipoPersonaje.ROJO);
        assertNotNull(nivel.getZonaInicio());
    }

    @Test
    public void testNivel1TieneZonaMeta() {
        Nivel nivel = FabricaNivel.cargar(1, TipoPersonaje.ROJO);
        assertNotNull(nivel.getZonaMeta());
    }

    @Test
    public void testNivel1TieneJugador() {
        Nivel nivel = FabricaNivel.cargar(1, TipoPersonaje.ROJO);
        assertNotNull(nivel.getJugador());
    }

    @Test
    public void testNivelInexistenteLanzaExcepcion() {
        assertThrows(NivelNoEncontradoException.class, () -> {
            FabricaNivel.cargar(99, TipoPersonaje.ROJO);
        });
    }

    @Test
    public void testCargarTodosLosNiveles() {
        java.util.List<Nivel> niveles = FabricaNivel.cargarTodos(TipoPersonaje.ROJO, 3);
        assertEquals(3, niveles.size());
    }
}
