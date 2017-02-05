var assert = require("assert");
var websitetitle = require("../websitetitle");
describe('websiteTitle', function() {
    describe('#getTitle', function() {
      it('should return LLL if title is not explicitly set', function() {
         assert.equal("Lauren's Lovely Landscapes", websitetitle.getTitle());
      });
    });
    describe('#setTitle', function() {
      it('should change title if explicitly set to other value', function() {
         websitetitle.setTitle("Joe's Ugly Renos")
         assert.equal("Joe's Ugly Renos", websitetitle.getTitle());
      });
    });


});