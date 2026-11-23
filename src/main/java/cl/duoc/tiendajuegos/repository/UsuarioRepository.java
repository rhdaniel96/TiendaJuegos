package cl.duoc.tiendajuegos.repository;

import cl.duoc.tiendajuegos.database.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioRepository {

    // VALIDAR CREDENCIALES DEL USUARIO
    public boolean validarUsuario(String usuario, String password)
            throws SQLException {

        String sql = """
                SELECT IDUSUARIO
                FROM USUARIO
                WHERE USUARIO = ?
                AND BINARY PASSWORD = ?
                """;

        try (
                Connection conexion = ConexionDB.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)
        ) {

            ps.setString(1, usuario);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next();
            }
        }
    }
}
