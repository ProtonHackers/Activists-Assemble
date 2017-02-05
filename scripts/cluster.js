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

/**
 * Takes in array of tweets stored in json and returns
 */
function cluster(raw, bandwidth) {
    var data = [];

    for (var i = 0; i < raw.length; i++) {
        try {
            data.push(raw[i]["gnip"]["profileLocations"][0]["geo"]["coordinates"])
        }
    }

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

    return centroids;
}
