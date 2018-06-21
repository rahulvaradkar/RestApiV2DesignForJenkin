//All global variables in this file.
//If any need to commit this file, 
//1. Remove it from .gitignore first.
//2. Make changes here and commit them.
//3. Once committed, ignore it again by 
//  a. running `git rm --cached src/test/rest-api/scripts/globals.js
//  b. And then adding this line to .gitignore back again "/src/test/rest-api/scripts/globals.js"

var Globals = {
  //change following line to match your test instance URL
  baseURL: "http://localhost:8080/BAE_4_3_JENKINS_TEST/",
 //baseURL:"https://bwrestapiqa.boardwalktech.com:8443/BAE_4_3_REST_API_TEST/"
 assertHow: "forNow",//"forNow" Or "clean"
  Whiteboard_Id : 1009,
  Collab_Id : 1000,
  Member_Id_1 : 1000,
  Member_Id_2 : 1005,
  //authorization : getAuthorization("user1",0,"nh1"),
  //invalidAuthorization : getAuthorization("user1",0,"ROOT|nh1"),
  authorization : btoa("user1:0:nh1"),
  invalidAuthorization : btoa("user1:0:ROOT|nh1"),

  importTid :-1
 
 
};



