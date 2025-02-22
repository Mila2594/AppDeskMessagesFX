package com.milacanete.messagesfx.models.responses;

import com.milacanete.messagesfx.models.Message;

import java.util.List;

/**
 * Clase que representa la respuesta de la petición de mensajes
 * Contiene una lista de mensajes
 */
public class MessagesResponse extends OkResponse {
    /**Atributo que representa la lista de mensajes*/
    private List<Message> messages;

    /**
     * Get de la lista de mensajes
     * @return Lista de mensajes
     */
    public List<Message> getMessages() {
        return messages;
    }
}
