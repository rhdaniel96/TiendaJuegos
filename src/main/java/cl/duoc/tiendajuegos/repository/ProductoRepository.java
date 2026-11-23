package cl.duoc.tiendajuegos.repository;

import cl.duoc.tiendajuegos.database.ConexionDB;
import cl.duoc.tiendajuegos.model.*;

import java.sql.*;

public class ProductoRepository {

    // AGREGAR PRODUCTO A LA BASE DE DATOS

    public void guardar(Producto producto) throws SQLException {

        String sql = """
                INSERT INTO PRODUCTO (
                    TIPO,
                    NOMBREPRODUCTO,
                    STOCK,
                    PRECIO,
                    MINJUGADORES,
                    MAXJUGADORES,
                    EDADMINIMA,
                    FRANQUICIA,
                    EDICION,
                    TIPOPRODUCTO,
                    TIPOACCESORIO,
                    MARCA
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        // ATRIBUTOS ESPECIFICOS: NULL POR DEFECTO

        Integer minJug = null;
        Integer maxJug = null;
        Integer edadMin = null;

        String franquicia = null;
        String edicion = null;
        String tipoTCG = null;

        String tipoAccesorio = null;
        String marca = null;

        // EXTRAER DATOS SEGUN LA CLASE

        if (producto instanceof JuegoMesa juego) {

            minJug = juego.getMinJugadores();
            maxJug = juego.getMaxJugadores();
            edadMin = juego.getEdadMinima();

        } else if (producto instanceof Tcg tcg) {

            franquicia = tcg.getFranquicia();
            edicion = tcg.getEdicion();
            tipoTCG = tcg.getTipoProducto();

        } else if (producto instanceof Accesorio accesorio) {

            tipoAccesorio = accesorio.getTipoProducto();
            marca = accesorio.getMarca();
        }

        // EJECUTAR INSERT

        try (
                Connection conexion = ConexionDB.getConnection();
                PreparedStatement ps = conexion.prepareStatement(
                        sql, Statement.RETURN_GENERATED_KEYS
                )
        ) {

            // CAMPOS GENERALES

            ps.setString(1, String.valueOf(producto.getTipo()));
            ps.setString(2, producto.getNombreProducto());
            ps.setInt(3, producto.getStock());
            ps.setInt(4, producto.getPrecio());

            // CAMPOS ESPECIFICOS

            ps.setObject(5, minJug, Types.INTEGER);
            ps.setObject(6, maxJug, Types.INTEGER);
            ps.setObject(7, edadMin, Types.INTEGER);

            ps.setObject(8, franquicia, Types.VARCHAR);
            ps.setObject(9, edicion, Types.VARCHAR);
            ps.setObject(10, tipoTCG, Types.VARCHAR);

            ps.setObject(11, tipoAccesorio, Types.VARCHAR);
            ps.setObject(12, marca, Types.VARCHAR);

            ps.executeUpdate();

            // RECUPERAR ID GENERADO POR MARIA DB

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    producto.setIdProducto(rs.getInt(1));
                }
            }
        }
    }
}
