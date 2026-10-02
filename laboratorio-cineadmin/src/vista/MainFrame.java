package vista;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * VISTA PRINCIPAL: la ventana que reparte las tres regiones con BorderLayout.
 * Izquierda: ingreso. Centro: historial. Abajo: dashboard.
 */
public class MainFrame extends JFrame {
    private PanelIngreso panelIngreso = new PanelIngreso();
    private PanelHistorial panelHistorial = new PanelHistorial();
    private PanelDashboard panelDashboard = new PanelDashboard();

    public MainFrame() {
        super("CineAdmin - Taquilla");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        add(panelIngreso, BorderLayout.WEST);
        add(panelHistorial, BorderLayout.CENTER);
        add(panelDashboard, BorderLayout.SOUTH);

        setSize(820, 480);
        setLocationRelativeTo(null);
    }

    public PanelIngreso getPanelIngreso() {
        return panelIngreso;
    }

    public PanelHistorial getPanelHistorial() {
        return panelHistorial;
    }

    public PanelDashboard getPanelDashboard() {
        return panelDashboard;
    }

    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
