package cl.duoc.tiendajuegos.controller;

import cl.duoc.tiendajuegos.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class InventarioController {

    @FXML
    private Button btnVolverM;

    @FXML
    private CheckBox chkJuego;

    @FXML
    private CheckBox chkTCG;

    @FXML
    private CheckBox chkAccesorio;

    @FXML
    private ComboBox cmbFiltro;

    @FXML
    private TextField txtFiltro;

    @FXML
    private Button btnFiltro;

    @FXML
    private TextField txtActualizar;

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnExportar;

    // Tabla y Columnas
    @FXML
    private TableView tblInventario;

    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colTipo;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colStock;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private TableColumn colMinJug;
    @FXML
    private TableColumn colMaxJug;
    @FXML
    private TableColumn colEdadMin;
    @FXML
    private TableColumn colFranquicia;
    @FXML
    private TableColumn colEdicion;
    @FXML
    private TableColumn colTipoProd;
    @FXML
    private TableColumn colTipoAccesorio;
    @FXML
    private TableColumn colMarca;



    // INITIALILIZE

    @FXML
    private void initialize() {




        btnVolverM.setOnAction(event -> abrirPantalla(event,"selector.fxml"));

    }

    // METODO QUE PERMITE EL MOVIMIENTO ENTRE PANTALLAS
    private void abrirPantalla(ActionEvent event, String archivo) {

        try {
            Main.cambiarVista(event, archivo);
        } catch (IOException e) {
            mostrarAlerta("Error al Cargar la Pantalla");
            e.printStackTrace();
        }

    }
    // ALERTAS
    private void mostrarAlerta(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }



}
