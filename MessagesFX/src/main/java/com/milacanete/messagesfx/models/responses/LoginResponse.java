package com.milacanete.messagesfx.models.responses;

/**
 * Clase que representa la respuesta de la petición de login
 */
public class LoginResponse extends OkResponse {
    /**
     * Atributos de la respuesta
     */
    private String token;
    private String name;
    private String image;

    /**
     * Get url de la imagen
     * @return string con la url de la imagen
     */
    public String getImage() {
        return image;
    }

    /**
     * Get nombre del usuario
     * @return string con el nombre del usuario
     */
    public String getName() {
        return name;
    }

    /**
     * Get token de la respuesta
     * @return string con el token de la respuesta
     */
    public String getToken() {
        return token;
    }




}
