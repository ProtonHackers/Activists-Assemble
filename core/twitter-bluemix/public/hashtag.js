
function makeEmptyArray(d) {
    var arr = [];
    for(var i = 0; i < d; i++) {
        arr.push(0);
    }
    return arr;
}

function max(hashtags2D) {
    var keys = [];
    for (var i = 0; i < hashtags2D.length; i++)
    {
        var maxKey = "";
        var numKey = 0;
        for (var key in hashtags2D[i]) {
            if (hashtags2D[i].hasOwnProperty(key)) {
                if (hashtags2D[i][key] >= numKey)
                {
                    maxKey = key;
                    numKey = hashtags2D[i][key];
                }
            }
        }

        keys.push(maxKey);

    }

    return keys;
}

function commonHashtags(tweets2D) {
    var hashtags = [];
    for (var i = 0; i < tweets2D.length; i++)
    {
        var eventHashtags = [];
        for (var j = 0; j < tweets2D[i].length; j++)
        {
            var hashtagDicts = tweets2D[i][j]["twitter_entities"]["hashtags"];
            for (var k = 0; k < hashtagDicts.length; k++)
            {
                if (eventHashtags[hashtagDicts[k]["text"]] === undefined) {
                    eventHashtags[hashtagDicts[k]["text"]] = 1;
                } else {
                    eventHashtags[hashtagDicts[k]["text"]] += 1;
                }
            }

        }

        hashtags.push(eventHashtags);
    }

    return max(hashtags);

}