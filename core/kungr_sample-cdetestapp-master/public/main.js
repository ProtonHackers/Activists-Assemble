function displaySearch(result) {
    console.log(result);
    document.write(JSON.stringify(result));
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
