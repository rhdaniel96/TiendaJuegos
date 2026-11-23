package cl.duoc.tiendajuegos.database;

import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) {

        try (Connection conexion = ConexionDB.getConnection()) {

            System.out.println("Conexion exitosa con MariaDB");

            System.out.println(
                    "Base de datos: " + conexion.getCatalog()
            );

        } catch (SQLException e) {

            System.out.println("Error de conexion");
            System.out.println(e.getMessage());
        }
    }
}
