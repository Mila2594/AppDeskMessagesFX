package com.milacanete.messagesfx.models.responses;

import com.google.gson.JsonObject;
import com.milacanete.messagesfx.models.Sent;

/**
 * Clase que representa la respuesta de un mensaje
 * Contiene el mensaje, el emisor, el receptor y la imagen
 */
public class MessageResponse extends OkResponse {
    /**
     * Atributo que representa el emisor del mensaje
     */
    private String from;
    private String to;
    private JsonObject message;
    private String image;
    private Sent sent;

    /**
     * Ger id del emisor
     * @return  string id del emisor
     */

    public String getFrom() {
        return from;
    }

    /**
     * Set id del emisor
     * @param from, id del emisor
     */

    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Get id del receptor
     * @return  string id del receptor
     */
    public String getTo() {
        return to;
    }

    /**
     * Set id del receptor
     * @param to, id del receptor
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Get mensaje
     * @return  JsonObject mensaje
     */
    public JsonObject getMessage() {
        return message;
    }

    /**
     * Set mensaje
     * @param message, JsonObject mensaje
     */
    public void setMessage(JsonObject message) {
        this.message = message;
    }

    /**
     * Get imagen
     * @return  string con la url de la imagen
     */
    public String getImage() {
        return image;
    }

    /**
     * Set imagen
     * @param image, string con la url de la imagen
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Get fecha de envío del mensaje
     * @return Sent con la fecha de envío
     */
    public Sent getSent() {
        return sent;
    }

    /**
     * Set fecha de envío del mensaje
     * @param sent, Sent con la fecha de envío
     */
    public void setSent(Sent sent) {
        this.sent = sent;
    }


}