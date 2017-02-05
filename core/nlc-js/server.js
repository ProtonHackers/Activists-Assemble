#! /usr/bin/env node
/**
 * Copyright 2015 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var watson = require('watson-developer-cloud');

var natural_language_classifier = watson.natural_language_classifier({
    username: '2b060e4a-c633-4347-ba2a-a18844f981e2',
    password: '5QODCTVpg5Oj',
    version: 'v1'
});

natural_language_classifier.classify({
        text: 'yayyyyyyy there are a lot of people here at the SFO protest who are all protesting the travel ban',
        classifier_id: 'f5bbbbx174-nlc-1387' },
    function(err, response) {
        if (err)
            console.log('error:', err);
        else
            console.log(JSON.stringify(response, null, 2));
    });
