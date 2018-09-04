/**
 * Author : Jaydeep Bobade Tekvision
 * Module : Creating Database
 */
var userIds = [];
var MembershipData = [];
var NeighborhoodData = [];
var CollaborationData = [];
var WhiteBoardData = [];
var Create_Data=(function(){
    var usersData;
    var authorization = getAuthorization("admin", 0, "ROOT");
    var authorization_1 = getAuthorization("restuser1", 0, "demo");
    var authorization_2 = getAuthorization("restuser1", 0, "demo2");
    var authorization_3 = getAuthorization("restuser1", 0, "Access_Control");
    //Users
    var user1, user2, user3, user4, user5;

    //Neighborhoods
    var Parent_Nh1, Parent_Nh2, Parent_Nh3, Child_PNh3_1, Child_CPNh3_1, Child_CPNh3_2;
    var Parent_Nh1_Id, Parent_Nh2_Id, Parent_Nh3_Id, Child_PNh3_1_Id, Child_CPNh3_1_Id, Child_CPNh3_2_Id;

    //Members
    var membership_Nh1, membership_Nh2_1, membership_Nh2_2, membership_Nh3, membership_Nh4, membership_Nh5, membership_Nh6;
    var membership_Nh1_Id, membership_Nh2_1_Id, membership_Nh2_2_Id, membership_Nh3_Id,membership_Nh4_Id, membership_Nh5_Id, membership_Nh6_Id;

    //Collaborations
    var Nh1_Collab, Nh2_Collab, Nh3_Collab;
    var Nh1_Collab_Id, Nh2_Collab_Id, Nh3_Collab_Id;

    //Whiteboards
    var Nh1_Collab_WB, Nh2_Collab_WB, Nh3_Collab_WB;
    var Nh1_Collab_WB_Id, Nh2_Collab_WB_Id, Nh3_Collab_WB_Id;

    //Relations
    var relation_1, relation_2, relation_3;

    function load_js(){
        debugger;
        var head= document.getElementsByTagName('head')[0];
        var script= document.createElement('script');
        script.src= 'global.js';
        head.appendChild(script);
    }

    function createDatabase()
    {
        var res = new $.Deferred();
        createUsers().then(function(){
            createNeighborhoods().then(function(){
                createMembers().then(function(){
                    createCollaborations().then(function(){
                        createWhiteboards().then(function(){
                            createRelations().then(function(){
                                res.resolve();
                            });                        
                        });
                    });
                });
            });
        });
        return res.promise();
    }

    /**
     * Creating Users
     */
    function createUsers()
    {
        var Response = new $.Deferred();

        user1 = {"firstName": "restuser1","lastName": "restuser1","password": "0","email": "restuser1","externalId": "restuser1","id": "0"};
        user2 = {"firstName": "restuser2","lastName": "restuser2","password": "0","email": "restuser2","externalId": "restuser2","id": "0"};
        user3 = {"firstName": "restuser3","lastName": "restuser3","password": "0","email": "restuser3","externalId": "restuser3","id": "0"};
        user4 = {"firstName": "restuser4","lastName": "restuser4","password": "0","email": "restuser4","externalId": "restuser4","id": "0"};
        user5 = {"firstName": "restuser5","lastName": "restuser5","password": "0","email": "restuser5","externalId": "restuser5","id": "0"};
        //var userIds = [];

        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/user", user1, authorization, "POST").then(function(result){
            userIds.push(result[0]);
            
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/user", user2, authorization, "POST").then(function(result){
                userIds.push(result[0]);
                TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/user", user3, authorization, "POST").then(function(result){
                    userIds.push(result[0]);
                    TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/user", user4, authorization, "POST").then(function(result){
                        userIds.push(result[0]);
                        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/user", user5, authorization, "POST").then(function(result){
                            userIds.push(result[0]);
                            Response.resolve();
                        });
                    });
                });
            });
        });

        return Response.promise();
    }
 
    /**
     * Creating Neighborhoods
     * demo
     * demo2
     * Access_Control
     *      Rows
     *          R_Add_Delete
     *          R_Add_Delete_1
     * */
    
    function createNeighborhoods()
    {
        var Response = new $.Deferred();

        Parent_Nh1 = {"level": "-1","name": "demo","id": "0","parentId": "-1","secure": "false"};
        Parent_Nh2 = {"level": "-1","name": "demo2","id": "0","parentId": "-1","secure": "false"};
        Parent_Nh3 = {"level": "-1","name": "Access_Control","id": "0","parentId": "-1","secure": "false"};

        //Creating demo Neighborhood
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood", Parent_Nh1, authorization, "POST").then(function (result) {
            Parent_Nh1_Id = result[0].id;
            NeighborhoodData.push(result[0]);
             //Creating demo2 Neighborhood
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood", Parent_Nh2, authorization, "POST").then(function (result) {
                Parent_Nh2_Id = result[0].id;
                NeighborhoodData.push(result[0]);
                //Creating Access_Control Neighborhood
                TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood", Parent_Nh3, authorization, "POST").then(function (result) {
                    Parent_Nh3_Id = result[0].id;
                    NeighborhoodData.push(result[0]);
                    Child_PNh3_1 = {"level": "2","name": "Rows","id": "0","parentId": Parent_Nh3_Id,"secure": "true"};
                    //Creating Rows Neighborhood under Access_Control Neighborhood
                    TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood", Child_PNh3_1, authorization, "POST").then(function (result) {
                        Child_PNh3_1_Id = result[0].id;
                        NeighborhoodData.push(result[0]);
                        Child_CPNh3_1 = {"level": "3","name": "Rows_Add_Delete","id": "0","parentId": Parent_Nh3_Id,"secure": "true"};
                        Child_CPNh3_2 = {"level": "3","name": "Rows_Add_Delete_1","id": "0","parentId": Parent_Nh3_Id,"secure": "true"};

                        //Creating Rows_Add_Delete Neighborhood under Rows Neighborhood
                        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood", Child_CPNh3_1, authorization, "POST").then(function (result) {
                            Child_CPNh3_1_Id = result[0].id; 
                            NeighborhoodData.push(result[0]);
                            //Creating Rows_Add_Delete_1 Neighborhood under Rows Neighborhood
                            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood", Child_CPNh3_2, authorization, "POST").then(function (result) {
                                Child_CPNh3_2_Id = result[0].id;
                                NeighborhoodData.push(result[0]);
                                Response.resolve();
                            });
                        });
                    });
                });
            });
        });
        return Response.promise();
    }

    /**
     * Creating Members
     * Neighborhoods                       Members
     * demo                             - restuser1.
     * demo2                            - restuser1, restuser2.
     * Access_Control                   - restuser1.
     *      Rows                        - restuser3.
     *          R_Add_Delete            - restuser4.
     *          R_Add_Delete_1          - restuser5.
     */
    function createMembers()
    {

        var Response = new $.Deferred();
        membership_Nh1 = { "userId" : userIds[0].id, "nhid": Parent_Nh1_Id, "id": 0};
        membership_Nh2_1 = { "userId" : userIds[0].id, "nhid": Parent_Nh2_Id, "id": 0};
        membership_Nh2_2 = { "userId" : userIds[1].id, "nhid": Parent_Nh2_Id, "id": 0};
        membership_Nh3 = { "userId" : userIds[0].id, "nhid": Parent_Nh3_Id, "id": 0};
        membership_Nh4 = { "userId" : userIds[2].id, "nhid": Child_PNh3_1_Id, "id": 0};
        membership_Nh5 = { "userId" : userIds[3].id, "nhid": Child_CPNh3_1_Id, "id": 0};
        membership_Nh6 = { "userId" : userIds[4].id, "nhid": Child_CPNh3_2_Id, "id": 0};
        //Creating Membership for restuser1 in demo neighborhood
        var M1 = TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + Parent_Nh1_Id + "/member", membership_Nh1, authorization, "POST").then(function (result) {
            membership_Nh1_Id = result[0].id;
            MembershipData.push(result[0]);
            //Creating Membership for restuser1 in demo2 neighborhood
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + Parent_Nh2_Id + "/member", membership_Nh2_1, authorization, "POST").then(function (result) {
                membership_Nh2_1_Id = result[0].id;
                MembershipData.push(result[0]);
                 //Creating Membership for restuser2 in demo2 neighborhood
                TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + Parent_Nh2_Id + "/member", membership_Nh2_2, authorization, "POST").then(function (result) {
                    membership_Nh2_2_Id = result[0].id;
                    MembershipData.push(result[0]);
                    //Creating Membership for restuser1 in Access_Control neighborhood
                    TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + Parent_Nh3_Id + "/member", membership_Nh3, authorization, "POST").then(function (result) {
                        membership_Nh3_Id = result[0].id;
                        MembershipData.push(result[0]);
                        //Creating Membership for restuser3 in Rows neighborhood
                        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + Child_PNh3_1_Id + "/member", membership_Nh4, authorization, "POST").then(function (result) {
                            membership_Nh4_Id = result[0].id;
                            MembershipData.push(result[0]);
                             //Creating Membership for restuser4 in Rows_Add_Delete neighborhood
                            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + Child_CPNh3_1_Id + "/member", membership_Nh5, authorization, "POST").then(function (result) {
                                membership_Nh5_Id = result[0].id;
                                MembershipData.push(result[0]);
                                 //Creating Membership for restuser5 in Rows_Add_Delete_1 neighborhood
                                TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + Child_CPNh3_2_Id + "/member", membership_Nh6, authorization, "POST").then(function (result) {
                                    membership_Nh6_Id = result[0].id;
                                    MembershipData.push(result[0]);
                                    Response.resolve();
                                });
                            });
                        });
                    });
                });
            });
        });
        return Response.promise();
    }

    /**
     * Creating Collaboration as follows :
     * demo Collaboration in demo Neighborhood
     * demo2 Collaboration in demo2 Neighborhood
     * Access_Cuboids Collaboration in Access_Control Neighborhood
     */
    function createCollaborations()
    {
        var Response = new $.Deferred();
        var data_;
        Nh1_Collab = {"name": "demo","purpose": "Test"};
        Nh2_Collab = {"name": "demo2","purpose": "Test"};
        Nh3_Collab = {"name": "Access_Cuboids","purpose": "Test"};

        //Creating demo collaboration in demo Neighborhood
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + Parent_Nh1_Id + "/member/" + membership_Nh1_Id + "/collaboration", Nh1_Collab, authorization_1, "POST").then(function (result) {
            Nh1_Collab_Id = result;
            data_ = {name: "demo",id : result};
            CollaborationData.push(data_);

            //Creating demo2 collaboration in demo2 Neighborhood
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + Parent_Nh2_Id + "/member/" + membership_Nh2_1_Id + "/collaboration", Nh2_Collab, authorization_2, "POST").then(function (result) {
                Nh2_Collab_Id = result;
                data_ = {name: "demo2",id : result};
                CollaborationData.push(data_);

                //Creating Access_Cuboids collaboration in Access_Control Neighborhood
                TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + Parent_Nh3_Id + "/member/" + membership_Nh3_Id + "/collaboration", Nh3_Collab, authorization_3, "POST").then(function (result) {
                    Nh3_Collab_Id = result;
                    data_ = {name: "Access_Cuboids",id : result};
                    CollaborationData.push(data_);
                    Response.resolve();
                });
            });
        });
        return Response.promise();

    }


    /**
     * Creating Whiteboard as follows :
     * demo WhiteBoard in demo Collaboration in demo Neighborhood
     * demo2 WhiteBoard in demo2 Collaboration in demo2 Neighborhood
     * Access_Tables in Access_Cuboids Collaboration in Access_Control Neioghborhood
     */
    function createWhiteboards()
    {
        var Response = new $.Deferred();
        var data_;
        Nh1_Collab_WB = { "name" : "demo"};
        Nh2_Collab_WB = { "name" : "demo2"};
        Nh3_Collab_WB = { "name" : "Access_Tables"};

        var WB1 = TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + Nh1_Collab_Id+ "/whiteboard", Nh1_Collab_WB,authorization_1, "POST").then(function (result) {
            Nh1_Collab_WB_Id = result;
            data_={name : "demo", id : result}
            WhiteBoardData.push(data_);

            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + Nh2_Collab_Id+ "/whiteboard", Nh2_Collab_WB, authorization_2, "POST").then(function (result) {
                Nh2_Collab_WB_Id = result;
                data_={name : "demo2", id : result}
                WhiteBoardData.push(data_);

                TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/collaboration/" + Nh3_Collab_Id+ "/whiteboard", Nh3_Collab_WB, authorization_3, "POST").then(function (result) {
                    Nh3_Collab_WB_Id = result;
                    data_={name : "Access_Tables", id : result}
                    WhiteBoardData.push(data_);

                    Response.resolve();
                });
            });
        });
        return Response.promise();
    }

    /**
     * Creating Relations
     * Access_Control NH and Rows NH
     * Access_Control NH and Rows_Add_Delete NH
     * Access_Control NH and Rows_Add_Delete_1 NH
     */
    function createRelations()
    {
        var Response = new $.Deferred();
        var root = 1;
        relation_1 = {"name": "RowAccess", "relatedNeighborhoodId": [{ "id": Parent_Nh3_Id }, { "id": Child_PNh3_1_Id }]};
        relation_2 = {"name": "RowAccess_2", "relatedNeighborhoodId": [{ "id": Parent_Nh3_Id }, { "id": Child_CPNh3_1_Id }]};
        relation_3 = {"name": "RowAccess_3", "relatedNeighborhoodId": [{ "id": Parent_Nh3_Id }, { "id": Child_CPNh3_2_Id }]};        
        TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + root + "/relation", relation_1, authorization_1, "POST").then(function (result) {
            TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + root + "/relation", relation_2, authorization_1, "POST").then(function (result) {
                TestUtils.sendRequest(GlobalData.Globals.baseURL + "rest/v1/neighborhood/" + root + "/relation", relation_3, authorization_1, "POST").then(function (result) {
                    Response.resolve();
                });
            });
        });

        return Response.promise();
    }

    var getUserIdByName = function (username){
        for(var i = 0; i < userIds.length; i++)
        {
            if(userIds[i].email == username)
            {
                usersData = userIds[i].id;
            }
        }
        return usersData;
    };

    var getNhIdByName = function(NhName){
        var NhId;
        for(var i = 0; i < NeighborhoodData.length; i++)
        {
            if(NeighborhoodData[i].name == NhName)
            {
                NhId = NeighborhoodData[i].id;
            }
        }
        return NhId;
    };
    var getMemberIdByUserId = function(userid,nhid){
        var MemberId;
        for(var i = 0; i < MembershipData.length; i++)
        {
            if(MembershipData[i].userId == userid && MembershipData[i].nhid == nhid)
            {
                MemberId = MembershipData[i].id;
            }
        }
        return MemberId;
    }

    var getCollabIdByName = function(collabName){
        var Collab_Id;
        for(var i = 0; i < CollaborationData.length; i++)
        {
            if(CollaborationData[i].name == collabName)
            {
                Collab_Id = CollaborationData[i].id;
            }
        }
        return Collab_Id;
    }

    var getWbIdByName = function(WbName){
        var Wb_Id;
        for(var i = 0; i < WhiteBoardData.length; i++)
        {
            if(WhiteBoardData[i].name == WbName)
            {
                Wb_Id = WhiteBoardData[i].id;
            }
        }
        return Wb_Id;
    }

    return {
        createDatabase : createDatabase,
        getUserIdByName : getUserIdByName,
        getNhIdByName : getNhIdByName,
        getMemberIdByUserId : getMemberIdByUserId,
        getCollabIdByName : getCollabIdByName,
        getWbIdByName : getWbIdByName
    }
})();