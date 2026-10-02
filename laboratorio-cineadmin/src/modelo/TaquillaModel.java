package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MODELO: el "marcador oficial" de la taquilla. Guarda los boletos vendidos y aplica
 * las reglas de negocio. No conoce la interfaz gráfica: no importa nada de javax.swing.
 */
public class TaquillaModel {
    private ArrayList<Boleto> boletos = new ArrayList<>();

    // Acumulado de ventas: se actualiza en cada venta para no recorrer toda la lista al pedir el total
    private double gananciaTotal = 0;

    /**
     * Regla de negocio: la película y la sala son obligatorias y el precio debe ser mayor que cero.
     * Devuelve el boleto creado para que el Controlador pueda pintar solo esa fila nueva.
     */
    public Boleto agregarBoleto(String pelicula, String sala, double precio) {
        if (pelicula == null || pelicula.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la película es obligatorio.");
        }
        if (sala == null || sala.trim().isEmpty()) {
            throw new IllegalArgumentException("La sala es obligatoria.");
        }
        // NaN e infinito no son precios válidos
        if (Double.isNaN(precio) || Double.isInfinite(precio) || precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser un valor mayor que cero.");
        }

        Boleto boleto = new Boleto(pelicula.trim(), sala.trim(), precio);
        boletos.add(boleto);
        gananciaTotal += precio;
        return boleto;
    }

    public double obtenerGananciaTotal() {
        return gananciaTotal;
    }

    public int obtenerCantidadBoletos() {
        return boletos.size();
    }

    /**
     * Devuelve la lista en modo solo lectura: quien la recibe puede recorrerla,
     * pero no agregar ni borrar boletos saltándose las reglas del Modelo.
     */
    public List<Boleto> getBoletos() {
        return Collections.unmodifiableList(boletos);
    }
}
