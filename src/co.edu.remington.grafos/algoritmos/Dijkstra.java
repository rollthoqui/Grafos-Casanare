package co.edu.remington.grafos.algoritmos;

import co.edu.remington.grafos.modelo.GrafoCasanare;
import co.edu.remington.grafos.modelo.Municipio;
import co.edu.remington.grafos.modelo.Via;

import java.util.*;

/**
 * Implementación del algoritmo de Dijkstra para rutas más cortas.
 * Soporta modo normal y modo con penalización por estado de vía.
 */
public class Dijkstra {

    private GrafoCasanare grafo;

    public Dijkstra(GrafoCasanare grafo) {
        this.grafo = grafo;
    }

    // ─── API pública ─────────────────────────────────────────────────────────

    /**
     * Calcula rutas más cortas desde el origen hacia todos los municipios.
     * @param origenId   ID del municipio de partida
     * @param penalizar  true → aplica penalización por estado de vía
     */
    public void calcularDesdeOrigen(int origenId, boolean penalizar) {
        if (!grafo.existeMunicipio(origenId)) {
            System.out.println("⚠  El municipio con ID " + origenId + " no existe.");
            return;
        }

        ResultadoDijkstra res = ejecutar(origenId, penalizar);

        String modo = penalizar ? "CON PENALIZACIÓN POR ESTADO DE VÍA" : "SIN PENALIZACIÓN";
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("   DIJKSTRA desde: " + grafo.getNombre(origenId) + "  [" + modo + "]");
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.printf("%-22s %-12s  %s%n", "Destino", "Distancia", "Ruta óptima");
        System.out.println("────────────────────────────────────────────────────────────");

        List<Integer> ids = new ArrayList<>(grafo.getMunicipios().keySet());
        Collections.sort(ids);

        for (int id : ids) {
            if (id == origenId) continue;
            double dist = res.distancias.get(id);
            String rutaStr = construirRutaStr(res.predecesores, origenId, id);
            if (dist == Double.MAX_VALUE) {
                System.out.printf("%-22s %-12s  %s%n", grafo.getNombre(id), "∞ (incomunicado)", "-");
            } else {
                System.out.printf("%-22s %-10.2f km  %s%n", grafo.getNombre(id), dist, rutaStr);
            }
        }
        System.out.println("════════════════════════════════════════════════════════════\n");
    }

    /**
     * Calcula y muestra la ruta más corta entre dos municipios específicos.
     * @param origenId   ID del municipio origen
     * @param destinoId  ID del municipio destino
     * @param penalizar  true → aplica penalización
     */
    public void calcularRuta(int origenId, int destinoId, boolean penalizar) {
        if (!grafo.existeMunicipio(origenId) || !grafo.existeMunicipio(destinoId)) {
            System.out.println("⚠  Uno o ambos municipios no existen.");
            return;
        }

        ResultadoDijkstra res = ejecutar(origenId, penalizar);
        double dist = res.distancias.get(destinoId);
        String rutaStr = construirRutaStr(res.predecesores, origenId, destinoId);
        String modo = penalizar ? "penalizada" : "real";

        System.out.printf("  %-12s → %-12s  |  %-10.2f km (%s)  |  %s%n",
                grafo.getNombre(origenId),
                grafo.getNombre(destinoId),
                dist, modo, rutaStr);
    }

    // ─── Núcleo del algoritmo ─────────────────────────────────────────────────

    /**
     * Ejecuta Dijkstra y retorna las distancias mínimas y predecesores.
     */
    public ResultadoDijkstra ejecutar(int origenId, boolean penalizar) {
        Map<Integer, Double>  distancias   = new HashMap<>();
        Map<Integer, Integer> predecesores = new HashMap<>();
        Set<Integer>          procesados   = new HashSet<>();

        // Inicializar distancias a infinito
        for (int id : grafo.getMunicipios().keySet()) {
            distancias.put(id, Double.MAX_VALUE);
            predecesores.put(id, -1);
        }
        distancias.put(origenId, 0.0);

        // Cola de prioridad: [distancia, id]
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1]));
        pq.offer(new int[]{origenId, 0});

        while (!pq.isEmpty()) {
            int[] actual = pq.poll();
            int u = actual[0];

            if (procesados.contains(u)) continue;
            procesados.add(u);

            Municipio m = grafo.getMunicipio(u);
            for (Via via : m.getConexiones()) {
                int v    = via.getMunicipioDestino();
                double w = penalizar ? via.getDistanciaPenalizada() : via.getDistanciaKm();

                double nuevaDist = distancias.get(u) + w;
                if (nuevaDist < distancias.get(v)) {
                    distancias.put(v, nuevaDist);
                    predecesores.put(v, u);
                    pq.offer(new int[]{v, (int)(nuevaDist * 100)});
                }
            }
        }

        return new ResultadoDijkstra(distancias, predecesores);
    }

    // ─── Utilidades ──────────────────────────────────────────────────────────

    /** Reconstruye la ruta como cadena legible. */
    private String construirRutaStr(Map<Integer, Integer> predecesores, int origen, int destino) {
        LinkedList<String> ruta = new LinkedList<>();
        int actual = destino;

        while (actual != -1) {
            ruta.addFirst(grafo.getNombre(actual));
            if (actual == origen) break;
            actual = predecesores.get(actual);
            if (actual == -1) return "(sin ruta)";
        }

        return String.join(" → ", ruta);
    }

    /** Obtiene la ruta como lista de IDs. */
    public List<Integer> obtenerRutaIds(Map<Integer, Integer> predecesores, int origen, int destino) {
        LinkedList<Integer> ruta = new LinkedList<>();
        int actual = destino;
        while (actual != -1) {
            ruta.addFirst(actual);
            if (actual == origen) break;
            actual = predecesores.get(actual);
        }
        return ruta;
    }

    // ─── Clase interna de resultado ───────────────────────────────────────────

    public static class ResultadoDijkstra {
        public final Map<Integer, Double>  distancias;
        public final Map<Integer, Integer> predecesores;

        public ResultadoDijkstra(Map<Integer, Double> distancias,
                                  Map<Integer, Integer> predecesores) {
            this.distancias   = distancias;
            this.predecesores = predecesores;
        }
    }
}
