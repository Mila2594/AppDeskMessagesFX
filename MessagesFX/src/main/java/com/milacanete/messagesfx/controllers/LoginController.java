package com.milacanete.messagesfx.controllers;

import com.milacanete.messagesfx.MessagesFX;
import com.milacanete.messagesfx.models.responses.LoginResponse;
import com.milacanete.messagesfx.service.HttpService;
import com.milacanete.messagesfx.utils.DialogUtils;
import com.milacanete.messagesfx.utils.ScreenLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Controlador para la pantalla de login
 * Maneja el evento de clic en el botón de login para validar el login y redirigir a la pantalla de mensajes
 * Validando las credenciales del usuario, muestra un mensaje de error si el login es incorrecto
 * Maneja el evento de clic en el hyperlink para redirigir a la pantalla de registro
 */
public class LoginController {

    /**
     * Campo de texto para el nombre de usuario
     */
    @FXML
    public TextField UserTextField;
    /**
     * Campo de contraseña oculta para el password
     */
    @FXML
    public javafx.scene.control.PasswordField PasswordField;
    /**
     * Etiqueta de error si el login es incorrecto
     */
    @FXML
    public Label ErrorRegisteredLabel;
    /**
     * Botón para loguear al usuario
     */
    @FXML
    public Button LoginButton;
    /**
     * Hyperlink para redirigir a la pantalla de registro
     */
    @FXML
    public Hyperlink NewAccountHyperlink;

    /**
     * Instancia de LoginResponse para guardar datos del usuario logueado
     */
    private static  LoginResponse loginResponse;

    public static LoginResponse getLoginResponse() {
        return loginResponse;
    }

    /**
     * Método para inicializar el controlador
     * Establece la visibilidad de la etiqueta de error, asigna las acciones al botón y al hyperlink
     */
    @FXML
    public void initialize() {
        ErrorRegisteredLabel.setVisible(false);

        LoginButton.setOnAction(_ -> handleLogin());
        NewAccountHyperlink.setOnAction(_ -> redirectToRegister());
    }

    /**
     * Método que maneja el evento de clink en el botón de login
     * Valida si los campos están vacíos y realiza el login
     * Sí el login es correcto, se guarda el token y redirigir a la pantalla messages
     * Si el login es incorrecto, se muestra un mensaje de error
     */
    @FXML
    private void handleLogin() {
        String userName = UserTextField.getText();
        String password = PasswordField.getText();

        if (userName.isEmpty() || password.isEmpty()) {
            DialogUtils.showError("Empty fields", "Please fill in all the fields.");
            return;
        }

        LoginResponse response = HttpService.login(userName, password);

        if (response == null || !response.isOk()) {
            ErrorRegisteredLabel.setVisible(true);
        } else {
            HttpService.setToken(response.getToken());
            loginResponse = response;
            redirectToMessages();
        }
    }

    /**
     * Método para redirigir a la pantalla de mensajes
     * Si no se puede cargar la pantalla de mensajes, registra el error
     */
    private void redirectToMessages() {
        try {
            FXMLLoader loader = new FXMLLoader(MessagesFX.class.getResource("/com/milacanete/messagesfx/messagesView.fxml"));
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.hide();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            DialogUtils.showError("Error", "Unable to load the messages screen.");
        }
    }

    /**
     * Método para redirigir a la pantalla de registro
     * Si no se puede cargar la pantalla de registro, registra el error
     */
    private void redirectToRegister() {
        try {
            ScreenLoader.loadScreen("/com/milacanete/messagesfx/registerView.fxml", (Stage) LoginButton.getScene().getWindow());
        } catch (IOException e) {
            DialogUtils.showError("Error", "Unable to load the register screen.");
        }
    }

}
