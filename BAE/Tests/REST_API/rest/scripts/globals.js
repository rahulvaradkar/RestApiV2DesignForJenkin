var Globals = {
  //change following line to match your test instance URL
	baseURL: "https://bwrestapiqa.boardwalktech.com:8443/BAE_4_4/",
};

var UserInput = {
	email: "user1", //User id email in database
	User_Id : 1000,
	Negative_UserId : -1000,
	Zero_USerId : 0,
	Non_Existing_UserId : 99999,
	Member_Id_1 : 1000,
	Member_Id_2 : 1005,
	Invalid_MemberId : 9999,
	Negative_MemberId : -1001,
	authorization : getAuthorization("user1",0,"nh1"),
	authorization_1 : getAuthorization("user2",0,"nh1"),
	invalidAuthorization : getAuthorization("userx",790,"userx")
};

var NeighborhoodInput = {
	ROOT_NhId : 1,
	NHID_1 : 2,
	NHID_1_Name : "nh1",
	NHID_2 : 685,
	NHID_2_Name : "nh2",
	NHID_3 : 22,   // should not contain any collaboration
	InvalidParent_Id : 99999,
	Invalid_Nh_Id : 99999,
	Invalid_Nh_Id_1 : 85693,
	Negative_Nh_Id : -1,
	Existing_NhName : "nh1"
};

var CollaborationInput = {
	Collab_Id_1 : 1000,
	Collab_Name_1 : "collab1",
	Collab_Id_2 : 1001,
	Collab_Name_2 : "collab2",
	Non_Existing_CollabId : 99999,
	Negative_CollabId : -99999
};

var WhiteboardInput={
	WB_Id : 1009,
	Invalid_WbId : 99999,
	Negative_WbId : -99999
};

var GridInput = {
 cells: [],
 columnArray: [-1,-1],
 columnCellArrays: [
  {
    "cellValues": [
   "Maharashtra",
   "Maharashtra",
   "Gujrat",
   "Gujrat"
    ],
    "cellFormulas": [
   "Maharashtra",
   "Maharashtra",
   "Gujrat",
   "Gujrat"
    ],
    "cellAccess": [
   2,
   2,
   2,
   2
    ],
    "columnId": -1,
    "colSequence": 1
  },
  {
    "cellValues": [
   "Pune",
   "Mumbai",
   "Surat",
   "Ahmedabad"
    ],
    "cellFormulas": [
   "Pune",
   "Mumbai",
   "Surat",
   "Ahmedabad"
    ],
    "cellAccess": [
   2,
   2,
   2,
   2
    ],
    "columnId": -1,
    "colSequence": 2
  }] ,
 columns: [
  {
   "active": true,
   "id": -1,
   "name": "state",
   "previousColumnSequence": 0,
   "previousColumnid": -1,
   "seqNo": 1,
   "tid": -1
    },
    {
   "active": true,
   "id": -1,
   "name": "city",
   "previousColumnSequence": 1,
   "previousColumnid": -1,
   "seqNo": 2,
   "tid": -1
    }],
 columns_DuplicateColumns:[{
  "active": true,
  "id": -1,
  "name": "state",
  "previousColumnSequence": 0,
  "previousColumnid": -1,
  "seqNo": 1,
  "tid": -1
   },
   {
  "active": true,
  "id": -1,
  "name": "state",
  "previousColumnSequence": 1,
  "previousColumnid": -1,
  "seqNo": 2,
  "tid": -1
   }],
 GridChangeBuffer : {
  "critical": 0,
  "criticalLevel": -1,
  "newRowArray": [
    {
   "active": true,
   "id": -1,
   "previousRowSequence": -1,
   "previousRowid": -1,
   "seqNo": 1,
   "tid": -1,
   "rowName": " ",
   "ownerId": -1,
   "ownerName": " ",
   "creationTid": -1,
   "createrId": -1,
   "ownershipAssignedTid": -1
    },
    {
   "active": true,
   "id": -1,
   "previousRowSequence": -1,
   "previousRowid": -1,
   "seqNo": 2,
   "tid": -1,
   "rowName": " ",
   "ownerId": -1,
   "ownerName": " ",
   "creationTid": -1,
   "createrId": -1,
   "ownershipAssignedTid": -1
    },
    {
   "active": true,
   "id": -1,
   "previousRowSequence": -1,
   "previousRowid": -1,
   "seqNo": 3,
   "tid": -1,
   "rowName": " ",
   "ownerId": -1,
   "ownerName": " ",
   "creationTid": -1,
   "createrId": -1,
   "ownershipAssignedTid": -1
    },
    {
   "active": true,
   "id": -1,
   "previousRowSequence": -1,
   "previousRowid": -1,
   "seqNo": 4,
   "tid": -1,
   "rowName": " ",
   "ownerId": -1,
   "ownerName": " ",
   "creationTid": -1,
   "createrId": -1,
   "ownershipAssignedTid": -1
    }
  ],
  "deletedRowArray": [],
  "newColumnArray": [
    {
   "active": true,
   "id": -1,
   "name": "state",
   "previousColumnSequence": 0,
   "previousColumnid": -1,
   "seqNo": 1,
   "tid": -1
    },
    {
   "active": true,
   "id": -1,
   "name": "city",
   "previousColumnSequence": 1,
   "previousColumnid": -1,
   "seqNo": 2,
   "tid": -1
    }
  ],
  "deletedColumnArray": []
   },
 rowArray : [-1,-1,-1,-1],
 rows :  [
  {
   "active": true,
   "id": -1,
   "previousRowSequence": -1,
   "previousRowid": -1,
   "seqNo": 1,
   "tid": -1,
   "rowName": " ",
   "ownerId": -1,
   "ownerName": " ",
   "creationTid": -1,
   "createrId": -1,
   "ownershipAssignedTid": -1
    },
    {
   "active": true,
   "id": -1,
   "previousRowSequence": -1,
   "previousRowid": -1,
   "seqNo": 2,
   "tid": -1,
   "rowName": " ",
   "ownerId": -1,
   "ownerName": " ",
   "creationTid": -1,
   "createrId": -1,
   "ownershipAssignedTid": -1
    },
    {
   "active": true,
   "id": -1,
   "previousRowSequence": -1,
   "previousRowid": -1,
   "seqNo": 3,
   "tid": -1,
   "rowName": " ",
   "ownerId": -1,
   "ownerName": " ",
   "creationTid": -1,
   "createrId": -1,
   "ownershipAssignedTid": -1
    },
    {
   "active": true,
   "id": -1,
   "previousRowSequence": -1,
   "previousRowid": -1,
   "seqNo": 4,
   "tid": -1,
   "rowName": " ",
   "ownerId": -1,
   "ownerName": " ",
   "creationTid": -1,
   "createrId": -1,
   "ownershipAssignedTid": -1
    }
  ],
  filter : "filter",
  view : "view",
  asOfTid : -1,
  neighborhoodHeirarchy : {
   "nh_level_2" : -1,
   "nh_level_3" : -1,
   "nh_Level_0" : -1,
   "nh_Level_1" : -1,
   "levels": 4
  },
  exportTid :-1,
  serverURL : "serverURL",
  serverName : "serverName",
  importTid : -1,
  baselineId : -1,
  importTid_1 : -1,
  cuboid_Id : 0
};

var Access_Control = {
	Access_Cuboid_Id : 2003326,
	Access_Cuboid_Name : "Access_Cuboid",
	NHID : 2127,
	NHID_Name : "Access_Control",
 	Collab_Id : 1487,
	Collab_Name : "Access_Cuboids",
	WB_Id : 2188,
	WB_Name : "Access_Tables",
	Row_Access_User_1 : getAuthorization("acuser1",0,"Access_Control|Rows"),
	User1_Id : 2762,
	User1_Member_Id_1 : 2001,
	Row_Access_User_2 : getAuthorization("acuser3",0,"Access_Control|Rows|R_Add_delete"),
	User2_Member_Id_2 : 1903,
	Row_Access_User_3 : getAuthorization("acuser4",0,"Access_Control|Rows|R_Add_delete_1"),
	User2_Member_Id_3 : 1907
};

