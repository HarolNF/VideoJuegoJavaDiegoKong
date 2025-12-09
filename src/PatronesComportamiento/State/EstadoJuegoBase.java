package PatronesComportamiento.State;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import PatronesEstructurales.Facade.Juego;

/**
 * Clase base para todos los estados del juego
 * Patrón STATE - Define interfaz común
 * 
 * INTEGRANTES
     *  Harol Raul Neciosup Fuentes
     *  Edwin Alexander Rojas Castro
     *  Diego Luis Alonso Mendoza Vargas
     *  Anthony Seclén Santisteban
     *  Jorge Edersson Chiroque Diaz
 */
public abstract class EstadoJuegoBase {
    
    protected GestorEstados gestorEstados;
    protected Juego juego;
    
    /**
     * Constructor
     */
    public EstadoJuegoBase(GestorEstados gestorEstados, Juego juego) {
        this.gestorEstados = gestorEstados;
        this.juego = juego;
    }
    
    /**
     * Llamado al entrar al estado
     */
    public abstract void entrar();
    
    /**
     * Actualización lógica del estado
     */
    public abstract void tick();
    
    /**
     * Renderizado del estado
     */
    public abstract void render(Graphics g);
    
    /**
     * Llamado al salir del estado
     */
    public abstract void salir();
    
    /**
     * Manejo de eventos de teclado
     */
    public abstract void keyPressed(KeyEvent e);
    public abstract void keyReleased(KeyEvent e);
}