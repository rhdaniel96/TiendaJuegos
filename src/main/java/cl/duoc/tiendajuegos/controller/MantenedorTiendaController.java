package cl.duoc.tiendajuegos.controller;

import cl.duoc.tiendajuegos.Main;
import cl.duoc.tiendajuegos.repository.CatalogoRepository;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

public class MantenedorTiendaController {

    // REPOSITORIO

    private final CatalogoRepository catalogoRepository =
            new CatalogoRepository();


    // CAMPOS GENERALES

    @FXML
    private ComboBox<String> cmbTipoProducto;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtStock;

    @FXML
    private TextField txtPrice;


    // BOTONES

    @FXML
    private Button btnIngresar;

    @FXML
    private Button btnInventario;

    @FXML
    private Button btnVolver;


    // VBOX JUEGOS

    @FXML
    private VBox vboxJuegos;

    @FXML
    private TextField txtMinJug;

    @FXML
    private TextField txtMaxJug;

    @FXML
    private TextField txtEdadMin;


    // VBOX TCG

    @FXML
    private VBox vboxTcg;

    @FXML
    private ComboBox<String> cmbFranquicia;

    @FXML
    private ComboBox<String> cmbEdicion;

    @FXML
    private ComboBox<String> cmbTipoTCG;


    // VBOX ACCESORIOS

    @FXML
    private VBox vboxAccesorio;

    @FXML
    private ComboBox<String> cmbTipoAccesorio;

    @FXML
    private ComboBox<String> cmbMarca;


    // INITIALIZE

    @FXML
    private void initialize() {

        // CARGAR COMBOBOX PRINCIPAL

        cmbTipoProducto.getItems().setAll(
                "Juegos de Mesa",
                "TCG",
                "Accesorio"
        );


        // CARGAR CATALOGOS DESDE MYSQL

        cargarCatalogos();


        // DESHABILITAR COMBOBOX DEPENDIENTES

        cmbEdicion.setDisable(true);
        cmbMarca.setDisable(true);


        // DETECTAR SELECCION DE FRANQUICIA

        cmbFranquicia.setOnAction(event ->
                cargarEdiciones()
        );


        // DETECTAR SELECCION DE TIPO DE ACCESORIO

        cmbTipoAccesorio.setOnAction(event ->
                cargarMarcas()
        );


        // OCULTAR LOS TRES VBOX SECUNDARIOS

        ocultarVBox();


        // DETECTAR SELECCION DE TIPO DE PRODUCTO

        cmbTipoProducto.setOnAction(event ->
                cambiarVBox()
        );


        // NAVEGACION ENTRE PANTALLAS

        btnInventario.setOnAction(event ->
                abrirPantalla(event, "inventario.fxml")
        );

        btnVolver.setOnAction(event ->
                abrirPantalla(event, "selector.fxml")
        );

    }


    // CARGAR CATALOGOS DESDE LA BASE DE DATOS

    private void cargarCatalogos() {

        try {

            cmbFranquicia.getItems().setAll(
                    catalogoRepository.listarFranquicias()
            );

            cmbTipoTCG.getItems().setAll(
                    catalogoRepository.listarTiposTCG()
            );

            cmbTipoAccesorio.getItems().setAll(
                    catalogoRepository.listarTiposAccesorios()
            );

        } catch (SQLException e) {

            mostrarAlerta("Error al cargar los catalogos");
            e.printStackTrace();

        }
    }


    // CARGAR EDICIONES SEGUN FRANQUICIA

    private void cargarEdiciones() {

        // OBTENER FRANQUICIA SELECCIONADA

        String franquicia = cmbFranquicia.getValue();


        // LIMPIAR EDICIONES ANTERIORES

        cmbEdicion.getSelectionModel().clearSelection();
        cmbEdicion.setValue(null);
        cmbEdicion.getItems().clear();
        cmbEdicion.setDisable(true);


        // COMPROBAR SELECCION

        if (franquicia == null) {
            return;
        }


        // CARGAR EDICIONES DESDE MYSQL

        try {

            cmbEdicion.getItems().setAll(
                    catalogoRepository.listarEdiciones(franquicia)
            );

            cmbEdicion.setDisable(
                    cmbEdicion.getItems().isEmpty()
            );

        } catch (SQLException e) {

            mostrarAlerta("Error al cargar las ediciones");
            e.printStackTrace();

        }
    }


    // CARGAR MARCAS SEGUN TIPO DE ACCESORIO

    private void cargarMarcas() {

        // OBTENER TIPO DE ACCESORIO SELECCIONADO

        String tipoAccesorio = cmbTipoAccesorio.getValue();


        // LIMPIAR MARCAS ANTERIORES

        cmbMarca.getSelectionModel().clearSelection();
        cmbMarca.setValue(null);
        cmbMarca.getItems().clear();
        cmbMarca.setDisable(true);


        // COMPROBAR SELECCION

        if (tipoAccesorio == null) {
            return;
        }


        // CARGAR MARCAS DESDE MYSQL

        try {

            cmbMarca.getItems().setAll(
                    catalogoRepository.listarMarcas(tipoAccesorio)
            );

            cmbMarca.setDisable(
                    cmbMarca.getItems().isEmpty()
            );

        } catch (SQLException e) {

            mostrarAlerta("Error al cargar las marcas");
            e.printStackTrace();

        }
    }


    // OCULTAR LOS VBOX SECUNDARIOS

    private void ocultarVBox() {

        configurarVBox(vboxJuegos, false);
        configurarVBox(vboxTcg, false);
        configurarVBox(vboxAccesorio, false);

    }


    // CAMBIAR VBOX SEGUN TIPO DE PRODUCTO

    private void cambiarVBox() {

        ocultarVBox();

        // OBTENER SELECCION DEL COMBOBOX PRINCIPAL

        String seleccion = cmbTipoProducto.getValue();


        // COMPROBAR QUE EXISTA SELECCION

        if (seleccion == null) {
            return;
        }


        // MOSTRAR VBOX CORRESPONDIENTE

        switch (seleccion) {

            case "Juegos de Mesa":
                configurarVBox(vboxJuegos, true);
                break;

            case "TCG":
                configurarVBox(vboxTcg, true);
                break;

            case "Accesorio":
                configurarVBox(vboxAccesorio, true);
                break;

        }
    }


    // CONFIGURAR VISIBILIDAD DE LOS VBOX

    private void configurarVBox(VBox vbox, boolean mostrar) {

        vbox.setVisible(mostrar);
        vbox.setManaged(mostrar);
        vbox.setDisable(!mostrar);

    }


    // NAVEGACION ENTRE PANTALLAS

    private void abrirPantalla(ActionEvent event, String archivo) {

        try {

            Main.cambiarVista(event, archivo);

        } catch (IOException e) {

            mostrarAlerta("Error al cargar la pantalla");
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
