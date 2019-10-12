var express = require("express");
var router = express.Router();
var bodyParser = require("body-parser");

var VerifyToken = require(__root + "auth/VerifyToken");

router.use(bodyParser.urlencoded({ extended: true }));
router.use(bodyParser.json());
var ParkingSpot = require("./ParkingSpot");

// CREATES A NEW spot
router.post("/", VerifyToken, function(req, res) {
  console.log(req.body);
  var a = false;
  if (req.body.heavy == "true") {
    a = true;
  }
  const price = parseInt(req.body.price);

  ParkingSpot.create(
    {
      name: req.body.name,
      timing: req.body.timing,
      userId: req.body.userId,
      phone: req.body.phone,
      lat: req.body.lat,
      lang: req.body.lang,
      price: price,
      ins: req.body.ins,
      heavy: a
    },
    function(err, spot) {
      if (err)
        return res
          .status(500)
          .send("There was a problem adding the information to the database.");
      res.status(200).send("Your Parking Place has been Registered");
    }
  );
});

// RETURNS ALL THE spots IN THE DATABASE
router.get("/", function(req, res) {
  ParkingSpot.find({}, function(err, spots) {
    if (err)
      return res
        .status(500)
        .send("There was a problem finding the ParkingSpots.");
    res.status(200).send(spots);
  });
});

// GETS A SINGLE spot FROM THE DATABASE
router.get("/:id", function(req, res) {
  ParkingSpot.findById(req.params.id, function(err, spot) {
    if (err)
      return res.status(500).send("There was a problem finding the spot.");
    if (!spot) return res.status(404).send("No spot found.");
    res.status(200).send(spot);
  });
});

// DELETES A spot FROM THE DATABASE
router.delete("/:id", function(req, res) {
  ParkingSpot.findByIdAndRemove(req.params.id, function(err, spot) {
    if (err)
      return res.status(500).send("There was a problem deleting the spot.");
    res.status(200).send("Parking Spot was deleted.");
  });
});

router.put(
  "/:id",
  /* VerifyToken, */ function(req, res) {
    ParkingSpot.findByIdAndUpdate(
      req.params.id,
      req.body,
      { new: true },
      function(err, spot) {
        if (err)
          return res.status(500).send("There was a problem updating the spot.");
        res.status(200).send(spot);
      }
    );
  }
);

module.exports = router;
