package testNG;

import org.testng.annotations.Test;

import org.testng.AssertJUnit;
import testNG.DataSet;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.boardwalk.database.DatabaseLoader;
import com.boardwalk.exception.SystemException;
import com.boardwalk.table.TableAccessList;
import com.boardwalk.table.TableInfo;
import com.boardwalk.table.TableManager;
import com.boardwalk.table.TableViewManager;

import java.io.*;
import java.sql.*;                  // JDBC package

import com.boardwalk.member.Member;
import com.boardwalk.user.UserManager;

public class xlLinkImportServiceTest {

	public static DataSet ds;
	
	public final static String Seperator = new Character((char)1).toString();
    public static String databasetype = null;
    public static String databasename = null;
	public static String InstanceName = null;
	public static String user = null;
	public static String password = null;
	public static String server = null;
	public static String port = null;
    public static String jdbcConnectionString = null;	
	public static String templatemode = null;
		
	static Properties properties=null;
	Connection connection2;
	DatabaseLoader databaseloader2 ;
	int userId;
	int tableId;
	String templateMode;
	String view;
	
	

	  
	
	
	   public static void initDatabaseForTest() throws IOException
	   {  
		   Properties serverproperties =null;
		   ds = new DataSet();
		   
		   try
		   {
				serverproperties = new Properties();
				InputStream is = new FileInputStream(DataSet.propFileLocation);
				serverproperties.load(is);
				
				    properties=serverproperties;
				    databasename = DataSet.databasename;
				    InstanceName = DataSet.InstanceName;//serverproperties.getProperty("InstanceName");
					user = DataSet.user;//serverproperties.getProperty("user");
					password = DataSet.password;//serverproperties.getProperty("password");
					server = DataSet.server;//serverproperties.getProperty("server");
					port = DataSet.port;serverproperties.getProperty("port");
					databasetype = DataSet.databasetype;//serverproperties.getProperty("databasetype");			 
					Class.forName("net.sourceforge.jtds.jdbc.Driver");
					jdbcConnectionString = ds.getjdbcConnectionString();//serverproperties.getProperty("jdbcConnectionString");
					templatemode = DataSet.templateMode;//serverproperties.getProperty("templatemode");
					
					//System.out.println("jdbc connection string "+jdbcConnectionString);
			
					
					
		   }
		   catch(Exception exception)
		   {
			   exception.printStackTrace();
		   }
	    }
	
	
	   

	   
	   
	   public static Connection getConnectionForTest()
		        throws SQLException
		    {
				Connection conn = null;
				
				
				try
				{
					initDatabaseForTest();
					DatabaseLoader.initDatabase(properties);
					System.out.println("-----------------------------"+jdbcConnectionString);
					conn =  DriverManager.getConnection(jdbcConnectionString);
		   		  	//conn.setAutoCommit(true);
			  	}
			  	catch(Exception sqe )
			  	{
						sqe.printStackTrace();
					System.out.println("There is a Database connection problem, either the database is down or the connection parameters are wrong");
					
				}

				 return conn;
		    }
	   
		/*	Junit :  buff-1001p0101722000025-10
	   Junit :  userId :1001       int
	   Junit :  userName :p        string
	   Junit :  userPassword :0       string
	   Junit :  memberId :1017       int
	   Junit :  nhId :2              int 
	   Junit :  tableId :2000025      int
	   Junit :  baselineId :-1        int
	   Junit :  view :            string
	   Junit :  mode :0            int
	   Junit :  connection :net.sourceforge.jtds.jdbc.ConnectionJDBC3@6fe46203
	   Junit :  tinfo :com.boardwalk.table.TableInfo@2ed96838
	  */
//	  		String[] requestInfo = buff.split(Seperator);
//	  		int userId = Integer.parseInt(requestInfo[0]);
//	  		String userName = requestInfo[1];
//	  		String userPassword = requestInfo[2];
//	  		int memberId = Integer.parseInt(requestInfo[3]);
//	  		int nhId = Integer.parseInt(requestInfo[4]);
//	  		int tableId = Integer.parseInt(requestInfo[5]);
//	  		int baselineId = Integer.parseInt(requestInfo[6]);
//	  		String view = requestInfo[7];
//	  		int mode = Integer.parseInt(requestInfo[8]);		
	
/*

	
	@Test(priority = 1)
	public void authMembercheck1() throws SQLException, IOException
	{
		
		
//		String userName = requestInfo[1];
//  	int memberId = Integer.parseInt(requestInfo[3]);
		
		initDatabaseForTest();
		DataSet.initBufferForTest();
		//initPropSheetValues();
		Connection conn3 = getConnectionForTest();
		
		connection2 = conn3;
		String sreqCol[] = ds.getRequestBufferArr();		
		String getnhIdCol[] =ds.getNhIdArr();		
		String getnhNameCol[] =ds.getNhNameArr();		
		String getUIdCol[] =ds.getUserIdArr();		
		String getIdCol[] =ds.getMemberIdArr();				
		String getReqCol;
		String[] reqInfo2 = null;

		
		
		for (int i = 0; i < 2; i++) {

			getReqCol = sreqCol[i];
			//System.out.println("              cath"+i+"         "+getReqCol);
			reqInfo2 = getReqCol.split(Seperator);
			
		String userName = reqInfo2[1];
	  	int memberId = Integer.parseInt(reqInfo2[2]);
	  	
		//System.out.println("userName      :"+userName);
		//System.out.println("memberId      :"+memberId);
		
	  	Member memberObj = UserManager.authenticateMember(connection2, userName, memberId);
		
		
		System.out.println("memberObj   :"+memberObj+"   :  "+memberObj.toString());
		
	  	
		System.out.println("memberObj.getNeighborhoodName()   :"+memberObj.getNeighborhoodName());
		System.out.println("memberObj.getNeighborhoodId   :"+memberObj.getNeighborhoodId());
		System.out.println("memberObj.getUserId   :"+memberObj.getUserId());
		System.out.println("memberObj.getId   :"+memberObj.getId());
	  	
		AssertJUnit.assertNotNull(memberObj);
		
		int getNhId =memberObj.getNeighborhoodId();
		String getNhName = memberObj.getNeighborhoodName();
		int getUId = memberObj.getUserId();
		int getId = memberObj.getId();
		
		AssertJUnit.assertEquals(getNhName, getnhNameCol[i]);
		AssertJUnit.assertEquals(getNhId, Integer.parseInt(getnhIdCol[i]));
		AssertJUnit.assertEquals(getUId, Integer.parseInt(getUIdCol[i]));
		AssertJUnit.assertEquals(getId, Integer.parseInt(getIdCol[i]));
			
		}
		
		for (int i = 4; i < 8; i++) {

			getReqCol = sreqCol[i];
		//	System.out.println("              cath"+i+"         "+getReqCol);
			reqInfo2 = getReqCol.split(Seperator);
		
			String userName = reqInfo2[1];
		  	int memberId = Integer.parseInt(reqInfo2[2]);
			
			Member memberObj2 = UserManager.authenticateMember(connection2, userName, memberId);
			
			//System.out.println("*************************************"+memberObj2);
			AssertJUnit.assertNull(memberObj2);
		
			
		}	
	
	} 
	

*/	 
	
	@Test(priority = 0)
	public void tableInfoCheck0() throws ClassNotFoundException, SQLException, SystemException, IOException 
	{
		
		System.out.println("Testing TableManager.getTableInfo function");
		
		

		Connection conn3 = null;
		
		initDatabaseForTest();
		DataSet.initBufferForTest();
		conn3=getConnectionForTest();		
		connection2 = conn3;
		
		String sreqCol[] = ds.getRequestBufferArr();		
		String cIdCol[] = ds.getCollaborationIdArr();		
		String cNameCol[] = ds.getCollaborationNameArr();		
		String lTime[] = ds.getLockTimeArr();		
		String tId[] = ds.getTableIdArr();		
		String tName[] = ds.getTableNameArr();		
		String wbId[] = ds.getWhiteboardIdArr();		
		String wbName[] = ds.getWhiteboardNameArr();		
		String lTId[] = ds.lockTidArr();		
		String getReqCol;
		String[] reqInfo2 = null;
		
		for (int i = 0; i < 1; i++) {

			getReqCol = sreqCol[i];
			System.out.println("              cath"+i+"         "+getReqCol);
			reqInfo2 = getReqCol.split(Seperator);
				
			//System.out.println(reqInfo[0]+"  "+reqInfo[1]+"  "+reqInfo[2]+"  "+reqInfo[3]+"  "+reqInfo[4]+"  "+reqInfo[5]+"  "+reqInfo[6]+"  "+reqInfo[7]+"  "+"  "+reqInfo[8]);
			//System.out.println(reqInfo1[0]+"  "+reqInfo1[1]+"  "+reqInfo1[2]+"  "+reqInfo1[3]+"  "+reqInfo1[4]+"  "+reqInfo1[5]+"  "+reqInfo1[6]+"  "+reqInfo1[7]+"  "+"  "+reqInfo1[8]);
			
			int userId = Integer.parseInt(reqInfo2[0]);
			System.out.println("******"+userId);
			int tableId = Integer.parseInt(reqInfo2[4]);
			System.out.println("******"+tableId);
			System.out.println("******"+conn3);
		 
		TableInfo tinfo = TableManager.getTableInfo(conn3, userId, tableId);
		
		
		
		//System.out.println("tinfo :******"+tinfo);
		//System.out.println("tinfo collaboration :"+tinfo.getCollaborationName());
		//System.out.println("tinfo id :"+tinfo.getCollaborationId());
		//System.out.println("tinfo   :"+tinfo+":");
		//System.out.println(tinfo.getClass().getName());
		//System.out.println("tinfo   getCollaborationId:"+tinfo+":"+tinfo.getCollaborationId());
		//System.out.println("tinfo   getCollaborationName():"+tinfo+":"+tinfo.getCollaborationName().toString());
		//System.out.println("tinfo   getCollaborationPurpose():"+tinfo+":"+tinfo.getCollaborationPurpose());
		//System.out.println("tinfo   getLockTime():"+tinfo+":"+tinfo.getLockTime());
		//System.out.println("tinfo   getTableId():"+tinfo+":"+tinfo.getTableId());
		//System.out.println("tinfo   getTableName():"+tinfo+":"+tinfo.getTableName());
		//System.out.println("tinfo   :"+tinfo+":"+tinfo.getWhiteboardId());
		//System.out.println("tinfo   :"+tinfo+":"+tinfo.getWhiteboardName());
		//System.out.println("tinfo   :"+tinfo+":"+tinfo.lockTid());
		//System.out.println("************************************************");
				
		
		//String tinfocId = ""+tinfo.getCollaborationId();
		//String tinfoCName = tinfo.getCollaborationName();
		//String getLTime = ""+tinfo.getLockTime();
		//String getTId = ""+tinfo.getTableId();
		//String TName = tinfo.getTableName();
		//String getWbId = ""+tinfo.getWhiteboardId();
		//String tinfowbName = tinfo.getWhiteboardName();
		//String getLockTid = ""+tinfo.lockTid(); 
		
		
		int tinfocId = tinfo.getCollaborationId();
		String tinfoCName = tinfo.getCollaborationName();
		long getLTime = tinfo.getLockTime();
		int getTId = tinfo.getTableId();
		String TName = tinfo.getTableName();
		int getWbId = tinfo.getWhiteboardId();
		String tinfowbName = tinfo.getWhiteboardName();
		int getLockTid = tinfo.lockTid(); 
	
	
		
		AssertJUnit.assertNotNull(tinfo);
		
		//System.out.println(lTId[i]);
		//System.out.println(tinfo.getTableId());
		//System.out.println(getTableId3);
			
			

		
		AssertJUnit.assertEquals(tinfocId,Integer.parseInt(cIdCol[i]));
		AssertJUnit.assertEquals(tinfoCName, cNameCol[i]);
		AssertJUnit.assertEquals(tinfowbName, wbName[i]);
		AssertJUnit.assertEquals(getLTime, Long.parseLong(lTime[i]));
		AssertJUnit.assertEquals(getLockTid, Integer.parseInt(lTId[i]));
		AssertJUnit.assertEquals(getTId, Integer.parseInt(tId[i]));
		AssertJUnit.assertEquals(getWbId, Integer.parseInt(wbId[i]));
		AssertJUnit.assertEquals(TName, tName[i]);
	
		

		
		}
			
		for (int i = 4; i <= 7; i++) {

			getReqCol = sreqCol[i];
			System.out.println("              cath"+i+"         "+getReqCol);
			reqInfo2 = getReqCol.split(Seperator);
				
			//System.out.println(reqInfo[0]+"  "+reqInfo[1]+"  "+reqInfo[2]+"  "+reqInfo[3]+"  "+reqInfo[4]+"  "+reqInfo[5]+"  "+reqInfo[6]+"  "+reqInfo[7]+"  "+"  "+reqInfo[8]);
			//System.out.println(reqInfo1[0]+"  "+reqInfo1[1]+"  "+reqInfo1[2]+"  "+reqInfo1[3]+"  "+reqInfo1[4]+"  "+reqInfo1[5]+"  "+reqInfo1[6]+"  "+reqInfo1[7]+"  "+"  "+reqInfo1[8]);
			
			int userId = Integer.parseInt(reqInfo2[0]);
			int tableId = Integer.parseInt(reqInfo2[4]);;
		 
		TableInfo tinfo = TableManager.getTableInfo(connection2, userId, tableId);
		System.out.println("tinfo for failure : "+tinfo);
		AssertJUnit.assertNull(tinfo);
			
		}
	
	}	

	
	
	/*	
	
	@Test(expectedExceptions=ArithmeticException.class)
	public void failureTestCheck(){
		int e = 1/1;
		System.out.println(e);
		
	}

	*/
	
	
	@Test(priority = 2)
	public void tableAccessList2() throws SQLException, IOException, SystemException
	 {
		
	  initDatabaseForTest();
	  DataSet.initBufferForTest();
	  //initPropSheetValues();
	  Connection conn3 = getConnectionForTest();  
	  connection2 = conn3;

	  String getReqCol;
	String[] reqInfo2 = null;
	
	String sreqCol[] = ds.getRequestBufferArr();
	
	  for (int i = 0; i <1; i++) {

			getReqCol = sreqCol[i];
			System.out.println("              cath"+i+"         "+getReqCol);
			reqInfo2 = getReqCol.split(Seperator);	  
	  
		int memberId = Integer.parseInt(reqInfo2[2]);
		int nhId = Integer.parseInt(reqInfo2[3]); 
		int userId = Integer.parseInt(reqInfo2[0]);
		int tableId = Integer.parseInt(reqInfo2[4]);
	 
	  TableInfo tinfo2 = TableManager.getTableInfo(connection2, userId, tableId);
	  TableAccessList ftal = TableViewManager.getSuggestedAccess(connection2, tinfo2, userId, memberId, nhId);
	  
	  System.out.println("ftal.getACL()                :"+ftal.getACL());
	  System.out.println("ftal.getId()                :"+ftal.getId());
	  System.out.println("ftal.getRelationship()                :"+ftal.getRelationship());
	  System.out.println("ftal.getSuggestedViewPreferenceBasedOnAccess()                :"+ftal.getSuggestedViewPreferenceBasedOnAccess());

	  
	  AssertJUnit.assertNotNull(ftal);
	 // AssertJUnit.assertEquals(true, ftal.canAdministerColumn());
	 // AssertJUnit.assertEquals(false,ftal.canAdministerTable());
	 // AssertJUnit.assertEquals(true,ftal.canDeleteRow());
	 // AssertJUnit.assertEquals(false,ftal.canReadLatestofMyGroup());
	 // AssertJUnit.assertEquals(false,ftal.canReadLatestofMyGroupAndAllChildren());
	 // AssertJUnit.assertEquals(false,ftal.canReadLatestofMyGroupAndImmediateChildren());
	 // AssertJUnit.assertEquals(true,ftal.canReadLatestOfTable());
	 // AssertJUnit.assertEquals(false,ftal.canReadLatestViewOfAll());
	 // AssertJUnit.assertEquals(false,ftal.canReadLatestViewOfAllChildren());
	 // AssertJUnit.assertEquals(false,ftal.canReadWriteLatestofMyGroup());
	 // AssertJUnit.assertEquals(false,ftal.canReadWriteLatestofMyGroupAndAllChildren());
	 // AssertJUnit.assertEquals(true,ftal.canWriteLatestOfTable());
	  AssertJUnit.assertEquals(33251,ftal.getACL());
	  AssertJUnit.assertEquals(-1,ftal.getId());
	  AssertJUnit.assertEquals(null,ftal.getRelationship());
	  AssertJUnit.assertEquals("LATEST",ftal.getSuggestedViewPreferenceBasedOnAccess());
	  
	  
	  
	  }
	  
	  
		  
//	  for (int i = 4; i <=7; i++) {
//
//			getReqCol = sreqCol[i];
//			System.out.println("              cath"+i+"         "+getReqCol);
//			reqInfo2 = getReqCol.split(Seperator);
//
//			
//			
//			int memberId = Integer.parseInt(reqInfo2[3]);
//			int nhId = Integer.parseInt(reqInfo2[4]); 
//			int userId = Integer.parseInt(reqInfo2[0]);
//			int tableId =Integer.parseInt(reqInfo2[5]);
//	 
//			
//			
//	  TableInfo tinfo2 = TableManager.getTableInfo(connection2, userId, tableId);
//	  System.out.println("*******************"+tinfo2);
//	  assertNull(tinfo2);
//	  
//  
//	  try {
//		  TableAccessList ftal = TableViewManager.getSuggestedAccess(connection2, tinfo2, userId, memberId, nhId);
//		  
//		  assertNull(ftal);
//		  
//		} catch (Exception e) {
//			System.out.println("***********************************");
//			e.printStackTrace();
//		}
//	   
//		  //System.out.println("*******************"+ftal);
//		  
//		  
//	
//	  
//  
//	  
////	  
////	  System.out.println("ftal.getACL()                :"+ftal.getACL());
////	  System.out.println("ftal.getId()                :"+ftal.getId());
////	  System.out.println("ftal.getRelationship()                :"+ftal.getRelationship());
////	  System.out.println("ftal.getSuggestedViewPreferenceBasedOnAccess()                :"+ftal.getSuggestedViewPreferenceBasedOnAccess());
//
//	 
//	  
//	  
//
//	  
//	  }	  

	 }
	
	

	


	
	@Test(priority = 3)
	public void getNeighbourhoodIdTest3() throws SQLException, IOException, SystemException
	{
		//DataSet.initDatabaseForTest();
		initDatabaseForTest();
		DataSet.initBufferForTest();
		Connection conn=getConnectionForTest();
		
		
		  String getReqCol;
			String[] reqInfo2 = null;
			
			String sreqCol[] = ds.getRequestBufferArr();
			String getnhIdCol[] =ds.getNhIdArr();
		
			
	for (int i = 0; i <=1; i++) {

					getReqCol = sreqCol[i];
					//System.out.println("              cath"+i+"         "+getReqCol);
					reqInfo2 = getReqCol.split(Seperator);
		
		try
		{
			String UserName = reqInfo2[1];
			int Memberid = Integer.parseInt(reqInfo2[2])  ;
						
			Member memberObj = UserManager.authenticateMember(conn, UserName, Memberid);
			int nhid=memberObj.getNeighborhoodId();		
			
			//System.out.println("request   "+i+"   processing");
			//System.out.println("nhid**************** "+nhid);
			
			AssertJUnit.assertEquals(nhid,Integer.parseInt(getnhIdCol[i]) );			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			//conn.close();
		}
				  
			  }
	
	for (int i = 4; i <=7; i++) {

		getReqCol = sreqCol[i];
	//	System.out.println("              cath"+i+"         "+getReqCol);
		reqInfo2 = getReqCol.split(Seperator);

		try
		{
			String UserName = reqInfo2[1];
			int Memberid = Integer.parseInt(reqInfo2[2])  ;			
			Member memberObj = UserManager.authenticateMember(conn, UserName, Memberid);
			
			//System.out.println("***********"+memberObj);
			
			int nhid=memberObj.getNeighborhoodId();		
			
	//		System.out.println("**************** "+nhid);

			AssertJUnit.assertEquals(nhid,null) ;			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			//conn.close();
		}
 
	}
		
		}
	
	
	
	
	
	
	@Test(priority = 4)
	 public void getTableBufferTest4() throws SQLException, IOException, SystemException
	 {
		
		DataSet.initBufferForTest();
		initDatabaseForTest();
	  Connection conn=null;
	  String Table_Buffer=null;
	 
	  
	  		String getReqCol;
			String[] reqInfo2 = null;
			
			String sreqCol[] = ds.getRequestBufferArr();
			String getTableBuffer[] = ds.getTablebufferArr();
			
	for (int i = 0; i <1; i++) {

					getReqCol = sreqCol[i];
					//System.out.println("              cath"+i+"         "+getReqCol);
					reqInfo2 = getReqCol.split(Seperator);
	  
	  
					
					try
					  {
						
						
					   conn=getConnectionForTest();
					  
						int memberId = Integer.parseInt(reqInfo2[2]);  
						
						int nhId = Integer.parseInt(reqInfo2[3]);  
						int userId = Integer.parseInt(reqInfo2[0]); 
						int tableId =Integer.parseInt(reqInfo2[4]); 
						int baselineId =Integer.parseInt(reqInfo2[5]); 
						int mode =0;  
//					   System.out.println(conn);
//					   System.out.println(tableId);
//					   System.out.println(userId);
//					   System.out.println(memberId);
//					   System.out.println(nhId);
//					   System.out.println(baselineId);
//					   System.out.println(view);
//					   System.out.println(mode);
					   Table_Buffer=TableViewManager.getTableBuffer(conn, tableId, userId,memberId, nhId, baselineId, "LATEST", mode);
					   //System.out.println("**"+i+"**");
					   //System.out.println("**"+Table_Buffer);
					   //System.out.println("**"+getTableBuffer[i]);
					   AssertJUnit.assertNotNull(Table_Buffer);
					   //AssertJUnit.assertEquals(Table_Buffer, getTableBuffer[i]);
					  }
					  catch(Exception e)
					  {
					   e.printStackTrace();
					  }    
	}
	  
	
	for (int i = 4; i <=7; i++) {

		getReqCol = sreqCol[i];
	//	System.out.println("              cath"+i+"         "+getReqCol);
		reqInfo2 = getReqCol.split(Seperator);

		try
		  {	
		   conn=getConnectionForTest();
		  
			int memberId = Integer.parseInt(reqInfo2[2]);  
			
			int nhId = Integer.parseInt(reqInfo2[3]);  
			int userId = Integer.parseInt(reqInfo2[0]); 
			int tableId =Integer.parseInt(reqInfo2[4]); 
			int baselineId =Integer.parseInt(reqInfo2[5]); 
			int mode =0;  
		   System.out.println(conn);
		   System.out.println(tableId);
		   System.out.println(userId);
		   System.out.println(memberId);
		   System.out.println(nhId);
		   System.out.println(baselineId);
		   System.out.println(view);
		   System.out.println(mode);
		   Table_Buffer=TableViewManager.getTableBuffer(conn, tableId, userId,memberId, nhId, baselineId, "LATEST", mode);
		   
		   AssertJUnit.assertNull(Table_Buffer);
		  }
		  catch(Exception e)
		  {
		   e.printStackTrace();
		  }    
		
}
	 }
	
	
		@Test(priority = 5)
		public void getAccessTableTest5() throws SQLException, IOException, SystemException
		{
			
			Connection conn3 =null;
			DataSet.initBufferForTest();
			initDatabaseForTest();

			
			
	  		String getReqCol;
			String[] reqInfo2 = null;
			
			String sreqCol[] = ds.getRequestBufferArr();
			
			for (int i = 0; i <=1; i++) {

					getReqCol = sreqCol[i];
					//System.out.println("              cath"+i+"         "+getReqCol);
					reqInfo2 = getReqCol.split(Seperator);

					try
			{
				
				conn3 = getConnectionForTest();
				
				int tableId = Integer.parseInt(reqInfo2[4]);//2000025;
				int userId = Integer.parseInt(reqInfo2[0]); //1001;
				
				boolean flag=false;
				int accessTableId=TableViewManager.getAccessTable(conn3, tableId, userId);
				//System.out.println("Access Table Id :"+accessTableId);
				if((accessTableId == -1) || (accessTableId == 2000000))
				{				
					flag=true;
				}
								
				AssertJUnit.assertEquals(true, flag);
									
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				conn3.close();
			}
			
			
			}
			
		}
		
		 
		
		@Test(priority = 6)
		public void getCriteriaTableTest6() throws SQLException, IOException, SystemException
		{
			initDatabaseForTest();
			Connection conn3 =null;
			int tableId = 2001282;
			int userId = 1000;
			
			try
			{
				conn3 = getConnectionForTest();
				boolean flag=false;
				int criteriaTableId = TableViewManager.getCriteriaTable(conn3, tableId, userId);
				
				if((criteriaTableId == -1) ||(criteriaTableId >2000000))
				{				
					flag=true;
				}
				
				//System.out.println("criteriaTable Id :"+criteriaTableId);
				//System.out.println("flag             :"+flag);
				
//				if(criteriaTableId > 2000000)
//				{
//					flag=true;
//				}
				//System.out.println("**************"+flag);
				AssertJUnit.assertEquals(true, flag);										
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				//conn3.close();
			}
		}
	 
	 	
}
