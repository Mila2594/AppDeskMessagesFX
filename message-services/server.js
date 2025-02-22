require('dotenv').config();

//Modulos 
const express = require('express');
const mongoose = require('mongoose');
const jwt = require('jsonwebtoken');
const sha256 = require('sha256');

//Crear servidor
const app = express();

// Parser json en las peticiones con límite aumentado
app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ limit: '50mb', extended: true }));

//Proveer archivos estaticos (imagenes)
app.use(express.static('public'));
app.use('/img', express.static(__dirname + '/img'));


//Conexion a la base de datos
const mongoURL = process.env.MONGO_URL || 'mongodb://localhost:27017/message-services';

mongoose.connect(mongoURL)
.then(() => console.log('Conexion exitosa a la base de datos'))
.catch((error) => console.log('Error al conectar a MongoDB', error))

//Establecer las rutas y usarlas
const userRoutes = require('./routes/userRoutes');
const messageRoutes = require('./routes/messageRoutes');

app.use('/user', userRoutes);
app.use('/message', messageRoutes);
console.log(app._router.stack.map(r => r.route && r.route.path).filter(Boolean));


//Definir puerto por defecto 
const port = process.env.PORT || 3000;

//Iniciar el servidor
app.listen(port, () => {
    console.log(`Servidor escuchando en el http://localhost:${port}`);
});