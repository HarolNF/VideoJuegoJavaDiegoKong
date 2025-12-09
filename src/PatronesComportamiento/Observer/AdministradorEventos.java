/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PatronesComportamiento.Observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * INTEGRANTES
     *  Harol Raul Neciosup Fuentes
     *  Edwin Alexander Rojas Castro
     *  Diego Luis Alonso Mendoza Vargas
     *  Anthony Seclén Santisteban
     *  Jorge Edersson Chiroque Diaz
 * Gestor central de eventos del juego
 * Patrón: Mediator + Observer + Singleton
 * Principio: SRP - Solo gestiona suscripciones y notificaciones
 * 
 */
public class AdministradorEventos {
    private static AdministradorEventos instance;
    private final List<IJugadorEventos> jugadorListeners;
    
    private AdministradorEventos() {
        this.jugadorListeners = new CopyOnWriteArrayList<>();
    }
    
    public static AdministradorEventos getInstance() {
        if (instance == null) {
            instance = new AdministradorEventos();
        }
        return instance;
    }
    
    public void registarJugadorListener(IJugadorEventos listener) {
        if (!jugadorListeners.contains(listener)) {
            jugadorListeners.add(listener);
            System.out.println("[EVENT] Listener registrado: " + 
                             listener.getClass().getSimpleName());
        }
    }
    
    public void anularRegistoJugadorListener(IJugadorEventos listener) {
        jugadorListeners.remove(listener);
    }
    
    public void LimpiarTodoListeners() {
        jugadorListeners.clear();
    }
    // 2. MEDIATOR (desacopla Jugador ↔ Listeners)
    public void dispararJugadorDañado(EventoJugadorDañado event) {
        for (IJugadorEventos listener : jugadorListeners) {
            try {
                listener.onJugadordanado(event);
            } catch (Exception e) {
                System.err.println("[EVENT] Error en listener: " + e.getMessage());
            }
        }
    }
    
    public void dispararMuerteJugador(EventoMuerteJugador event) {
        for (IJugadorEventos listener : jugadorListeners) {
            try {
                listener.onJugadorMuerto(event);
            } catch (Exception e) {
                System.err.println("[EVENT] Error en listener: " + e.getMessage());
            }
        }
    }
    
    public void dispararReaparicionJugador(EventoReaparicionJugador event) {
        for (IJugadorEventos listener : jugadorListeners) {
            try {
                listener.onJuegadorReaparece(event);
            } catch (Exception e) {
                System.err.println("[EVENT] Error en listener: " + e.getMessage());
            }
        }
    }
    
    public void dispararJugadorRecogeObjeto(EventoJugadorRecogeObjeto event) {
        for (IJugadorEventos listener : jugadorListeners) {
            try {
                listener.onJugadorRecogeObjeto(event);
            } catch (Exception e) {
                System.err.println("[EVENT] Error en listener: " + e.getMessage());
            }
        }
    }
    
    public void dispararJugadorGanaPoder(EventoJugadorPoder event) {
        for (IJugadorEventos listener : jugadorListeners) {
            try {
                listener.onJugadorPoder(event);
            } catch (Exception e) {
                System.err.println("[EVENT] Error en listener: " + e.getMessage());
            }
        }
    }
    
    public int getListenerCount() {
        return jugadorListeners.size();
    }
}
