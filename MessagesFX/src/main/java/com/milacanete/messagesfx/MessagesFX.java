package com.milacanete.messagesfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal de la aplicación
 * Carga la vista de login al iniciar la aplicación
 */
public class MessagesFX extends Application {

    /**
     * Método para cargar la vista de login al iniciar la aplicación
     * @param stage, ventana principal de la aplicación
     * @throws IOException, excepción en caso de error al cargar la vista
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader sceneLoader = new FXMLLoader(MessagesFX.class.getResource("loginView.fxml"));
        Scene scene = new Scene(sceneLoader.load());
        stage.setTitle("MessagesFX App");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Método principal de la aplicación
     * @param args, argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
