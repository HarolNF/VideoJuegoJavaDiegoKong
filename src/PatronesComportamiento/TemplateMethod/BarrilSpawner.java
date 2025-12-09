/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PatronesComportamiento.TemplateMethod;
import Entidades.Enemigos.Barril;
import SistemaDeSoporte.Handler;
import java.awt.Point;
import java.util.List;
/**
INTEGRANTES
     *  Harol Raul Neciosup Fuentes
     *  Edwin Alexander Rojas Castro
     *  Diego Luis Alonso Mendoza Vargas
     *  Anthony Seclén Santisteban
     *  Jorge Edersson Chiroque Diaz
 */
public class BarrilSpawner extends Spawner<Barril> {
    
    /**
     * Constructor
     */
    public BarrilSpawner(Handler handler, List<Point> spawnPoints) {
        super(handler, spawnPoints);
    }
    
    @Override
    protected Barril crearEntidad(Point spawnPoint) {
        // Dirección aleatoria
        int probabilidad = random.nextInt(100);
        int direccion = (probabilidad < 71) ? 1 : -1;
        
        // Crear barril
        return new Barril(
            spawnPoint.x,
            spawnPoint.y,
            2,  // scale
            handler,
            direccion
        );
    }
    
    @Override
    protected String getNombreSpawner() {
        return "BARRIL SPAWNER";
    }
    
    @Override
    protected void onEntidadSpawneada(Barril barril, Point spawnPoint) {
        System.out.println("[BARRIL] Dirección: " + 
                          (barril.getDireccion() > 0 ? "DERECHA" : "IZQUIERDA"));
    }
}