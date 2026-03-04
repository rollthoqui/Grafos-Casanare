package co.edu.remington.grafos.modelo;

import java.util.*;

/**
 * Representa el grafo completo de la red vial de Casanare.
 * Utiliza lista de adyacencia mediante HashMap<Integer, List<Via>>.
 */
public class GrafoCasanare {

    private HashMap<Integer, Municipio> municipios;

    public GrafoCasanare() {
        municipios = new HashMap<>();
    }

    // ─── Construcción del grafo ──────────────────────────────────────────────

    /** Agrega un municipio (nodo) al grafo. */
    public void agregarMunicipio(int id, String nombre) {
        if (municipios.containsKey(id)) {
            System.out.println("⚠  Ya existe el municipio con ID " + id);
            return;
        }
        municipios.put(id, new Municipio(id, nombre));
    }

    /**
     * Agrega una vía bidireccional (arista) entre dos municipios.
     * @param origenId       ID del municipio origen
     * @param destinoId      ID del municipio destino
     * @param distanciaKm    Distancia en kilómetros
     * @param estado         "Bueno", "Regular" o "Malo"
     */
    public void agregarVia(int origenId, int destinoId, double distanciaKm, String estado) {
        Municipio origen  = municipios.get(origenId);
        Municipio destino = municipios.get(destinoId);

        if (origen == null || destino == null) {
            System.out.println("⚠  Uno o ambos municipios no existen en el grafo.");
            return;
        }

        origen.agregarVia(new Via(destinoId, distanciaKm, estado));
        destino.agregarVia(new Via(origenId,  distanciaKm, estado));
    }

    // ─── Visualización ──────────────────────────────────────────────────────

    /** Muestra la lista de adyacencia del grafo. */
    public void mostrarGrafo() {
        System.out.println("\n════════════════════════════════════════════════════");
        System.out.println("         RED VIAL DEL DEPARTAMENTO DE CASANARE");
        System.out.println("════════════════════════════════════════════════════");

        List<Integer> ids = new ArrayList<>(municipios.keySet());
        Collections.sort(ids);

        for (int id : ids) {
            Municipio m = municipios.get(id);
            System.out.printf("%-20s (%d vías):%n", m.toString(), m.getConexiones().size());
            for (Via v : m.getConexiones()) {
                Municipio dest = municipios.get(v.getMunicipioDestino());
                System.out.printf("   %-18s %.1f km  [%s]%n",
                        "→ " + dest.getNombre(), v.getDistanciaKm(), v.getEstado());
            }
        }
        System.out.println("════════════════════════════════════════════════════\n");
    }

    /** Muestra la lista de municipios registrados. */
    public void listarMunicipios() {
        System.out.println("\n──────────────────────────────────");
        System.out.println("  ID  │  Municipio");
        System.out.println("──────────────────────────────────");
        List<Integer> ids = new ArrayList<>(municipios.keySet());
        Collections.sort(ids);
        for (int id : ids) {
            System.out.printf("  %-3d │  %s%n", id, municipios.get(id).getNombre());
        }
        System.out.println("──────────────────────────────────\n");
    }

    // ─── Getters / Utilidades ────────────────────────────────────────────────

    public Municipio getMunicipio(int id) {
        return municipios.get(id);
    }

    public HashMap<Integer, Municipio> getMunicipios() {
        return municipios;
    }

    public int getTotalMunicipios() {
        return municipios.size();
    }

    public boolean existeMunicipio(int id) {
        return municipios.containsKey(id);
    }

    public String getNombre(int id) {
        Municipio m = municipios.get(id);
        return m != null ? m.getNombre() : "Desconocido";
    }
}
