package Entidades;

import SistemaDeSoporte.Handler;
import PatronesComportamiento.State.EstadoVidaJugador;
import PatronesComportamiento.Observer.EventoJugadorDañado;
import PatronesComportamiento.Observer.EventoReaparicionJugador;
import PatronesComportamiento.Observer.EventoJugadorRecogeObjeto;
import PatronesComportamiento.Observer.EventoJugadorPoder;
import PatronesComportamiento.Observer.EventoMuerteJugador;
import PatronesEstructurales.Composite.ComponenteFisicasJugador;
import PatronesEstructurales.Composite.ComponenteEscaleraJugador;
import PatronesEstructurales.Composite.ComponenteColisionJugador;
import PatronesCreacionales.Singleton.Texturas;
import PatronesComportamiento.Observer.AdministradorEventos;
import PatronesComportamiento.Statregy.PoderMartillo;
import SistemaDeSoporte.*;
import SistemaGFX.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
/**
 * INTEGRANTES
     *  Harol Raul Neciosup Fuentes
     *  Edwin Alexander Rojas Castro
     *  Diego Luis Alonso Mendoza Vargas
     *  Anthony Seclén Santisteban
     *  Jorge Edersson Chiroque Diaz
 * 
 */
public class Jugador extends JuegoObjetos {
    private static final float WIDTH = 16; //ancho 
    private static final float HEIGHT = 16; // alto
    
    // DEPENDENCIAS INYECTADAS 
    private final AdministradorEventos eventManager;
    private final Handler handler;
    private final Texturas textura;
    
    // COMPONENTES (Composite Patron) 
    private final ComponenteFisicasJugador fisicas;
    private final ComponenteEscaleraJugador escalera;
    private final ComponenteColisionJugador collision;
    
    //  ESTADO (State Patron)
    private EstadoVidaJugador estadoVida;
    private Point puntoSpawn;
    private boolean invulnerable;
    
    // POWER-UPS 
    private PoderMartillo poderMartillo;
    private boolean tieneMartillo;
    
    // ANIMACIONES
    private Animacion jugadorCaminaS;
    private Animacion jugadorSubeEscalera;
    private Animacion jugadorBajaEscalera;
    private Animacion jugadorCaminaMartillo;
    private Animacion muerteAnimacion;
    private Animacion currAnimacion;
    
    private BufferedImage[] spriteS;
    private BufferedImage[] spriteMartillo;
    private BufferedImage[] spriteMuerte;
    private boolean adelante = true;
        
    /**
     * Constructor con inyección de dependencias
     */
    public Jugador(float x, float y, int scale, Handler handler, AdministradorEventos eventManager) {
        super(x, y, ObjetosID.Jugador, WIDTH, HEIGHT, scale);
        
        // Inyección de dependencias
        this.handler = handler;
        this.eventManager = eventManager;
        this.textura = PatronesEstructurales.Facade.Juego.getTextura();
        
        // Inicializar componentes (Composite Patron)
        this.fisicas = new ComponenteFisicasJugador(this, handler);
        this.escalera = new ComponenteEscaleraJugador(this, handler);
        this.collision = new ComponenteColisionJugador(this, handler, escalera, fisicas);
        
        // Inicializar estado
        this.estadoVida = new EstadoVidaJugador.Vivo(this);
        this.estadoVida.entrar();
        
        this.puntoSpawn = new Point((int)x, (int)y);
        this.invulnerable = false;
        
        // Inicializar power-ups
        this.poderMartillo = new PoderMartillo(this, handler);
        this.tieneMartillo = false;
        
        // Inicializar animaciones
        inicializarAnimaciones();
        
        System.out.println("[JUGADOR] Creado con sistema de eventos y componentes");
    }
    
    /**
     * Constructor legacy (compatibilidad)
     */
    public Jugador(float x, float y, int scale, Handler handler) {
        this(x, y, scale, handler, AdministradorEventos.getInstance());
    }
    
    // ==================== INICIALIZACIÓN ====================
    
    private void inicializarAnimaciones() {
        spriteMuerte = textura.getMarioMuerte();
        spriteMartillo = textura.getMarioMartillo();
        spriteS = textura.getMarioS();
        
        jugadorCaminaS = new Animacion(5, spriteS[1], spriteS[2], spriteS[3]);
        jugadorSubeEscalera = new Animacion(5, spriteS[5], spriteS[6], spriteS[7], 
                                           spriteS[8], spriteS[9], spriteS[10], spriteS[11]);
        jugadorBajaEscalera = new Animacion(5, spriteS[5], spriteS[6], spriteS[7], 
                                           spriteS[8], spriteS[9], spriteS[10], spriteS[11]);
        jugadorCaminaMartillo = new Animacion(5, spriteMartillo[0], spriteMartillo[1],
                                             spriteMartillo[2], spriteMartillo[3],
                                             spriteMartillo[4], spriteMartillo[5]);
        muerteAnimacion = new Animacion(50, spriteMuerte[0], spriteMuerte[1],
                                       spriteMuerte[2], spriteMuerte[3], spriteMuerte[4]);
        
        currAnimacion = jugadorCaminaS;
    }
    
    // ==================== TICK  ====================
    
    @Override
    public void tick() {
        // Validar estado
        if (estadoVida == null) {
            System.err.println("[ERROR] estadoVida NULL! Reinicializando...");
            estadoVida = new EstadoVidaJugador.Vivo(this);
            estadoVida.entrar();
        }
        
        // Tick del estado de vida
        estadoVida.tick();
        
        // Si está muerto o muriendo, no ejecutar lógica normal
        if (estadoVida instanceof EstadoVidaJugador.Muerto || 
            estadoVida instanceof EstadoVidaJugador.Muriendo) {
            return;
        }
        
        // Tick de componentes
        escalera.tick();
        
        //  APLICAR FÍSICA CORRECTAMENTE
        if (escalera.isEnEscalera()) {
            // En escalera: NO aplicar gravedad
            // La velocidad vertical es controlada por el componente de escalera
        } else {
            // Fuera de escalera: aplicar gravedad
            fisicas.aplicarGravedad();
        }
        
        // Power-ups
        if (poderMartillo != null) {
            poderMartillo.tick();
            tieneMartillo = poderMartillo.isActivo();
        }
        
        //  MOVIMIENTO (debe ir DESPUÉS de aplicar gravedad)
        fisicas.tick();
        
        // Límite de seguridad
        if (getY() > 2000) {
            System.err.println("[EMERGENCIA] Jugador cayó fuera del mapa");
            if (estadoVida instanceof EstadoVidaJugador.Vivo) {
                recibirDanio(null);
            }
            return;
        }
        
        // COLISIONES (debe ir DESPUÉS del movimiento)
        if (estadoVida.tieneColision()) {
            collision.procesarColisiones();
        }
        
        // Verificar colisiones con enemigos
        verificarColisionesEnemigos();
        
        // Actualizar animación
        actualizarAnimacion();
    }
    
    // ==================== RECIBIR DAÑO  ====================
    
    /**
     * Recibe daño - Observer Patron
     * 
     *  SRP: Solo gestiona su propio estado
     *  OCP: Listeners externos manejan consecuencias
     *  DIP: No depende de implementaciones concretas
     */
    public void recibirDanio(JuegoObjetos enemigo) {
        // Verificar si puede recibir daño
        if (!estadoVida.puedeRecibirDanio() || invulnerable) {
            return;
        }
        
        System.out.println("[JUGADOR] ¡Recibió daño de: " + 
                          (enemigo != null ? enemigo.getId() : "CAÍDA") + "!");
        
        //  EMITIR EVENTO (Notificar a observadores)
        EventoJugadorDañado event = new EventoJugadorDañado(this, enemigo);
        eventManager.dispararJugadorDañado(event);
        
        //  Cambiar solo MI estado (SRP)
        cambiarEstadoVida(new EstadoVidaJugador.Muriendo(this));
        
        // Emitir evento de muerte
        EventoMuerteJugador deathEvent = new EventoMuerteJugador(this, enemigo);
        eventManager.dispararMuerteJugador(deathEvent);
    }
    
    /**
     * Respawnea al jugador 
     */
    public void respawnear() {
    System.out.println("[JUGADOR] Reapareciendo en punto de spawn.");
        
        if (puntoSpawn != null) {
            // 1. Reposicionar en el punto de spawn
            setX(puntoSpawn.x);
            setY(puntoSpawn.y);
            
            // 2. Limpiar física
            setVelX(0);
            setVely(0);
            fisicas.aplicarGravedad(); // Es crucial resetear la gravedad
    
        } else {
            System.err.println("[JUGADOR ERROR] No se ha configurado puntoSpawn. Aparecerá en (0,0).");
            setX(0); setY(0);
        }
        
        // 3. Resetear power-ups 
        if (tieneMartillo) {
            tieneMartillo = false; 
            poderMartillo = null; 
        }
        
        // 4. Asegurar la animación correcta (no la de muerte)
        currAnimacion = jugadorCaminaS; 
    
        adelante = true;
        
        // EMITIR EVENTO de respawn
        EventoReaparicionJugador event = new EventoReaparicionJugador(
            this, puntoSpawn.x, puntoSpawn.y
        );
        eventManager.dispararReaparicionJugador(event);
        
        System.out.println("[JUGADOR] Respawn completado");
    }
    
    /**
     * Colecta un item 
     */
    public void colectarItem(String itemType, int puntos) {
        System.out.println("[JUGADOR] Colectó: " + itemType + " (+" + puntos + " pts)");
        
        // EMITIR EVENTO (Los listeners manejarán puntos, sonido, etc.)
        EventoJugadorRecogeObjeto event = new EventoJugadorRecogeObjeto(
            this, itemType, puntos
        );
        eventManager.dispararJugadorRecogeObjeto(event);
    }
    
    // ==================== VERIFICAR COLISIONES ENEMIGOS ====================
    
    private void verificarColisionesEnemigos() {
        // No recibir daño si es invulnerable o no puede recibir daño
        if (!estadoVida.puedeRecibirDanio() || invulnerable) {
            return;
        }
        
        // Si tiene martillo activo, no recibe daño
        if (tieneMartillo && poderMartillo.isActivo()) {
            return;
        }
        
        try {
            for (JuegoObjetos obj : handler.getGameObjs()) {
                if (obj == null) continue;
                
                ObjetosID id = obj.getId();
                
                // Verificar colisión con enemigos
                boolean esEnemigo = (id == ObjetosID.Barril || 
                                    id == ObjetosID.Fuego || 
                                    id == ObjetosID.DiegoKong);
                
                if (esEnemigo && getBounds().intersects(obj.getBounds())) {
                    recibirDanio(obj);
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Excepción en verificarColisionesEnemigos: " + e.getMessage());
        }
    }
    
    // ==================== ANIMACIÓN ====================
    
    private void actualizarAnimacion() {
        if (escalera.isEnEscalera()) {
            // En escalera (siempre usa animación normal)
            if (escalera.isSubiendoEscalera()) {
                currAnimacion = jugadorSubeEscalera;
                currAnimacion.runAnimacion();
            } else if (escalera.isBajandoEscalera()) {
                currAnimacion = jugadorBajaEscalera;
                currAnimacion.runAnimacion();
            }
            // Si está quieto en escalera, no correr animación
        } else {
            // Fuera de escalera
            
            // SELECCIONAR ANIMACIÓN SEGÚN SI TIENE MARTILLO
            if (tieneMartillo) {
                currAnimacion = jugadorCaminaMartillo;
            } else {
                currAnimacion = jugadorCaminaS;
            }
            
            // Ejecutar animación solo si se está moviendo horizontalmente Y NO ESTÁ SALTANDO
            if (getVelX() != 0 && !fisicas.hasSalto()) {
                currAnimacion.runAnimacion();
            }
        }
    }
    
    // ==================== RENDER ====================
    
    @Override
    public void render(Graphics g) {
        // Si está en estado MUERTO, no renderizar
        if (estadoVida instanceof EstadoVidaJugador.Muerto) {
            return;
        }
        
        // Si está RESPAWNEANDO y parpadeando, alternar visibilidad
        if (estadoVida instanceof EstadoVidaJugador.Respawneando) {
            EstadoVidaJugador.Respawneando respawn = (EstadoVidaJugador.Respawneando) estadoVida;
            if (respawn.isParpadeando()) {
                return;
            }
        }
        
        // Animación de muerte
        if (estadoVida instanceof EstadoVidaJugador.Muriendo) {
            renderAnimacionMuerte(g);
            return;
        }
        
        // Renderizado normal
        BufferedImage[] spritesActuales;
        Animacion animacionCaminar;
        int anchoRender, altoRender;
        int xRender, yRender;
        
        if (tieneMartillo && !escalera.isEnEscalera()) {
            spritesActuales = spriteMartillo;
            animacionCaminar = jugadorCaminaMartillo;
            anchoRender = (int) getWidth() * 2;
            altoRender = (int) getHeight() * 2;
            xRender = (int) getX() - (int) getWidth() / 2;
            yRender = (int) getY() - (int) getHeight();
        } else {
            spritesActuales = spriteS;
            animacionCaminar = currAnimacion;
            anchoRender = (int) getWidth();
            altoRender = (int) getHeight();
            xRender = (int) getX();
            yRender = (int) getY();
        }
        
        if (spritesActuales == null || spritesActuales.length == 0) {
            spritesActuales = spriteS;
            animacionCaminar = currAnimacion;
            anchoRender = (int) getWidth();
            altoRender = (int) getHeight();
            xRender = (int) getX();
            yRender = (int) getY();
        }
        
        // Renderizar según estado
        if (escalera.isEnEscalera()) {
            if (escalera.isSubiendoEscalera() || escalera.isBajandoEscalera()) {
                currAnimacion.drawAnimacion(g, (int) getX(), (int) getY(), 
                                           (int) getWidth(), (int) getHeight());
            } else {
                g.drawImage(spriteS[5], (int) getX(), (int) getY(), 
                           (int) getWidth(), (int) getHeight(), null);
            }
        } else if (fisicas.hasSalto()) {
            BufferedImage spriteJump = spritesActuales[3];
            if (adelante) {
                g.drawImage(spriteJump, xRender, yRender, anchoRender, altoRender, null);
            } else {
                g.drawImage(spriteJump, xRender + anchoRender, yRender, 
                           -anchoRender, altoRender, null);
            }
        } else if (getVelX() > 0) {
            animacionCaminar.drawAnimacion(g, xRender, yRender, anchoRender, altoRender);
            adelante = true;
        } else if (getVelX() < 0) {
            animacionCaminar.drawAnimacion(g, xRender + anchoRender, yRender, 
                                           -anchoRender, altoRender);
            adelante = false;
        } else {
            BufferedImage spriteReposo = spritesActuales[0];
            if (adelante) {
                g.drawImage(spriteReposo, xRender, yRender, anchoRender, altoRender, null);
            } else {
                g.drawImage(spriteReposo, xRender + anchoRender, yRender, 
                           -anchoRender, altoRender, null);
            }
        }
        
        // Renderizar efecto del martillo
        if (poderMartillo != null && tieneMartillo) {
            poderMartillo.render(g);
        }
        
        // Indicador visual de invulnerabilidad
        if (invulnerable && !(estadoVida instanceof EstadoVidaJugador.Respawneando)) {
            g.setColor(new Color(255, 255, 0, 100));
            g.fillOval((int)getX() - 5, (int)getY() - 5, 
                      (int)getWidth() + 10, (int)getHeight() + 10);
        }
    }
    
    private void renderAnimacionMuerte(Graphics g) {
        muerteAnimacion.runAnimacion();
        
        if (adelante) {
            muerteAnimacion.drawAnimacion(g, 
                (int)getX(), (int)getY(), 
                (int)getWidth(), (int)getHeight()
            );
        } else {
            muerteAnimacion.drawAnimacion(g, 
                (int)(getX() + getWidth()), (int)getY(), 
                (int)-getWidth(), (int)getHeight()
            );
        }
        
        EstadoVidaJugador.Muriendo muriendo = (EstadoVidaJugador.Muriendo) estadoVida;
        int frame = muriendo.getFrameActual();
        
        // Frame 0: Flash blanco de impacto
        if (frame == 1) {
            g.setColor(new Color(255, 255, 255, 80));
            g.fillRect((int)getX() - 2, (int)getY() - 2, 
                      (int)getWidth() + 4, (int)getHeight() + 4);
        }
    }
    
    // ==================== POWER-UPS ====================
    
    public void activarMartillo() {
        if (poderMartillo != null) {
            poderMartillo.activar();
            tieneMartillo = true;
            
            // EMITIR EVENTO
            EventoJugadorPoder event = new EventoJugadorPoder(this, "MARTILLO");
            eventManager.dispararJugadorGanaPoder(event);
            
            System.out.println("[PLAYER] ¡Martillo activado!");
        }
    }

    public void activarMartillo(int duracionSegundos) {
        if (poderMartillo != null) {
            poderMartillo.activar(duracionSegundos * 60);
            tieneMartillo = true;
            
            // EMITIR EVENTO
            EventoJugadorPoder event = new EventoJugadorPoder(this, "MARTILLO");
            eventManager.dispararJugadorGanaPoder(event);
        }
    }
    
    public void golpearConMartillo() {
        if (poderMartillo != null && tieneMartillo) {
            poderMartillo.golpear();
        }
    }
    
    public boolean tieneMartillo() {
        return tieneMartillo;
    }
    
    public PoderMartillo getPoderMartillo() {
        return poderMartillo;
    }
    
    // ==================== MOVIMIENTOS ====================
    
    public void iniciarSalto() {
        if (!escalera.isEnEscalera()) {
            fisicas.iniciarSalto();
        }
    }
    
    public void subirEscalera() {
        if (tieneMartillo) return;
        escalera.subirEscalera(fisicas.getVelocidadEscalera());
    }
    
    public void bajarEscalera() {
        if (tieneMartillo) return;
        escalera.bajarEscalera(fisicas.getVelocidadEscalera());
    }
    
    public void detenerMovimientoVertical() {
        escalera.detenerMovimientoVertical();
    }
    
    public void moverIzquierda() {
        if (!escalera.isEnEscalera()) {
            fisicas.moverIzquierda();
        } else {
            escalera.salirEscalera();
            fisicas.moverIzquierda();
        }
    }
    
    public void moverDerecha() {
        if (!escalera.isEnEscalera()) {
            fisicas.moverDerecha();
        } else {
            escalera.salirEscalera();
            fisicas.moverDerecha();
        }
    }
    
    public void detenerMovimiento() {
        if (!escalera.isEnEscalera()) {
            fisicas.detenerMovimiento();
        }
    }
    
    // ==================== HITBOXES ====================
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle(
            (int)(getX() + getWidth() / 4),
            (int)(getY() + getHeight() / 2),
            (int) getWidth() / 2,
            (int) getHeight() / 2
        );
    }
    
    public Rectangle getBoundsTop() {
        return new Rectangle(
            (int) (getX() + getWidth() / 4),
            (int) getY(),
            (int) getWidth() / 2,
            (int) getHeight() / 2
        );
    }
    
    public Rectangle getBoundsRight() {
        return new Rectangle(
            (int) (getX() + getWidth() - 5),
            (int) getY() + 5,
            5,
            (int) getHeight() - 10
        );
    }
    
    public Rectangle getBoundsLeft() {
        return new Rectangle(
            (int) getX(),
            (int) (getY() + 5),
            5,
            (int) (getHeight() - 10)
        );
    }
    
    // ==================== ESTADO DE VIDA ====================
    
    public void cambiarEstadoVida(EstadoVidaJugador nuevoEstado) {
        if (estadoVida != null) {
            estadoVida.salir();
        }
        estadoVida = nuevoEstado;
        estadoVida.entrar();
    }
    
    public EstadoVidaJugador getEstadoVida() {
        return estadoVida;
    }
    
    public boolean estaVivo() {
        return estadoVida instanceof EstadoVidaJugador.Vivo || 
               estadoVida instanceof EstadoVidaJugador.Respawneando;
    }
    
    public boolean estaMuriendo() {
        return estadoVida instanceof EstadoVidaJugador.Muriendo;
    }
    
    public boolean estaMuerto() {
        return estadoVida instanceof EstadoVidaJugador.Muerto;
    }
    
    // ==================== GETTERS/SETTERS ====================
    
    public void setPuntoSpawn(int x, int y) {
        puntoSpawn = new Point(x, y);
        System.out.println("[JUGADOR] Nuevo punto de spawn: (" + x + ", " + y + ")");
    }
    
    public Point getPuntoSpawn() {
        return puntoSpawn;
    }
    
    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }
    
    public boolean isInvulnerable() {
        return invulnerable;
    }
    
    public boolean hasSalto() {
        return fisicas.hasSalto();
    }
    
    public void setSalto(boolean salto) {
        fisicas.setSalto(salto);
    }
    
    public boolean isEnEscalera() {
        return escalera.isEnEscalera();
    }
    
    public boolean isSubiendoEscalera() {
        return escalera.isSubiendoEscalera();
    }
    
    public boolean isBajandoEscalera() {
        return escalera.isBajandoEscalera();
    }
    
    @Override
    public void aplicarGravedad() {
        if (!escalera.isEnEscalera()) {
            fisicas.aplicarGravedad();
        }
    }
}