/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package PatronesComportamiento.Observer;

/** 
 * INTEGRANTES
     *  Harol Raul Neciosup Fuentes
     *  Edwin Alexander Rojas Castro
     *  Diego Luis Alonso Mendoza Vargas
     *  Anthony Seclén Santisteban
     *  Jorge Edersson Chiroque Diaz

 * Interfaz para observadores de eventos de Jugador
 * Patrón: Observer Patron
 */
public interface IJugadorEventos {
    void onJugadordanado(EventoJugadorDañado event);
    void onJugadorMuerto(EventoMuerteJugador event);
    void onJuegadorReaparece(EventoReaparicionJugador event);
    void onJugadorRecogeObjeto(EventoJugadorRecogeObjeto event);
    void onJugadorPoder(EventoJugadorPoder event);
}
