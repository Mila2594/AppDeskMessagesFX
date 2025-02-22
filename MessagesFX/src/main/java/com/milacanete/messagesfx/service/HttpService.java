package com.milacanete.messagesfx.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import com.google.gson.*;
import com.milacanete.messagesfx.models.responses.*;

/**
 * Servicio para manejar solicitudes HTTP y gestionar la autenticación de usuario.
 * Realiza operaciones de login, registro, gestión de mensajes, usuarios y avatar utilizando métodos HTTP.
 * Además, maneja la serialización/deserialización de objetos JSON y la inclusión de un token de autenticación.
 */
public class HttpService {

    /**
     * URL base para las solicitudes HTTP
     */
    private static final String baseUrl = "http://127.0.0.1:3000/";
    /**
     * Token de autenticación, inicialmente null
     */
    private static String token = null;
    /**
     * Gson para serializar y deserializar JSON, configurado para convertir LocalDate a String y viceversa
     */
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date,_,_) -> new JsonPrimitive(date.toString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)(json,_,_) -> ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDate())
            .create();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static void setToken(String token) {
        HttpService.token = token;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Método para validar si el cuerpo de la solicitud es un JSON
     * @param body, String
     * @return Boolean true si es un JSON, false si no lo es
     */
    public static boolean isJson(String body) {
        try {
            JsonElement element = gson.fromJson(body, JsonElement.class);
            return element.isJsonObject();
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    /**
     * Método genérico para realizar solicitudes HTTP
     * @param endpoint, String
     * @param method, String
     * @param body, String
     * @return response
     */
    public static String makeRequest(String endpoint, String method, String body) {
        if (body != null && !isJson(body)) {
            return null;
        }

        try {
            URL url = new URL(baseUrl + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");

            //si el token existe se agrega a la cabecera
            if (token != null) {
                connection.setRequestProperty("Authorization", "Bearer " + token);
            }

            //métodos POST y PUT enviar cuerpo en la solicitud
            if (method.equals("POST") || method.equals("PUT")) {
                connection.setDoOutput(true);
                try(OutputStream os = connection.getOutputStream()) {
                    byte[] input = Objects.requireNonNull(body).getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }

            //lectura de la respuesta con buffer
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }

            return response.toString();
        } catch (Exception e) {
            return "Network error: " + e.getMessage();
        }
    }

    /**
     * Metodo para convertir respuestas JSON a objetos usan Gson
     * @param jsonResponse, String
     * @param responseType, Class
     * @return gson.fromJson(jsonResponse,responseType)
     */
    private static <T> T parseResponse(String jsonResponse, Class<T> responseType) {
        if (jsonResponse == null) {
            return null;
        }

        try {
            if (jsonResponse.startsWith("{") && jsonResponse.endsWith("}")) {
                return gson.fromJson(jsonResponse, responseType);
            } else {
                // Si el string no es un JSON válido, devolver un objeto de respuesta con el error
                return gson.fromJson("{\"ok\":false,\"error\":\"" + jsonResponse + "\"}", responseType);
            }
        } catch (JsonSyntaxException e) {
            // Si se produce un error al intentar parsear el JSON, devolver un objeto de respuesta con el error
            return gson.fromJson("{\"ok\":false,\"error\":\"" + e.getMessage() + "\"}", responseType);
        }
    }

    /**
     * Login de usuario, solicitud POST
     * @param username, String
     * @param password, String
     * @return makeRequest("/login","POST",data)
     */
    public static LoginResponse login(String username, String password) {
        String data = "{\"name\":\"" + username + "\",\"password\":\"" + password + "\"}";
        String response = makeRequest("user/login", "POST", data);
        return parseResponse(response, LoginResponse.class);
    }

    /**
     * Registro de usuario, solicitud POST
     * @param username, String
     * @param password, String
     * @return makeRequest("/register","POST",data)
     */
    public static UserResponse register(String username, String password, String avatar) {
        String data = "{\"name\":\"" + username + "\",\"password\":\"" + password + "\",\"image\":\"" + avatar + "\"}";
        String response = makeRequest("user/register", "POST", data);
        try {
            return parseResponse(response, UserResponse.class);
        } catch (JsonSyntaxException e) {
            // Leer la respuesta como un string
            assert response != null;
            if (response.contains("400")) {
                // Crear un objeto UserResponse con el error correspondiente
                UserResponse userResponse = new UserResponse();
                userResponse.setOk(false);
                userResponse.setError("El usuario ya existe");
                return userResponse;
            } else if (response.contains("Connection refused")) {
                // Crear un objeto UserResponse con el error correspondiente
                UserResponse userResponse = new UserResponse();
                userResponse.setOk(false);
                userResponse.setError("Error de conexión al servicio");
                return userResponse;
            } else {
                // Crear un objeto UserResponse con el error correspondiente
                UserResponse userResponse = new UserResponse();
                userResponse.setOk(false);
                userResponse.setError("Error desconocido");
                return userResponse;
            }
        }
    }

    /**
     * Traer todos los usuarios, solicitud GET
     * @return makeRequest("/users","GET",null)
     */
    public static UsersResponse getAllUsers() {
        String response = makeRequest("user/users", "GET", null);
        return parseResponse(response, UsersResponse.class);
    }

    /**
     * Actualizar imagen avatar del usuario, solicitud PUT
     * @param avatarUrl, String
     * @return makeRequest("/users/"+userId,"PUT",data)
     */
    public static String updateAvatar(String avatarUrl) {
        String data = "{\"image\":\"data:image/jpeg;base64," + avatarUrl + "\"}";
        return makeRequest("user/users", "PUT", data);
    }

    /**
     * Crear mensaje, solicitud POST
     * @param from, String
     * @param to, String
     * @param message, String
     * @param image, String
     * @return makeRequest("/messages","POST",data)
     */
    public static MessageResponse createMessage(String from, String to, String message, String image) {
        String sent = LocalDateTime.now().format(formatter);
        String data = "{\"from\":\"" + from + "\",\"to\":\"" + to + "\",\"message\":\"" + message + "\",\"image\":\"" + image + "\",\"sent\":\"" + sent + "\"}";
        String response = makeRequest("message/messages/" + to, "POST", data);
        try {
            return parseResponse(response, MessageResponse.class);
        } catch (JsonSyntaxException e) {
            // Leer la respuesta como un string
            assert response != null;
            if (response.contains("400")) {
                // Crear un objeto MessageResponse con el error correspondiente
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setOk(false);
                messageResponse.setError("Error en la solicitud: datos inválidos");
                return messageResponse;
            } else if (response.contains("Connection refused")) {
                // Crear un objeto MessageResponse con el error correspondiente
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setOk(false);
                messageResponse.setError("Error de conexión al servicio");
                return messageResponse;
            } else {
                // Crear un objeto MessageResponse con el error correspondiente
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setOk(false);
                messageResponse.setError("Error desconocido");
                return messageResponse;
            }
        }
    }

    /**
     * Traer todos los mensajes, solicitud GET
     * @return makeRequest("/messages","GET",null)
     */
    public static MessagesResponse getAllMessages() {
        String response = makeRequest("message/messages", "GET", null);
        return parseResponse(response, MessagesResponse.class);
    }

    /**
     * Eliminar mensaje, solicitud DELETE
     * @param messageId, String
     * @return makeRequest("/messages/"+messageId,"DELETE",null)
     */
    public static String deleteMessage(String messageId) {
        return makeRequest("message/messages/" + messageId, "DELETE", null);
    }

}
