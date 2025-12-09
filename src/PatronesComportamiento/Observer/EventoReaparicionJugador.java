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
/**
 * Evento: Jugador respawneó
 */
public class EventoReaparicionJugador extends EventoJuego {
    private final float spawnX;
    private final float spawnY;
    
    public EventoReaparicionJugador(Jugador source, float x, float y) {
        super(source);
        this.spawnX = x;
        this.spawnY = y;
    }
    
    public float getSpawnX() { return spawnX; }
    public float getSpawnY() { return spawnY; }
}
