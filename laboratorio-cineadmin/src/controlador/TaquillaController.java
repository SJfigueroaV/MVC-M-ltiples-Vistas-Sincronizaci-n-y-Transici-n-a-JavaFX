package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;
import modelo.Boleto;
import modelo.TaquillaModel;
import vista.MainFrame;
import vista.PanelDashboard;
import vista.PanelHistorial;
import vista.PanelIngreso;

/**
 * CONTROLADOR ORQUESTADOR: recibe el Modelo y TODAS las vistas. Cuando se vende un boleto,
 * actualiza el Modelo y sincroniza el historial y el dashboard en el mismo clic.
 */
public class TaquillaController {
    // Pesos colombianos: separador de miles con punto (ej: $12.000)
    private static final Locale COLOMBIA = Locale.forLanguageTag("es-CO");

    private TaquillaModel modelo;
    private MainFrame ventana;
    private PanelIngreso vistaIngreso;
    private PanelHistorial vistaHistorial;
    private PanelDashboard vistaDashboard;

    // El constructor recibe a todos los actores
    public TaquillaController(TaquillaModel modelo, MainFrame ventana, PanelIngreso vistaIngreso,
                              PanelHistorial vistaHistorial, PanelDashboard vistaDashboard) {
        this.modelo = modelo;
        this.ventana = ventana;
        this.vistaIngreso = vistaIngreso;
        this.vistaHistorial = vistaHistorial;
        this.vistaDashboard = vistaDashboard;

        // Escuchar el clic del botón "Vender"
        this.vistaIngreso.agregarListenerVender(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarVenta();
            }
        });

        // Al arrancar, el dashboard muestra lo que diga el Modelo (no un texto fijo de la Vista)
        actualizarDashboard();
    }

    private void procesarVenta() {
        // 1. Leer la Vista de ingreso
        String pelicula = vistaIngreso.getPelicula();
        String sala = vistaIngreso.getSala();
        String textoPrecio = vistaIngreso.getPrecio();

        // Validación temprana: ninguna caja puede estar vacía antes de convertir a número
        if (pelicula.isEmpty() || sala.isEmpty() || textoPrecio.isEmpty()) {
            ventana.mostrarMensajeError("Por favor complete la película, la sala y el precio.");
            return;
        }

        try {
            // Se acepta coma o punto como separador decimal
            double precio = Double.parseDouble(textoPrecio.replace(',', '.'));

            // 2. a) Crear el boleto en el Modelo
            Boleto boleto = modelo.agregarBoleto(pelicula, sala, precio);

            // 3. b) Agregar SOLO la fila nueva a la tabla (no se repinta todo el historial)
            agregarFilaHistorial(boleto);

            // 4. c) Pedir los nuevos totales al Modelo y refrescar el dashboard
            actualizarDashboard();

            // 5. Limpiar la Vista de ingreso
            vistaIngreso.limpiarCampos();

        } catch (NumberFormatException ex) {
            ventana.mostrarMensajeError("El precio debe ser un valor numérico (ej: 12000).");
        } catch (IllegalArgumentException ex) {
            ventana.mostrarMensajeError(ex.getMessage());
        }
    }

    private void agregarFilaHistorial(Boleto boleto) {
        DefaultTableModel lienzo = vistaHistorial.getModeloTabla();
        Object[] filaNueva = {
            modelo.obtenerCantidadBoletos(), boleto.getPelicula(), boleto.getSala(), formatearDinero(boleto.getPrecio())
        };
        lienzo.addRow(filaNueva);
        vistaHistorial.mostrarUltimaFila();
    }

    private void actualizarDashboard() {
        vistaDashboard.setTotalVendido("Total Vendido: " + formatearDinero(modelo.obtenerGananciaTotal()));
        vistaDashboard.setBoletosEmitidos("Boletos Emitidos: " + modelo.obtenerCantidadBoletos());
    }

    // Los centavos solo se muestran si el valor los tiene (ej: $12.000 o $12.000,50)
    private String formatearDinero(double valor) {
        if (valor == Math.rint(valor)) {
            return String.format(COLOMBIA, "$%,.0f", valor);
        }
        return String.format(COLOMBIA, "$%,.2f", valor);
    }
}
