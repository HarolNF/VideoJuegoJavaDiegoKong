/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PatronesEstructurales.Adapter;

import PatronesComportamiento.Observer.EventoJugadorDañado;
import PatronesComportamiento.Observer.EventoJugadorPoder;
import PatronesComportamiento.Observer.EventoJugadorRecogeObjeto;
import PatronesComportamiento.Observer.EventoMuerteJugador;
import PatronesComportamiento.Observer.EventoReaparicionJugador;
import PatronesComportamiento.Observer.IJugadorEventos;

/**
INTEGRANTES
     *  Harol Raul Neciosup Fuentes
     *  Edwin Alexander Rojas Castro
     *  Diego Luis Alonso Mendoza Vargas
     *  Anthony Seclén Santisteban
     *  Jorge Edersson Chiroque Diaz
/**
 * Adapter para implementar solo los eventos necesarios
 * Patrón: Adapter
 */
public abstract class AdaptadorEventosJugador implements IJugadorEventos {
    @Override
    public void onJugadordanado(EventoJugadorDañado event) {}
    
    @Override
    public void onJugadorMuerto(EventoMuerteJugador event) {}
    
    @Override
    public void onJuegadorReaparece(EventoReaparicionJugador event) {}
    
    @Override
    public void onJugadorRecogeObjeto(EventoJugadorRecogeObjeto event) {}
    
    @Override
    public void onJugadorPoder(EventoJugadorPoder event) {}
}
