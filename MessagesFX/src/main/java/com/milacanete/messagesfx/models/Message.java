package com.milacanete.messagesfx.models;

import com.google.gson.annotations.SerializedName;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Base64;

/**
 * Message class para representar un mensaje
 */
public class Message {
    /**
     * Attributes
     */
    @SerializedName("_id")
    private String id;
    private User from;
    private String to;
    private String message;
    private String image;
    private LocalDate sent;

    // Default constructor
    public Message() {
    }

    /**
     * Constructor with all fields
     * @param id, string
     * @param from, User
     * @param to, string
     * @param message, string
     * @param image, string
     * @param sent, LocalDate
     */
    public Message(String id, User from, String to, String message, String image, LocalDate sent) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.message = message;
        this.image = image;
        this.sent = sent;
    }

    /**
     * Constructor without id
     * @return Message
     */
    public String getId() {
        return id;
    }

    /**
     * Get from user
     * @return User
     */
    public User getFrom() {
        return from;
    }

    /**
     * Get to user
     * @return String
     */
    public String getTo() {
        return to;
    }

    /**
     * Get message to
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get Sent date
     * @return LocalDate
     */
    public LocalDate getSent() {
        return sent;
    }


    /**
     * Método para obtener la imagen del mensaje
     * Codifica la imagen en base64 y la convierte en un objeto Image
     * @return ImageView
     */
    public ImageView getImageView() {
        if (image == null || image.isEmpty()) {
            return new ImageView();
        }

        byte[] decodedBytes = Base64.getDecoder().decode(image);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
        Image img = new Image(inputStream);
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(50);
        imgView.setPreserveRatio(true);
        return imgView;
    }
}