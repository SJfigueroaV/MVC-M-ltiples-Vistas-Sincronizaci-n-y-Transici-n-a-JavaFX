package vista;

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * VISTA 3 (Dashboard): las métricas en tiempo real. Muestra el texto que le entregue
 * el Controlador; no suma ni cuenta nada por su cuenta.
 */
public class PanelDashboard extends JPanel {
    private JLabel lblTotalVendido = new JLabel("Total Vendido: $0", SwingConstants.CENTER);
    private JLabel lblBoletosEmitidos = new JLabel("Boletos Emitidos: 0", SwingConstants.CENTER);

    public PanelDashboard() {
        setLayout(new GridLayout(1, 2, 10, 0));
        setBorder(BorderFactory.createTitledBorder("Dashboard"));

        Font grande = new Font(Font.SANS_SERIF, Font.BOLD, 24);
        lblTotalVendido.setFont(grande);
        lblBoletosEmitidos.setFont(grande);

        add(lblTotalVendido);
        add(lblBoletosEmitidos);
    }

    public void setTotalVendido(String texto) {
        lblTotalVendido.setText(texto);
    }

    public void setBoletosEmitidos(String texto) {
        lblBoletosEmitidos.setText(texto);
    }
}
