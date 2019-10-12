var express = require("express");
var router = express.Router();
var bodyParser = require("body-parser");
const geolib = require('geolib');
var VerifyToken = require(__root + "auth/VerifyToken");
var utm =  require('utm');
var math = require('mathjs');
var kmeans = require('node-kmeans');
var mongoose = require("mongoose");
var nos = 5;

//Globals
var srcu,theta;
var Loc = mongoose.model('Loc' , new mongoose.Schema({ lat: Number , lng: Number}, 
  { collection : 'general' })); 

router.use(bodyParser.urlencoded({ extended: true }));

router.use(bodyParser.json());

const gmap = require('@google/maps').createClient({
  key : 'AIzaSyAurou_pOhlh_dHlPtnKG1f8qIwZBRhXr4'
})


var sortByProperty = function (property) {
  return function (x, y) {
      return ((x[property] === y[property]) ? 0 : ((x[property] > y[property]) ? 1 : -1));
  };
};

function getPlaces(query,callback){
  response = [];
  gmap.placesNearby(query,function(err,res){
    if(!err){
      response[0] = res.json;
      if("next_page_token" in response[0]){
        gmap.placesNearby({pagetoken: response[0]["next_page_token"]} ,function(err,res){
          if(!err){
            response[1] = res.json;
            if("next_page_token" in response[1]){
              gmap.placesNearby({pagetoken:response[1]["next_page_token"]},function(err,res){
                if(!err){
                  response[2] = res.json;
                  callback(response);
                }
                else console.log(err); 
              });
            }
            else callback(response);
          }
          else console.log(err);
        });
      }
      else callback(response);
    }
    else console.log(err);
  });
}

function trans(old){
  //console.log(old);
  var p = utm.fromLatLon(old["lat"],old["lng"]);
  //console.log(p);
   return {
    easting: (p["easting"]-srcu["easting"])*math.cos(theta) + (p["northing"]-srcu["northing"])*math.sin(theta),
    northing: (p["northing"]-srcu["northing"])*math.cos(theta) - (p["easting"]-srcu["easting"])*math.sin(theta),
    zoneNum: p["zoneNum"],
    zoneLetter: p["zoneLetter"]
  }
}

function safe_gen(src, dst,callback)
{
  gmap.geocode({address: src}, function(err,res){
    if(!err){
      srcl = res.json.results[0].geometry.location;
      gmap.geocode({address: dst}, function(err,res){
        if(!err){
          dstl = res.json.results[0].geometry.location;
          rad = geolib.getDistance(srcl,dstl);
          console.log(rad);
          getPlaces({location: srcl ,radius: rad},function(res){
              nears = res;
              srcu = utm.fromLatLon(srcl["lat"],srcl["lng"]);
              dstu = utm.fromLatLon(dstl["lat"],dstl["lng"]);
              if(dstu["easting"]-srcu["easting"]!=0)
                theta = math.atan((dstu["northing"]-srcu["northing"])/(dstu["easting"]-srcu["easting"])) + math.pi/2;
              else theta = math.pi;
              Loc.find({},function(err,res){
                gen = res;
                if(!err){
                  pts = [];
                  var i = 0;
                  for(let near of nears){
                    for(let pt of near["results"]){
                      if(trans(pt["geometry"]["location"])["northing"]>0){
                        pts[i] = pt["geometry"]["location"];
                        i=i+1;
                      }
                    }
                  }
                  ptsval = [];
                  k = 0;
                  for(let i of pts){
                    i['attr'] = 0;
                    for(let j of gen){
                      z = geolib.getDistance(i,{lat:j["lat"] , lng:j["lng"]});
                      if(z!=0) i['attr'] = i['attr'] + 1/(z*z);
                      else i['attr'] = i['attr'] + 10;
                    }
                    ptsval[k] = i;
                    k = k+1;
                  }
                  ptsval.sort(sortByProperty('attr'));
                  var vector = [];
                  var ptsvalu = [];
                  for(let i in ptsval) {
                    ptsvalu[i]=utm.fromLatLon(ptsval[i]["lat"],ptsval[i]["lng"]);
                    vector[i] = [ptsvalu[i]['easting'], ptsvalu[i]['northing']];
                  }
                  kmeans.clusterize(vector, {k: (ptsval.length-ptsval.length%10)/10},function(err,res){
                    if(!err){
                      for(let i of res){
                        i['attr'] = 0;
                        for(let j of i.clusterInd){
                          i['attr'] = i['attr'] + ptsval[j]['attr'];
                        }
                        i['attr'] = i['attr']/i.clusterInd.length;
                      }
                      wp = res;
                      wp.sort(sortByProperty('attr'));
                      wpf = [];
                      j = 0;
                      for(let i of wp[0].clusterInd){
                          wpf[j] = pts[i];
                          j++;
                      }
                      gmap.directions({origin:srcl, destination:dstl, waypoints:wpf}, function(err,res){
                        callback(res);
                      });
                    }
                    else console.log(err);
                  });
                }
                else console.log(err);
              });
            });
          
        }
        else console.log(err);
      });
    }
    else console.log(err);
    
  });
}

router.post("/", function(req, res) {
  console.log(req.body);
  switch(parseInt(req.body.mod)){
    case 1:
        safe_gen(req.body.src,req.body.dst,function(response){
          var s = response.requestUrl;
          var st = "https://www.google.com/maps/dir/?api=1&"  + s.slice(s.indexOf("?")+1,s.indexOf("key")-2);
          res.status(200).send(st);
        });
  }
    
});


module.exports = router;
