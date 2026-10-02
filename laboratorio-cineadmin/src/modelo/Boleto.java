package modelo;

/**
 * Un boleto vendido en la taquilla. Es inmutable: una vez emitido, sus datos no cambian.
 */
public class Boleto {
    private final String pelicula;
    private final String sala;
    private final double precio;

    public Boleto(String pelicula, String sala, double precio) {
        this.pelicula = pelicula;
        this.sala = sala;
        this.precio = precio;
    }

    public String getPelicula() {
        return pelicula;
    }

    public String getSala() {
        return sala;
    }

    public double getPrecio() {
        return precio;
    }
}
