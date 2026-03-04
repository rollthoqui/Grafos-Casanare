package co.edu.remington.grafos.algoritmos;

import co.edu.remington.grafos.modelo.GrafoCasanare;
import co.edu.remington.grafos.modelo.Municipio;
import co.edu.remington.grafos.modelo.Via;

import java.util.*;

/**
 * Implementación del algoritmo BFS (Breadth-First Search).
 * Recorre el grafo por niveles de profundidad a partir de un nodo origen.
 */
public class RecorridoBFS {

    private GrafoCasanare grafo;

    public RecorridoBFS(GrafoCasanare grafo) {
        this.grafo = grafo;
    }

    /**
     * Ejecuta BFS desde el nodo origen dado.
     * Muestra el orden de visita y el nivel de profundidad de cada municipio.
     *
     * @param origenId ID del municipio de partida
     */
    public void ejecutar(int origenId) {
        if (!grafo.existeMunicipio(origenId)) {
            System.out.println("⚠  El municipio con ID " + origenId + " no existe.");
            return;
        }

        Map<Integer, Boolean> visitado = new HashMap<>();
        Map<Integer, Integer> nivel    = new HashMap<>();
        Map<Integer, Integer> padre    = new HashMap<>();

        Queue<Integer> cola = new LinkedList<>();

        // Inicializar todos como no visitados
        for (int id : grafo.getMunicipios().keySet()) {
            visitado.put(id, false);
        }

        visitado.put(origenId, true);
        nivel.put(origenId, 0);
        padre.put(origenId, -1);
        cola.add(origenId);

        List<Integer> orden = new ArrayList<>();

        System.out.println("\n════════════════════════════════════════════════════");
        System.out.println("   RECORRIDO BFS desde: " + grafo.getNombre(origenId));
        System.out.println("════════════════════════════════════════════════════");
        System.out.printf("%-5s %-20s %-8s %-20s%n", "Paso", "Municipio", "Nivel", "Viene de");
        System.out.println("────────────────────────────────────────────────────");

        int paso = 1;

        while (!cola.isEmpty()) {
            int actual = cola.poll();
            orden.add(actual);

            String nombrePadre = padre.get(actual) == -1
                    ? "Origen"
                    : grafo.getNombre(padre.get(actual));

            System.out.printf("%-5d %-20s %-8d %-20s%n",
                    paso++,
                    grafo.getNombre(actual),
                    nivel.get(actual),
                    nombrePadre);

            Municipio m = grafo.getMunicipio(actual);
            // Ordenar vecinos por ID para resultado determinista
            List<Via> vias = new ArrayList<>(m.getConexiones());
            vias.sort(Comparator.comparingInt(Via::getMunicipioDestino));

            for (Via via : vias) {
                int vecino = via.getMunicipioDestino();
                if (!visitado.get(vecino)) {
                    visitado.put(vecino, true);
                    nivel.put(vecino, nivel.get(actual) + 1);
                    padre.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }

        System.out.println("════════════════════════════════════════════════════");
        System.out.println("  Municipios accesibles desde " + grafo.getNombre(origenId)
                + ": " + orden.size());

        // Detectar desconectados
        List<String> desconectados = new ArrayList<>();
        for (int id : grafo.getMunicipios().keySet()) {
            if (!visitado.getOrDefault(id, false)) {
                desconectados.add(grafo.getNombre(id));
            }
        }

        if (desconectados.isEmpty()) {
            System.out.println("  ✔ Todos los municipios son accesibles desde el origen.");
        } else {
            System.out.println("  ✘ Municipios NO accesibles: " + desconectados);
        }
        System.out.println("════════════════════════════════════════════════════\n");
    }
}
