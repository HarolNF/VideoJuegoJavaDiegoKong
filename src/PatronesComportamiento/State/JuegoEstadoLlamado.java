/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PatronesComportamiento.State;

import PatronesComportamiento.Observer.EventoJugadorDañado;
import PatronesComportamiento.Observer.EventoJugadorRecogeObjeto;
import PatronesEstructurales.Adapter.AdaptadorEventosJugador;
import PatronesCreacionales.Singleton.EstadoJuego;

/**
 *
INTEGRANTES
     *  Harol Raul Neciosup Fuentes
     *  Edwin Alexander Rojas Castro
     *  Diego Luis Alonso Mendoza Vargas
     *  Anthony Seclén Santisteban
     *  Jorge Edersson Chiroque Diaz
 */
public class JuegoEstadoLlamado extends AdaptadorEventosJugador {
    private final EstadoJuego estadoJuego;
    
    public JuegoEstadoLlamado(EstadoJuego estadoJuego) {
        this.estadoJuego = estadoJuego;
    }
    
    @Override
    public void onJugadordanado(EventoJugadorDañado event) {
        System.out.println("[GAME STATE] Jugador recibió daño");
        estadoJuego.perderVida();
        estadoJuego.resetearRacha();
    }
    
    @Override
    public void onJugadorRecogeObjeto(EventoJugadorRecogeObjeto event) {
        System.out.println("[GAME STATE] Jugador colectó: " + event.getTipoObjeto());
        estadoJuego.sumarPuntos(event.getPuntosGanados());
    }
}
