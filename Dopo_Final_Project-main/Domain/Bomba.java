package Domain;

import java.awt.Rectangle;
import java.io.Serializable;

/**
 * Elemento especial estático que destruye cualquier elemento que pase por ella.
 * Desaparece despues de explotar
 * 
 * @author MurilloRubiano
 * @version 1.5
 */
public class Bomba extends EntidadJuego implements Colisionable {
    
    private static final long serialVersionUID = 1L;
    private boolean explotada;
    
    /**
     * Crea una bomba en la posición indicada
     * 
     * @param posicion ubicacion de la bomba
     */
    public Bomba(Posicion posicion){
        super(posicion, 16, 16);
        this.explotada = false;
        this.activa = true;
    }
    
    /**
     * Detona la bomba, destruyendo al elemento que la tocó.
     * La bomba desaparece despues de explotar.
     */
    public void explotar(){
        if(!explotada){
            explotada = true;
            activa = false;
        }
    }
    
    @Override
    public java.awt.Rectangle getBounds() {
        return new java.awt.Rectangle((int) posicion.getX(), (int) posicion.getY(), ancho, alto);
    }
    
    /**
     * Indica si la bomba ya exloto
     * 
     * @return true si ya no esta activa
     */
    public boolean isExplotada(){
        return explotada;
    }
    
    /**
     * Reinicia la bomba a su estado inicial.
     */
    public void reiniciar() {
        explotada = false;
        activa = true;
    }
    
    @Override
    public boolean colisionaCon(Colisionable otro){
        return this.getBounds().intersects(otro.getBounds());
    }
    
    @Override
    public void actualizar(){
        //Estatica
    }
}