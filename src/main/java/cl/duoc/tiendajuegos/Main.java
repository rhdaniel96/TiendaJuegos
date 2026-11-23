
package cl.duoc.tiendajuegos;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        mostrarVista(stage, "pantallaInicio.fxml");
    }

    // CARGAR UNA VISTA
    public static void mostrarVista(Stage stage, String archivo)
            throws IOException {

        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource(
                        "/cl/duoc/tiendajuegos/view/" + archivo
                )
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("Tienda de Juegos");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    // CAMBIAR DE VISTA DESDE UN EVENTO
    public static void cambiarVista(ActionEvent event, String archivo)
            throws IOException {

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        mostrarVista(stage, archivo);
    }
}
