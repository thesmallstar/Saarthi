/*
 *
 *  Dollar1400
 *  Sarthi
 *
 */

//****** imports*****//
var express = require("express");
//const mongoose = require("mongoose");

//**end imports**//

//makings
var app = express();
//

//*********Routes**********//

//Authentication and Registration
var auth = require("./routes/auth");
app.use("/auth", auth);
//

//Sarthi Listens
var port = 3000;
app.listen(port, () => console.log(`Sarthi app listening on port ${port}!`));
//
