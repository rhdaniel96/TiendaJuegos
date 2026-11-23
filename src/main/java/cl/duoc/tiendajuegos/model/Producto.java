package cl.duoc.tiendajuegos.model;

public abstract class Producto {
    private int idProducto;
    private char tipo; // J - T - A -> autoasignado segun seleccion de ComboBox mantenedor
    private String nombreProducto;
    private int stock;
    private int precio;

    public Producto(int idProducto, char tipo, String nombreProducto, int stock, int precio) {
        this.idProducto = idProducto;
        this.tipo = tipo;
        this.nombreProducto = nombreProducto;
        this.stock = stock;
        this.precio = precio;
    }

    // GETTER & SETTERs

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}

