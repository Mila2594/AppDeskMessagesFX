package com.milacanete.messagesfx.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Clase para cargar las pantallas de la aplicación
 */
public class ScreenLoader {

    /**
     * Método para cargar una pantalla en el stage
     * @param viewPath, ruta de la vista a cargar
     * @param stage, stage donde se cargará la vista
     * @throws IOException, excepción en caso de error al cargar la vista
     */
    public static void loadScreen(String viewPath, Stage stage) throws IOException {
        Parent view = FXMLLoader.load(Objects.requireNonNull(ScreenLoader.class.getResource(viewPath)));
        Scene view1Scene = new Scene(view);
        stage.hide();
        stage.setScene(view1Scene);
        stage.show();
    }

}
