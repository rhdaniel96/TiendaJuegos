package cl.duoc.tiendajuegos.repository;

import cl.duoc.tiendajuegos.database.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class CatalogoRepository {

    // LISTAR FRANQUICIAS

    public List<String> listarFranquicias() throws SQLException {

        String sql = "SELECT FRANQUICIA FROM FRANQUICIA "
                + "ORDER BY FRANQUICIA";

        return listarOpciones(sql);
    }

    // LISTAR TIPOS DE PRODUCTO TCG

    public List<String> listarTiposTCG() throws SQLException {

        String sql = "SELECT TIPOPRODUCTO FROM TIPO_PRODUCTO "
                + "WHERE TIPOPRODUCTO IS NOT NULL "
                + "ORDER BY TIPOPRODUCTO";

        return listarOpciones(sql);
    }

    // LISTAR TIPOS DE ACCESORIOS

    public List<String> listarTiposAccesorios() throws SQLException {

        String sql = "SELECT TIPOACCESORIO FROM TIPO_ACCESORIO "
                + "WHERE TIPOACCESORIO IS NOT NULL "
                + "ORDER BY TIPOACCESORIO";

        return listarOpciones(sql);
    }

    // LISTAR TODAS LAS MARCAS

    public List<String> listarMarcas() throws SQLException {

        String sql = "SELECT MARCA FROM MARCA "
                + "WHERE MARCA IS NOT NULL "
                + "ORDER BY MARCA";

        return listarOpciones(sql);
    }

    // LISTAR MARCAS SEGUN TIPO DE ACCESORIO

    public List<String> listarMarcas(String tipoAccesorio)
            throws SQLException {

        String sql = """
                SELECT M.MARCA
                FROM MARCA M
                INNER JOIN MARCA_ACCESORIO MA
                    ON M.IDMARCA = MA.IDMARCA
                INNER JOIN TIPO_ACCESORIO TA
                    ON MA.IDTIPOACCESORIO = TA.IDTIPOACCESORIO
                WHERE TA.TIPOACCESORIO = ?
                AND M.MARCA IS NOT NULL
                ORDER BY M.MARCA
                """;

        return listarOpciones(sql, tipoAccesorio);
    }

    // LISTAR EDICIONES SEGUN FRANQUICIA

    public List<String> listarEdiciones(String franquicia)
            throws SQLException {

        String sql = """
                SELECT E.EDICION
                FROM EDICION E
                INNER JOIN FRANQUICIA F
                    ON E.IDFRANQUICIA = F.IDFRANQUICIA
                WHERE F.FRANQUICIA = ?
                AND E.EDICION IS NOT NULL
                ORDER BY E.EDICION
                """;

        return listarOpciones(sql, franquicia);
    }

    // METODO GENERAL PARA OBTENER OPCIONES

    private List<String> listarOpciones(
            String sql, String... parametros)
            throws SQLException {

        List<String> opciones = new ArrayList<>();

        try (
                Connection conexion = ConexionDB.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            for (int i = 0; i < parametros.length; i++) {
                ps.setString(i + 1, parametros[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    opciones.add(rs.getString(1));
                }
            }
        }

        return opciones;
    }
}
