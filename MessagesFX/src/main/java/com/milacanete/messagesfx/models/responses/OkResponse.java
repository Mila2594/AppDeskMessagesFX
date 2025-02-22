package com.milacanete.messagesfx.models.responses;

/**
 * Clase para manejar la respuesta de la API
 * Maneja el estado de la respuesta y el mensaje de error
 */
public class OkResponse {
    /**
     * Atributos de la clase
     */
    private boolean ok;
    private String error;

    /**
     * Booleano para saber si la respuesta es correcta
     * @return true si la respuesta es correcta, false si no
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * Setter para el booleano de respuesta correcta
     * @param b booleano para saber si la respuesta es correcta
     */
    public void setOk(boolean b) {
    }

    /**
     * Setter para el mensaje de error
     * @param error mensaje de error
     */
    public void setError(String error) {
    }
}
