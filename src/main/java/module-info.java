module cl.duoc.tiendajuegos {

    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Base de datos  mediante JDBC
    requires java.sql;
    requires org.mariadb.jdbc;

    // Permite cargar elementos FXML del paquete principal
    opens cl.duoc.tiendajuegos to javafx.fxml;

    // Permite cargar a FXMLLoader acceder a los controladores y a sus miembros anotados con @FXML.
    opens cl.duoc.tiendajuegos.controller to javafx.fxml;

    // Permite acceder a Main y Launcher
    exports cl.duoc.tiendajuegos;
}
