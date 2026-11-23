package cl.duoc.tiendajuegos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import cl.duoc.tiendajuegos.Main;
import cl.duoc.tiendajuegos.repository.UsuarioRepository;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.SQLException;

public class PantallaInicioController {

    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField txtPass;

    @FXML
    private Button btnIngresar;

    @FXML
    private Label lblError;

    // Metodo que nos permitira comunicarnos con la BD mediante ConexionDB
    private final UsuarioRepository usuarioRepository =
            new UsuarioRepository();

    @FXML
    private void ingresar(ActionEvent event) {

        String usuario = txtUser.getText();     //obtenemos los valores ingresados en el TexField
        String password = txtPass.getText();   // //obtenemos los valores ingresados en el PasswordField


        // VALIDACIONES !!

        if(usuario.isBlank() || password.isBlank()){
            mostrarError("Debe Completar Todos los Campos");
            return;
        }

        //

        try {
            if(usuarioRepository.validarUsuario(usuario, password)) {
                Main.cambiarVista(event, "selector.fxml");
            } else {
                mostrarError("Usuario o contrasena incorrectos");
            }
        } catch (SQLException e) {

            mostrarAlerta("Error de Conexion con la Base de Datos");

        } catch (IOException e) {
            mostrarAlerta("Error al Cargar la Pantalla");
        }

    }

    // ALERTAS

        // metodo que muestra alertas como ventana emergente (SQL y IO)
    private void mostrarAlerta(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
        // metodo que muestra alertas con Label rojo (errores en password y user)
    private void mostrarError(String mensaje) {

        lblError.setText(mensaje);
        lblError.setVisible(true);
        lblError.setManaged(true);
    }

    private void ocultarError() {

        lblError.setText("");
        lblError.setVisible(false);
        lblError.setManaged(false);

    }

}
