// Copyright IBM Corp. 2015  All Rights Reserved.
// IBM Insights for Twitter Demo App

// optimized for speed

var cellColors = [ "689fd5", "87b2dd", "a3c4e5", "c7ddf3", "e1edfb" ];

function getCellColor(depth) {
	return cellColors[Math.min(depth, cellColors.length - 1)];
}

function renderArray(arr, depth) {
	depth = depth || 0;
	var s = "";
	if (arr.length) {
		s += '<div class="cde_array">'

		for (var i = 0; i < arr.length; i++) {
			s += '<div class="cde_item">';
			if (typeof arr[i] == "object") {
				if (arr[i] instanceof Array) {
					s += renderArray(arr[i], depth + 1);
				} else {
					s += renderObject(arr[i], depth + 1);
				}
			} else {
				s += '<span style="padding:3px;">' + arr[i] + '</span>';
			}
			s += '</div>';
		}
		s += '</div>';
	}
	return s;
}

function renderObject(obj, depth) {
	depth = depth || 0; 
	var s = "";
	s += '<div class="cde_object">'
	+		'<table cellpadding="0" cellspacing="0px" width="100%">'
	for (var n in obj) {
		s +=	'<tr>'
		+	 		'<td  align="left" valign="top" class="cde_cell_property"'
		+					' bgcolor="#' + getCellColor(depth) + '" style="border:1px solid #cccccc;">' 
		+ 				'<span style="padding:5px; color:' + (depth == 0 ? '#FFFFFF' : '#000000') + '" style="border:1px solid silver;">'
		+					n
		+				'</span>'
		+ 			'</td>'
		+			'<td align="left" valign="top" width="100%" class="cde_cell_value" bgcolor="#' + getCellColor(depth) + '"' 
		+				(typeof obj[n] != "object" ? ' style="border:1px solid #cccccc;"' : '') + '>';

		if (n == "image") {
			s += '<img src="' + obj[n] + '"/>'
		} else if (typeof obj[n] == "object") {
			if (obj[n] instanceof Array) {
				s += renderArray(obj[n], depth + 1);
			} else {
				s += renderObject(obj[n], depth + 1);
			}
		} else {
			s += '<span style="padding:5px;">' + obj[n] + '</span>';
		}

		s += 		'</td>'
		+		'</tr>';
	}
	s +=	'</table>'
	+	'</div>';
	return s;
}

function renderTweetBody(body, evidence) {
	if (evidence && evidence.length) {
		var i, l = evidence.length;
		for (i = 0; i < l; i++) {
			body = body.split(evidence[i].sentimentTerm)
					.join('<span class="sentiment_' + evidence[i].polarity.toLowerCase() + '">' + evidence[i].sentimentTerm + '</span>')
		}
	}
	return body;
}

function renderSMATweet(tweet, id) {
	var actor = tweet.message.actor || {};
	var sentiment = "";
	var evidence = [];
	if (tweet && tweet.cde && tweet.cde.content && tweet.cde.content.sentiment) {
		if (tweet.cde.content.sentiment.polarity) {
			sentiment = tweet.cde.content.sentiment.polarity.toLowerCase();
		}
		if (tweet.cde.content.sentiment.evidence) {
			evidence = tweet.cde.content.sentiment.evidence;
		}
	}
	var s = 
		'<div class="i4twitter_item">'
	+		'<table style="width:700px; margin: 0 auto;">'
	+			'<tr>'
	+				'<td valign="top" rowspan="3">'
	+					'<img class="i4twitter_image" src="' + actor.image + '">'
	+				'</td>'
	+				'<td width="100%">'
	+					'<span class="i4twitter_name">' + actor.displayName + '</span>'
	+					'<span class="i4twitter_user">@' + actor.preferredUsername + '</span>'
	+				'</td>'
	+			'</tr>'
	+			'<tr>'
	+				'<td>'
	+					'<div style="border-bottom:1px solid silver;">'
	+						'<span class="i4twitter_sentiment i4twitter_sentiment_' + sentiment + '">'
	+							'&nbsp;'
	+						'</span>'
	+						'<span class="i4twitter_body">' 
	+ 							renderTweetBody(tweet.message.body, evidence) 
	+ 						'</span>'
	+					'</div>'
	+				'</td>'
	+			'</tr>'
	+			'<tr>'
	+				'<td>'
	+					'<span class="i4twitter_insight">IBM </span>'
	+					'<a href="javascript:showSection(\'tweet\', \'insight\', ' + id + ')">'
	+						'<span id="i4twitter_insight_link_' + id + '" class="i4twitter_insight">Insights</span>'
	+					'</a>'
	+					'<span class="i4twitter_insight"> for </span>'
	+					'<a href="javascript:showSection(\'insight\', \'tweet\', ' + id + ')">'
	+						'<span id="i4twitter_tweet_link_' + id + '" class="i4twitter_tweet">Twitter</span>'
	+					'</a>'
	+				'</td>'
	+			'</tr>'
	+		'<table>'
	+	'</div>'
	+ 	'<div id="i4twitter_insight_' + id + '" style="display:none;"></div>'
	+ 	'<div id="i4twitter_tweet_' + id + '" style="display:none;"></div>';
	return s;
}

var activeViews = {};
var activeTweets = [];

function renderSMATweets(tweets) {
	var s = "";
	activeViews = {};
	activeTweets = tweets;
	var i, l = tweets.length;
	for (i = 0; i < l; i++) {
		s += renderSMATweet(tweets[i], i);
	}
	return s;
}

function showSection(from, to, id) {
	var efrom = $('#i4twitter_' + from + '_' + id);
	var efromlink = $('#i4twitter_' + from + '_link_' + id);
	var eto = $('#i4twitter_' + to + '_' + id);
	var etolink = $('#i4twitter_' + to + '_link_' + id);
	if (eto.is(":visible")) {
		eto.hide("slow");
		etolink.css("font-size", "10px");
	} else {
		efrom.hide("slow");
		efromlink.css("font-size", "10px");
		if (!activeViews[to + id]) {
			eto.html(renderObject(to == "insight" ? { cde: activeTweets[id].cde } : { message: activeTweets[id].message }));
			activeViews[to + id] = true;
		}
		eto.show("slow");
		etolink.css("font-size", "14px");
	}
}

function searchEnter() {
	if (searchText().trim() != "") {
		document.getElementById('search_button').click();
	}
}

// 332x270  166x135
function spinnerStart() {
	$("#display_spinner").html('<img class="spinner" width="166px" height="135px" src="images/twitter_flapping.gif"/>');
}

function spinnerStop() {
	$("#display_spinner").html('');
}

function searchText() {
	return $("#search_text").val();
}

function searchReset() {
	$("#display_query").text("");
	$("#display_count").text("");
	$("#display_markup").text("");
}

function displaySearch(result) {
	if (result.error) {
		$("#display_query").text("Error: " + result.status_code);
		$("#display_markup").text(result.error.description);
	} else if (result.search && result.search.results) {
		$("#display_query").text(searchText());
		$("#display_count").text(result.search.results);
		$("#display_markup").html(renderSMATweets(result.tweets));
	} else {
		$("#display_query").text("No results");
	}
}

function displayCount(query, count) {
	$("#display_query").text(query);
	$("#display_count").text(count);
	$("#display_markup").text("");
}

function showError(msg) {
	$("#display_query").text(msg);
	$("#display_count").text("");
}

function countTweets(term) {
	if (term != "") {
		searchReset();
		spinnerStart();
	   	$.ajax({
			url: "/api/count",
			type: 'GET',
			contentType:'application/json',
			data: {
				q: term
			},
	  		success: function(data) {
	  			spinnerStop();
				displayCount(data.query, data.count);
			},
			error: function(xhr, textStatus, thrownError) {
	  			spinnerStop();
				showError("Error: " + textStatus);
			}
		});
	}
}

function searchTweets(term) {
	if (term != "") {
		searchReset();
		spinnerStart();
	   	$.ajax({
			url: "/api/search",
			type: 'GET',
			contentType:'application/json',
			data: {
				q: term
			},
	  		success: function(data) {
	  			spinnerStop();
				displaySearch(data);
			},
			error: function(xhr, textStatus, thrownError) {
	  			spinnerStop();
				showError("Error: " + textStatus);
			}
		});
	}
}
