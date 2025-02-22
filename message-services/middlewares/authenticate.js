//modulos
const jwt = require('jsonwebtoken');

//variables de entorno
const JWT_SECRET = process.env.JWT_SECRET;

//middleware de autenticacion
const authenticate = (req, res, next) => {

    //validar si el token es proporcionado
    const token = req.headers.authorization?.split(' ')[1];
    if (!token) {
        return res.status(401).json({ 
            message: 'Token is required' });
    }

    //validar si el token es valido
    jwt.verify(token, JWT_SECRET, (error, decoded) => {
        if (error) {
            return res.status(401).json({ message: 'Invalid token' });
        }
        req.user = decoded; //guardar el usuario en la peticion
        next();
    });
};

module.exports = authenticate;