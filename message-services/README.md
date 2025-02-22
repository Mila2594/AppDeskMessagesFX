# 🛠️ Message Service - Backend

## 📝 Descripción
Este es el backend de **MessagesFX**, desarrollado con **Node.js** y **MongoDB**. Expone una API REST para la autenticación de usuarios, gestión de mensajes y otras operaciones necesarias para la aplicación.

## 🚀 Requisitos
- **Node.js** (versión recomendada: 16+)
- **MongoDB** (local o remoto)

## 📂 Instalación y Configuración
1. Accede a la carpeta del backend:
   ```sh
   cd message-services
   ```
2. Instala las dependencias del proyecto:
   ```sh
   npm install
   npm dotenv
   ```
3. Copia el archivo de configuración `.env.example` y renómbralo como `.env`:
   - En **Linux/macOS**:
     ```sh
     cp .env.example .env
     ```
   - En **Windows** (cmd):
     ```sh
     copy .env.example .env
     ```
4. Edita el archivo `.env` con la configuración adecuada:
   ```env
   MONGO_URL=mongodb://127.0.0.1:27017/message_services
   PORT=3000
   JWT_SECRET=tu_clave_secreta
   ```

## ▶️ Ejecución del Servidor
Para iniciar el backend, ejecuta:
```sh
npm start
```
El servidor se ejecutará en `http://localhost:3000/`.

## 🔍 Pruebas con Postman
Para probar la API, puedes importar la colección `MESSAGE-SERVICES.postman_collection.json` en Postman y ejecutar las solicitudes.

## 📜 Licencia
Este proyecto es de uso libre y está bajo la **Licencia MIT**. Consulta el archivo LICENSE para más detalles.
