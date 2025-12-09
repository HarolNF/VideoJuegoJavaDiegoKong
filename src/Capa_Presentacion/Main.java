package Capa_Presentacion;

import SistemaGFX.Configuracion;
import PatronesEstructurales.Facade.Juego;

/**
 * Curso: Diseño De Patrones
*  INTEGRANTES
     *  Harol Raul Neciosup Fuentes
     *  Edwin Alexander Rojas Castro
     *  Diego Luis Alonso Mendoza Vargas
     *  Anthony Seclén Santisteban
     *  Jorge Edersson Chiroque Diaz
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("\n==============================================");
        System.out.println("   " + Configuracion.NOMBRE_JUEGO);
        System.out.println("==============================================\n");
        
        try {
            // Crear y arrancar el juego
            Juego juego = new Juego();
            juego.iniciar();
            
        } catch (Exception e) {
            System.err.println("\n Error al iniciar el juego:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}