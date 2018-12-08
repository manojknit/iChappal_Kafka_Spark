var Alexa = require('alexa-sdk');
const aws = require('aws-sdk');
const dynamo = new aws.DynamoDB.DocumentClient();
const tableName = process.env.TABLE_NAME;
const https = require('https');
var http = require("http");

exports.handler = function(event, context, callback) {
    var alexa = Alexa.handler(event, context);


    alexa.registerHandlers(handlers);
    alexa.execute();
};

var handlers = {
    'LaunchRequest': function () {
        this.emit('GetNewFactIntent');
    },

    /*
    'GetNewFactIntent': function () {
        var say = 'Hello Manoj! Lets begin!' + getFact();
        this.emit(':tell', say );
    },
    */
    'GetNewFactIntent': function () {
     getSubmittedQuotes(this);
        //var say = 'Hello Manoj! ' + getSubmittedQuotes(this);
        //this.emit(':tell', say );
    },
    
    'GetSensorData': function () {
     getSensorRelatedData(this);
    },
    'GetStarbucksData': function () {
     getStarbucksAppData(this);
    },
    'GetiChappalData': function () {
     getiChappalRelatedData(this);
    },
    'AMAZON.HelpIntent': function () {
         this.emit(':ask', 'Learn everything you need to know about Amazon Web Services to pass your exam by listening to your very own study notes. You can start by saying, Kumar help me study.', 'try again');
     },

     'AMAZON.CancelIntent': function () {
         this.emit(':tell', 'Goodbye Kumar');
     },

     'AMAZON.StopIntent': function () {
         this.emit(':tell', 'Goodbye Kumar');
     }
};

//  helper functions  ===================================================================
function isEmptyObject(obj) {
  return !Object.keys(obj).length;
}

function getFact() {
    var myFacts = [
    '<audio src=\"https://s3.amazonaws.com/noterepository-mp3/110baec9-a61c-4fd8-aa06-49fd76e88eaf.mp3" />\'',
    '<audio src=\"https://s3.amazonaws.com/noterepository-mp3/83764c46-39ba-4c3b-8493-1e98e4037f84.mp3" />\'',
    '<audio src=\"https://s3.amazonaws.com/noterepository-mp3/68b4bb24-5610-4b90-b6f9-36a67babc363.mp3" />\''
        ]

    var newFact = randomPhrase(myFacts);

    return newFact;
}

function randomPhrase(array) {
    // the argument is an array [] of words or phrases
    var i = 0;
    i = Math.floor(Math.random() * array.length);
    return(array[i]);
}



function getiChappalRelatedData(thisval){

 var url = 'https://firestore.googleapis.com/v1beta1/projects/my-project-415-341/databases/(default)/documents/kf-step-detail?orderBy=dateon%20Desc';
 https.get(url, (res) => {
  console.log('statusCode:', res.statusCode);
  console.log('headers:', res.headers);
  var body = '';
    res.on('data', (d) => {
      body += d;
    });
      res.on('end', function() {
          
         // console.log('len='+length);
          var parsed = JSON.parse(body);
          var length = parsed.documents.length;
          length=1;
          console.log('len='+length);
          /*
          for (var key in parsed.documents) {
            if (parsed.documents.hasOwnProperty(key)) {
              console.log(key + ": " + parsed.documents[key]);
            }
          }*/
          var steps = parsed.documents[length-1]['fields']['steps'].stringValue;
          var dateon = parsed.documents[length-1]['fields']['dateon'].timestampValue;
          //var temperature = parsed.documents[length-1]['fields']['temperature']['integerValue'];
          //var sensorname = JSON.stringify(parsed.documents[length-1]['fields']['sensorname']['stringValue']);
          let stepsval = parseInt(steps+'');
          //let moistval = parseInt(moisture+'');
          console.log("Get if="+stepsval+"condi="+(stepsval > 80)+'.');
          var advice = ' ';
          if(stepsval < 6000)
          {
            advice = ' I would advice you to walk '+ (6000-stepsval) +'more steps to remain healthy.';
          }
          else
           advice = 'Well Done. You have achieved your goal for today.';
          //if(tempval > 6000)
          //{
          //  advice = advice+' Temperature is hot here. I would advice you to water the plant to cope with temperature.';
          //}
          var say = 'Here is your health summary. As of ' + new Date(dateon).toDateString() + '. your total steps count is '+ steps +' .' + advice +' Thank You.';
          thisval.emit(':tell', say );
          //console.log("data= "+JSON.stringify(body));
      });
  }).on('error', (e) => {
    //console.error(e);
    thisval.emit(':tell', 'vaseuno, Error in getting humidity' );
  });
        
}

