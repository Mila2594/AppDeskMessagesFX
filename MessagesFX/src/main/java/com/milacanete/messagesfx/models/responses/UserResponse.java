package com.milacanete.messagesfx.models.responses;

import com.milacanete.messagesfx.models.User;

/**
 * Clase para la respuesta de usuario
 */
public class UserResponse extends OkResponse {
    /**
     * Usuario
     */
    private User user;

    /**
     * Get user
     * @return user
     */
    public User getUser() {
        return user;
    }
}
