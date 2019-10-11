var express = require("express");
var router = express.Router();

//Start routes
router.post("/login", function(req, res) {
  res.send("In will let the user login");
});

router.post("/register", function(req, res) {
  res.send("I will register the User");
});

router.post("/verify", function(req, res) {
  res.send("I will check if th euser is logged in");
});
//end routes

module.exports = router;
