package com.milacanete.messagesfx.controllers;

import com.milacanete.messagesfx.models.Message;
import com.milacanete.messagesfx.models.User;
import com.milacanete.messagesfx.models.responses.LoginResponse;
import com.milacanete.messagesfx.models.responses.MessageResponse;
import com.milacanete.messagesfx.models.responses.MessagesResponse;
import com.milacanete.messagesfx.models.responses.UsersResponse;
import com.milacanete.messagesfx.service.HttpService;
import com.milacanete.messagesfx.utils.DialogUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

/**
 * Controlador de la interfaz para gestionar mensajes y usuarios.
 * Permite mostrar, enviar y eliminar mensajes, además de actualizar avatares
 * e interactuar con el backend mediante `HttpService`.
 */

public class MessagesController {
    /**
     * ImageView para mostrar el avatar del usuario
     */
    @FXML
    private ImageView AvatarUserImageView;
    /**
     * Botón para cambiar la imagen del avatar
     */
    @FXML
    private Button ChangeImageButton;
    /**
     * Botón para eliminar un mensaje
     */
    @FXML
    private Button DeleteMessageButton;
    /**
     * ImageView para mostrar la imagen seleccionada del mensaje a enviar
     */
    @FXML
    private ImageView ImageMessageImageView;
    /**
     * TableView para mostrar los mensajes
     */
    @FXML
    private TableView<Message> MessagesUserTableView;
    /**
     * TableColumn para mostrar el usuario que envió el mensaje
     */
    @FXML
    private TableColumn<Message, String> SenderUserColumn;
    /**
     * TableColumn para mostrar la fecha de envío del mensaje
     */
    @FXML
    private TableColumn<Message, String> SentColumn;
    /**
     * TableColumn para mostrar la imagen del mensaje
     */
    @FXML
    private TableColumn<Message, ImageView> ImagenColumn;
    /**
     * TableColumn para mostrar el mensaje
     */
    @FXML
    private TableColumn<Message, String> MessageColumn;
    /**
     * TableColumn para mostrar el avatar del usuario que envió el mensaje
     */
    @FXML
    private TableColumn<Message, ImageView> AvatarSenderColumn;
    /**
     * Label para mostrar el nombre del usuario
     */
    @FXML
    private Label NameUserLabel;
    /**
     * TextField para escribir un nuevo mensaje
     */
    @FXML
    private TextField NewMessageTextField;
    /**
     * TableColumn para mostrar el nombre del usuario
     */
    @FXML
    private TableView<User> UsersTableView;
    /**
     * TableColumn para mostrar el nombre del usuario
     */
    @FXML
    private TableColumn<User, String> NickNameColumn;
    /**
     * TableColumn para mostrar el avatar del usuario
     */
    @FXML
    private TableColumn<User, ImageView> AvatarColumn;
    /**
     * Botón para refrescar la lista de mensajes
     */
    @FXML
    private Button RefreshButton;
    /**
     * Botón para seleccionar una imagen
     */
    @FXML
    private Button SelectImageButton;
    /**
     * Botón para enviar un mensaje
     */
    @FXML
    private Button SendMessageButton;
    /**
     * Variable temporal, Archivo seleccionado de la imagen
     */
    private File selectedImage;
    /**
     * Lista de usuarios
     */
    private List<User> usersList;

    /**
     * Inicializa la clase controladora.
     * Enlaza los datos de la tabla con las columnas correspondientes.
     * Carga la lista de mensajes y la lista de usuarios.
     * Configura los botones de la interfaz.
     * Configura listener para habilitar/deshabilitar el botón de enviar mensaje y el botón de eliminar mensaje.
     */
    @FXML
    private void initialize() {

        LoginResponse loginResponse = LoginController.getLoginResponse();
        if (loginResponse != null) {
            NameUserLabel.setText(loginResponse.getName());
            User user = new User();
            user.setImage(loginResponse.getImage()); // Establece la imagen en el objeto User
            ImageView imageView = user.getImageView(); // Llama al método getImageView() para obtener la imagen decodificada
            AvatarUserImageView.setImage(imageView.getImage()); // Establece la imagen en el ImageView
        }

        //configurar las columnas de la tabla users
        NickNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));        // Usar SimpleObjectProperty para el ImageView
        AvatarColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getImageView()));

        SenderUserColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getFrom() == null) {
                return new SimpleStringProperty("Unknown");
            } else {
                return new SimpleStringProperty(cellData.getValue().getFrom().getName());
            }
        });

        AvatarSenderColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getFrom() == null) {
                return new SimpleObjectProperty<>(new ImageView());
            } else {
                return new SimpleObjectProperty<>(cellData.getValue().getFrom().getImageView());
            }
        });
        MessageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMessage()));
        ImagenColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getImageView()));
        SentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSent().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        // Cargar lista de mensajes a la tabla
        loadMessages();
        // Cargar lista de usuarios a la tabla
        loadUsers();

        // Configurar el botón de seleccionar imagen
        SelectImageButton.setOnAction(_ -> handleSelectImage());

        // Configurar el botón de enviar mensaje
        SendMessageButton.setOnAction(_ -> sendMessage());

        // Deshabilitar el botón de enviar mensaje por defecto
        SendMessageButton.setDisable(true);

        // Agregar listener al campo de texto para habilitar/deshabilitar el botón de enviar mensaje
        NewMessageTextField.textProperty().addListener((_, _, newValue) -> SendMessageButton.setDisable(newValue.trim().isEmpty()));

        //configurar el botón de refrescar
        RefreshButton.setOnAction(_ -> loadMessages());

        // Habilitar el botón de eliminar mensaje cuando se seleccione un mensaje
        MessagesUserTableView.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> DeleteMessageButton.setDisable(newValue == null));

        // Configurar el botón de eliminar mensaje
        DeleteMessageButton.setOnAction(_ -> deleteMessage());

        // Configurar el botón de cambiar imagen
        ChangeImageButton.setOnAction(_ -> handleChangeAvatar());
    }

    /**
     * Método para cargar la lista de usuarios
     * Obtiene la lista de usuarios desde el servicio HttpService y la asigna a la tabla UsersTableView
     */
    private void loadUsers() {
        UsersResponse usersResponse = HttpService.getAllUsers();
        if (usersResponse != null && usersResponse.getUsers() != null) {
            usersList = usersResponse.getUsers();
            UsersTableView.setItems(FXCollections.observableArrayList(usersList));
        }
    }

    /**
     * Método para cargar la lista de mensajes
     * Obtiene la lista de mensajes desde el servicio HttpService y la asigna a la tabla MessagesUserTableView
     */
    private void loadMessages() {
        MessagesResponse messagesResponse = HttpService.getAllMessages();
        if (messagesResponse != null && messagesResponse.getMessages() != null) {
            List<Message> messages = messagesResponse.getMessages();
            MessagesUserTableView.setItems(FXCollections.observableArrayList(messages));
        }
    }

    /**
     * Método para enviar un mensaje
     * Obtiene el mensaje del campo de texto NewMessageTextField
     * Valida que el mensaje no esté vacío
     * Obtiene el usuario logueado y el usuario seleccionado
     * Obtiene la imagen seleccionada y la convierte a Base64
     * Envía el mensaje al servicio HttpService
     */
    private void sendMessage() {
        String messageText = NewMessageTextField.getText();
        if (messageText == null || messageText.isEmpty()) {
            DialogUtils.showError("Error", "Message text is empty");
            return;
        }

        LoginResponse loginResponse = LoginController.getLoginResponse();
        if (loginResponse == null) {
            DialogUtils.showError("Error", "User is not logged in");
            return;
        }

        User selectedUser = UsersTableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            //mostrar dialogo de error
            DialogUtils.showError("Error, No user selected", "Please select a user to send a message to");
            return;
        }

        String from = getUserIdByName(loginResponse.getName()); // Obtener el ID del usuario logueado por nombre
        String to = getUserIdByName(selectedUser.getName()); // Obtener el ID del usuario seleccionado por nombre

        // Obtener la imagen desde el ImageView
        String image = null;
        if (selectedImage != null) {
            try {
                image = convertImageToBase64(selectedImage);
            } catch (IOException e) {
                DialogUtils.showError("Error", "Error encoding image to Base64");
            }
        }

        MessageResponse messageResponse = HttpService.createMessage(from, to, messageText, image);
        if (messageResponse != null && messageResponse.isOk()) {
            DialogUtils.showInfo("Success", "Message sent successfully");
            NewMessageTextField.clear();
            selectedImage = null;
            ImageMessageImageView.setImage(null);
        } else {
            DialogUtils.showError("Error", "Failed to send message");
        }
    }

    /**
     * Método para obtener el ID de un usuario por su nombre
     * @param name, String
     * @return String, ID del usuario
     */
    private String getUserIdByName(String name) {
        for (User user : usersList) {
            if (user.getName().equals(name)) {
                return user.getId();
            }
        }
        return null;
    }

    /**
     * Método para convertir una imagen a Base64
     * @param file, File
     * @return String, imagen en formato Base64
     * @throws IOException, si hay un error al leer el archivo
     */
    private String convertImageToBase64(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = fis.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        }
    }

    /**
     * Método para comprobar si un archivo es una imagen
     * @param file, File
     * @return Boolean, true si es una imagen, false si no lo es
     */
    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".gif") || fileName.endsWith(".bmp");
    }

    /**
     * Método para manejar la selección de una imagen
     * Muestra un diálogo para seleccionar una imagen
     * Valida que la imagen seleccionada sea válida
     * Muestra la imagen seleccionada en el ImageView ImageMessageImageView
     */
    private void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            if (isImageFile(selectedFile)) {
                selectedImage = selectedFile;
                Image image = new Image(selectedFile.toURI().toString());
                ImageMessageImageView.setImage(image);
            } else {
                DialogUtils.showError("Invalid image format", "Please select an image with a valid format.");
            }
        }
    }

    /**
     * Método para eliminar un mensaje
     * Obtiene el mensaje seleccionado de la tabla MessagesUserTableView
     * Valida que se haya seleccionado un mensaje
     * Envía la petición al servicio HttpService
     * Muestra un mensaje de éxito o error
     */
    private void deleteMessage() {
        Message selectedMessage = MessagesUserTableView.getSelectionModel().getSelectedItem();
        if (selectedMessage == null) {
            DialogUtils.showError("Error", "No message selected");
            return;
        }

        String messageId = selectedMessage.getId();
        String response = HttpService.deleteMessage(messageId);

        if (response.contains("\"ok\":true")) {
            DialogUtils.showInfo("Success", "Message deleted successfully");
            loadMessages();
        } else {
            DialogUtils.showError("Error", "Failed to delete message");
        }
    }

    /**
     * Método para manejar el cambio de avatar
     * Muestra un diálogo para seleccionar una imagen
     * Valida que la imagen seleccionada sea válida
     * Convierte la imagen a Base64
     * Envía la petición al servicio HttpService
     * Muestra un mensaje de éxito o error
     */
    private void handleChangeAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            if (isImageFile(selectedFile)) {
                try {
                    String base64Image = convertImageToBase64(selectedFile);
                    updateAvatar(base64Image);
                } catch (IOException e) {
                    DialogUtils.showError("Error", "Error encoding image to Base64");
                }
            } else {
                DialogUtils.showError("Invalid image format", "Please select an image with a valid format.");
            }
        }
    }

    /**
     * Método para actualizar el avatar del usuario
     * valída que el usuario esté logueado
     * Convierte la imagen a Base64
     * Envía la petición al servicio HttpService
     * Sí la respuesta contiene "ok:true", actualiza el avatar en la interfaz
     * Sí la respuesta contiene "ok:false", muestra un mensaje de error
     * @param base64Image, String
     */
    private void updateAvatar(String base64Image) {
        LoginResponse loginResponse = LoginController.getLoginResponse();
        if (loginResponse == null) {
            DialogUtils.showError("Error", "User is not logged in");
            return;
        }

        String response = HttpService.updateAvatar(base64Image);

        if (response.contains("\"ok\":true")) {
            Image image = new Image(new ByteArrayInputStream(Base64.getDecoder().decode(base64Image)));
            AvatarUserImageView.setImage(image);
            loadUsers();
            loadMessages();
        } else {
            DialogUtils.showError("Error", "Failed to update avatar");
        }
    }
}
