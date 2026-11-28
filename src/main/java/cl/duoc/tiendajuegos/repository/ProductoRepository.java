package cl.duoc.tiendajuegos.repository;

import cl.duoc.tiendajuegos.database.ConexionDB;
import cl.duoc.tiendajuegos.model.*;
import java.util.ArrayList;
import java.util.List;

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


    // CONSULTAR PRODUCTOS DE MARIA DB

    public List<Producto> listarProductos() throws SQLException {

        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT * FROM PRODUCTO ORDER BY IDPRODUCTO";

        try (
                Connection conexion = ConexionDB.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                // ATRIBUTOS GENERALES

                int id = rs.getInt("IDPRODUCTO");
                String tipoSQL = rs.getString("TIPO");

                if (tipoSQL == null || tipoSQL.isBlank()) {
                    throw new SQLException(
                            "Producto sin tipo. ID: " + id
                    );
                }

                char tipo = tipoSQL.charAt(0);

                String nombre = rs.getString("NOMBREPRODUCTO");
                int stock = rs.getInt("STOCK");
                int precio = rs.getInt("PRECIO");

                Producto producto;

                switch (tipo) {

                    case 'J':

                        producto = new JuegoMesa(
                                id, tipo, nombre, stock, precio,
                                rs.getInt("MINJUGADORES"),
                                rs.getInt("MAXJUGADORES"),
                                rs.getInt("EDADMINIMA")
                        );
                        break;

                    case 'T':

                        producto = new Tcg(
                                id, tipo, nombre, stock, precio,
                                rs.getString("FRANQUICIA"),
                                rs.getString("EDICION"),
                                rs.getString("TIPOPRODUCTO")
                        );
                        break;

                    case 'A':

                        producto = new Accesorio(
                                id, tipo, nombre, stock, precio,
                                rs.getString("TIPOACCESORIO"),
                                rs.getString("MARCA")
                        );
                        break;

                    default:

                        throw new SQLException(
                                "Tipo no valido para producto ID: " + id
                        );
                }

                productos.add(producto);
            }
        }

        return productos;
    }


    // VERIFICAR SI YA EXISTE UN PRODUCTO

    public boolean existeNombre(String nombre) throws SQLException {

        String sql = """
                SELECT 1
                FROM PRODUCTO
                WHERE NOMBREPRODUCTO = ?
                LIMIT 1
                """;

        try (
                Connection conexion = ConexionDB.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next();
            }
        }
    }


    // ACTUALIZAR UN CAMPO DE UN PRODUCTO

    public boolean actualizarCampo(
            int idProducto,
            String columna,
            Object nuevoValor) throws SQLException {

        // IDENTIFICAR LA COLUMNA DE MARIA DB

        String columnaSQL = switch (columna) {

            case "Nombre" -> "NOMBREPRODUCTO";
            case "Stock" -> "STOCK";
            case "Precio" -> "PRECIO";

            case "Min Jugadores" -> "MINJUGADORES";
            case "Max Jugadores" -> "MAXJUGADORES";
            case "Edad Minima" -> "EDADMINIMA";

            case "Franquicia" -> "FRANQUICIA";
            case "Edicion" -> "EDICION";
            case "Tipo Producto" -> "TIPOPRODUCTO";

            case "Tipo Accesorio" -> "TIPOACCESORIO";
            case "Marca" -> "MARCA";

            default -> throw new IllegalArgumentException(
                    "La columna seleccionada no se puede modificar"
            );
        };

        // CONSTRUIR LA CONSULTA

        String sql = "UPDATE PRODUCTO SET "
                + columnaSQL
                + " = ? WHERE IDPRODUCTO = ?";

        // EJECUTAR ACTUALIZACION

        try (
                Connection conexion = ConexionDB.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setObject(1, nuevoValor);
            ps.setInt(2, idProducto);

            int filasModificadas = ps.executeUpdate();

            return filasModificadas == 1;
        }
    }


    // ACTUALIZAR FRANQUICIA Y EDICION

    public boolean actualizarFranquiciaYEdicion(
            int idProducto,
            String franquicia,
            String edicion) throws SQLException {

        String sql = """
                UPDATE PRODUCTO
                SET FRANQUICIA = ?,
                    EDICION = ?
                WHERE IDPRODUCTO = ?
                  AND TIPO = 'T'
                """;

        try (
                Connection conexion = ConexionDB.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, franquicia);
            ps.setString(2, edicion);
            ps.setInt(3, idProducto);

            return ps.executeUpdate() == 1;
        }
    }


    // ACTUALIZAR TIPO DE ACCESORIO Y MARCA

    public boolean actualizarTipoAccesorioYMarca(
            int idProducto,
            String tipoAccesorio,
            String marca) throws SQLException {

        String sql = """
                UPDATE PRODUCTO
                SET TIPOACCESORIO = ?,
                    MARCA = ?
                WHERE IDPRODUCTO = ?
                  AND TIPO = 'A'
                """;

        try (
                Connection conexion = ConexionDB.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, tipoAccesorio);
            ps.setString(2, marca);
            ps.setInt(3, idProducto);

            return ps.executeUpdate() == 1;
        }
    }


}
