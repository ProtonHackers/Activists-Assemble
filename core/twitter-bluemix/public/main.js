function displaySearch(result) {
    console.log(result);
    document.write(JSON.stringify(result));
    cluster(result['tweets'],1000);
}

function analyzeTweet(tweetdata) {
    var watson = require('watson-developer-cloud');

    var natural_language_classifier = watson.natural_language_classifier({
        username: '2b060e4a-c633-4347-ba2a-a18844f981e2',
        password: '5QODCTVpg5Oj',
        version: 'v1'
    });

    natural_language_classifier.classify({
            text: tweetdata,
            classifier_id: 'f5bbbbx174-nlc-1387' },
        function(err, response) {
            if (err)
                console.log('error:', err);
            else
                return console.log(JSON.stringify(response, null, 2));
        });
}

function getParameters(name) {
    if (name = (new RegExp('[?&]' + encodeURIComponent(name) + '=([^&]*)'))
            .exec(location.search))
        return decodeURIComponent(name[1]);
}

window.onload = function () {
    var val = getParameters('test');
    console.log(val);
    searchTweets(val);
};


function searchTweets(term) {
    if (term != "") {
        $.ajax({
            url: "/api/search",
            type: 'GET',
            contentType: 'application/json',
            data: {
                q: term
            },
            success: function (data) {
                displaySearch(data);
            },
            error: function (xhr, textStatus, thrownError) {
               console.log("error in tweets")
            }
        });
    }
}
