

var database = firebase.database();

function displaySearch(result) {
    firebase.database().ref().set(JSON.stringify(result));
    console.log(result);
    document.write(JSON.stringify(result));
    console.log(result['tweets']);

    cluster(result['tweets'], 2);
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
function euclideanDistance(x1, y1, x2, y2) {
    return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))
}

function avg(data2d) {
    var xSum = 0;
    var ySum = 0;

    for (var i = 0; i < data2d.length; i++) {
        xSum += data2d[i][0];
        ySum += data2d[i][1];
    }
    return [xSum / data2d.length, ySum / data2d.length];
}
function unique(data2d) {
    var uniques = [];
    for (var i = 0; i < data2d.length; i++) {
        var contained = false;
        for (var j = 0; j < uniques.length; j++) {
            if (data2d[i][0] == uniques[j][0] && data2d[i][1] == uniques[j][1]) {
                contained = true;
            }
        }

        if (!contained) {
            uniques.push(data2d[i]);
        }
    }

    uniques.sort();
    return uniques;
}

function cluster(raw, bandwidth) {
    var data = [];

    for (var i = 0; i < raw.length; i++) {
        try {
            var mainTweet = raw[i];
            console.log(mainTweet);
            var listTweet = mainTweet["message"];
            console.log(listTweet);
            var gnips = listTweet["gnip"];
            console.log(gnips);
            data.push(raw[i]["message"]["gnip"]["profileLocations"][0]["geo"]["coordinates"]);
            console.log('successful');
        }
        catch (e) {
            console.log(e);
        }
    }
    console.log(data);

    var centroids = [];

    for (i = 0; i < data.length; i++) {
        centroids[i] = data[i];

    }

    centroids.sort();

    while (true) {
        var new_centroids = [];

        for (i = 0; i < centroids.length; i++) {
            var inBandwidth = [];
            var centroid = centroids[i];

            for (var j = 0; j < data.length; j++) {
                if (euclideanDistance(data[j][0], data[j][1], centroid[0], centroid[1]) < bandwidth) {
                    inBandwidth.push(data[j]);
                }
            }

            var new_centroid = avg(inBandwidth);
            new_centroids.push(new_centroid);

        }

        var uniques = unique(new_centroids);
        var prevCentroids = centroids;

        centroids = uniques;

        var optimized = true;

        for (i = 0; i < centroids.length; i++) {
            if (!centroids[i][0] == prevCentroids[i][0] || !centroids[i][1] == prevCentroids[i][1]) {
                optimized = false;
                break;
            }
        }

        if (optimized) {
            break;
        }
    }

    var tweets2d = [];

    for (i = 0; i < raw.length; i++) {
        try {
            var coord = raw[i]["message"]["gnip"]["profileLocations"][0]["geo"]["coordinates"];
            var minDistance = 1000;
            var centroidIndex = 0;
            for (j = 0; j < centroids.length; j++) {
                centroid = centroids[j];
                if(euclideanDistance(coord[0], coord[1], centroid[0], centroid[1]) < minDistance) {
                    minDistance = euclideanDistance(coord[0], coord[1], centroid[0], centroid[1]);
                    centroidIndex = j;
                    console.log(minDistance)
                }


            }

            if (tweets2d[centroidIndex] === undefined) {
                tweets2d[centroidIndex] = [raw[i]]
            } else {
                console.log(raw[i]);
                tweets2d[centroidIndex].push(raw[i]);
            }

            console.log(raw[i]);


        } catch (e) {
            console.log(e);
        }
    }

    console.log("tweets");
    console.log(tweets2d);
    console.log("centroids");
    console.log(centroids);
    return [tweets2d, centroids];
}

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
