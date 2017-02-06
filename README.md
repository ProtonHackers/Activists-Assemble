# Activists Assemble
Activists Assemble locates a protest, detects whether it is safe, sends you directions to its location, and has a live Twitter update of the event.
## How we built it
We used the IBM Bluemix API as well as the Twitter-Insight API to get information from tweets including the hashtags, geolocation, and whether the tweet was positive, negative, or contained certain keywords which we flagged as dangerous or safe (we used this information to detect whether the protest was safe or violent). We then created a clustering algorithm to group tweets based on location, and found where the user was located, and found the nearest protest near him or her. This data is stored in Firebase. We sent this information to the app, where the user can see all this information in a friendly user interface.
## Problems
Learning how to use Bluemix API and getting the tweet metadata, getting the clustering algorithm to work, and tying the clustering algorithm and the Bluemix API together.
## Awards
Our Project won Best Android Application, and Best Use of IBM Bluemix API
## Future
<ul>
<li>Launch the app on Google Play and IOS Play store<li>
<li>Improve on keyword recognition by adding more data to the Bluemix API<li>

</ul>
