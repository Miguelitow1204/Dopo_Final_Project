package Domain;

import java.io.*;

/**
 * Lee un archivo .txt de configuracion y construye un Nivel
 * 
 * @author Murillo-Rubiano
 * @version 1.2
 */
public class LectorNivel {
    public static Nivel cargar(String ruta, TipoPersonaje tipo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(ruta));
        Nivel nivel = null;
        Jugador jugador = null;
        String linea;

        while ((linea = reader.readLine()) != null) {
            linea = linea.trim();
            if (linea.isEmpty() || linea.startsWith("#")) {
                continue;
            }

            String[] partes = linea.split("=", 2);
            if (partes.length < 2)
                continue;

            String key = partes[0].trim();
            String value = partes[1].trim();

            if (key.equals("NUMERO")) {
                int numero = Integer.parseInt(value);
                nivel = new Nivel(numero, 60);

            } else if (key.equals("TIEMPO")) {
                if (nivel != null)
                    nivel = new Nivel(nivel.getNumero(), Integer.parseInt(value));

            } else if (key.equals("JUGADOR")) {
                String[] coords = value.split(",");
                double x = Double.parseDouble(coords[0]);
                double y = Double.parseDouble(coords[1]);
                jugador = new Jugador(new Posicion(x, y), "Player", tipo);
                if (nivel != null)
                    nivel.setJugador(jugador);

            } else if (key.equals("ZONA_INICIO")) {
                String[] c = value.split(",");
                nivel.setZonaInicio(new ZonaSegura(
                        new Posicion(Double.parseDouble(c[0]), Double.parseDouble(c[1])),
                        Integer.parseInt(c[2]), Integer.parseInt(c[3]), TipoZona.INICIO));
                        
            } else if (key.equals("ZONA_INTERMEDIA")) {
                String[] c = value.split(",");
                nivel.setZonaIntermedia(new ZonaSegura(
                        new Posicion(Double.parseDouble(c[0]), Double.parseDouble(c[1])),
                        Integer.parseInt(c[2]), Integer.parseInt(c[3]), TipoZona.INTERMEDIA));
                        
            } else if (key.equals("ZONA_META")) {
                String[] c = value.split(",");
                nivel.setZonaMeta(new ZonaSegura(
                        new Posicion(Double.parseDouble(c[0]), Double.parseDouble(c[1])),
                        Integer.parseInt(c[2]), Integer.parseInt(c[3]), TipoZona.META));

            } else if (key.equals("PARED")) {
                String[] c = value.split(",");
                nivel.agregarPared(new Wall(
                        new Posicion(Double.parseDouble(c[0]), Double.parseDouble(c[1])),
                        Integer.parseInt(c[2]), Integer.parseInt(c[3])));

            } else if (key.equals("MONEDA")) {
                String[] c = value.split(",");
                nivel.agregarMoneda(new Moneda(
                        new Posicion(Double.parseDouble(c[0]), Double.parseDouble(c[1]))));

            } else if (key.equals("ENEMIGO_LINEAL")) {
                String[] c = value.split(",");
                double x = Double.parseDouble(c[0]);
                double y = Double.parseDouble(c[1]);
                double vel = Double.parseDouble(c[2]);
                boolean horizontal = c[3].trim().equalsIgnoreCase("H");
                Posicion puntoA = new Posicion(
                        Double.parseDouble(c[4]), Double.parseDouble(c[5]));
                Posicion puntoB = new Posicion(
                        Double.parseDouble(c[6]), Double.parseDouble(c[7]));
                nivel.agregarEnemigo(new EnemigoLineal(
                        new Posicion(x, y), 14, 14, vel, puntoA, puntoB, horizontal));
            } else if (key.equals("ENEMIGO_CIRCULAR")) {
                String[] c = value.split(",");
                double cx = Double.parseDouble(c[0]);
                double cy = Double.parseDouble(c[1]);
                double radio = Double.parseDouble(c[2]);
                double velAngular = Double.parseDouble(c[3]);
                nivel.agregarEnemigo(new EnemigoPatrullero(
                        new Posicion(cx, cy), radio, velAngular));
            } else if (key.equals("ENEMIGO_ACELERADO")){
                String[] c = value.split(",");
                double x = Double.parseDouble(c[0]);
                double y = Double.parseDouble(c[1]);
                double vel = Double.parseDouble(c[2]);
                boolean horizontal = c[3].trim().equalsIgnoreCase("H");
                Posicion puntoA = new Posicion(
                        Double.parseDouble(c[4]), Double.parseDouble(c[5]));
                Posicion puntoB = new Posicion(
                        Double.parseDouble(c[6]), Double.parseDouble(c[7]));
                nivel.agregarEnemigo(new EnemigoAcelerador(
                        new Posicion(x, y), 14, 14, vel, puntoA, puntoB, horizontal));
            } else if (key.equals("FUENTE_VIDA")) {
                String[] c = value.split(",");
                nivel.agregarFuenteVida(new FuenteVida(
                        new Posicion(Double.parseDouble(c[0]), 
                        Double.parseDouble(c[1]))));
            }
        }
        reader.close();
        return nivel;
    }
}