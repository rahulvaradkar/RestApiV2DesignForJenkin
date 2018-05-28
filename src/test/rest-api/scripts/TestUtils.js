//Undecided asserts
function undecidedAssertEqual(assert, value, forNow, neat, message) {
  if(Globals.assertHow == "forNow") {
    assert.equal(value, forNow, message);
  } else {
    assert.equal(value, neat, message);
  }
}

//check whether number is > n
function isValidNumber(number, n) {
  if(!n) {
    n = 0;
  }
  return $.isNumeric(number) && number > n;
}

//Check whether there are 1 or more elements
function hasOneOrMoreElements(object) {
  return object.length > 0;
}

//Check whether object is null
function isNullObject(object) {
  return object === null;
}

//Check whether object has null attributes
function hasNullAttributes(object, props) {
  var hasNullAttributes = false;
  for(var i = 0; i < props.length; i++) {
    if(object[props[i]] == null) {
      hasNullAttributes = true;
    }
  }
  return hasNullAttributes;
}

//Check whether object has valid properties
function hasValidProperties(object, props) {
  var hasValidProps = true;
  for(var i = 0; i < props.length; i++) {
    if(!object.hasOwnProperty(props[i])) {
      hasValidProps = false;
    }
  }
  return hasValidProps;
}