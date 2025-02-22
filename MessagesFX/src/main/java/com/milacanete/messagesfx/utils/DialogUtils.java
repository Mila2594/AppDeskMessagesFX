package com.milacanete.messagesfx.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Clase para proporcionar métodos de utilidad para mostrar diálogos de error, información y confirmación
 */
public class DialogUtils {

    /**
     * Muestra un diálogo de error con un título y un mensaje
     * @param title, string con el título del diálogo
     * @param message, string con el mensaje del diálogo
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo de información con un título y un mensaje
     * @param title, string con el título del diálogo
     * @param message, string con el mensaje del diálogo
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo de confirmación con un título y un mensaje
     * @param title, string con el título del diálogo
     * @param message, string con el mensaje del diálogo
     * @return boolean, true si el usuario ha confirmado, false si ha cancelado
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
