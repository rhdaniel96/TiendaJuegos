package cl.duoc.tiendajuegos.controller;

import cl.duoc.tiendajuegos.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import cl.duoc.tiendajuegos.model.*;
import cl.duoc.tiendajuegos.repository.ProductoRepository;
import cl.duoc.tiendajuegos.repository.CatalogoRepository;

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
    private ComboBox<String> cmbFiltro;

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

        // CONFIGURAR COLUMNAS
        configurarColumnas();

        // CONFIGURAR OPCIONES DEL COMBOBOX

        configurarFiltro();

        // CONFIGURAR SELECCION DE CELDAS

        configurarSeleccionCelda();

        // ACTIVAR CHECKBOX AL INICIAR
        chkJuego.setSelected(true);
        chkTCG.setSelected(true);
        chkAccesorio.setSelected(true);

        // BOTON FILTRAR

        btnFiltro.setOnAction(event -> aplicarFiltros());
        // FILTRAR CUANDO CAMBIE LA COLUMNA
        cmbFiltro.setOnAction(event -> aplicarFiltros());
        // FILTRAR MIENTRAS SE ESCRIBE
        txtFiltro.textProperty().addListener(
                (observable, anterior, nuevo) -> aplicarFiltros()
        );

        // CONECTAR LISTA FILTRADA A LA TABLA
        productosOrdenados.comparatorProperty().bind(
                tblInventario.comparatorProperty()
        );

        tblInventario.setItems(productosOrdenados);

        // DETECTAR CAMBIOS EN CHECKBOX
        chkJuego.setOnAction(event -> aplicarFiltros());
        chkTCG.setOnAction(event -> aplicarFiltros());
        chkAccesorio.setOnAction(event -> aplicarFiltros());

        // CARGAR DATOS
        cargarInventario();

        // ACTUALIZAR TABLA
        btnActualizarTabla.setOnAction(event ->
                cargarInventario()
        );

        // ACTUALIZAR PRODUCTO SELECCIONADO

        btnActualizar.setOnAction(event -> actualizarProducto());

        // VOLVER AL MENU
        btnVolverM.setOnAction(event ->
                abrirPantalla(event, "selector.fxml")
        );
    }



    private final ProductoRepository productoRepository =
            new ProductoRepository();

    private final CatalogoRepository catalogoRepository =
            new CatalogoRepository();

// LISTA ORIGINAL DE PRODUCTOS

    private final ObservableList<Producto> productos =
            FXCollections.observableArrayList();

// LISTA FILTRADA

    private final FilteredList<Producto> productosFiltrados =
            new FilteredList<>(productos, p -> true);

// LISTA ORDENADA PARA TABLEVIEW

    private final SortedList<Producto> productosOrdenados =
            new SortedList<>(productosFiltrados);

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


    private void configurarSeleccionCelda() {

        // PERMITIR SELECCIONAR CELDAS INDIVIDUALES

        tblInventario.getSelectionModel()
                .setCellSelectionEnabled(true);

        // PERMITIR SOLO UNA CELDA A LA VEZ

        tblInventario.getSelectionModel()
                .setSelectionMode(SelectionMode.SINGLE);

        // DETECTAR LA CELDA SELECCIONADA

        tblInventario.getFocusModel()
                .focusedCellProperty()
                .addListener((observable, anterior, actual) -> {

                    if (actual == null ||
                            actual.getRow() < 0 ||
                            actual.getTableColumn() == null) {
                        return;
                    }

                    // OBTENER VALOR DE LA CELDA

                    Object valor = actual.getTableColumn()
                            .getCellData(actual.getRow());

                    // MOSTRAR VALOR EN EL TEXTFIELD

                    txtActualizar.setText(
                            valor == null ? "" : valor.toString()
                    );
                });
    }


    private void actualizarProducto() {

        // VERIFICAR QUE EXISTA UNA CELDA SELECCIONADA

        if (tblInventario.getSelectionModel()
                .getSelectedCells().isEmpty()) {

            mostrarAdvertencia("Debe seleccionar una celda");
            return;
        }

        // OBTENER FILA Y COLUMNA SELECCIONADAS

        TablePosition<?, ?> posicion = tblInventario
                .getSelectionModel()
                .getSelectedCells()
                .get(0);

        int fila = posicion.getRow();

        if (fila < 0 || fila >= tblInventario.getItems().size()
                || posicion.getTableColumn() == null) {

            mostrarAdvertencia("Seleccion invalida");
            return;
        }

        Producto producto = tblInventario
                .getItems()
                .get(fila);

        String columna = posicion.getTableColumn().getText();

        // OBTENER VALORES ANTERIOR Y NUEVO

        Object valorAnterior = posicion
                .getTableColumn()
                .getCellData(fila);

        String nuevoTexto = txtActualizar.getText().trim();

        if (nuevoTexto.isBlank()) {
            mostrarAdvertencia("Debe ingresar un nuevo valor");
            return;
        }

        try {

            Object nuevoValor;

            // VALIDAR SEGUN LA COLUMNA

            switch (columna) {

                case "Nombre":

                    if (nuevoTexto.length() > 60) {
                        throw new IllegalArgumentException(
                                "El nombre no puede superar 60 caracteres"
                        );
                    }

                    if (!nuevoTexto.equals(producto.getNombreProducto())
                            && productoRepository.existeNombre(nuevoTexto)) {

                        throw new IllegalArgumentException(
                                "Ya existe un producto con ese nombre"
                        );
                    }

                    nuevoValor = nuevoTexto;
                    break;

                case "Stock":
                case "Precio":

                    int numero = Integer.parseInt(nuevoTexto);

                    if (numero < 0) {
                        throw new IllegalArgumentException(
                                columna + " no puede ser negativo"
                        );
                    }

                    nuevoValor = numero;
                    break;


                case "Min Jugadores":
                case "Max Jugadores":
                case "Edad Minima":

                    // VERIFICAR QUE SEA UN JUEGO DE MESA

                    if (!(producto instanceof JuegoMesa juego)) {
                        throw new IllegalArgumentException(
                                "Esta columna solo corresponde a Juegos de Mesa"
                        );
                    }

                    // CONVERTIR NUEVO VALOR A ENTERO

                    int numeroJuego = Integer.parseInt(nuevoTexto);

                    // IMPEDIR VALORES NEGATIVOS

                    if (numeroJuego < 0) {
                        throw new IllegalArgumentException(
                                columna + " no puede ser negativo"
                        );
                    }

                    // VALIDACIONES ESPECIFICAS

                    switch (columna) {

                        case "Min Jugadores":

                            if (numeroJuego < 1 ||
                                    numeroJuego > juego.getMaxJugadores()) {

                                throw new IllegalArgumentException(
                                        "El minimo debe ser mayor que cero "
                                                + "y no superar al maximo de jugadores"
                                );
                            }

                            break;

                        case "Max Jugadores":

                            if (numeroJuego < juego.getMinJugadores()) {

                                throw new IllegalArgumentException(
                                        "El maximo no puede ser menor "
                                                + "que el minimo de jugadores"
                                );
                            }

                            break;

                        case "Edad Minima":

                            // EL VALOR 0 ESTA PERMITIDO

                            break;
                    }

                    nuevoValor = numeroJuego;
                    break;


                case "Tipo Producto":

                    // VERIFICAR QUE SEA UN TCG

                    if (!(producto instanceof Tcg)) {

                        throw new IllegalArgumentException(
                                "Esta columna solo corresponde a productos TCG"
                        );
                    }

                    // VERIFICAR QUE EL VALOR EXISTA EN MARIA DB

                    if (!catalogoRepository.listarTiposTCG()
                            .contains(nuevoTexto)) {

                        throw new IllegalArgumentException(
                                "El tipo de producto TCG no existe en el catalogo"
                        );
                    }

                    nuevoValor = nuevoTexto;

                    break;


                case "Edicion":

                    // VERIFICAR QUE SEA UN TCG

                    if (!(producto instanceof Tcg tcg)) {

                        throw new IllegalArgumentException(
                                "Esta columna solo corresponde a productos TCG"
                        );
                    }

                    // OBTENER FRANQUICIA ACTUAL DEL PRODUCTO

                    String franquiciaActual = tcg.getFranquicia();

                    // VERIFICAR QUE LA EDICION PERTENEZCA A LA FRANQUICIA

                    if (!catalogoRepository.listarEdiciones(franquiciaActual)
                            .contains(nuevoTexto)) {

                        throw new IllegalArgumentException(
                                "La edicion ingresada no pertenece a la "
                                        + "franquicia " + franquiciaActual
                        );
                    }

                    // ASIGNAR NUEVO VALOR

                    nuevoValor = nuevoTexto;

                    break;


                    default:

                    mostrarAdvertencia(
                            "Esta columna todavia no esta habilitada "
                                    + "para modificaciones"
                    );
                    return;
            }

            // COMPROBAR SI REALMENTE CAMBIO EL VALOR

            if (nuevoValor.toString().equals(
                    String.valueOf(valorAnterior))) {

                mostrarAdvertencia(
                        "El nuevo valor es igual al actual"
                );
                return;
            }

            // VENTANA DE CONFIRMACION

            Alert confirmacion = new Alert(
                    Alert.AlertType.CONFIRMATION
            );

            confirmacion.setTitle("Confirmar actualizacion");

            confirmacion.setHeaderText(
                    "¿Esta seguro que desea continuar?"
            );

            confirmacion.setContentText(
                    "Producto: " + producto.getNombreProducto()
                            + "\nID: " + producto.getIdProducto()
                            + "\nColumna: " + columna
                            + "\nValor anterior: " + valorAnterior
                            + "\nNuevo valor: " + nuevoValor
            );

            confirmacion.getButtonTypes().setAll(
                    ButtonType.YES,
                    ButtonType.NO
            );

            ButtonType respuesta = confirmacion
                    .showAndWait()
                    .orElse(ButtonType.NO);

            // CANCELAR SI NO CONFIRMA

            if (respuesta != ButtonType.YES) {
                return;
            }

            // ACTUALIZAR EN MARIA DB

            boolean actualizado = productoRepository.actualizarCampo(
                    producto.getIdProducto(),
                    columna,
                    nuevoValor
            );

            if (actualizado) {

                // RECARGAR DATOS DESDE MARIA DB

                cargarInventario();

                txtActualizar.clear();

                Alert exito = new Alert(
                        Alert.AlertType.INFORMATION
                );

                exito.setTitle("Actualizacion correcta");
                exito.setHeaderText(null);
                exito.setContentText(
                        "Producto actualizado correctamente"
                );
                exito.showAndWait();

            } else {

                mostrarAlerta(
                        "No se encontro el producto en la base de datos"
                );
            }

        } catch (NumberFormatException e) {

            mostrarAdvertencia(
                    "Debe ingresar un numero entero valido"
            );

        } catch (IllegalArgumentException e) {

            mostrarAdvertencia(e.getMessage());

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {

                mostrarAdvertencia(
                        "Ya existe un producto con ese nombre"
                );

            } else {

                mostrarAlerta(
                        "Error al actualizar el producto en MariaDB"
                );
            }

            e.printStackTrace();
        }
    }







    private void aplicarFiltros() {

        productosFiltrados.setPredicate(producto -> {

            // PRIMER FILTRO: TIPO DE PRODUCTO

            char tipo = producto.getTipo();

            boolean mostrarTipo = switch (tipo) {

                case 'J' -> chkJuego.isSelected();
                case 'T' -> chkTCG.isSelected();
                case 'A' -> chkAccesorio.isSelected();

                default -> false;
            };

            if (!mostrarTipo) {
                return false;
            }

            // SEGUNDO FILTRO: TEXTO Y COLUMNA

            String criterio = txtFiltro.getText()
                    .trim()
                    .toLowerCase();

            String seleccion = cmbFiltro.getValue();

            // BUSQUEDA EN TODAS LAS COLUMNAS

            if (seleccion == null ||
                    seleccion.equals("Todas las columnas")) {

                if (criterio.isEmpty()) {
                    return true;
                }

                for (TableColumn<Producto, ?> columna :
                        tblInventario.getColumns()) {

                    String valor = obtenerValorColumna(
                            columna, producto
                    ).toLowerCase();

                    if (valor.contains(criterio)) {
                        return true;
                    }
                }

                return false;
            }

            // BUSQUEDA EN UNA COLUMNA ESPECIFICA

            for (TableColumn<Producto, ?> columna :
                    tblInventario.getColumns()) {

                if (columna.getText().equals(seleccion)) {

                    String valor = obtenerValorColumna(
                            columna, producto
                    ).toLowerCase();

                    // SI NO HAY TEXTO, MOSTRAR VALORES NO VACIOS

                    if (criterio.isEmpty()) {
                        return !valor.isEmpty();
                    }

                    return valor.contains(criterio);
                }
            }

            return false;
        });
    }




    private void cargarInventario() {

        try {

            productos.setAll(
                    productoRepository.listarProductos()
            );

        } catch (SQLException e) {

            mostrarAlerta(
                    "Error al cargar el inventario desde MariaDB"
            );

            e.printStackTrace();
        }
    }



    private void configurarFiltro() {

        // OPCION PARA BUSCAR EN TODAS LAS COLUMNAS

        cmbFiltro.getItems().add("Todas las columnas");

        // AGREGAR LOS NOMBRES DE LAS 13 COLUMNAS

        for (TableColumn<Producto, ?> columna :
                tblInventario.getColumns()) {

            cmbFiltro.getItems().add(columna.getText());
        }

        // SELECCION INICIAL

        cmbFiltro.getSelectionModel().selectFirst();
    }


    private String obtenerValorColumna(
            TableColumn<Producto, ?> columna,
            Producto producto) {

        var observable = columna.getCellObservableValue(producto);

        if (observable == null || observable.getValue() == null) {
            return "";
        }

        return observable.getValue().toString();
    }






    // ALERTAS
    private void mostrarAlerta(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarAdvertencia(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.WARNING);

        alerta.setTitle("Advertencia");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }



}
