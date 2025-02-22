const User = require('../models/User');
const sha256 = require('sha256');
const jwt = require('jsonwebtoken');
const fs = require('fs');
const path = require('path');

/**
 * Login usuario 
 */
const login = async (req, res) => {
    try {
        const { name, password } = req.body;

        //valida si ambos campos son proporcionados
        if (!name || !password) {
            return res.status(400).json({ message: 'Both name and password are required' });
        }

        //validar si el usuario existe y si la contraseña es correcta (encriptada)
        const user = await User.findOne({ name });
        if (!user || user.password !== sha256(password)) {
            return res.status(401).json({ message: 'User or password incorrect' });
        }

        //generar token
        const token = jwt.sign({ _id: user._id }, process.env.JWT_SECRET, { expiresIn: '1y' });

        //devolver token y datos del usuario
        return res.json({
            ok: true,
            token,
            name: user.name,
            image: user.image
        });

    } catch (error) {
        console.log('JWT_SECRET:', process.env.JWT_SECRET); 
        console.log('Error en login', error);
        return res.status(500).json({ 
            ok: false,
            message: 'Internal server error' });
    }
};

/**
 * Registrar usuario
 */
const register = async (req, res) => {
    try {
        const { name, password, image } = req.body;

        //validar si todos los campos son proporcionados
        if (!name || !password || !image) {
            return res.status(400).json({ message: 'All fields are required' });
        }

        //validar si el usuario ya existe
        const user = await User.findOne({ name: name.toLowerCase() });
        if (user) {
            return res.status(400).json({ 
                ok: false,
                message: 'User already exists' });
        }

        //encriptar contraseña 
        const passwordHash = sha256(password);

        // Verificar si el directorio 'img' existe, si no, crearlo
        const dir = path.join(__dirname, '..', 'img');
        if (!fs.existsSync(dir)) {
            fs.mkdirSync(dir);
        }

        
        // Guardar imagen en el sistema de archivos con un nombre único
        const timestamp = Date.now();
        const filePath = path.join(dir, `${timestamp}.jpg`);
        fs.writeFileSync(filePath, Buffer.from(image, 'base64'));

        
        // Crear nuevo usuario con la ruta de la imagen
        const newUser = new User({ name, password: passwordHash, image: `img/${timestamp}.jpg` });
        await newUser.save();

        res.json({ ok: true });
    } catch (error) {
        console.log('Error en register', error);
        return res.status(500).json({ 
            ok: false,
            message: "User couldn't be registered" });
    }
};

/**
 * obtener todos los usuarios
 */
const getAllUsers = async (req, res) => {
    try {
        //obtener todos los usuarios en un array - json
        const users = await User.find();
        return res.json({ ok: true, users });
    } catch (error) {
        console.log('Error en GET / users:', error);
        return res.status(500).json({ 
            ok: false,
            message: 'Internal server error' });
    }
};

/**
 * Actualizar imagen avatar del usuario
 */
const updateImage = async (req, res) => {
    try {
        // Obtener el usuario usando el id del token
        const user = await User.findById(req.user._id);
        if (!user) {
            return res.status(404).json({
                mensaje: 'User not found'
            });
        }

        // Verificar que la imagen sea proporcionada en base64
        if (!req.body.image) {
            return res.status(400).json({
                message: 'Image is required'
            });
        }

        // Verificar que la imagen esté en formato base64
        const base64Pattern = /^data:image\/(png|jpg|jpeg|gif|bmp|webp);base64,/;
        if (!base64Pattern.test(req.body.image)) {
            return res.status(400).json({ message: 'Image must be in base64 format' });
        }

        // Si ya existe una imagen, eliminarla
        if (user.image) {
            const oldImagePath = path.join(__dirname, '..', user.image);
            if (fs.existsSync(oldImagePath)) {
                fs.unlinkSync(oldImagePath);
            }
        }

        // Eliminar el prefijo
        const base64Data = req.body.image.split(',')[1];

        // Guardar nueva imagen en base64
        const newImagePath = `img/${Date.now()}_${user.name}.jpg`;
        const imageBuffer = Buffer.from(base64Data, 'base64');
        fs.writeFileSync(path.join(__dirname, '..', newImagePath), imageBuffer);

        // Actualizar usuario con la nueva imagen
        user.image = newImagePath;
        await user.save();
        
        res.json({ ok: true });

    } catch (error) {
        console.log('Error updateImage', error);
        return res.status(500).json({
            ok: false,
            error: `Error updating user: ${req.user._id}`
        });
    }
};

module.exports = { login, register, getAllUsers, updateImage };