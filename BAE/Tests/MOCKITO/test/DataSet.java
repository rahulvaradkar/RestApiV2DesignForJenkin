package test;

/**
*
* @author  Jaydeep Bobade & Pranav Bhagwat
*/
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
// JDBC package
import java.sql.Connection;
import java.util.Properties;

import com.boardwalk.database.DatabaseLoader;

public class DataSet {

	public final static String Seperator = new Character((char)1).toString();
    public static String databasetype = null;
    public static String databasename = null;
	public static String InstanceName = null;
	public static String user = null;
	public static String password = null;
	public static String server = null;
	public static String port = null;
    public static String jdbcConnectionString = null;
	public static String instance_name = "BAE_4_4";
	public static String testdata_location = "C:\\Tomcat8\\webapps\\"+instance_name+"\\testdata.properties";
	public static String testdata3_location = "C:\\Tomcat8\\webapps\\"+instance_name+"\\testdata3.properties";
	public static String exportdata_location = "C:\\Tomcat8\\webapps\\"+instance_name+"\\Export.properties";
	
	public static String CollabName=null;
	public static String WhiteboardName=null;
	public static String sqlPath=null;
	
	public static String requestbuffer0 = null;
	public static String requestbuffer1 = null;
	public static String requestbuffer2 = null;
	public static String requestbuffer3 = null;
	public static String requestbuffer4 = null;
	public static String requestbuffer5 = null;
	public static String requestbuffer6 = null;
	public static String requestbuffer7 = null;
	
	public static String getCollaborationId0=null;
	public static String getCollaborationName0=null;
	public static String getCollaborationPurpose0=null;
	public static String getLockTime0=null;
	public static String getTableId0=null;
	public static String getTableName0=null;
	public static String getWhiteboardId0=null;
	public static String getWhiteboardName0=null;
	public static String lockTid0=null;

	public static String getCollaborationId1=null;
	public static String getCollaborationName1=null;
	public static String getCollaborationPurpose1=null;
	public static String getLockTime1=null;
	public static String getTableId1=null;
	public static String getTableName1=null;
	public static String getWhiteboardId1=null;
	public static String getWhiteboardName1=null;
	public static String lockTid1=null;

	public static String getCollaborationId2=null;
	public static String getCollaborationName2=null;
	public static String getCollaborationPurpose2=null;
	public static String getLockTime2=null;
	public static String getTableId2=null;
	public static String getTableName2=null;
	public static String getWhiteboardId2=null;
	public static String getWhiteboardName2=null;
	public static String lockTid2=null;

	public static String getCollaborationId3=null;
	public static String getCollaborationName3=null;
	public static String getCollaborationPurpose3=null;
	public static String getLockTime3=null;
	public static String getTableId3=null;
	public static String getTableName3=null;
	public static String getWhiteboardId3=null;
	public static String getWhiteboardName3=null;
	public static String lockTid3=null;
	
	public static String templatemode = null;
	public static String Memberid=null;
	public static String UserName=null;
	public static String userPassword=null;
	public static String NHID=null;
	public static String baseLineId;
	public static String mode=null;
	public static String UserID=null;
	public static int tableid;
	public static String nhName=null;
	
	public static String view=null;
	static Properties properties=null;
	Connection connection2;
	DatabaseLoader databaseloader2 ;
	static int userId;
	static int nhId;
	static String tableId;
	static int baselineId;	
	static String templateMode;
	static int memberid;
	static int Mode;
	
	public static String NhId0;
	public static String NhId1;
	public static String NhId2;
	public static String NhId3;
	
	public static String UId0;
	public static String UId1;
	public static String UId2;
	public static String UId3;
	
	public static String memberId0;
	public static String memberId1;
	public static String memberId2;
	public static String memberId3;
	
	public static String NhName0=null;
	public static String NhName1=null;
	public static String NhName2=null;
	public static String NhName3=null;
	
	public static String tno;
	public static int ImportId;
	public static int ExportId;
	public static int Columns;
	public static int Rows;
	public DataSet() throws IOException
	  {
		initDatabaseForTest();
		//initBufferForTest();
	  }
	  
	   public static void initDatabaseForTest() throws IOException
	   {		   		   
		   Properties serverproperties2 =null;
		   Properties serverproperties3=null;
		   try
		   {
			   	serverproperties3=new Properties();
			   	InputStream is3 = new FileInputStream(exportdata_location);
				serverproperties3.load(is3);				
				tno=serverproperties3.getProperty("tno");
				
				serverproperties2 = new Properties();
				InputStream is2 = new FileInputStream(testdata_location);
				serverproperties2.load(is2);
				new DatabaseLoader(serverproperties2);
				
			    databasename = serverproperties2.getProperty("databasename");
				InstanceName = serverproperties2.getProperty("InstanceName");
				user = serverproperties2.getProperty("user");
				password = serverproperties2.getProperty("password");
				server = serverproperties2.getProperty("server");
				port = serverproperties2.getProperty("port");
				databasetype = serverproperties2.getProperty("databasetype");				
				sqlPath=serverproperties2.getProperty("sqlpath");
				
				CollabName=serverproperties2.getProperty("CollabName");
				WhiteboardName=serverproperties2.getProperty("WhiteboardName");
				
				UserName=serverproperties2.getProperty("userName");
				userPassword=serverproperties2.getProperty("userPassword");
				
				tableId=serverproperties2.getProperty("tableId");				
				tableid=Integer.parseInt(tableId);				
				
				Memberid=serverproperties2.getProperty("memberId");
				memberid=Integer.parseInt(Memberid);
				
				UserID=serverproperties2.getProperty("userId");
				userId=Integer.parseInt(UserID);
				
				NHID=serverproperties2.getProperty("nhId");
				nhId=Integer.parseInt(NHID);
				
				baseLineId=serverproperties2.getProperty("baselineId");
				baselineId=Integer.parseInt(baseLineId);
				
				nhName=serverproperties2.getProperty("nhName");
				view=serverproperties2.getProperty("view");
				mode=serverproperties2.getProperty("mode");
				Mode=Integer.parseInt(mode);
				templateMode=serverproperties2.getProperty("TemplateMode");
				jdbcConnectionString=serverproperties2.getProperty("jdbcConnectionString");	
				
				ImportId=Integer.parseInt(serverproperties2.getProperty("ImportId"));
				ExportId=Integer.parseInt(serverproperties2.getProperty("ExportId"));
				Columns=Integer.parseInt(serverproperties2.getProperty("Columns"));
				Rows=Integer.parseInt(serverproperties2.getProperty("Rows"));
		   }
		   catch(Exception exception)
		   {
			   exception.printStackTrace();
		   }
	 }
//	 
//	   public static void initBufferForTest() throws IOException
//	   {		   
//		   Properties serverproperties =null; 			  
//		   try
//		   {
//				serverproperties = new Properties();
//				InputStream is = new FileInputStream("D:\\Heat\\BAE_4_3\\Dataset.properties");
//				serverproperties.load(is);
//				
//				requestbuffer0 = serverproperties.getProperty("requestbuffer0");
//				requestbuffer1 = serverproperties.getProperty("requestbuffer1");
//				requestbuffer2 = serverproperties.getProperty("requestbuffer2");
//				requestbuffer3 = serverproperties.getProperty("requestbuffer3");
//				requestbuffer4 = serverproperties.getProperty("requestbuffer4");
//				requestbuffer5 = serverproperties.getProperty("requestbuffer5");
//				requestbuffer6 = serverproperties.getProperty("requestbuffer6");
//				requestbuffer7 = serverproperties.getProperty("requestbuffer7");
//				
//				getCollaborationId0=serverproperties.getProperty("getCollaborationId0");
//				getCollaborationName0=serverproperties.getProperty("getCollaborationName0");
//				getCollaborationPurpose0=serverproperties.getProperty("getCollaborationPurpose0");
//				getLockTime0=serverproperties.getProperty("getLockTime0");
//				getTableId0=serverproperties.getProperty("getTableId0");
//				getTableName0=serverproperties.getProperty("getTableName0");
//				getWhiteboardId0=serverproperties.getProperty("getWhiteboardId0");
//				getWhiteboardName0=serverproperties.getProperty("getWhiteboardName0");
//				lockTid0=serverproperties.getProperty("lockTid0");
//				
//				getCollaborationId1=serverproperties.getProperty("getCollaborationId1");
//				getCollaborationName1=serverproperties.getProperty("getCollaborationName1");
//				getCollaborationPurpose1=serverproperties.getProperty("getCollaborationPurpose1");
//				getLockTime1=serverproperties.getProperty("getLockTime1");
//				getTableId1=serverproperties.getProperty("getTableId1");
//				getTableName1=serverproperties.getProperty("getTableName1");
//				getWhiteboardId1=serverproperties.getProperty("getWhiteboardId1");
//				getWhiteboardName1=serverproperties.getProperty("getWhiteboardName1");
//				lockTid1=serverproperties.getProperty("lockTid1");
//				
//				getCollaborationId2=serverproperties.getProperty("getCollaborationId2");
//				getCollaborationName2=serverproperties.getProperty("getCollaborationName2");
//				getCollaborationPurpose2=serverproperties.getProperty("getCollaborationPurpose2");
//				getLockTime2=serverproperties.getProperty("getLockTime2");
//				getTableId2=serverproperties.getProperty("getTableId2");
//				getTableName2=serverproperties.getProperty("getTableName2");
//				getWhiteboardId2=serverproperties.getProperty("getWhiteboardId2");
//				getWhiteboardName2=serverproperties.getProperty("getWhiteboardName2");
//				lockTid2=serverproperties.getProperty("lockTid2");
//				
//				getCollaborationId3=serverproperties.getProperty("getCollaborationId3");
//				getCollaborationName3=serverproperties.getProperty("getCollaborationName3");
//				getCollaborationPurpose3=serverproperties.getProperty("getCollaborationPurpose3");
//				getLockTime3=serverproperties.getProperty("getLockTime3");
//				getTableId3=serverproperties.getProperty("getTableId3");
//				getTableName3=serverproperties.getProperty("getTableName3");
//				getWhiteboardId3=serverproperties.getProperty("getWhiteboardId3");
//				getWhiteboardName3=serverproperties.getProperty("getWhiteboardName3");
//				lockTid3=serverproperties.getProperty("lockTid3");
//
//				NhId0=(serverproperties.getProperty("getNhId0"));
//				NhName0=serverproperties.getProperty("getNhName0");
//				UId0=(serverproperties.getProperty("getUId0"));
//				memberId0=(serverproperties.getProperty("getId0"));	
//					
//				NhId1=(serverproperties.getProperty("getNhId1"));
//				NhName1=serverproperties.getProperty("getNhName1");
//				UId1=(serverproperties.getProperty("getUId1"));
//				memberId1=(serverproperties.getProperty("getId1"));
//				
//				NhId2=(serverproperties.getProperty("getNhId2"));
//				NhName2=serverproperties.getProperty("getNhName2");
//				UId2=(serverproperties.getProperty("getUId2"));
//				memberId2=(serverproperties.getProperty("getId2"));
//				
//				NhId3=(serverproperties.getProperty("getNhId3"));
//				NhName3=serverproperties.getProperty("getNhName3");
//				UId3=(serverproperties.getProperty("getUId3"));
//				memberId3=(serverproperties.getProperty("getId3"));							
//		   }
//		   catch(Exception exception)
//		   {
//			   exception.printStackTrace();
//		   }
//	  }
//	 

	public String getTestDataLocation()
	{
		return testdata_location;
		
	}
	
	public String getInstanceName()
	{
		return instance_name;
		
	}
	
	public String getTestData3Location()
	{
		return testdata3_location;
		
	}
	
	public String getExportDataLocation()
	{
		return exportdata_location;
		
	}

	   public String getDatabaseName()
	   {
		   return databasename;
	   }
	   
	 
	   
	   public String  getDbUserName()
	   {
		   return user;
	   }
	   
	   public String getDbUserPassword()
	   {
		   return password;
	   }
	   
	   public String getDbServer()
	   {
		   return server;
	   }
	   
	   public String getDbPort()
	   {
		   return port;
	   }
	   
	   public String getDbType()
	   {
		   return databasetype;
	   }
	   
	   public String getSqlPath()
	   {
		   return sqlPath;
	   }
	   public String getTno()
	   {
		   return tno;
	   }
	   public String getjdbcConnectionString()
	   {
		   return jdbcConnectionString;
	   }
	   
	   public String getCollabName()
	   {
		   return CollabName;
	   }
	   
	   public String getWbName()
	   {
		   return WhiteboardName;
	   }
	   
	   public int getUserId()
	   {
		   return userId;
	   }
	   
	   public String getUsername()
	   {
		   return UserName;
	   }
	   
	   public String getUserPassword()
	   {
		   return userPassword;
	   }
	   	   
	   public  int getMemberID()
	   {
		   return memberid;
	   }
	   
	   public int getTableID()
	   {
		   return tableid;
	   }
	   
	   public int getNHID()
	   {
		   return nhId;
	   }
	   
	   public String getNhName()
	   {
		   return nhName;
	   }
	   public int getBaselineId()
	   {
		   return baselineId;
	   }
	   
	   public String getView()
	   {
		   return view;
	   }
	   
	   public int getMode()
	   {
		   return Mode;
	   }
	   
	   public String getTemplateMode()
	   {
		   return templatemode;
	   }
	   
	   public int getImportId()
	   {
		   return ImportId;
	   }
	   
	   public int getExportId()
	   {
		   return ExportId;
	   }
	   
	   public int getColumns()
	   {
		   return Columns;
	   }
	   
	   public int getRows()
	   {
		   return Rows;
	   }
	   public String[] getNhIdArr()
	   {
		   String[] NHID_= {NhId0,NhId1,NhId2,NhId3};
		   return NHID_;
	   }
	   
	   public String[] getNhNameArr()
	   {
		   String[] NHNAME= {NhName0,NhName1,NhName2,NhName3};
		   return NHNAME;
	   }
	   
	   public String[] getUserIdArr()
	   {
		   String[] USERID= {UId0,UId1,UId2,UId3};
		   return USERID;
	   }
	   
	   public String[] getMemberIdArr()
	   {
		   String[] MEMBERID= {memberId0,memberId1,memberId2,memberId3};
		   return MEMBERID;
	   }
	 
	   public String[] getRequestBufferArr()
	   {
		   String[] reqBuffer= {requestbuffer0,requestbuffer1,requestbuffer3,requestbuffer4,requestbuffer5,requestbuffer6,requestbuffer7};
		   return reqBuffer;
	   }
	   
	   public String[] getCollaborationIdArr()
	   {
		   String[] getCollab= {getCollaborationId0,getCollaborationId1,getCollaborationId2,getCollaborationId3};
		   return getCollab;
	   }
	  
	   public String[] getCollaborationNameArr()
	   {
		   String[] getCollabName= {getCollaborationName0,getCollaborationName1,getCollaborationName2,getCollaborationName3};
		   return getCollabName;
	   }
	   
	   public String[] getCollaborationPurposeArr()
	   {
		   String[] getCollabPurpose= {getCollaborationPurpose0,getCollaborationPurpose1,getCollaborationPurpose2,getCollaborationPurpose3};
		   return getCollabPurpose;
	   }
	   
	   public String[] getLockTimeArr()
	   {
		   String[] getLcktime= {getLockTime0,getLockTime1,getLockTime2,getLockTime3};
		   return getLcktime;
	   }
	   
	   public String[] getTableIdArr()
	   {
		   String[] gettableid= {getTableId0,getTableId1,getTableId2,getTableId3};
		   return gettableid;
	   }
	   
	   public String[] getTableNameArr()
	   {
		   String[] gettablename= {getTableName0,getTableName1,getTableName2,getTableName3};
		   return gettablename;
	   }
	   
	   public String[] getWhiteboardIdArr()
	   {
		   String[] getWbBoardId= {getWhiteboardId0,getWhiteboardId1,getWhiteboardId2,getWhiteboardId3};
		   return getWbBoardId;
	   }
	   
	   public String[] getWhiteboardNameArr()
	   {
		   String[] getWbBoardName= {getWhiteboardName0,getWhiteboardName1,getWhiteboardName2,getWhiteboardName3};
		   return getWbBoardName;
	   }
	   
	   public String[] lockTidArr()
	   {
		   String[] getLockTid= {lockTid0,lockTid1,lockTid2,lockTid3};
		   return getLockTid;
	   }
}
	   
