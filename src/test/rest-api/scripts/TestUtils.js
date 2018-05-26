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