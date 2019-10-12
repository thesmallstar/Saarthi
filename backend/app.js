var express = require("express");
var app = express();
var db = require("./db");
global.__root = __dirname + "/";

app.get("/api", function(req, res) {
  res.status(200).send("API works.");
});

var UserController = require(__root + "user/UserController");
app.use("/api/users", UserController);

var AuthController = require(__root + "auth/AuthController");
app.use("/api/auth", AuthController);

var PathController = require(__root + "path/PathController");
app.use("/api/path", PathController);

var ParkingController = require(__root + "parking/ParkingController");
app.use("/api/parking", ParkingController);
module.exports = app;
