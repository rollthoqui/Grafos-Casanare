package co.edu.remington.grafos.utilidades;

import co.edu.remington.grafos.modelo.GrafoCasanare;
import co.edu.remington.grafos.modelo.Municipio;
import co.edu.remington.grafos.modelo.Via;


import java.util.*;

/**
 * Utilidades para visualizar la red vial de Casanare.
 * Incluye:
 *  - Matriz de adyacencia (distancias directas)
 *  - Detección de grafo conexo
 *  - Identificación de municipios puente (nodos críticos)
 */
public class MatrizAdyacencia {

    private GrafoCasanare grafo;

    public MatrizAdyacencia(GrafoCasanare grafo) {
        this.grafo = grafo;
    }

    // ─── Matriz de adyacencia ─────────────────────────────────────────────────

    /**
     * Imprime la matriz de adyacencia con las distancias directas en km.
     * Muestra 0 en la diagonal y ∞ donde no hay vía directa.
     */
    public void mostrar() {
        List<Integer> ids = new ArrayList<>(grafo.getMunicipios().keySet());
        Collections.sort(ids);
        int n = ids.size();

        // Construir mapa id → índice de columna
        Map<Integer, Integer> indice = new HashMap<>();
        for (int i = 0; i < n; i++) indice.put(ids.get(i), i);

        // Llenar matriz
        double[][] mat = new double[n][n];
        for (double[] fila : mat) Arrays.fill(fila, Double.MAX_VALUE);
        for (int i = 0; i < n; i++) mat[i][i] = 0;

        for (int id : ids) {
            Municipio m = grafo.getMunicipio(id);
            for (Via v : m.getConexiones()) {
                mat[indice.get(id)][indice.get(v.getMunicipioDestino())] = v.getDistanciaKm();
            }
        }

        System.out.println("\n════════════════════════════════════════════════════════════════════");
        System.out.println("                       MATRIZ DE ADYACENCIA (km)");
        System.out.println("════════════════════════════════════════════════════════════════════");

        // Encabezado de columnas
        System.out.printf("%-14s", "");
        for (int id : ids) System.out.printf("%-10s", abreviar(grafo.getNombre(id)));
        System.out.println();
        System.out.println("──────────────────────────────────────────────────────────────────");

        // Filas
        for (int i = 0; i < n; i++) {
            System.out.printf("%-14s", abreviar(grafo.getNombre(ids.get(i))));
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == Double.MAX_VALUE) System.out.printf("%-10s", "∞");
                else if (mat[i][j] == 0)           System.out.printf("%-10s", "0");
                else                               System.out.printf("%-10.0f", mat[i][j]);
            }
            System.out.println();
        }
        System.out.println("════════════════════════════════════════════════════════════════════\n");
    }

    // ─── Conectividad ─────────────────────────────────────────────────────────

    /**
     * Determina si el grafo es conexo (todos los municipios están conectados).
     * @return true si el grafo es conexo
     */
    public boolean esConexo() {
        if (grafo.getTotalMunicipios() == 0) return true;

        int origen = grafo.getMunicipios().keySet().iterator().next();
        Set<Integer> visitados = bfsVisitados(origen);

        boolean conexo = visitados.size() == grafo.getTotalMunicipios();

        System.out.println("\n──────────────────────────────────────────────────");
        System.out.println("  ANÁLISIS DE CONECTIVIDAD DEL GRAFO");
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  Municipios totales : " + grafo.getTotalMunicipios());
        System.out.println("  Municipios alcanzados desde origen: " + visitados.size());

        if (conexo) {
            System.out.println("  ✔ El grafo ES CONEXO.");
            System.out.println("    Todos los municipios están interconectados.");
        } else {
            System.out.println("  ✘ El grafo NO ES CONEXO.");
            for (int id : grafo.getMunicipios().keySet()) {
                if (!visitados.contains(id)) {
                    System.out.println("    Municipio desconectado: " + grafo.getNombre(id));
                }
            }
        }
        System.out.println("──────────────────────────────────────────────────\n");

        return conexo;
    }

    // ─── Municipios puente ────────────────────────────────────────────────────

    /**
     * Identifica los "municipios puente": nodos cuya eliminación desconecta el grafo.
     * Utiliza el método de fuerza bruta: eliminar cada nodo y verificar conectividad.
     */
    public void identificarMunicipiosPuente() {
        System.out.println("\n──────────────────────────────────────────────────");
        System.out.println("  MUNICIPIOS PUENTE (nodos críticos de conectividad)");
        System.out.println("──────────────────────────────────────────────────");

        List<String> puentes = new ArrayList<>();

        for (int candidato : grafo.getMunicipios().keySet()) {
            if (esNodoPuente(candidato)) {
                puentes.add(String.format("  ★ %-20s (ID %d)", grafo.getNombre(candidato), candidato));
            }
        }

        if (puentes.isEmpty()) {
            System.out.println("  No se encontraron municipios puente.");
            System.out.println("  La red vial es robusta: eliminar cualquier municipio");
            System.out.println("  no desconecta la red completamente.");
        } else {
            System.out.println("  Los siguientes municipios son CRÍTICOS para la red:");
            for (String p : puentes) System.out.println(p);
            System.out.println();
            System.out.println("  ⚠ Su eliminación desconectaría la red vial.");
        }
        System.out.println("──────────────────────────────────────────────────\n");
    }

    // ─── Métodos auxiliares ───────────────────────────────────────────────────

    /**
     * Verifica si al eliminar el nodo 'id', el grafo queda desconectado.
     */
    private boolean esNodoPuente(int id) {
        int totalSinNodo = grafo.getTotalMunicipios() - 1;
        if (totalSinNodo <= 1) return false;

        // Buscar un nodo de partida diferente al candidato
        int origen = -1;
        for (int k : grafo.getMunicipios().keySet()) {
            if (k != id) { origen = k; break; }
        }

        Set<Integer> visitados = bfsExcluyendo(origen, id);
        return visitados.size() < totalSinNodo;
    }

    /** BFS que excluye un nodo del recorrido. */
    private Set<Integer> bfsExcluyendo(int origen, int excluido) {
        Set<Integer> visitados = new HashSet<>();
        Queue<Integer> cola = new LinkedList<>();

        visitados.add(origen);
        cola.add(origen);

        while (!cola.isEmpty()) {
            int actual = cola.poll();
            Municipio m = grafo.getMunicipio(actual);
            for (Via v : m.getConexiones()) {
                int vecino = v.getMunicipioDestino();
                if (vecino != excluido && !visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        }
        return visitados;
    }

    /** BFS normal para verificar conectividad. */
    private Set<Integer> bfsVisitados(int origen) {
        Set<Integer> visitados = new HashSet<>();
        Queue<Integer> cola = new LinkedList<>();
        visitados.add(origen);
        cola.add(origen);

        while (!cola.isEmpty()) {
            int actual = cola.poll();
            for (Via v : grafo.getMunicipio(actual).getConexiones()) {
                int vecino = v.getMunicipioDestino();
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        }
        return visitados;
    }

    /** Abrevia nombres largos para la matriz. */
    private String abreviar(String nombre) {
        return nombre.length() > 9 ? nombre.substring(0, 8) + "." : nombre;
    }
}
