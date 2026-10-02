package vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * VISTA 1 (Ingreso): las cajas de texto y el botón "Vender". Solo muestra y entrega
 * lo que el usuario escribió; no valida ni calcula nada.
 */
public class PanelIngreso extends JPanel {
    private JTextField txtPelicula = new JTextField(12);
    private JTextField txtSala = new JTextField(12);
    private JTextField txtPrecio = new JTextField(12);
    private JButton btnVender = new JButton("Vender");

    public PanelIngreso() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Venta de boletos"));

        JPanel campos = new JPanel(new GridLayout(6, 1, 0, 4));
        campos.add(new JLabel("Película:"));
        campos.add(txtPelicula);
        campos.add(new JLabel("Sala:"));
        campos.add(txtSala);
        campos.add(new JLabel("Precio ($):"));
        campos.add(txtPrecio);

        // Campos y botón juntos en la parte de arriba del panel
        JPanel formulario = new JPanel(new BorderLayout(0, 10));
        formulario.add(campos, BorderLayout.CENTER);
        formulario.add(btnVender, BorderLayout.SOUTH);

        add(formulario, BorderLayout.NORTH);
    }

    public String getPelicula() {
        return txtPelicula.getText().trim();
    }

    public String getSala() {
        return txtSala.getText().trim();
    }

    public String getPrecio() {
        return txtPrecio.getText().trim();
    }

    public void agregarListenerVender(ActionListener listener) {
        btnVender.addActionListener(listener);
    }

    public void limpiarCampos() {
        txtPelicula.setText("");
        txtSala.setText("");
        txtPrecio.setText("");
        txtPelicula.requestFocusInWindow();
    }
}
