# 💬 MessagesFX

## 📝 Descripción
MessagesFX es una aplicación de escritorio desarrollada con **JavaFX** que consume un servicio web basado en **Node.js** y **MongoDB**. Su objetivo principal es aprender y practicar el consumo de peticiones **REST** mediante una interfaz intuitiva.

La aplicación permite a los usuarios registrarse e iniciar sesión. Dentro de la vista principal, los usuarios pueden:
- Cambiar su imagen de avatar.
- Refrescar la lista de mensajes recibidos.
- Enviar mensajes a otros usuarios.
- Eliminar mensajes de su bandeja de entrada.

Todas las acciones incorporan validaciones necesarias para garantizar una experiencia fluida y evitar errores, como asegurarse de que haya un usuario seleccionado antes de enviar un mensaje.

## 📁 Estructura del Proyecto
Este repositorio contiene dos carpetas principales:
- **`message-services/`**: Contiene el backend desarrollado en Node.js y MongoDB, con su propio README.
- **`MessagesFX/`**: Contiene la aplicación de escritorio desarrollada en JavaFX.

## ✨ Características principales
- **Gestión de usuarios**: Registro e inicio de sesión de usuarios.
- **Mensajería**: Envío, recepción y eliminación de mensajes.
- **Personalización**: Posibilidad de cambiar la imagen de perfil.
- **Validaciones**: Control de errores y restricciones en cada acción.
- **Consumo de API REST**: Comunicación con un backend en Node.js y MongoDB.

## 🚀 Requisitos
- **Sistema operativo:** Windows, macOS o Linux.
- **Herramientas necesarias:**
  - IntelliJ IDEA o cualquier IDE compatible con Java.
  - Scene Builder para el diseño de la interfaz.
  - JavaFX SDK.
  - Node.js y MongoDB para el backend.

## 📂 Instalación General
1. Clona este repositorio:
   ```sh
   git clone https://github.com/TU_USUARIO/MessagesFX.git
   ```
2. Sigue las instrucciones de instalación del backend en `message-services/README.md`.
3. Para ejecutar el frontend:
   - Abre el proyecto en **IntelliJ IDEA**.
   - Configura el soporte para **JavaFX**.
   - Asegúrate de que el backend en **Node.js** y **MongoDB** esté ejecutándose correctamente.
   - Ejecuta la aplicación desde **IntelliJ IDEA**.

## 🎥 Flujo de la Aplicación
Puedes ver un video demostrativo del flujo de la aplicación en el siguiente enlace:
[![Ver video](https://img.shields.io/badge/📺-Ver%20Video-blue)](https://drive.google.com/drive/folders/1cRJwJstl7nGZigQ2uY_hUNQphWSaCg2m)

## 🛠️ Tecnologías Utilizadas
- **JavaFX**: Para el desarrollo de la interfaz gráfica.
- **Scene Builder**: Para diseñar las vistas de la aplicación.
- **CSS**: Para la personalización del estilo visual.
- **Node.js y Express**: Backend y API REST.
- **MongoDB**: Base de datos para el almacenamiento de información.

## 📜 Licencia
Este proyecto es de uso libre y está bajo la **Licencia MIT**. Consulta el archivo LICENSE para más detalles.
