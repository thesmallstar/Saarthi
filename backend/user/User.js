var mongoose = require("mongoose");
var UserSchema = new mongoose.Schema({
  firstName: {
    type: String,
    default: ""
  },
  lastName: {
    type: String,
    default: ""
  },
  phone: {
    type: String,
    default: ""
  },
  password: {
    type: String,
    default: ""
  }
});
mongoose.model("User", UserSchema);

module.exports = mongoose.model("User");
