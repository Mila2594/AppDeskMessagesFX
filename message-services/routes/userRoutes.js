//modulos
const express = require('express');
const { login, register, getAllUsers, updateImage } = require('../controllers/userController');
const authenticate = require('../middlewares/authenticate');

//crear router
const router = express.Router();

//rutas
router.post('/login', login);
router.post('/register', register);
router.get('/users', authenticate, getAllUsers);
router.put('/users', authenticate, updateImage);

//exportar router
module.exports = router;