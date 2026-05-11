package Domain;

import java.awt.Rectangle;

/**
 * Solid wall that blocks the player's path and limits enemy bounce-outs
 * @author (MurilloRubiano)
 * @version 1.0 
 */
public class Wall extends EntidadJuego implements Colisionable{
	/**
	 * Creates a wall in given position and dimension
	 * 
	 * @param posicion
	 * @param ancho
	 * @param alto
	 */
	public Wall(Posicion posicion, int ancho, int alto) {
		super(posicion, ancho, alto);
		this.activa = true;
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) posicion.getX(), (int) posicion.getY(), ancho, alto);
	}
	
	@Override
	public boolean colisionaCon(Colisionable otro) {
		return this.getBounds().intersects(otro.getBounds());
	}
	
	@Override
	public void actualizar() {
		//Wall are static
	}

}
