package com.milacanete.messagesfx.models;

import com.google.gson.annotations.SerializedName;
import com.milacanete.messagesfx.service.HttpService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * User class para representar un usuario
 */
public class User {
    /**
     * Atributos de la clase User
     */
    @SerializedName("_id")
    private String id;
    private String name;

    public String getPassword() {
        return password;
    }

    private String password;
    private String image;


    // Default constructor
    public User() {
    }

    /**
     * Constructor con todos los atributos
     * @param id, string
     * @param name, string
     * @param password, string
     * @param image, string
     */
    public User(String id, String name, String password, String image) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.image = image;
    }

    /**
     * Constructor sin password
     * @param id, string
     * @param name, string
     * @param image, string
     */
    public User(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    /**
     * Get id usuario
     * @return id, string
     */
    public String getId() {
        return id;
    }

    /**
     * Get nombre usuario
     * @return name, string
     */
    public String getName() {
        return name;
    }

    /**
     * Set url imagen usuario
     * @param image, string
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Método para obtener la imagen del avatar del usuario
     * Codifica la imagen en base64 y la convierte en un objeto Image
     * @return ImageView
     */
    public ImageView getImageView() {
        if (image == null || image.isEmpty()) {
            return new ImageView();
        }

        String imageUrl = HttpService.getBaseUrl() + image;
        ImageView imgView = new ImageView(new Image(imageUrl));
        imgView.setFitHeight(50);
        imgView.setPreserveRatio(true);
        return imgView;
    }
}
