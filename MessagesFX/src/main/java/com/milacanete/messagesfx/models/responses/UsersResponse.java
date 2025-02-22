package com.milacanete.messagesfx.models.responses;

import com.milacanete.messagesfx.models.User;

import java.util.List;

/**
 * Clase que representa la respuesta de la petición de usuarios
 * Contiene una lista de usuarios
 */
public class UsersResponse extends OkResponse {
    /**Atributo que representa la lista de usuarios*/
    private List<User> users;

    /**
     * Get de la lista de usuarios
     * @return lista de usuarios
     */
    public List<User> getUsers() {
        return users;
    }
}
