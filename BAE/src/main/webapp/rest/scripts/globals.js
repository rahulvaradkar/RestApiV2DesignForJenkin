var Globals = {
  //change following line to match your test instance URL
	baseURL: "http://localhost:8080/BAE_ReST/",
};

var UserInput = {
	email: "j", //User id email in database
	User_Id : 1001,
	Negative_UserId : -1001,
	Zero_USerId : 0,
	Non_Existing_UserId : 99999,
	Member_Id_1 : 1001,
	Member_Id_2 : 1013,
	Invalid_MemberId : 9999,
	Negative_MemberId : -1001,
	authorization : getAuthorization("j",0,"JSAddin"),
	authorization_1 : getAuthorization("j5",0,"JSAddin"),
	invalidAuthorization : getAuthorization("j",0,"ROOT|demo|JSAddin")
};

var NeighborhoodInput = {
	ROOT_NhId : 1,
	NHID_1 : 2,
	NHID_1_Name : "demo",
	NHID_2 : 3,
	NHID_2_Name : "JSAddin",
	NHID_3 : 30,   // should not contain any collaboration
	InvalidParent_Id : 99999,
	Invalid_Nh_Id : 99999,
	Invalid_Nh_Id_1 : 85693,
	Negative_Nh_Id : -1,
	Existing_NhName : "demo"
};

var CollaborationInput = {
	Collab_Id_1 : 1001,
	Collab_Name_1 : "demo",
	Collab_Id_2 : 1002,
	Collab_Name_2 : "JS_Addin",
	Non_Existing_CollabId : 99999,
	Negative_CollabId : -99999
};

var WhiteboardInput={
	WB_Id : 1002,
	Invalid_WbId : 99999,
	Negative_WbId : -99999
};

var GridInput = {
	cells: [],
	columnArray: [-1,-1],
	columnCellArrays: [{
							"cellValues": ["Maharashtra","Maharashtra","Gujrat","Gujrat"], 
    						"cellFormulas": ["Maharashtra","Maharashtra","Gujrat","Gujrat"], 
							"cellAccess": [2,2,2,2], "columnId": -1 , "colSequence" : 1
						},  
    					{
							"cellValues": ["Pune","Mumbai","Surat","Ahmadabad"], 
    						"cellFormulas": ["Pune","Mumbai","Surat","Ahmadabad"], 
							"cellAccess": [2,2,2,2], "columnId": -1 , "colSequence" : 2
						}] ,
	columns: [
				{"seqNo": 1,"name": "state","active": true,"previousColumnid":-1,"previousColumnSequence": -1,"id": -1,"tid": -1},
				{"seqNo": 2,"name": "city","active": true,"previousColumnid":-1,"previousColumnSequence": 1,"id": -1,"tid": -1}
			],
	columns_DuplicateColumns:[
								{"seqNo": 1,"name": "state","active": true,"previousColumnid":-1,"previousColumnSequence": -1,"id": -1,"tid": -1},
								{"seqNo": 2,"name": "state","active": true,"previousColumnid":-1,"previousColumnSequence": 1,"id": -1,"tid": -1}
							],
	GridChangeBuffer : {},
	rowArray : [-1,-1,-1,-1],
	rows :  [
		{  
			"active" : true ,  
			"id" : -1 ,  
			"previousRowSequence" : -1 ,  
			"previousRowid" : -1 ,  
			"seqNo" : 1 ,  
			"tid" : -1 ,  
			"rowName" : " " ,  
			"ownerId" : -1 ,  
			"ownerName" :  " " ,  
			"creationTid" : -1 ,  
			"createrId" : -1 , 
			"ownershipAssignedTid" : -1  
		} , 
		{  
			"active" : true ,  
			"id" : -1 ,  
			"previousRowSequence" : -1 ,  
			"previousRowid" : -1 ,  
			"seqNo" : 2 ,  
			"tid" : -1 ,  
			"rowName" : " " ,  
			"ownerId" : -1 ,  
			"ownerName" :  " " ,  
			"creationTid" : -1 ,  
			"createrId" : -1 , 
			"ownershipAssignedTid" : -1  
		} , 
		{  
			"active" : true ,  
			"id" : -1 ,  
			"previousRowSequence" : -1 ,  
			"previousRowid" : -1 ,  
			"seqNo" : 3 ,  
			"tid" : -1 ,  
			"rowName" : " " ,  
			"ownerId" : -1 ,  
			"ownerName" :  " " ,  
			"creationTid" : -1 ,  
			"createrId" : -1 , 
			"ownershipAssignedTid" : -1  
		} , 
		{  
			"active" : true ,  
			"id" : -1 ,  
			"previousRowSequence" : -1 ,  
			"previousRowid" : -1 ,  
			"seqNo" : 4 ,  
			"tid" : -1 ,  
			"rowName" : " " ,  
			"ownerId" : -1 ,  
			"ownerName" :  " " ,  
			"creationTid" : -1 ,  
			"createrId" : -1 , 
			"ownershipAssignedTid" : -1  
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