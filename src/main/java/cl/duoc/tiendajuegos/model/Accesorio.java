package cl.duoc.tiendajuegos.model;

public class Accesorio extends Producto{
    private String tipoAccesorio;
    private String marca;

    // CONSTRUCTOR

    public Accesorio(int idProducto, char tipo, String nombreProducto, int stock, int precio, String tipoProducto, String marca) {
        super(idProducto, tipo, nombreProducto, stock, precio);
        this.tipoAccesorio = tipoProducto;
        this.marca = marca;
    }

    // GETTERS & SETEERS


    public String getTipoProducto() {
        return tipoAccesorio;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoAccesorio = tipoProducto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
}
