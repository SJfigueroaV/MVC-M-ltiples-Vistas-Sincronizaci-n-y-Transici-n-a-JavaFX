import controlador.TaquillaController;
import javax.swing.SwingUtilities;
import modelo.TaquillaModel;
import vista.MainFrame;

/**
 * CLASE PRINCIPAL: crea el Modelo y las Vistas, y se los entrega todos al Controlador.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Creamos el Modelo (el marcador oficial) y la ventana con sus tres paneles
                TaquillaModel modelo = new TaquillaModel();
                MainFrame ventana = new MainFrame();

                // El Controlador recibe el Modelo y todas las vistas que debe mantener sincronizadas
                new TaquillaController(modelo, ventana, ventana.getPanelIngreso(),
                        ventana.getPanelHistorial(), ventana.getPanelDashboard());

                ventana.setVisible(true);
            }
        });
    }
}
