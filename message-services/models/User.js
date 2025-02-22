const mongoose = require('mongoose');
const { Schema } = mongoose;
//crear esquema de usuario
const userSchema = new Schema({
    name: { type:String, required:true, trim:true, minlength:1, unique:true, match: /^[a-zA-Z0-9]+$/ },
    password: { type: String, required: true, minlength: 4 },
    image:{ type: String, required: true }
});

//crear modelo de usuario
const User = mongoose.model('User', userSchema, 'users');
module.exports = User;