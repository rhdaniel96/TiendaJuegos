package cl.duoc.tiendajuegos.controller;

import cl.duoc.tiendajuegos.Main;
import cl.duoc.tiendajuegos.repository.CatalogoRepository;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

import cl.duoc.tiendajuegos.model.Producto;
import cl.duoc.tiendajuegos.model.JuegoMesa;
import cl.duoc.tiendajuegos.model.Tcg;
import cl.duoc.tiendajuegos.model.Accesorio;
import cl.duoc.tiendajuegos.repository.ProductoRepository;

public class MantenedorTiendaController {

    // REPOSITORIOS

    private final CatalogoRepository catalogoRepository =
            new CatalogoRepository();

    private final ProductoRepository productoRepository =
            new ProductoRepository();


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

    @FXML
    private Button btnLimpiar;


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

        // AGREGAR NUEVO PRODUCTO

        btnIngresar.setOnAction(event -> agregarProducto());

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

    // METODO AGREGAR NUEVO PRODUCTO A LA BD
    private void agregarProducto() {

        // 1. OBTENER TIPO DE PRODUCTO

        String seleccion = cmbTipoProducto.getValue();

        // 2. VERIFICAR QUE EXISTA UNA SELECCION

        if (seleccion == null) {
            mostrarAdvertencia("Debe seleccionar un tipo de producto");
            return;
        }

        try {

            // 3. OBTENER Y VALIDAR CAMPOS GENERALES

            String nombre = txtName.getText().trim();

            if (nombre.isBlank() || nombre.length() > 60) {
                throw new IllegalArgumentException(
                        "El nombre debe contener entre 1 y 60 caracteres"
                );
            }

            int stock = leerEntero(txtStock, "Stock");
            int precio = leerEntero(txtPrice, "Precio");

            // 4. CREAR PRODUCTO SEGUN SELECCION

            Producto producto;

            switch (seleccion) {

                case "Juegos de Mesa":

                    int minJug = leerEntero(
                            txtMinJug, "Minimo de jugadores"
                    );

                    int maxJug = leerEntero(
                            txtMaxJug, "Maximo de jugadores"
                    );

                    int edadMin = leerEntero(
                            txtEdadMin, "Edad minima"
                    );

                    if (minJug < 1 || maxJug < minJug) {
                        throw new IllegalArgumentException(
                                "El minimo debe ser mayor que cero "
                                        + "y no superar al maximo de jugadores"
                        );
                    }

                    producto = new JuegoMesa(
                            0, 'J', nombre, stock, precio,
                            minJug, maxJug, edadMin
                    );

                    break;

                case "TCG":

                    String franquicia = obtenerSeleccion(
                            cmbFranquicia, "Franquicia"
                    );

                    String edicion = obtenerSeleccion(
                            cmbEdicion, "Edicion"
                    );

                    String tipoTCG = obtenerSeleccion(
                            cmbTipoTCG, "Tipo de producto TCG"
                    );

                    producto = new Tcg(
                            0, 'T', nombre, stock, precio,
                            franquicia, edicion, tipoTCG
                    );

                    break;

                case "Accesorio":

                    String tipoAccesorio = obtenerSeleccion(
                            cmbTipoAccesorio, "Tipo de accesorio"
                    );

                    String marca = obtenerSeleccion(
                            cmbMarca, "Marca"
                    );

                    producto = new Accesorio(
                            0, 'A', nombre, stock, precio,
                            tipoAccesorio, marca
                    );

                    break;

                default:

                    throw new IllegalArgumentException(
                            "Tipo de producto no valido"
                    );
            }

            // 5. GUARDAR EN MARIA DB

            productoRepository.guardar(producto);

            // 6. INFORMAR RESULTADO

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);

            alerta.setTitle("Producto registrado");
            alerta.setHeaderText(null);
            alerta.setContentText(
                    "Producto agregado correctamente. ID: "
                            + producto.getIdProducto()
            );

            alerta.showAndWait();

        } catch (IllegalArgumentException e) {

            mostrarAdvertencia(e.getMessage());

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                mostrarAdvertencia(
                        "Ya existe un producto con ese nombre"
                );
            } else {
                mostrarAlerta("Error al guardar el producto");
            }

            e.printStackTrace();
        }
    }


// VALIDAR TEXTFIELD NUMERICO

    private int leerEntero(TextField campo, String nombreCampo) {

        String valor = campo.getText().trim();

        if (valor.isBlank()) {
            throw new IllegalArgumentException(
                    "Debe completar el campo: " + nombreCampo
            );
        }

        try {

            int numero = Integer.parseInt(valor);

            if (numero < 0) {
                throw new IllegalArgumentException(
                        nombreCampo + " no puede ser negativo"
                );
            }

            return numero;

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    nombreCampo + " debe ser un numero entero valido"
            );
        }
    }


// VALIDAR COMBOBOX

    private String obtenerSeleccion(
            ComboBox<String> combo, String nombreCampo) {

        String seleccion = combo.getValue();

        if (seleccion == null || seleccion.isBlank()) {
            throw new IllegalArgumentException(
                    "Debe seleccionar: " + nombreCampo
            );
        }

        return seleccion;
    }


// MOSTRAR ADVERTENCIAS DE VALIDACION

    private void mostrarAdvertencia(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.WARNING);

        alerta.setTitle("Datos incompletos o invalidos");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }



}
