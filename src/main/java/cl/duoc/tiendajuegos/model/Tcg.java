package cl.duoc.tiendajuegos.model;

public class Tcg extends Producto{
    private String franquicia;
    private String edicion;
    private String tipoProducto;

    //CONSTRUCTOR

    public Tcg(int idProducto, char tipo, String nombreProducto, int stock, int precio, String franquicia, String edicion, String tipoProducto) {
        super(idProducto, tipo, nombreProducto, stock, precio);
        this.franquicia = franquicia;
        this.edicion = edicion;
        this.tipoProducto = tipoProducto;
    }

    // GETTERS & SETTERS


    public String getFranquicia() {
        return franquicia;
    }

    public void setFranquicia(String franquicia) {
        this.franquicia = franquicia;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }
}
