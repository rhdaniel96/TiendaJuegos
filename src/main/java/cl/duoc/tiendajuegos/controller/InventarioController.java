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

import cl.duoc.tiendajuegos.model.*;
import cl.duoc.tiendajuegos.repository.ProductoRepository;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;

import java.sql.SQLException;

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

    @FXML
    private Button btnActualizarTabla;



    // Tabla y Columnas

    @FXML
    private TableView<Producto> tblInventario;

    @FXML
    private TableColumn<Producto, Integer> colId;

    @FXML
    private TableColumn<Producto, String> colTipo;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Integer> colStock;

    @FXML
    private TableColumn<Producto, Integer> colPrecio;

    @FXML
    private TableColumn<Producto, Integer> colMinJug;

    @FXML
    private TableColumn<Producto, Integer> colMaxJug;

    @FXML
    private TableColumn<Producto, Integer> colEdadMin;

    @FXML
    private TableColumn<Producto, String> colFranquicia;

    @FXML
    private TableColumn<Producto, String> colEdicion;

    @FXML
    private TableColumn<Producto, String> colTipoProd;

    @FXML
    private TableColumn<Producto, String> colTipoAccesorio;

    @FXML
    private TableColumn<Producto, String> colMarca;




    // INITIALILIZE

    @FXML
    private void initialize() {

        // CONFIGURAR LAS COLUMNAS

        configurarColumnas();

        // CARGAR DATOS AL ABRIR INVENTARIO

        cargarInventario();

        // ACTUALIZAR TABLA DESDE MARIA DB

        btnActualizarTabla.setOnAction(event ->
                cargarInventario()
        );

        // VOLVER AL SELECTOR

        btnVolverM.setOnAction(event ->
                abrirPantalla(event, "selector.fxml")
        );
    }

    private final ProductoRepository productoRepository =
            new ProductoRepository();

    // METODO QUE PERMITE EL MOVIMIENTO ENTRE PANTALLAS
    private void abrirPantalla(ActionEvent event, String archivo) {

        try {
            Main.cambiarVista(event, archivo);
        } catch (IOException e) {
            mostrarAlerta("Error al Cargar la Pantalla");
            e.printStackTrace();
        }

    }


    private void configurarColumnas() {

        // ATRIBUTOS GENERALES

        colId.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(
                        c.getValue().getIdProducto()
                )
        );

        colTipo.setCellValueFactory(c ->
                new ReadOnlyStringWrapper(
                        String.valueOf(c.getValue().getTipo())
                )
        );

        colNombre.setCellValueFactory(c ->
                new ReadOnlyStringWrapper(
                        c.getValue().getNombreProducto()
                )
        );

        colStock.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(
                        c.getValue().getStock()
                )
        );

        colPrecio.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(
                        c.getValue().getPrecio()
                )
        );


        // JUEGOS DE MESA

        colMinJug.setCellValueFactory(c -> {
            Producto p = c.getValue();

            return new ReadOnlyObjectWrapper<Integer>(
                    p instanceof JuegoMesa j
                            ? j.getMinJugadores() : null
            );
        });

        colMaxJug.setCellValueFactory(c -> {
            Producto p = c.getValue();

            return new ReadOnlyObjectWrapper<Integer>(
                    p instanceof JuegoMesa j
                            ? j.getMaxJugadores() : null
            );
        });

        colEdadMin.setCellValueFactory(c -> {
            Producto p = c.getValue();

            return new ReadOnlyObjectWrapper<Integer>(
                    p instanceof JuegoMesa j
                            ? j.getEdadMinima() : null
            );
        });


        // TCG

        colFranquicia.setCellValueFactory(c -> {
            Producto p = c.getValue();

            return new ReadOnlyStringWrapper(
                    p instanceof Tcg t
                            ? t.getFranquicia() : null
            );
        });

        colEdicion.setCellValueFactory(c -> {
            Producto p = c.getValue();

            return new ReadOnlyStringWrapper(
                    p instanceof Tcg t
                            ? t.getEdicion() : null
            );
        });

        colTipoProd.setCellValueFactory(c -> {
            Producto p = c.getValue();

            return new ReadOnlyStringWrapper(
                    p instanceof Tcg t
                            ? t.getTipoProducto() : null
            );
        });


        // ACCESORIOS

        colTipoAccesorio.setCellValueFactory(c -> {
            Producto p = c.getValue();

            return new ReadOnlyStringWrapper(
                    p instanceof Accesorio a
                            ? a.getTipoProducto() : null
            );
        });

        colMarca.setCellValueFactory(c -> {
            Producto p = c.getValue();

            return new ReadOnlyStringWrapper(
                    p instanceof Accesorio a
                            ? a.getMarca() : null
            );
        });
    }


    private void cargarInventario() {

        try {

            tblInventario.setItems(
                    FXCollections.observableArrayList(
                            productoRepository.listarProductos()
                    )
            );

        } catch (SQLException e) {

            mostrarAlerta(
                    "Error al cargar el inventario desde MariaDB"
            );

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
