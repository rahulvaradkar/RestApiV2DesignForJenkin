"use strict";


		


var base64 = require('base64');
var utf8 = require('utf8');

var text = 'foo © bar 𝌆 baz';
//var bytes = utf8.encode(text);
var encoded = base64.encode(text);
console.log(encoded);
// → 'Zm9vIMKpIGJhciDwnYyGIGJheg=='

//"use strict";
console.log('Hello, world!');



phantom.exit();