package PatronesComportamiento.State;

import PatronesCreacionales.Singleton.EstadoJuego;
import Entidades.Jugador;
import java.awt.Graphics;

/**
 * Patrón STATE para manejar estados de vida del jugador
 * Estados: VIVO → MURIENDO → MUERTO → RESPAWNEANDO
 * 
 * INTEGRANTES
     *  Harol Raul Neciosup Fuentes
     *  Edwin Alexander Rojas Castro
     *  Diego Luis Alonso Mendoza Vargas
     *  Anthony Seclén Santisteban
     *  Jorge Edersson Chiroque Diaz
 */
public abstract class EstadoVidaJugador {
    
    protected Jugador jugador;
    
    public EstadoVidaJugador(Jugador jugador) {
        this.jugador = jugador;
    }
    
    /**
     * Métodos abstractos que cada estado implementa diferente
     */
    public abstract void entrar();
    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract void salir();
    
    /**
     * Determina si el jugador puede moverse
     */
    public abstract boolean puedeMoverse();
    
    /**
     * Determina si el jugador puede recibir daño
     */
    public abstract boolean puedeRecibirDanio();
    
    /**
     * Determina si el jugador tiene colisión
     */
    public abstract boolean tieneColision();
    
    // ==================== ESTADO: VIVO ====================
    
    public static class Vivo extends EstadoVidaJugador {
        
        public Vivo(Jugador player) {
            super(player);
        }
        
        @Override
        public void entrar() {
            System.out.println("[ESTADO] Jugador: VIVO");
        }
        
        @Override
        public void tick() {
            // Comportamiento normal del jugador
        }
        
        @Override
        public void render(Graphics g) {
            // Renderizado normal (controlado por Jugador)
        }
        
        @Override
        public void salir() {
            // Nada especial
        }
        
        @Override
        public boolean puedeMoverse() {
            return true;
        }
        
        @Override
        public boolean puedeRecibirDanio() {
            return true;
        }
        
        @Override
        public boolean tieneColision() {
            return true;
        }
    }
    
    // ==================== ESTADO: MURIENDO ====================
    
    public static class Muriendo extends EstadoVidaJugador {
        
        private int ticksAnimacion;
        private static final int DURACION_ANIMACION = 120; // 2 segundos (más tiempo)
        
        // Control de animación de muerte
        private int frameActual;
        private static final int TICKS_POR_FRAME = 20; // Más lento (0.33 seg por frame)
        
        public Muriendo(Jugador player) {
            super(player);
            this.ticksAnimacion = 0;
            this.frameActual = 0;
        }
        
        @Override
        public void entrar() {
            System.out.println("[ESTADO] Jugador: MURIENDO - Iniciando animación");
            
            // Detener movimiento horizontal
            jugador.setVelX(0);
            jugador.setSalto(false);
            
            // Impulso inicial hacia arriba (estilo Donkey Kong)
            jugador.setVely(-6);
            
        }
        
        @Override
        public void tick() {
            ticksAnimacion++;
            
            // ==================== FÍSICA DE MUERTE ====================
            // Fase 1 (0-20 ticks): Salto inicial hacia arriba
            if (ticksAnimacion < 20) {
                jugador.setVely(-6);
            } 
            // Fase 2 (20-90 ticks): Caída con gravedad
            else if (ticksAnimacion < 90) {
                jugador.aplicarGravedad();
                
                // Limitar velocidad de caída
                if (jugador.getVely() > 10) {
                    jugador.setVely(10);
                }
            }
            // Fase 3 (90-120 ticks): PARADO EN EL ÚLTIMO FRAME
            else {
                jugador.setVely(0);
                frameActual = 5; // ultimo frame (índice 5 = frame 6)
            }
            
            // Aplicar movimiento vertical solo en fases 1 y 2
            if (ticksAnimacion < 90) {
                jugador.setY(jugador.getY() + jugador.getVely());
            }
            
            // ==================== CONTROL DE ANIMACIÓN ====================
            // Actualizar frame de animación cada X ticks (solo en fases 1-2)
            if (ticksAnimacion < 90) {
                frameActual = ticksAnimacion / TICKS_POR_FRAME;
                
                // Limitar al último frame si excede
                if (frameActual >= 6) { // 6 frames totales (0-5)
                    frameActual = 5;
                }
            }
            
            // ==================== FINALIZAR ANIMACIÓN ====================
            if (ticksAnimacion >= DURACION_ANIMACION) {
                System.out.println("[MURIENDO] Animación completa - Cambiando a estado MUERTO");
                jugador.cambiarEstadoVida(new Muerto(jugador));
            }
        }
        
        @Override
        public void render(Graphics g) {
            // La animación se renderiza en Jugador.render()
            // Este método solo controla la lógica
        }
        
        @Override
        public void salir() {
            System.out.println("[ESTADO] Animación de muerte completada");
        }
        
        @Override
        public boolean puedeMoverse() {
            return false;
        }
        
        @Override
        public boolean puedeRecibirDanio() {
            return false; // Invulnerable mientras muere
        }
        
        @Override
        public boolean tieneColision() {
            return false; // Sin colisión durante muerte
        }
        
        /**
         * Obtiene el frame actual de la animación (0-5)
         */
        public int getFrameActual() {
            return frameActual;
        }
    }
    
    // ==================== ESTADO: MUERTO ====================
    
    public static class Muerto extends EstadoVidaJugador {
        
        private int ticksEspera;
        private static final int DURACION_ESPERA = 90; // 1.5 segundos (antes solo 1 seg)
        
        public Muerto(Jugador player) {
            super(player);
            this.ticksEspera = 0;
        }
        
        @Override
        public void entrar() {
            System.out.println("[ESTADO] Jugador: MUERTO");
            jugador.setVelX(0);
            jugador.setVely(0);
            jugador.aplicarGravedad();
            // Verificar vidas restantes
            EstadoJuego estado = EstadoJuego.getInstance();
            
            if (estado.getVidas() > 0) {
                System.out.println("[VIDA] Vidas restantes: " + estado.getVidas() + " - Preparando respawn...");
            } else {
                System.out.println("[GAME OVER] Sin vidas restantes");
            }
        }
        
        @Override
        public void tick() {
            ticksEspera++;
            
            //  DESPUÉS DE ESPERAR, RESPAWNEAR AUTOMÁTICAMENTE
            if (ticksEspera >= DURACION_ESPERA) {
                EstadoJuego estado = EstadoJuego.getInstance();
                
                if (estado.getVidas() > 0) {
                    System.out.println("[RESPAWN] Iniciando respawn...");
                    jugador.cambiarEstadoVida(new Respawneando(jugador));
                } else {
                }
            }
        }
        
        @Override
        public void render(Graphics g) {
            // No renderizar nada (jugador "desaparecido")
        }
        
        @Override
        public void salir() {
            System.out.println("[ESTADO] Saliendo del estado MUERTO");
        }
        
        @Override
        public boolean puedeMoverse() {
            return false;
        }
        
        @Override
        public boolean puedeRecibirDanio() {
            return false;
        }
        
        @Override
        public boolean tieneColision() {
            return false;
        }
    }
    
    // ==================== ESTADO: RESPAWNEANDO ====================
    
    public static class Respawneando extends EstadoVidaJugador {
        
        private int ticksInvulnerabilidad;
        private static final int DURACION_INVULNERABILIDAD = 120; // 2 segundos
        private boolean parpadeando;
        
        public Respawneando(Jugador player) {
            super(player);
            this.ticksInvulnerabilidad = 0;
            this.parpadeando = false;
        }
        
        @Override
        public void entrar() {
            System.out.println("[ESTADO] Jugador: RESPAWNEANDO (invulnerable)");
            jugador.respawnear();
        }
        
        @Override
        public void tick() {
            ticksInvulnerabilidad++;
            
            // Efecto de parpadeo (alternancia cada 5 ticks)
            if (ticksInvulnerabilidad % 10 < 5) {
                parpadeando = true;
            } else {
                parpadeando = false;
            }
            
            // Terminar invulnerabilidad
            if (ticksInvulnerabilidad >= DURACION_INVULNERABILIDAD) {
                jugador.cambiarEstadoVida(new Vivo(jugador));
            }
        }
        
        @Override
        public void render(Graphics g) {
            // Renderizado normal pero con parpadeo
            // El parpadeo se controla desde Jugador.render()
        }
        
        @Override
        public void salir() {
            System.out.println("[ESTADO] Invulnerabilidad terminada");
        }
        
        @Override
        public boolean puedeMoverse() {
            return true; // Puede moverse mientras es invulnerable
        }
        
        @Override
        public boolean puedeRecibirDanio() {
            return false; // Invulnerable durante respawn
        }
        
        @Override
        public boolean tieneColision() {
            return true; // Tiene colisión con bloques
        }
        
        public boolean isParpadeando() {
            return parpadeando;
        }
    }
}