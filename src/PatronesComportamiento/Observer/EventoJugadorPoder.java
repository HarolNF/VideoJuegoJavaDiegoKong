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
 */
public class EventoJugadorPoder extends EventoJuego {
    private final String powerUpType;
    
    public EventoJugadorPoder(Jugador source, String powerUpType) {
        super(source);
        this.powerUpType = powerUpType;
    }
    
    public String getPowerUpType() { return powerUpType; }
}
