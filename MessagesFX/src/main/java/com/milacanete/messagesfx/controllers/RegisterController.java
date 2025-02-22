package com.milacanete.messagesfx.controllers;


import com.milacanete.messagesfx.models.responses.UserResponse;
import com.milacanete.messagesfx.service.HttpService;
import com.milacanete.messagesfx.utils.DialogUtils;
import com.milacanete.messagesfx.utils.ScreenLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Controlador para la pantalla registro
 * Maneja el evento de clic en el botón de registrar para validar el registro y redirigir a la pantalla de login
 * Validando las credenciales del usuario, muestra un mensaje de error si el registro es incorrecto
 * Maneja el evento de clic en el botón de cancelar para redirigir a la pantalla de login
 * Maneja el evento de clic en el botón de seleccionar imagen para abrir un diálogo para seleccionar la imagen
 */
public class RegisterController {

    /**
     * Campo para cargar un fichero de tipo imagen
     */
    @FXML
    public ImageView AvatarImageView;
    /**
     * Botón para cancelar el registro
     */
    @FXML
    private Button CancelButton;
    /**
     * Campo de contraseña oculta para el password
     */
    @FXML
    private PasswordField ConfirmPasswordField;
    /**
     * Campo de contraseña oculta para el password
     */
    @FXML
    private PasswordField PasswordField;
    /**
     * Botón para registrar al usuario
     */
    @FXML
    private Button RegisterButton;
    /**
     * Botón para seleccionar una imagen
     */
    @FXML
    private Button SelectImageButton;
    /**
     * Campo de texto para el nombre de usuario
     */
    @FXML
    private TextField UserTextField;
    /**
     * Guardar la imagen seleccionada
     */
    private File selectedImage;

    /**
     * Método para inicializar el controlador
     * Asigna las acciones a los botones Cancelar, Registrar y Seleccionar imagen
     */
    @FXML
    public void initialize() {

        CancelButton.setOnAction(_ -> handleCancel());
        RegisterButton.setOnAction(_ -> handleRegister());
        SelectImageButton.setOnAction(_ -> handleSelectImage());


        CancelButton.setOnAction(_ -> {
            // Verificar si los campos están vacíos
            String userName = UserTextField.getText().trim();
            String password = PasswordField.getText().trim();
            String confirmPassword = ConfirmPasswordField.getText().trim();

            if (!userName.isEmpty() || !password.isEmpty() || !confirmPassword.isEmpty()) {
                // Si hay datos, mostrar un mensaje de confirmación
                boolean confirm = DialogUtils.showConfirmation(
                        "Confirm",
                        "Are you sure you want to go back to login? All the information you entered will be lost."
                );
                if (confirm) {
                    redirectToLogin(); // Redirigir al login si el usuario confirma
                }
            } else {
                // Si los campos están vacíos, redirigir sin mostrar confirmación
                redirectToLogin();
            }
        });
        RegisterButton.setOnAction(_ -> handleRegister());
        SelectImageButton.setOnAction(_ -> handleSelectImage());
    }

    /**
     * Método que maneja el evento de clic en el botón de seleccionar imagen
     * Crea un dialogo para seleccionar la imagen, si se selecciona una imagen, se muestra en el ImageView
     */
    private void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
        Stage stage = (Stage) SelectImageButton.getScene().getWindow();
        File tempImage = fileChooser.showOpenDialog(stage);

        if (tempImage != null) {
            selectedImage = tempImage;
            AvatarImageView.setImage(new Image(selectedImage.toURI().toString()));
        }
    }

    /**
     * Método que maneja el evento de clic en el botón de registrar
     * Valida si los campos están vacíos y realiza el registro
     * Sí el registro es correcto, se muestra un dialogo para redirigir a la pantalla de login
     * Si el registro es incorrecto, se muestra un mensaje de error en un dialog0
     */
    private void handleRegister() {
        String userName = UserTextField.getText().trim();
        String password = PasswordField.getText().trim();
        String confirmPassword = ConfirmPasswordField.getText().trim();

        if (userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            DialogUtils.showError("Empty fields", "Please fill in all the fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            DialogUtils.showError("Password mismatch", "The passwords do not match.");
            return;
        }

        if (selectedImage == null) {
            DialogUtils.showError("No image selected", "Please select an image.");
            return;
        }

        if (!isImageFile(selectedImage)) {
            DialogUtils.showError("Invalid image format", "Please select an image with a valid format.");
            return;
        }

        UserResponse userResponse = proceedWithRegistration(userName, password, selectedImage);
        if (userResponse != null && userResponse.isOk()) {
            boolean confirm = DialogUtils.showConfirmation("Confirm", "User registered successfully. Redirecting to login...");
            if (confirm) redirectToLogin();
        } else {
            DialogUtils.showError("Error", "Registration failed.");
        }
    }

    /**
     * Método para realizar registro del nuevo usuario
     * @param userName, String
     * @param password, String
     * @param selectedImage, File
     * @return UserResponse si el registro es correcto, null si no lo es
     */
    private UserResponse proceedWithRegistration(String userName, String password, File selectedImage) {
        try {
            String base64Image = convertImageToBase64(selectedImage);
            return HttpService.register(userName, password, base64Image);
        } catch (IOException e) {
            DialogUtils.showError("Error", "Error encoding image to Base64");
            return null;
        }
    }

    /**
     * Método auxiliar para comprobar si el archivo cargado es  una imagen
     * @param file, File
     * @return Boolean true si es una imagen, false si no lo es
     */
    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".gif") || fileName.endsWith(".bmp");
    }

    /**
     * Método auxiliar para convertir imagen a base64
     * @param file, File
     * @exception IOException, si hay un error al leer el archivo
     * @return String base64
     */
    private String convertImageToBase64(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = fis.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        }
    }

    /**
     * Método que maneja el evento de clic en el botón de cancelar
     * Verifica si hay datos en los campos, si los hay, muestra un mensaje de confirmación
     * Si el usuario confirma, redirige a la pantalla de login
     * Si no hay datos en los campos, redirige a la pantalla de login
     */
    private void handleCancel() {
        String userName = UserTextField.getText().trim();
        String password = PasswordField.getText().trim();
        String confirmPassword = ConfirmPasswordField.getText().trim();

        if (!userName.isEmpty() || !password.isEmpty() || !confirmPassword.isEmpty()) {
            boolean confirm = DialogUtils.showConfirmation("Confirm", "Are you sure you want to go back to login? All the information you entered will be lost.");
            if (confirm) redirectToLogin();
        } else {
            redirectToLogin();
        }
    }

    /**
     * Método que maneja el evento de clic en el botón de cancelar
     * Redirige a la pantalla de login
     * Si no se puede cargar la pantalla de login, registra el error
     */
    private void redirectToLogin() {
        try {
            ScreenLoader.loadScreen("/com/milacanete/messagesfx/loginView.fxml", (Stage) CancelButton.getScene().getWindow());
        } catch (IOException e) {
            DialogUtils.showError("Error", "Unable to load the login screen.");
        }
    }




}
