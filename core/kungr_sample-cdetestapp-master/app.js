var express = require('express');
var request = require('request');

var app = express();

app.use(app.router);
app.use(express.bodyParser());
app.use(express.errorHandler());
app.use(express.static(__dirname + '/public')); //setup static public directory


var services = JSON.parse(process.env.VCAP_SERVICES || "{}");
var host = (process.env.VCAP_APP_HOST || 'localhost');
var port = (process.env.VCAP_APP_PORT || 3002);

var insight_host = services["twitterinsights"] ? services["twitterinsights"][0].credentials.url : "";

var MAX_TWEETS = 200;

// callback - done(err, data)
function insightRequest(path, query, done) {
    request({
        method: "GET",
        url: insight_host + '/api/v1/messages' + path,
        qs: {
            q: query,
            size: MAX_TWEETS
        }
    }, function (err, response, data) {
        if (err) {
            console.log(JSON.parse(data));
            done(err);
        } else {
            console.log(JSON.parse(data));
            if (response.statusCode == 200) {
                try {

                    done(null, JSON.parse(data));
                } catch (e) {
                    done({
                        error: {
                            description: e.message
                        },
                        status_code: response.statusCode
                    });
                }
            } else {
                done({
                    error: {
                        description: data
                    },
                    status_code: response.statusCode
                });
            }
        }
        console.log(data);
        // runFunction();

        // document.write(data);
    });
}
app.get('/api/search', function (req, res) {
    insightRequest("/search", req.param("q"), function (err, data) {

        if (err) {
            res.send(err).status(400);
        } else {

            res.json(data);

        }
    });
});

// app.get('/api/count', function (req, res) {
//     insightRequest("/count", req.param("q"), function (err, data) {
//         if (err) {
//             res.send(err).status(400);
//         } else {
//             res.json({
//                 query: req.param("q"),
//                 count: data.search.results
//             });
//
//         }
//     });
//     console.log(val);
// });

app.listen(port, host);
console.log('App started on port ' + port);
