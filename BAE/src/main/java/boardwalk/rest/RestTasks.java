package boardwalk.rest;


import com.boardwalk.database.DatabaseLoader;
import boardwalk.connection.*;
import com.boardwalk.exception.*;
import java.io.*;
import java.sql.*;
import java.util.*;				//for Properties
import com.boardwalk.database.*;
import com.boardwalk.collaboration.*;
import boardwalk.collaboration.*;
import com.boardwalk.whiteboard.*;
import boardwalk.table.*;
import boardwalk.neighborhood.*;
import com.boardwalk.neighborhood.*;

//Added by Rahul Varadkar on 13-FEB-2018 for Link Import Table
//import com.boardwalk.table.*;		
//import com.boardwalk.member.Member;
//import com.boardwalk.user.UserManager;
//import com.boardwalk.excel.*;
//Added by Rahul Varadkar on 13-FEB-2018 for Link Import Table


import java.util.regex.Matcher;		//Added for Submit Cuboid by Rahul Varadkar on  21-MARCH-2018
import java.util.regex.Pattern;		//Added for Submit Cuboid by Rahul Varadkar on  21-MARCH-2018

import io.swagger.api.NeighborhoodApiService;
//GetBcpInstance
import io.swagger.model.BcpInstanceInfo;
import io.swagger.model.NhList;
import io.swagger.model.UserList;

import io.swagger.model.MemberList;
import io.swagger.model.Memberships;

//For bcpInstance(nhHierarchy)
import io.swagger.model.NhChild; 
import io.swagger.model.NhHierarchy;
import io.swagger.model.ThisNhDetails;
import io.swagger.model.Neighborhood;
import com.boardwalk.neighborhood.*;		//NHTree
import com.boardwalk.user.*;



//GetGridCellBuffer
import io.swagger.model.CellBuffer;
import io.swagger.model.CuboidInfo;
import com.boardwalk.table.TableManager;
import com.boardwalk.table.TableInfo;


//Added by Rahul Varadkar on 20-April-2018
import java.sql.*;
import java.util.*;
import boardwalk.connection.*;
import boardwalk.neighborhood.*;
import com.boardwalk.exception.BoardwalkException;
//Added by Rahul Varadkar on 20-April-2018

public class RestTasks {


	private RestTasks()
	{	
	}

	
	public static CellBuffer GetGridCellBuffer(String tableId)
	{
		String loginName = "admin";
		String loginPwd = "0";
		
        // get the connection
        Connection connection = null;
        try
        {
            DriverManager.registerDriver(new com.microsoft.jdbc.sqlserver.SQLServerDriver());
        }
        catch(Exception e)
        {
            System.out.println("Problem registering JDBC driver");
        }
        String jdbcConnectionString = "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=BAE_4_3;user=BOARDWALK_APPLICATION_USER;password=BOARDWALK_APPLICATION_USER";

        try
        {
            connection =  DriverManager.getConnection(jdbcConnectionString);
            System.out.println("Connection established successfully");
        }
        catch( SQLException sqe )
        {
            System.out.println("There is a Database connection problem");
        }
    	
        // Get an authenticated boardwalk connection
        BoardwalkConnection bwcon = null;
        try
        {
            bwcon = BoardwalkConnectionManager.getBoardwalkConnection(connection, loginName, loginPwd, -1);
            System.out.println("Successfully obtained authenticated Boardwalk connection");
        }
        catch(BoardwalkException bwe)
        {
            System.out.println("Authentication/Connection Failed");
        }

        int userId = bwcon.getUserId();
		
		
		// get the table contents
		try
		{

		//	TableInfo tinfo = TableManager.getTableInfo( connection, userId, Integer.parseInt(tableId) ) ;

			CuboidInfo ci = new CuboidInfo();
/*			ci.setAcessTableId(acessTableId);
			ci.setAsOfTid(asOfTid);
			ci.setBaselineId(baselineId);
			ci.setCriteriaTableId(criteriaTableId);
			ci.setExportTid(exportTid);
			ci.setFilter(filter);
			ci.setId(id);
			ci.setImportTid(importTid);
			ci.setName(name);
*/
			BoardwalkTableContents bwtbcon = BoardwalkTableManager.getTableContents(
																		bwcon, Integer.parseInt(tableId));
			System.out.println("Sucessfully fetched the table from the database");

			// get the columns
			System.out.print("\t\t");
			Vector columns = bwtbcon.getColumns();
			Iterator icols = columns.iterator();
			while (icols.hasNext())
			{
				BoardwalkColumn c = (BoardwalkColumn)icols.next();
				System.out.print(c.getName() + ":" + c.getId() + ",");
			}
			System.out.print("\n");
			// get the rows and cells
			Vector rows = bwtbcon.getRows();
			Iterator irows = rows.iterator();
			while (irows.hasNext())
			{
				BoardwalkRow r = (BoardwalkRow)irows.next();
				System.out.print(r.getId() + ":" + r.getOwner()+ "\t");
				Vector cells = bwtbcon.getCellsForRow(r.getId());
				Iterator icells = cells.iterator();
				while (icells.hasNext())
				{
					BoardwalkCell cell = (BoardwalkCell)icells.next();
					System.out.print(cell.getStringValue() + ",");
				}
				System.out.print("\n");
			}

		}
		catch (BoardwalkException bwe)
		{
			System.out.println("Error fetching table");
		}

        
		
		
		
		CellBuffer cb = new CellBuffer ();
/*		cb.setColumns(columns);
		cb.setRows(rows);
		cb.setCells(cells);
		cb.setInfo(info);
*/		
		return cb;
	}

	
	
	public static BcpInstanceInfo GetBcpInstance( String serverName, String serverUrl,  String loginName, String loginPwd)
	{
        // get the connection
        Connection connection = null;
        try
        {
            DriverManager.registerDriver(new com.microsoft.jdbc.sqlserver.SQLServerDriver());
        }
        catch(Exception e)
        {
            System.out.println("Problem registering JDBC driver");
        }
        String jdbcConnectionString = "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=BAE_4_3;user=BOARDWALK_APPLICATION_USER;password=BOARDWALK_APPLICATION_USER";

        try
        {
            connection =  DriverManager.getConnection(jdbcConnectionString);
            System.out.println("Connection established successfully");
        }
        catch( SQLException sqe )
        {
            System.out.println("There is a Database connection problem");
        }
    	
        // Get an authenticated boardwalk connection
        BoardwalkConnection bwcon = null;
        try
        {
            bwcon = BoardwalkConnectionManager.getBoardwalkConnection(connection, loginName, loginPwd, -1);
            System.out.println("Successfully obtained authenticated Boardwalk connection");
        }
        catch(BoardwalkException bwe)
        {
            System.out.println("Authentication/Connection Failed");
        }

        ArrayList <UserList> uList = new ArrayList<UserList>();
        ArrayList <MemberList> baeMemberships = new ArrayList<MemberList>();
          
        try
        {
            // Get the list of users
            Vector userList = BoardwalkUserManager.getUserList(bwcon);
            Iterator ui = userList.iterator();
            while (ui.hasNext())
            {
				BoardwalkUser bu = (BoardwalkUser)ui.next();
		    	UserList u1 = new UserList();
		    	u1.setUserId(bu.getId());  
		    	u1.setUserName(bu.getUserName());
				u1.setActive(bu.getActive());
				u1.setExternalUserName(bu.getExtUserName());
				u1.setFirstName(bu.getFirstName());
				u1.setLastName(bu.getLastName());
		    	uList.add(u1);
				System.out.println(bu.getId() + ":" + bu.getUserName() + ":" + bu.getFirstName() + ":" + bu.getLastName());
				
				
                Vector ml = BoardwalkUserManager.getMembershipList(bwcon, bu.getId());
				Iterator mli = ml.iterator();
				
		        ArrayList <Memberships> mmList = new ArrayList<Memberships>();
				
				while (mli.hasNext())		//All memberships of a User
				{
					BoardwalkMember m = (BoardwalkMember)mli.next();
					System.out.println ("id = " + m.getId() + " userid = " + m.getUserId() +
						" nhid = " + m.getNeighborhoodId() + " nhname = " + m.getNeighborhoodName());

					Memberships mms = new Memberships();
					mms.setMemberId(m.getId());
					mms.setNhId(m.getNeighborhoodId());
					mms.setNhName(m.getNeighborhoodName());
					mmList.add(mms);
				}

				MemberList mml = new MemberList();
				mml.setUserId(bu.getId());
				mml.setUserName(bu.getUserName()  );
				mml.setMemberships(mmList);
								
				baeMemberships.add(mml);		//Adding Membership Details with Index as UserId
            }
        }
        catch (BoardwalkException bwe)
        {
            System.out.println("Error fetching users from database");
        }
        
		User adminUser = UserManager.getUser(connection, "admin");

		ArrayList <NhHierarchy> nieghborhoods = new ArrayList<NhHierarchy>();

		
    	BcpInstanceInfo bcpii = new BcpInstanceInfo();
    	
        NhHierarchy nhh_0;
        NhHierarchy nhh_1;
        NhHierarchy nhh_2;
        NhHierarchy nhh_3;
        
		int nhCount = 0;
        User managedBy ;

        ThisNhDetails thisNh_0;				//Root NH  -needed
/*        ArrayList <NhChild> nh0Children;		//Children of NH-0
        ArrayList <NhChild> nh1Children;		//Children of NH-1
        ArrayList <NhChild> nh2Children;		//Children of NH-2
        ArrayList <NhChild> nh3Children;		//Children of NH-3
*/        
        Vector nhTree = NeighborhoodManager.getNeighborhoodTree(connection, adminUser.getId());
		Iterator nh0Iter = nhTree.iterator();
		while (nh0Iter.hasNext())		//Level-0 NH
		{
	        nhh_0 = new NhHierarchy();
			nhCount ++;
			NHTree nht = (NHTree)nh0Iter.next();
			
			com.boardwalk.neighborhood.Neighborhood nh0 =  nht.getNeighborhood() ;
			
	        thisNh_0 = new ThisNhDetails();
			thisNh_0.setThisLevel(nh0.getLevels());			// 0 level
			managedBy = UserManager.getUserbyId(connection,  nh0.getManagedby());
			thisNh_0.setCreatedById( -1 );
			thisNh_0.setCreatedOn("Not Available");
			thisNh_0.setIsActive(true );
			thisNh_0.setIsSecure(nh0.isSecure());
			thisNh_0.setLevel0Id(nh0.getLevel0Id());
			thisNh_0.setLevel1Id(nh0.getLevel1Id());
			thisNh_0.setLevel2Id(nh0.getLevel2Id());
			thisNh_0.setLevel3Id(nh0.getLevel3Id());
			thisNh_0.setManagedById(nh0.getManagedby());
//			thisNh.setManagedByName(  managedBy.getFirstName() + " " + managedBy.getLastName() + " (" + managedBy.getAddress() + ")");
			thisNh_0.setNhName(nh0.getName());
			thisNh_0.setTxId(-1);		//not available from library

			nhh_0.setThisNh(thisNh_0);
			
			Vector nh1Tree = nht.getChildren();

			if (!nh1Tree.isEmpty())
			{
				NhChild nh1Child;
				ThisNhDetails thisNh_1;
				
				nhh_1 =  new NhHierarchy() ;
				Iterator nh1Iter = nh1Tree.iterator();
				while (nh1Iter.hasNext())			// LEVEL-1 NH
				{
					nhCount++;
					NHTree nh1t = (NHTree)nh1Iter.next();
					com.boardwalk.neighborhood.Neighborhood nh1 =  nh1t.getNeighborhood();

					nhh_1 = new NhHierarchy();	
					
					nh1Child = new NhChild();
					nh1Child.setLevel(nh1.getLevels());
					nh1Child.setId(nh1.getId());
					nh1Child.setName(nh1.getName());
//					nh1Child.setIsActive(isActive);					//need review
//					nh1Child.setNeighborhoodId(neighborhoodId);		//need review
//					nh1Child.setNhChildren( nhChildren);				//need review
//					nh1Child.addNhChildrenItem(  )
					
					nhh_0.addNhChildrenItem(nh1Child );				// Adding as a Child of Parent
					
					thisNh_1 = new ThisNhDetails();
					thisNh_1.setThisLevel(nh1.getLevels());
					managedBy = UserManager.getUserbyId(connection,  nh1.getManagedby());
					thisNh_1.setCreatedById( -1 );
					thisNh_1.setCreatedOn("Not Available");
					thisNh_1.setIsActive(true );
					thisNh_1.setIsSecure(nh1.isSecure());
					thisNh_1.setLevel0Id(nh1.getLevel0Id());
					thisNh_1.setLevel1Id(nh1.getLevel1Id());
					thisNh_1.setLevel2Id(nh1.getLevel2Id());
					thisNh_1.setLevel3Id(nh1.getLevel3Id());
					thisNh_1.setManagedById(nh1.getManagedby());
//					nh1Child.setManagedByName(  managedBy.getFirstName() + " " + managedBy.getLastName() + " (" + managedBy.getAddress() + ")");
					thisNh_1.setNhName(nh1.getName());
					thisNh_1.setTxId(-1);

					nhh_1.setThisNh(thisNh_1);

					Vector nh2Tree = nh1t.getChildren();
//			        nh1Children = new ArrayList<NhChild>();
					if (!nh2Tree.isEmpty())			
					{
						NhChild nh2Child;
						ThisNhDetails thisNh_2;
						nhh_2 =  new NhHierarchy() ;
						
						Iterator nh2Iter = nh2Tree.iterator();
						while (nh2Iter.hasNext())					// LEVEL-2 NH
						{
							nhCount++;
							NHTree nh2t = (NHTree)nh2Iter.next();
							com.boardwalk.neighborhood.Neighborhood  nh2 =  nh2t.getNeighborhood();

							nhh_2 = new NhHierarchy();	
							
							nh2Child = new NhChild();
							nh2Child.setLevel(nh2.getLevels());
							nh2Child.setId(nh2.getId());
							nh2Child.setName(nh2.getName());
							
							nhh_1.addNhChildrenItem(nh2Child);			// Adding as a Child of Parent

							
							//nhh_1.addNhChildrenItem(nh2Child);
							thisNh_2 = new ThisNhDetails();
							thisNh_2.setThisLevel(nh1.getLevels());
							managedBy = UserManager.getUserbyId(connection,  nh1.getManagedby());
							thisNh_2.setCreatedById( -1 );
							thisNh_2.setCreatedOn("Not Available");
							thisNh_2.setIsActive(true );
							thisNh_2.setIsSecure(nh1.isSecure());
							thisNh_2.setLevel0Id(nh1.getLevel0Id());
							thisNh_2.setLevel1Id(nh1.getLevel1Id());
							thisNh_2.setLevel2Id(nh1.getLevel2Id());
							thisNh_2.setLevel3Id(nh1.getLevel3Id());
							thisNh_2.setManagedById(nh1.getManagedby());
//							nh1Child.setManagedByName(  managedBy.getFirstName() + " " + managedBy.getLastName() + " (" + managedBy.getAddress() + ")");
							thisNh_2.setNhName(nh1.getName());
							thisNh_2.setTxId(-1);

							nhh_2.setThisNh(thisNh_2);
							
							Vector nh3Tree = nh2t.getChildren();
//					        nh2Children = new ArrayList<NhChild>();

					        if (!nh3Tree.isEmpty())
							{
								NhChild nh3Child;
								ThisNhDetails thisNh_3;
								
								NhChild nh4Child = new NhChild();
								nhh_3 =  new NhHierarchy() ;

								Iterator nh3Iter = nh3Tree.iterator();
								while (nh3Iter.hasNext())			// LEVEL-3 NH
								{
									//nh3sb = new StringBuffer();
									nhCount++;
									NHTree nh3t = (NHTree)nh3Iter.next();
									com.boardwalk.neighborhood.Neighborhood nh3 =  nh3t.getNeighborhood();

									nhh_3 = new NhHierarchy();	
									
									nh3Child = new NhChild();
									nh3Child.setLevel(nh3.getLevels());
									nh3Child.setId(nh3.getId());
									nh3Child.setName(nh3.getName());

									nhh_2.addNhChildrenItem(nh3Child);		// Adding as a Child of Parent
									
									//nhh_1.addNhChildrenItem(nh2Child);
									thisNh_3 = new ThisNhDetails();
									thisNh_3.setThisLevel(nh1.getLevels());
									managedBy = UserManager.getUserbyId(connection,  nh1.getManagedby());
									thisNh_3.setCreatedById( -1 );
									thisNh_3.setCreatedOn("Not Available");
									thisNh_3.setIsActive(true );
									thisNh_3.setIsSecure(nh1.isSecure());
									thisNh_3.setLevel0Id(nh1.getLevel0Id());
									thisNh_3.setLevel1Id(nh1.getLevel1Id());
									thisNh_3.setLevel2Id(nh1.getLevel2Id());
									thisNh_3.setLevel3Id(nh1.getLevel3Id());
									thisNh_3.setManagedById(nh1.getManagedby());
//									nh1Child.setManagedByName(  managedBy.getFirstName() + " " + managedBy.getLastName() + " (" + managedBy.getAddress() + ")");
									thisNh_3.setNhName(nh1.getName());
									thisNh_3.setTxId(-1);

									nhh_3.setThisNh(thisNh_3);
									nhh_3.addNhChildrenItem(null);
									//nieghborhoods.add(nhh_3);	
							     	//bcpii.addNieghborhoodsItem(nhh_3);
									//nhh_3.setNhChildren(null);				// No Child Neighborhood after  LEVEL-3
								}	// End of nh3Iter While
								//nieghborhoods.add(nhh_3);
						     	//bcpii.addNieghborhoodsItem(nhh_3);
							}
						}// End of nh2Iter While
						//nieghborhoods.add(nhh_2);	
				     	//bcpii.addNieghborhoodsItem(nhh_2);
					}
				}// End of nh1Iter While
				//nieghborhoods.add(nhh_1);	
		     	//bcpii.addNieghborhoodsItem(nhh_1);
			}	// End of If Nh0 has children
			nieghborhoods.add(nhh_0);	
	     	//bcpii.addNieghborhoodsItem(nhh_0);

		}// End of nh0Iter While

    	System.out.println("inside BcpInstanceApiServiceImpl....");
//    	BcpInstanceInfo bcpii = new BcpInstanceInfo();

    	bcpii.setLoginName(loginName);
    	bcpii.setLoginPwd(loginPwd);
    	bcpii.setServerAlias(serverName);
    	bcpii.setServerUrl(serverUrl);
//need to get proper NH-0 List from database
    	ArrayList<NhList> nh0List = new  ArrayList<NhList>();		// <NhList>;
    	NhList e = new NhList();
    	e.setNhId(1);  e.setNhName("APAC"); e.setNhLevel(0);
    	nh0List.add(e);
    	
    	NhList e1 = new NhList();
    	e1.setNhId(2);  e1.setNhName("ANZ"); e1.setNhLevel(0);
    	nh0List.add(e1);
    	
    	bcpii.setNh0List(nh0List);
    	bcpii.setUserList(uList);
     	bcpii.setMembershipList( baeMemberships);
     	bcpii.setNieghborhoods(nieghborhoods);
    	//bcpii.setNieghborhoods( );

     	return bcpii;
	}

	
   

	
}
