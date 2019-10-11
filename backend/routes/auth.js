var express = require("express");
var router = express.Router();

var bodyParser = require("body-parser");
router.use(bodyParser.urlencoded({ extended: false }));
router.use(bodyParser.json());
var User = require("./models/User");

//Start routes
router.post("/login", function(req, res) {
  res.send("In will let the user login");
});

router.post("/register", function(req, res) {
  res.send("I will be used to register the user.");
});

router.post("/verify", function(req, res) {
  res.send("I will check if th euser is logged in");
});
//end routes

module.exports = router;
