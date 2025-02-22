//modulos
const express = require('express');
const { getAllMessages, sendMessage, deleteMessage } = require('../controllers/messageController');
const authenticate = require('../middlewares/authenticate');

//crear router
const router = express.Router();

//rutas
router.get('/messages', authenticate, getAllMessages);
router.post('/messages/:toUserId', authenticate, sendMessage);
router.delete("/messages/:id", authenticate,deleteMessage);

//exportar router
module.exports = router;