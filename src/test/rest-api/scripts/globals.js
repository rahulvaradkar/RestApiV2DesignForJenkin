//All global variables in this file.
//If any need to commit this file, 
//1. Remove it from .gitignore first.
//2. Make changes here and commit them.
//3. Once committed, ignore it again by 
//  a. running `git rm --cached src/test/rest-api/scripts/globals.js
//  b. And then adding this line to .gitignore back again "/src/test/rest-api/scripts/globals.js"
var Globals = {
  //change following line to match your test instance URL
  baseURL: "http://localhost:8081/BAE4/",
  assertHow: "forNow",//Or "clean"
   Whiteboard_Id : 1002,
  Collab_Id : 1001,
  Member_Id_1 : 1001,
  Member_Id_2 : 1013,
  authorization : getAuthorization("j",0,"JSAddin")
};