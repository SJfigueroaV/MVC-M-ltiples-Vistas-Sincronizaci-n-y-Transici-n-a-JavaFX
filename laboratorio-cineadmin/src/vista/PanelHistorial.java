package vista;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * VISTA 2 (Historial): el JTable con los boletos vendidos, envuelto en un JScrollPane.
 * La tabla arranca vacía; el Controlador es quien le agrega las filas.
 */
public class PanelHistorial extends JPanel {
    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public PanelHistorial() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Historial de ventas"));

        String[] columnas = { "#", "Película", "Sala", "Precio" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false; // El historial es de solo lectura
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    // El "lienzo" donde el Controlador pinta las filas
    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    // Lleva la vista hasta la última fila para que la venta recién hecha quede a la vista
    public void mostrarUltimaFila() {
        int ultima = tabla.getRowCount() - 1;
        if (ultima >= 0) {
            tabla.scrollRectToVisible(tabla.getCellRect(ultima, 0, true));
        }
    }
}
