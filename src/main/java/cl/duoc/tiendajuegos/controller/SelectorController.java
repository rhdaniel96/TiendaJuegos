package cl.duoc.tiendajuegos.controller;

import cl.duoc.tiendajuegos.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;

import java.io.IOException;

public class SelectorController {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnInventario;

    @FXML
    private Button btnAdministrar;

    @FXML
    private Button btnBackLog;

    @FXML
    private void initialize(){
            // ACCION DE BOTONES
        btnAdd.setOnAction(event -> abrirPantalla(event, "mantenedorTienda.fxml"));
        btnInventario.setOnAction(event -> abrirPantalla(event, "inventario.fxml"));
        btnAdministrar.setOnAction(event -> abrirPantalla(event, "administracion.fxml"));
        btnBackLog.setOnAction(event -> abrirPantalla(event,"pantallaInicio.fxml"));

    }

    // METODO QUE CAMBIA DE PANTALLA

    private void abrirPantalla(ActionEvent event, String archivo) {

        try {
            Main.cambiarVista(event, archivo);
        } catch (IOException e) {
            mostrarAlerta("Error al Cargar la Pantalla");
            e.printStackTrace();
        }

    }

    // ALERTA DE ERROR

    private void mostrarAlerta(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }




}
