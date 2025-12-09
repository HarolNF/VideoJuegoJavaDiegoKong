/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PatronesComportamiento.Observer;

import Entidades.Jugador;

/**
INTEGRANTES
     *  Harol Raul Neciosup Fuentes
     *  Edwin Alexander Rojas Castro
     *  Diego Luis Alonso Mendoza Vargas
     *  Anthony Seclén Santisteban
     *  Jorge Edersson Chiroque Diaz
 * Evento: Jugador coleccionó item
 */
public class EventoJugadorRecogeObjeto extends EventoJuego {
    private final String tipoObjeto;
    private final int puntosGanados;
    
    public EventoJugadorRecogeObjeto(Jugador source, String tipoObjeto, int puntosGanados) {
        super(source);
        this.tipoObjeto = tipoObjeto;
        this.puntosGanados = puntosGanados;
    }
    
    public String getTipoObjeto() { return tipoObjeto; }
    public int getPuntosGanados() { return puntosGanados; }
}
