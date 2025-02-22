//modelo
const { default: mongoose } = require('mongoose');
const Message = require('../models/Message');
const User = require('../models/User');

/**
 * Obtener todos los mensajes de un usuario
 */
const getAllMessages = async (req, res) => {
    try {
        const userId = req.user._id; //obtiene el id del usuario autentificado

        //obtiene todos los mensajes del usuario autentificado
        const messages = await Message.find({ to: userId })
        .populate("from", "name image")
        .sort({ sent : 1 }); //ordena los mensaje del mas antiguo al mas reciente

        res.json({ ok: true, messages });
    } catch (error) {
        res.status(500).json({
            ok: false,
            error: `Error getting messages for user: ${req.user._id}`
        });
    }
};


/**
 * Enviar un nuevo mensaje a un usuario
 */
const sendMessage = async (req, res) => {
    try {
        const { message, image, sent} = req.body; //obtiene los datos del mensaje
        let { toUserId } = req.params; //id del usuario al que se envia el mensaje
        const fromUserId = req.user._id; //obtiene id del usuario desde el token

        toUserId = toUserId ? toUserId.trim() : null;
        
        //validar que el mensaje tenga contenido
        if (!message || message.trim() === '') {
            return res.status(400).json({
                ok: false,
                error: "Message content is required"
            });
        }

        //validar formato del id de toUser y que existe en la base de datos
        if(!mongoose.Types.ObjectId.isValid(toUserId)) {
            return res.status(400).json({
                ok: false,
                error: "Invalid recipient ID"
            });
        }

        const recipient = await User.findById(toUserId);
        if(!recipient){
            return res.status(404).json({
                ok: false,
                error: "Recipient user not found"
            });
        }

        //crear nuevo mensaje
        const newMessage = new Message({
            from: fromUserId,
            to: toUserId,
            message,
            image: image || null,
            sent: sent || Date.now()
        });

        //guardar mensaje en la base de datos
        const savedMessage = await newMessage.save();
        res.json({
            ok: true,
            message: savedMessage
        });
    } catch (error) {
        console.log("Error sending message:", error);
        res.status(500).json({
            ok: false,
            error: `Error sending a message to: ${req.params.toUserId}`
        });
    }
};


/**
 * Eliminar un mensaje
 */
const deleteMessage = async (req, res) => {
    try {
        const { id } = req.params;

        //validar ID
        if (!mongoose.Types.ObjectId.isValid(id)){
            return res.status(400).json({
                ok: false,
                error: "Invalid message ID"
            });
        }

        //buscar y eliminar mensaje
        const message = await Message.findByIdAndDelete(id);

        //Enviar 404 si el mensaje no existe 
        if(!message){
            return res.status(404).json({
                ok: false,
                error: "Message not found"
            });
        }

        res.json({ok: true});
    
    }catch (error){
        res.status(500).json({
            ok: false,
            error: `Error deleting message: ${id}`
        });
    }
};

module.exports = {
    getAllMessages,
    sendMessage,
    deleteMessage
};


