package cl.duoc.tiendajuegos.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    // DATOS DE CONEXION
    private static final String URL =
            "jdbc:mariadb://localhost:3306/tienda_juegos_db?sslMode=disable";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    // METODO PARA ESTABLECER LA CONEXION
    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}
