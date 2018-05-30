/*
 * Author - Pavan Divekar
 * Function - encodeString
 * Description - converts normal string into base64 encoded format
 * param - 
 *  stringToEncode - the string that needs to be encoded
 * Created on - 30-05-2018
 * Edited on -
*/
var encodeString = function(stringToEncode){
    return btoa(stringToEncode);
}
/*
 * Author - Pavan Divekar
 * Function - decodeString
 * Description - converts base64 encoded string into normal string
 * param - 
 *  stringToDecode - the encoded string that needs to be decoded
 * Created on - 30-05-2018
 * Edited on -
*/
var decodeString = function(stringToDecode){
    return atob(stringToDecode);
}
/*
 * Author - Pavan Divekar
 * Function - getAuthorization
 * Description - builds string with authorization parameters
 * param - 
 *  userName, password, nhPath
 * Created on - 30-05-2018
 * Edited on -
*/
var getAuthorization = function(userName, password, nhPath){
    //
    var authString = userName+":"+password+":"+nhPath;
    return encodeString(authString);
}