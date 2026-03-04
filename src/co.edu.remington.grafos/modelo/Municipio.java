package co.edu.remington.grafos.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un nodo (municipio) del grafo de Casanare.
 * Cada municipio tiene un id, un nombre y una lista de vías (conexiones).
 */
public class Municipio {

    private int id;
    private String nombre;
    private List<Via> conexiones;

    public Municipio(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.conexiones = new ArrayList<>();
    }

    // ─── Métodos ─────────────────────────────────────────────────────────────

    public void agregarVia(Via via) {
        conexiones.add(via);
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Via> getConexiones() {
        return conexiones;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s", id, nombre);
    }
}
