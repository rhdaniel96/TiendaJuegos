package cl.duoc.tiendajuegos.model;

public class JuegoMesa extends Producto {
    private int minJugadores;
    private int maxJugadores;
    private int edadMinima;

    public JuegoMesa(int idProducto, char tipo, String nombreProducto, int stock, int precio, int minJugadores, int maxJugadores, int edadMinima) {
        super(idProducto, tipo, nombreProducto, stock, precio);
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.edadMinima = edadMinima;
    }

    // GETTERS & SETTERS

    public int getMinJugadores() {
        return minJugadores;
    }

    public void setMinJugadores(int minJugadores) {
        this.minJugadores = minJugadores;
    }

    public int getMaxJugadores() {
        return maxJugadores;
    }

    public void setMaxJugadores(int maxJugadores) {
        this.maxJugadores = maxJugadores;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(int edadMinima) {
        this.edadMinima = edadMinima;
    }




}
