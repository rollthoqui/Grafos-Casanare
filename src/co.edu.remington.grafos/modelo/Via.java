package co.edu.remington.grafos.modelo;

/**
 * Representa una arista (vía) del grafo de Casanare.
 * Contiene el municipio destino, la distancia en km y el estado de la vía.
 */
public class Via {

    private int municipioDestino;
    private double distanciaKm;
    private String estado; // "Bueno", "Regular", "Malo"

    public Via(int municipioDestino, double distanciaKm, String estado) {
        this.municipioDestino = municipioDestino;
        this.distanciaKm = distanciaKm;
        this.estado = estado;
    }

    // ─── Getters ────────────────────────────────────────────────────────────

    public int getMunicipioDestino() {
        return municipioDestino;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public String getEstado() {
        return estado;
    }

    /**
     * Devuelve la distancia penalizada según el estado de la vía:
     *   Malo    → ×1.5
     *   Regular → ×1.2
     *   Bueno   → ×1.0
     */
    public double getDistanciaPenalizada() {
        switch (estado) {
            case "Malo":    return distanciaKm * 1.5;
            case "Regular": return distanciaKm * 1.2;
            default:        return distanciaKm;
        }
    }

    @Override
    public String toString() {
        return String.format("→ Municipio[%d] | %.1f km | Estado: %s",
                municipioDestino, distanciaKm, estado);
    }
}
