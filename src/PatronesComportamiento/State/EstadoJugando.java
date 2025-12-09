package PatronesComportamiento.State;

import PatronesComportamiento.State.EstadoJuegoBase;
import PatronesComportamiento.State.EstadoJuegoEnum;
import PatronesCreacionales.Singleton.EstadoJuego;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import PatronesComportamiento.State.GestorEstados;
import PatronesEstructurales.Facade.Juego;

/**
 * Estado JUGANDO - El juego está activo
 * 
INTEGRANTES
     *  Harol Raul Neciosup Fuentes
     *  Edwin Alexander Rojas Castro
     *  Diego Luis Alonso Mendoza Vargas
     *  Anthony Seclén Santisteban
     *  Jorge Edersson Chiroque Diaz
 */
public class EstadoJugando extends EstadoJuegoBase {
    
    private boolean primeraVez = true;
    
    public EstadoJugando(GestorEstados gestorEstados, Juego juego) {
        super(gestorEstados, juego);
    }
    
    @Override
    public void entrar() {
        System.out.println("[ESTADO] Entrando a JUGANDO");
        
        if (primeraVez) { 
            if (juego.getGestorNiveles() != null) {
                
                Entidades.Jugador player = juego.getHandler().getPlayer();
                if (player != null) {
                    player.cambiarEstadoVida(new PatronesComportamiento.State.EstadoVidaJugador.Respawneando(player)); 
                    System.out.println("[ESTADO] Jugador forzado al estado RESPAWNEANDO para un reinicio limpio.");
                } else {
                    System.err.println("[ERROR] Jugador es NULL al intentar reiniciar el estado.");
                }
                //  Llama al método para resetear estadísticas y cargar el Nivel 1
                juego.getGestorNiveles().reiniciar(); 
                
                System.out.println("[ESTADO] Partida completamente reiniciada (Nivel 1 y stats)");
            }
            primeraVez = false;
        }
    }
    
    @Override
    public void tick() {
        // Verificar si el jugador perdió todas las vidas
        if (EstadoJuego.getInstance().getVidas() <= 0) {
            System.out.println("[ESTADO] ¡GAME OVER! Sin vidas restantes");
            gestorEstados.cambiarEstado(EstadoJuegoEnum.GAME_OVER);
            primeraVez = true; // Reset para próxima partida
        }
    }
    
    @Override
    public void render(Graphics g) {
    }
    
    @Override
    public void salir() {
        System.out.println("[ESTADO] Saliendo de JUGANDO");
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        // Solo capturar ESC para volver al menú
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("[ESTADO] Volviendo al menú principal...");
            primeraVez = true; // Reset para próxima partida
            gestorEstados.cambiarEstado(EstadoJuegoEnum.MENU_PRINCIPAL);
        }
        if (e.getKeyCode() == KeyEvent.VK_F1) {
        System.out.println("[DEBUG] Forzando victoria...");
        gestorEstados.cambiarEstado(EstadoJuegoEnum.VICTORIA);
        return;
    }
    
    // ESC para volver al menú
    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        System.out.println("[ESTADO] Volviendo al menú principal...");
        primeraVez = true;
        gestorEstados.cambiarEstado(EstadoJuegoEnum.MENU_PRINCIPAL);
    }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // Manejado por Teclas.java
    }
}