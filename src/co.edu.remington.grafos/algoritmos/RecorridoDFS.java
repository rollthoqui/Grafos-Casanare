package co.edu.remington.grafos.algoritmos;

import co.edu.remington.grafos.modelo.GrafoCasanare;
import co.edu.remington.grafos.modelo.Municipio;
import co.edu.remington.grafos.modelo.Via;

import java.util.*;

/**
 * Implementación del algoritmo DFS (Depth-First Search).
 * Recorre el grafo en profundidad y muestra el camino completo recorrido.
 */
public class RecorridoDFS {

    private GrafoCasanare grafo;
    private Map<Integer, Boolean> visitado;
    private List<Integer> orden;
    

    public RecorridoDFS(GrafoCasanare grafo) {
        this.grafo = grafo;
    }

    /**
     * Ejecuta DFS desde el nodo origen dado.
     * Muestra el orden de exploración y el camino completo recorrido.
     *
     * @param origenId ID del municipio de partida
     */
    public void ejecutar(int origenId) {
        if (!grafo.existeMunicipio(origenId)) {
            System.out.println("⚠  El municipio con ID " + origenId + " no existe.");
            return;
        }

        visitado     = new HashMap<>();
        orden        = new ArrayList<>();
        

        for (int id : grafo.getMunicipios().keySet()) {
            visitado.put(id, false);
        }

        System.out.println("\n════════════════════════════════════════════════════");
        System.out.println("   RECORRIDO DFS desde: " + grafo.getNombre(origenId));
        System.out.println("════════════════════════════════════════════════════");

        dfsRecursivo(origenId, 0);

        System.out.println("\n  Orden completo de visita:");
        StringBuilder sb = new StringBuilder("  ");
        for (int i = 0; i < orden.size(); i++) {
            sb.append(grafo.getNombre(orden.get(i)));
            if (i < orden.size() - 1) sb.append(" → ");
        }
        System.out.println(sb);

        System.out.println("\n  Total municipios visitados: " + orden.size());

        // Detectar desconectados
        List<String> desconectados = new ArrayList<>();
        for (int id : grafo.getMunicipios().keySet()) {
            if (!visitado.getOrDefault(id, false)) {
                desconectados.add(grafo.getNombre(id));
            }
        }
        if (desconectados.isEmpty()) {
            System.out.println("  ✔ No hay municipios desconectados de la red.");
        } else {
            System.out.println("  ✘ Municipios desconectados: " + desconectados);
        }
        System.out.println("════════════════════════════════════════════════════\n");
    }

    /** Recursión DFS con seguimiento de profundidad */
    private void dfsRecursivo(int actual, int profundidad) {
        visitado.put(actual, true);
        orden.add(actual);

        String sangria = "  " + "  ".repeat(profundidad);
        System.out.printf("%s[%d] %-18s (prof. %d)%n",
                sangria, actual, grafo.getNombre(actual), profundidad);

        Municipio m = grafo.getMunicipio(actual);
        List<Via> vias = new ArrayList<>(m.getConexiones());
        vias.sort(Comparator.comparingInt(Via::getMunicipioDestino));

        for (Via via : vias) {
            int vecino = via.getMunicipioDestino();
            if (!visitado.get(vecino)) {
                dfsRecursivo(vecino, profundidad + 1);
            }
        }
    }
}
