# Activists Assemble
### Submitted to Los Altos Hacks II
Created by Rohan Koodli, Vineet Mudupalli, Vikranth Srivatsa, and Aubhro Sengupta

Winner: Best Android App

Winner: Best Use of IBM Bluemix APIs

Check out the Devpost link [here](https://devpost.com/software/activists-assemble).

## Inspiration
We were inspired by the recent political protests across America. We wanted to make an app that could help the user easily locate a protest near them.

## What it does
Activists Assemble locates a protest, detects whether it is safe, sends you directions to its location, and has a live Twitter update of the event.

## How we built it
We used the IBM Bluemix API as well as the Twitter-Insight API to get information from tweets including the hashtags, geolocation, and whether the tweet was positive, negative, or contained certain keywords which we flagged as dangerous or safe (we used this information to detect whether the protest was safe or violent). We then created a clustering algorithm to group tweets based on location, and found where the user was located, and found the nearest protest near him or her. This data is stored in Firebase. We sent this information to the app, where the user can see all this information in a friendly user interface.

## Challenges we ran into
Learning how to use Bluemix API and getting the tweet metadata, getting the clustering algorithm to work, and tying the clustering algorithm and the Bluemix API together.

## Accomplishments that we're proud of
Getting Bluemix to work, and learning JavaScript (we didn't really know it very well).

## What we learned
How to use the IBM Bluemix API for various machine learning tasks. 

## What's next for Activists Assemble
Launch the app on the Google Play/iOS App Stores. Improve on keyword recognition by adding more data to the Bluemix API.
