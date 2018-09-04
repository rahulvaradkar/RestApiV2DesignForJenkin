
var GlobalData=(function(){

	var Globals= {
		//change following line to match your test instance URL
		baseURL: "http://localhost:8080/BAE_Rest_rmhc/",
		assertHow : "forNow"
	};
	var UserInput = {
		email: 1, 	// email of User in database
		User_Id: 2,
		Negative_UserId: 3,
		Zero_USerId: 4,
		Non_Existing_UserId: 5,
		Member_Id_1: 6,   	//Member ID 1 of user
		Member_Id_2: 7,   	//Member ID 2 of user
		Invalid_MemberId: 8,
		Negative_MemberId: 9,
		authorization: 10,    //user1
		authorization_1: 11, //user2
		invalidAuthorization: 12 //invalid Authorization
	};
	var NeighborhoodInput = {ROOT_NhId: 1,NHID_1: 2,NHID_1_Name: "demo",NHID_2: 3,NHID_2_Name: "demo2",NHID_3: 5, collaborationInvalidParent_Id: 99999,Invalid_Nh_Id: 99999,
		Invalid_Nh_Id_1: 85693,Negative_Nh_Id: -1,Existing_NhName: "demo"};
	var CollaborationInput= {Collab_Id_1: 1000,Collab_Name_1: "demo",Collab_Id_2: 1001,Collab_Name_2: "demo2",
		Non_Existing_CollabId: 99999,Negative_CollabId: -99999};
	var WhiteboardInput;
	var GridInput = {
		cells: [],
		columnArray: [-1, -1],
		columnCellArrays: [
			{
				"cellValues": ["Maharashtra", "Maharashtra", "Gujrat", "Gujrat"],
				"cellFormulas": ["Maharashtra", "Maharashtra", "Gujrat", "Gujrat"],
				"cellAccess": [2, 2, 2, 2],
				"columnId": -1,
				"colSequence": 1
			},
			{
				"cellValues": ["Pune", "Mumbai", "Surat", "Ahmedabad"],
				"cellFormulas": ["Pune", "Mumbai", "Surat", "Ahmedabad"],
				"cellAccess": [2, 2, 2, 2],
				"columnId": -1,
				"colSequence": 2
			}],
		columns: [
			{ "active": true, "id": -1, "name": "state", "previousColumnSequence": 0, "previousColumnid": -1, "seqNo": 1, "tid": -1 },
			{ "active": true, "id": -1, "name": "city", "previousColumnSequence": 1, "previousColumnid": -1, "seqNo": 2, "tid": -1 }
		],
		columns_DuplicateColumns: [
			{ "active": true, "id": -1, "name": "state", "previousColumnSequence": 0, "previousColumnid": -1, "seqNo": 1, "tid": -1 },
			{ "active": true, "id": -1, "name": "state", "previousColumnSequence": 1, "previousColumnid": -1, "seqNo": 2, "tid": -1 }
		],
		GridChangeBuffer: {
			"critical": 0,
			"criticalLevel": -1,
			"newRowArray": [
				{
					"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 1, "tid": -1, "rowName": " ",
					"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
				},
				{
					"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 2, "tid": -1, "rowName": " ",
					"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
				},
				{
					"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 3, "tid": -1, "rowName": " ",
					"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
				},
				{
					"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 4, "tid": -1, "rowName": " ",
					"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
				}
			],
			"deletedRowArray": [],
			"newColumnArray": [
				{ "active": true, "id": -1, "name": "state", "previousColumnSequence": 0, "previousColumnid": -1, "seqNo": 1, "tid": -1 },
				{ "active": true, "id": -1, "name": "city", "previousColumnSequence": 1, "previousColumnid": -1, "seqNo": 2, "tid": -1 }
			],
			"deletedColumnArray": []
		},
		rowArray: [-1, -1, -1, -1],
		rows: [
			{
				"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 1, "tid": -1, "rowName": " ",
				"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
			},
			{
				"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 2, "tid": -1, "rowName": " ",
				"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
			},
			{
				"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 3, "tid": -1, "rowName": " ",
				"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
			},
			{
				"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 4, "tid": -1, "rowName": " ",
				"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
			}
		],
		filter: "filter",
		view: "view",
		asOfTid: -1,
		neighborhoodHeirarchy: { "nh_level_2": -1, "nh_level_3": -1, "nh_Level_0": -1, "nh_Level_1": -1, "levels": 4 },
		exportTid: -1,
		serverURL: "serverURL",
		serverName: "serverName",
		importTid: -1,
		baselineId: -1,
		importTid_1: -1,
		cuboid_Id: 0
	};
	var Access_Control= {
			Access_Cuboid_Id: 2002742,						//Create Cuboid for Access Testing in Access_Control NH
			Access_Cuboid_Name: "Access_Cuboid",			//Access Cuboid Name
			NHID: 1965,										//Create Parent Neighbourhood 
			NHID_Name: "Access_Control",					//Name of NHID 
			Collab_Id: 1582,								//Create Collaboration under Access_Control NH
			Collab_Name: "Access_Cuboids",					//Collaboration Name
			WB_Id: 2172,									//Create Whiteboard ID under  Access_Cuboids Collaboration
			WB_Name: "Access_Tables",						//Whiteboard Name

			//Create User with Named With User1 and Child Neighbourhood under the Access_Control NH with name "Rows"
			//and make member user1 of Rows NH	
			Row_Access_User_1: getAuthorization("User1", 0, "Access_Control|Rows"),
			User1_Id: 2979,
			User1_Member_Id_1: 1899,

			//Create User with Named With User3 and Child Neighbourhood under the "Rows" NH with name "R_Add_Delete"
			//and make member user1 of R_Add_Delete NH
			Row_Access_User_2: getAuthorization("User3", 0, "Access_Control|Rows|R_Add_Delete"),
			User2_Member_Id_2: 1903,

			//Create User with Named With User4 and Child Neighbourhood under the "Rows" NH with name "R_Add_Delete_1"
			//and make member user1 of R_Add_Delete_1 NH
			Row_Access_User_3: getAuthorization("User4", 0, "Access_Control|Rows|R_Add_Delete_1"),
			User2_Member_Id_3: 1907
		};

	function buildData(){
	
		var data = Create_Data.getUserIdByName("restuser1");
		var auth =  getAuthorization("restuser1", 0, "demo");
		var auth_1 = getAuthorization("restuser2", 0, "demo2");
		var invalid_auth = getAuthorization("restuser1", 0, "ROOT|demo|JSAddin");

		var Nh__1 = Create_Data.getNhIdByName("demo");
		var Nh__2 = Create_Data.getNhIdByName("demo2");
		var Nh__3 = Create_Data.getNhIdByName("Access_Control");
		var Nh__4 = Create_Data.getNhIdByName("Rows");
		var Nh__5 = Create_Data.getNhIdByName("Rows_Add_Delete");
		var Nh__6 = Create_Data.getNhIdByName("Rows_Add_Delete_1");
		debugger;

		var M_Id_1 = Create_Data.getMemberIdByUserId(data,Nh__1);
		var M_Id_2 = Create_Data.getMemberIdByUserId(data,Nh__1);

		var collabId_1 = Create_Data.getCollabIdByName("demo");
		var collabId_2 = Create_Data.getCollabIdByName("demo2");

		var Wb_Id = Create_Data.getWbIdByName("demo");
		this.UserInput = {
			email: "restuser1", 	// email of User in database
			User_Id: data,
			Negative_UserId: -1001,
			Zero_USerId: 0,
			Non_Existing_UserId: 99999,
			Member_Id_1: M_Id_1,   	//Member ID 1 of user
			Member_Id_2: M_Id_2,   	//Member ID 2 of user
			Invalid_MemberId: 9999,
			Negative_MemberId: -1001,
			authorization: auth,    //user1
			authorization_1: auth_1, //user2
			invalidAuthorization: invalid_auth //invalid Authorization
		};

		this.NeighborhoodInput = {
			ROOT_NhId: 1,				// NH ID of Root Neighbourhood
			NHID_1: Nh__1,
			NHID_1_Name: "demo",
			NHID_2: Nh__2,
			NHID_2_Name: "demo2",
			NHID_3: Nh__4,   				// should not contain any collaboration
			InvalidParent_Id: 99999,
			Invalid_Nh_Id: 99999,
			Invalid_Nh_Id_1: 85693,
			Negative_Nh_Id: -1,
			Existing_NhName: "demo"
		};

		this.CollaborationInput = {
			Collab_Id_1: collabId_1,			//Collaboration is under NHID_1
			Collab_Name_1: "demo",
			Collab_Id_2: collabId_2,			//Collaboration is under NHID_2
			Collab_Name_2: "demo2",
			Non_Existing_CollabId: 99999,
			Negative_CollabId: -99999
		};

		this.WhiteboardInput = {
			WB_Id: Wb_Id,
			Invalid_WbId: 99999,
			Negative_WbId: -99999
		};

		// Cuboid Inputs for Grid.js and Submit.js File
		this.GridInput = {
			cells: [],
			columnArray: [-1, -1],
			columnCellArrays: [
				{
					"cellValues": ["Maharashtra", "Maharashtra", "Gujrat", "Gujrat"],
					"cellFormulas": ["Maharashtra", "Maharashtra", "Gujrat", "Gujrat"],
					"cellAccess": [2, 2, 2, 2],
					"columnId": -1,
					"colSequence": 1
				},
				{
					"cellValues": ["Pune", "Mumbai", "Surat", "Ahmedabad"],
					"cellFormulas": ["Pune", "Mumbai", "Surat", "Ahmedabad"],
					"cellAccess": [2, 2, 2, 2],
					"columnId": -1,
					"colSequence": 2
				}],
			columns: [
				{ "active": true, "id": -1, "name": "state", "previousColumnSequence": 0, "previousColumnid": -1, "seqNo": 1, "tid": -1 },
				{ "active": true, "id": -1, "name": "city", "previousColumnSequence": 1, "previousColumnid": -1, "seqNo": 2, "tid": -1 }
			],
			columns_DuplicateColumns: [
				{ "active": true, "id": -1, "name": "state", "previousColumnSequence": 0, "previousColumnid": -1, "seqNo": 1, "tid": -1 },
				{ "active": true, "id": -1, "name": "state", "previousColumnSequence": 1, "previousColumnid": -1, "seqNo": 2, "tid": -1 }
			],
			GridChangeBuffer: {
				"critical": 0,
				"criticalLevel": -1,
				"newRowArray": [
					{
						"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 1, "tid": -1, "rowName": " ",
						"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
					},
					{
						"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 2, "tid": -1, "rowName": " ",
						"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
					},
					{
						"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 3, "tid": -1, "rowName": " ",
						"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
					},
					{
						"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 4, "tid": -1, "rowName": " ",
						"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
					}
				],
				"deletedRowArray": [],
				"newColumnArray": [
					{ "active": true, "id": -1, "name": "state", "previousColumnSequence": 0, "previousColumnid": -1, "seqNo": 1, "tid": -1 },
					{ "active": true, "id": -1, "name": "city", "previousColumnSequence": 1, "previousColumnid": -1, "seqNo": 2, "tid": -1 }
				],
				"deletedColumnArray": []
			},
			rowArray: [-1, -1, -1, -1],
			rows: [
				{
					"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 1, "tid": -1, "rowName": " ",
					"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
				},
				{
					"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 2, "tid": -1, "rowName": " ",
					"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
				},
				{
					"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 3, "tid": -1, "rowName": " ",
					"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
				},
				{
					"active": true, "id": -1, "previousRowSequence": -1, "previousRowid": -1, "seqNo": 4, "tid": -1, "rowName": " ",
					"ownerId": -1, "ownerName": " ", "creationTid": -1, "createrId": -1, "ownershipAssignedTid": -1
				}
			],
			filter: "filter",
			view: "view",
			asOfTid: -1,
			neighborhoodHeirarchy: { "nh_level_2": -1, "nh_level_3": -1, "nh_Level_0": -1, "nh_Level_1": -1, "levels": 4 },
			exportTid: -1,
			serverURL: "serverURL",
			serverName: "serverName",
			importTid: -1,
			baselineId: -1,
			importTid_1: -1,
			cuboid_Id: 0
		};

		this.Access_Control = {
			Access_Cuboid_Id: 2002742,						//Create Cuboid for Access Testing in Access_Control NH
			Access_Cuboid_Name: "Access_Cuboid",			//Access Cuboid Name
			NHID: 1965,										//Create Parent Neighbourhood 
			NHID_Name: "Access_Control",					//Name of NHID 
			Collab_Id: 1582,								//Create Collaboration under Access_Control NH
			Collab_Name: "Access_Cuboids",					//Collaboration Name
			WB_Id: 2172,									//Create Whiteboard ID under  Access_Cuboids Collaboration
			WB_Name: "Access_Tables",						//Whiteboard Name

			//Create User with Named With User1 and Child Neighbourhood under the Access_Control NH with name "Rows"
			//and make member user1 of Rows NH	
			Row_Access_User_1: getAuthorization("User1", 0, "Access_Control|Rows"),
			User1_Id: 2979,
			User1_Member_Id_1: 1899,

			//Create User with Named With User3 and Child Neighbourhood under the "Rows" NH with name "R_Add_Delete"
			//and make member user1 of R_Add_Delete NH
			Row_Access_User_2: getAuthorization("User3", 0, "Access_Control|Rows|R_Add_Delete"),
			User2_Member_Id_2: 1903,

			//Create User with Named With User4 and Child Neighbourhood under the "Rows" NH with name "R_Add_Delete_1"
			//and make member user1 of R_Add_Delete_1 NH
			Row_Access_User_3: getAuthorization("User4", 0, "Access_Control|Rows|R_Add_Delete_1"),
			User2_Member_Id_3: 1907
		};
	}

	return {
		UserInput : UserInput,
		NeighborhoodInput : NeighborhoodInput,
		CollaborationInput : CollaborationInput,
		WhiteboardInput : WhiteboardInput,
		GridInput : GridInput,
		Access_Control : Access_Control,
		Globals : Globals,
		buildData : buildData
	}

})();


/*
	Users For Access Controls: User1, User3, User4
	Neighbourhood Structure :
					Access_Control						Parent						j is member 
						'|-> Rows						Child of Access_Control		User1 is member
								|-> R_Add_Delete		Child of Rows				User3 is Member
								|-> R_Add_Delete_1		Child of Rows				User4 is Member
*/