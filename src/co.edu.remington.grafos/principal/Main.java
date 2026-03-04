package co.edu.remington.grafos.principal;

import co.edu.remington.grafos.algoritmos.Dijkstra;
import co.edu.remington.grafos.algoritmos.RecorridoBFS;
import co.edu.remington.grafos.algoritmos.RecorridoDFS;
import co.edu.remington.grafos.modelo.GrafoCasanare;
import co.edu.remington.grafos.utilidades.MatrizAdyacencia;

import java.util.Scanner;

/**
 * Punto de entrada del sistema de planificación de rutas de Casanare.
 * Carga los datos del problema y presenta un menú interactivo por consola.
 */
public class Main {

    private static GrafoCasanare      grafo;
    private static RecorridoBFS       bfs;
    private static RecorridoDFS       dfs;
    private static Dijkstra           dijkstra;
    private static MatrizAdyacencia   matriz;
    private static Scanner            sc = new Scanner(System.in);

    public static void main(String[] args) {

        cargarDatos();

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = leerEntero("  Seleccione una opción: ");
            System.out.println();

            switch (opcion) {
                // ── PARTE A ──────────────────────────────────────────────
                case 1  -> grafo.listarMunicipios();
                case 2  -> grafo.mostrarGrafo();

                // ── PARTE B ──────────────────────────────────────────────
                case 3  -> bfs.ejecutar(0);
                case 4  -> dfs.ejecutar(0);
                case 5  -> {
                    System.out.print("  Ingrese ID del municipio de inicio: ");
                    int id = leerEntero("");
                    bfs.ejecutar(id);
                }
                case 6  -> {
                    System.out.print("  Ingrese ID del municipio de inicio: ");
                    int id = leerEntero("");
                    dfs.ejecutar(id);
                }

                // ── PARTE C ──────────────────────────────────────────────
                case 7  -> dijkstra.calcularDesdeOrigen(0, false);
                case 8  -> dijkstra.calcularDesdeOrigen(0, true);
                case 9  -> mostrarRutasPredefinidas();
                case 10 -> {
                    grafo.listarMunicipios();
                    int origen  = leerEntero("  ID municipio origen : ");
                    int destino = leerEntero("  ID municipio destino: ");
                    System.out.println("\n  ── Ruta sin penalización ──");
                    dijkstra.calcularRuta(origen, destino, false);
                    System.out.println("  ── Ruta con penalización por estado de vía ──");
                    dijkstra.calcularRuta(origen, destino, true);
                    System.out.println();
                }

                // ── PARTE D ──────────────────────────────────────────────
                case 11 -> matriz.esConexo();
                case 12 -> matriz.identificarMunicipiosPuente();
                case 13 -> matriz.mostrar();
                case 14 -> registrarMunicipio();
                case 15 -> registrarVia();

                case 0  -> {
                    System.out.println("  👋 Hasta pronto. Sistema de rutas de Casanare finalizado.\n");
                    salir = true;
                }
                default -> System.out.println("  ⚠  Opción no válida. Intente de nuevo.\n");
            }
        }
        sc.close();
    }

    // ─── Carga de datos del problema ─────────────────────────────────────────

    private static void cargarDatos() {
        grafo = new GrafoCasanare();

        // ── Nodos: Municipios ──────────────────────────────────────────────
        grafo.agregarMunicipio(0, "Yopal");
        grafo.agregarMunicipio(1, "Aguazul");
        grafo.agregarMunicipio(2, "Tauramena");
        grafo.agregarMunicipio(3, "Maní");
        grafo.agregarMunicipio(4, "Orocué");
        grafo.agregarMunicipio(5, "Villanueva");
        grafo.agregarMunicipio(6, "Monterrey");
        grafo.agregarMunicipio(7, "Paz de Ariporo");
        grafo.agregarMunicipio(8, "Trinidad");
        grafo.agregarMunicipio(9, "Hato Corozal");

        // ── Aristas: Vías (bidireccionales) ───────────────────────────────
        grafo.agregarVia(0, 1, 28,  "Bueno");
        grafo.agregarVia(0, 7, 92,  "Regular");
        grafo.agregarVia(0, 3, 65,  "Bueno");
        grafo.agregarVia(1, 2, 35,  "Bueno");
        grafo.agregarVia(1, 3, 42,  "Regular");
        grafo.agregarVia(2, 5, 48,  "Bueno");
        grafo.agregarVia(2, 6, 55,  "Malo");
        grafo.agregarVia(3, 4, 78,  "Regular");
        grafo.agregarVia(5, 6, 22,  "Bueno");
        grafo.agregarVia(7, 8, 45,  "Regular");
        grafo.agregarVia(7, 9, 38,  "Bueno");
        grafo.agregarVia(8, 4, 95,  "Malo");
        grafo.agregarVia(9, 8, 52,  "Regular");

        // ── Instanciar algoritmos ──────────────────────────────────────────
        bfs      = new RecorridoBFS(grafo);
        dfs      = new RecorridoDFS(grafo);
        dijkstra = new Dijkstra(grafo);
        matriz   = new MatrizAdyacencia(grafo);

        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║  SISTEMA DE PLANIFICACIÓN DE RUTAS - CASANARE        ║");
        System.out.println("║  Gobernación de Casanare | Red Vial Inteligente       ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println("  ✔ Red vial cargada: 10 municipios, 13 vías.\n");
    }

    // ─── Menú principal ───────────────────────────────────────────────────────

    private static void mostrarMenu() {
        System.out.println("┌─────────────────────────────────────────────────────┐");
        System.out.println("│                    MENÚ PRINCIPAL                    │");
        System.out.println("├───────────────────────────────────────────────────── ┤");
        System.out.println("│  PARTE A — Estructura del grafo                      │");
        System.out.println("│   1. Listar municipios                               │");
        System.out.println("│   2. Mostrar grafo (lista de adyacencia)             │");
        System.out.println("├──────────────────────────────────────────────────────┤");
        System.out.println("│  PARTE B — Recorridos                                │");
        System.out.println("│   3. BFS desde Yopal                                 │");
        System.out.println("│   4. DFS desde Yopal                                 │");
        System.out.println("│   5. BFS desde municipio personalizado               │");
        System.out.println("│   6. DFS desde municipio personalizado               │");
        System.out.println("├──────────────────────────────────────────────────────┤");
        System.out.println("│  PARTE C — Rutas más cortas (Dijkstra)               │");
        System.out.println("│   7. Dijkstra desde Yopal (sin penalización)         │");
        System.out.println("│   8. Dijkstra desde Yopal (con penalización)         │");
        System.out.println("│   9. Rutas predefinidas + comparación                │");
        System.out.println("│  10. Ruta entre dos municipios a elegir              │");
        System.out.println("├──────────────────────────────────────────────────────┤");
        System.out.println("│  PARTE D — Análisis de red y gestión                 │");
        System.out.println("│  11. Verificar si el grafo es conexo                 │");
        System.out.println("│  12. Identificar municipios puente                   │");
        System.out.println("│  13. Mostrar matriz de adyacencia                    │");
        System.out.println("│  14. Registrar nuevo municipio                       │");
        System.out.println("│  15. Registrar nueva vía                             │");
        System.out.println("├──────────────────────────────────────────────────────┤");
        System.out.println("│   0. Salir                                            │");
        System.out.println("└──────────────────────────────────────────────────────┘");
    }

    // ─── Opción 9: rutas predefinidas con comparación ────────────────────────

    private static void mostrarRutasPredefinidas() {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("   RUTAS PREDEFINIDAS — COMPARACIÓN NORMAL vs PENALIZADA");
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.printf("%-34s %-15s %-15s%n", "Ruta", "Sin penal. (km)", "Con penal. (km)");
        System.out.println("────────────────────────────────────────────────────────────");

        int[][] pares = {{0, 6}, {0, 4}, {9, 5}};
        for (int[] par : pares) {
            Dijkstra.ResultadoDijkstra normal = dijkstra.ejecutar(par[0], false);
            Dijkstra.ResultadoDijkstra penal  = dijkstra.ejecutar(par[0], true);

            double dNormal = normal.distancias.get(par[1]);
            double dPenal  = penal.distancias.get(par[1]);

            System.out.printf("%-34s %-15.2f %-15.2f%n",
                    grafo.getNombre(par[0]) + " → " + grafo.getNombre(par[1]),
                    dNormal, dPenal);
        }
        System.out.println("════════════════════════════════════════════════════════════");

        // Mostrar rutas detalladas
        System.out.println("\n  Detalle de rutas (sin penalización):");
        dijkstra.calcularRuta(0, 6, false);   // Yopal → Monterrey
        dijkstra.calcularRuta(0, 4, false);   // Yopal → Orocué
        dijkstra.calcularRuta(9, 5, false);   // Hato Corozal → Villanueva

        System.out.println("\n  Detalle de rutas (con penalización por estado):");
        dijkstra.calcularRuta(0, 6, true);
        dijkstra.calcularRuta(0, 4, true);
        dijkstra.calcularRuta(9, 5, true);
        System.out.println();
    }

    // ─── Opción 14: registrar nuevo municipio ────────────────────────────────

    private static void registrarMunicipio() {
        System.out.println("  ── Registrar nuevo municipio ──");
        int id = leerEntero("  ID del nuevo municipio: ");
        if (grafo.existeMunicipio(id)) {
            System.out.println("  ⚠ Ya existe un municipio con ese ID.\n");
            return;
        }
        System.out.print("  Nombre del municipio: ");
        String nombre = sc.nextLine().trim();
        grafo.agregarMunicipio(id, nombre);
        System.out.println("  ✔ Municipio '" + nombre + "' agregado con ID " + id + ".\n");
    }

    // ─── Opción 15: registrar nueva vía ──────────────────────────────────────

    private static void registrarVia() {
        System.out.println("  ── Registrar nueva vía (bidireccional) ──");
        grafo.listarMunicipios();

        int origen  = leerEntero("  ID municipio origen : ");
        int destino = leerEntero("  ID municipio destino: ");

        if (!grafo.existeMunicipio(origen) || !grafo.existeMunicipio(destino)) {
            System.out.println("  ⚠ Uno o ambos municipios no existen.\n");
            return;
        }

        double dist = leerDouble("  Distancia en km     : ");

        System.out.print("  Estado de la vía (Bueno / Regular / Malo): ");
        String estado = sc.nextLine().trim();
        if (!estado.equals("Bueno") && !estado.equals("Regular") && !estado.equals("Malo")) {
            System.out.println("  ⚠ Estado no válido. Debe ser: Bueno, Regular o Malo.\n");
            return;
        }

        grafo.agregarVia(origen, destino, dist, estado);
        System.out.printf("  ✔ Vía agregada: %s ↔ %s | %.1f km | %s%n%n",
                grafo.getNombre(origen), grafo.getNombre(destino), dist, estado);
    }

    // ─── Helpers de lectura ───────────────────────────────────────────────────

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Ingrese un número entero válido.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Ingrese un número decimal válido.");
            }
        }
    }
}
