const mongoose = require("mongoose");
const { Schema } = mongoose;

//Crear esquema de mensajes
const messageSchema = new Schema({
    from: { type: Schema.Types.ObjectId, ref: 'User', required: true },
    to: { type: Schema.Types.ObjectId, ref: 'User', required: true },
    message: {type: String, required: true, trim: true, minlength: 1},
    image: {type: String, required: false},
    sent: {type: Date, required: true, default: Date.now, trim: true, minlength: 1}
});

//Crear modelo de mensajes
const Message = mongoose.model('Message', messageSchema, 'messages');
module.exports = Message;