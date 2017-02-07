var watson = require('watson-developer-cloud');

var natural_language_classifier = watson.natural_language_classifier({
    username: '2b060e4a-c633-4347-ba2a-a18844f981e2',
    password: '5QODCTVpg5Oj',
    version: 'v1'
});

natural_language_classifier.classify({
        text: ' danger  asdfsadfasdfasdfsadfasdf',
        classifier_id: 'f5bbbbx174-nlc-1387'
},
    function(err, response) {
        if (err)
            console.log('error:', err);
        else
            console.log(JSON.stringify(response, null, 2));
});
