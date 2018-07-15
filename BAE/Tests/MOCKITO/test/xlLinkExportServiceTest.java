//package test;
//
//import static org.junit.Assert.*;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.Properties;
//
//import org.junit.Test;
//
//import com.boardwalk.database.DatabaseLoader;
//import com.boardwalk.exception.SystemException;
//import com.boardwalk.table.TableAccessList;
//import com.boardwalk.table.TableInfo;
//import com.boardwalk.table.TableManager;
//import com.boardwalk.table.TableViewManager;
//
//import java.io.*;
//import java.util.*;
//
//import javax.servlet.*;
//import javax.servlet.http.*;
//
//import com.boardwalk.table.*;
//import com.boardwalk.excel.*;
//import com.boardwalk.exception.*;
//import com.boardwalk.database.*;
//
//import java.sql.*;                  // JDBC package
//
//import javax.sql.*;                 // extended JDBC packa
//
//import com.boardwalk.member.Member;
//import com.boardwalk.user.UserManager;
////import com.sun.glass.ui.CommonDialogs.Type;
//
//public class JUnitTest {
//
//    public static String databasetype = null;
//    public static String databasename = null;
//	public static String InstanceName = null;
//	public static String user = null;
//	public static String password = null;
//	public static String server = null;
//	public static String port = null;
//    public static String jdbcConnectionString = null;
//    public static String Memberid=null;
//	public static String propFileLocation = "E:\\wksp2\\BAE_4_3\\WebContent\\props\\boardwalk.properties";
//	Properties serverproperties =null;		  
//
//	Connection connection2;
//	DatabaseLoader databaseloader2 ;
//	int userId;
//	int tableId;
//	String templateMode;
//		
//	public static void initDatabaseForTest() throws IOException
//	{		  
//		Properties serverproperties =null;		  
//		try
//		{
//			serverproperties = new Properties();
//			InputStream is = new FileInputStream(propFileLocation);
//			serverproperties.load(is);
//			databasename = serverproperties.getProperty("databasename");
//			InstanceName = serverproperties.getProperty("InstanceName");
//			user = serverproperties.getProperty("user");
//			password = serverproperties.getProperty("password");
//			server = serverproperties.getProperty("server");
//			port = serverproperties.getProperty("port");
//			databasetype = serverproperties.getProperty("databasetype");
//			Memberid=serverproperties.getProperty("memberId");
//			
//			
//					
//			
//				
//				Class.forName("net.sourceforge.jtds.jdbc.Driver");
//				jdbcConnectionString = serverproperties.getProperty("jdbcConnectionString");
//		}
//		   catch(Exception exception)
//		   {
//			   exception.printStackTrace();
//		   }
//	    }
//	
//	  public static Connection getConnectionForTest()
//		        throws SQLException
//		    {
//				Connection conn = null;
//				
//				try
//				{
//					
//					conn =  DriverManager.getConnection(jdbcConnectionString);
//		   		  	//conn.setAutoCommit(true);
//			  	}
//			  	catch(Exception sqe )
//			  	{
//						sqe.printStackTrace();
//					System.out.println("There is a Database connection problem, either the database is down or the connection parameters are wrong");
//					
//				}
//
//				 return conn;
//		    }
//	   	
//	   @Test
//	   public void checkMembershipStatus() throws SQLException, SystemException, ClassNotFoundException, IOException 
//	   {
//	 /*	
//		Junit :  buff-1001p0101722000025-10
// 		Junit :  userId :1001       int
// 		Junit :  userName :p        string
// 		Junit :  userPassword :0       string
// 		Junit :  memberId :1017       int
// 		Junit :  nhId :2              int 
// 		Junit :  tableId :2000025      int
// 		Junit :  baselineId :-1        int
// 		Junit :  view :            string
// 		Junit :  mode :0            int
// 		Junit :  connection :net.sourceforge.jtds.jdbc.ConnectionJDBC3@6fe46203
// 		Junit :  tinfo :com.boardwalk.table.TableInfo@2ed96838
//	 */		
//		initDatabaseForTest();	
//		Connection conn2 = getConnectionForTest();		
//		connection2 = conn2;
//		//databaseloader2 = new DatabaseLoader(new Properties());
//		//connection2 = databaseloader2.getConnection();
//		 
//		userId = 1001;
//		templateMode = "USER";
//		tableId = 2000004;
//		
//		String connection  = "net.sourceforge.jtds.jdbc.ConnectionJDBC3@6fe46203";		
//		//TableInfo tinfo = TableManager.getTableInfo(connection2, userId, tableId);
//		//assertEquals(tinfo, "com.boardwalk.table.TableInfo@2ed96838");
//
//		int checkMembershipStatus = UserManager.checkMembershipStatus(connection2, userId, templateMode,"", tableId);
//		System.out.println("*****************************************"+checkMembershipStatus);
//		
//		assertNotNull(checkMembershipStatus);
//		assertEquals(checkMembershipStatus, -1);
//	//	TableInfo tinfo = TableManager.getTableInfo(connection2, userId, tableId);
//	}
//	
//	@Test
//	public void tableInfoCheck() throws ClassNotFoundException, SQLException, SystemException, IOException 
//	{
//		initDatabaseForTest();
//		Connection conn3 = getConnectionForTest();		
//		connection2 = conn3;
//		//databaseloader2 = new DatabaseLoader(new Properties());
//		//connection2 = databaseloader2.getConnection();
//		
//		userId = 1001;
//		templateMode = "USER";
//		tableId = 2000004;
//		 
//		TableInfo tinfo = TableManager.getTableInfo(connection2, userId, tableId);
//		System.out.println("tinfo collaboration"+tinfo.getCollaborationName());
//		System.out.println("tinfo id"+tinfo.getCollaborationId());
//		System.out.println("tinfo   :"+tinfo+":");
//		System.out.println(tinfo.getClass().getName());
//		assertNotNull(tinfo);
//		//assertEquals(tinfo.toString().contains(""), "com.boardwalk.table.TableInfo@7fbe847c");
//		//assertEquals(tinfo.getClass().getName(), "com.boardwalk.table.TableInfo");
//	}
//	
//	@Test
//	public void authMembercheck() throws SQLException, IOException
//	{
//		String userName = "pavan1";
//		String userPassword = "0";
//		int memberId = 1001;
//		
//		initDatabaseForTest();
//		Connection conn3 = getConnectionForTest();
//		connection2 = conn3;
//
//		Member memberObj = UserManager.authenticateMember(connection2, userName,memberId);
//		System.out.println("memberObj   :"+memberObj+"   :  "+memberObj.toString());
//		
//		Member memberObj2 = UserManager.authenticateMember(connection2, "p2",memberId);
//		assertNull(memberObj2);
//	}
//	
//	@Test
//	public void getNeighbourhoodIdTest() throws SQLException, IOException, SystemException
//	{
//		String NeighbourhoodId=null;
//		try
//		{
//			serverproperties = new Properties();
//			InputStream is = new FileInputStream(propFileLocation);
//			serverproperties.load(is);
//			NeighbourhoodId=serverproperties.getProperty("nhId");
//			System.out.println(NeighbourhoodId);
//			System.out.println(Memberid);
//			Member memberObj = UserManager.authenticateMember(connection2, user,Integer.parseInt(Memberid));
//			int nhid=memberObj.getNeighborhoodId();
//			assertEquals(nhid, NeighbourhoodId);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	
//	public void TableAccessList() throws SQLException, IOException, SystemException
//	{
//		initDatabaseForTest();
//		Connection conn3 = getConnectionForTest();		
//		connection2 = conn3;
//
//		int memberId = 1001;       
//		int nhId = 1; 
//		int userId = 1001;
//		int tableId = 2000004;
//	
//		TableInfo tinfo2 = TableManager.getTableInfo(connection2, userId, tableId);
//		TableAccessList ftal = TableViewManager.getSuggestedAccess(connection2, tinfo2, userId, memberId, nhId);
//	}
//}
//
package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
// JDBC package
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;

import javax.servlet.ServletException;


import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.boardwalk.database.DatabaseLoader;
import com.boardwalk.database.TransactionManager;
import com.boardwalk.excel.xlErrorNew;
import com.boardwalk.exception.BoardwalkException;
import com.boardwalk.exception.SystemException;
import com.boardwalk.member.Member;
import com.boardwalk.table.TableAccessList;
import com.boardwalk.table.TableAccessRequest;
import com.boardwalk.table.TableInfo;
import com.boardwalk.table.TableManager;
import com.boardwalk.table.TableViewManager;
import com.boardwalk.user.UserManager;

import servlets.xlService;

//public class JUnitTest {
//	public static void main(String args[]) throws IOException, BoardwalkException, SQLException, SystemException{
//		System.out.println("inside JUnitTest");
//		BoardwalkRequestReaderTest brt = new BoardwalkRequestReaderTest();
//		xlLinkExportService le = new xlLinkExportService();
//		le.processHeader(brt.getNextContent());
//	}
//}


public class xlLinkExportServiceTest extends xlService
{
	public static String databasetype = null;
    public static String databasename = null;
	public static String InstanceName = null;
	public static String user = null;
	public static String password = null;
	public static String server = null;
	public static String port = null;
    public static String jdbcConnectionString = null;
    public static String Memberid=null;
	public static String propFileLocation = "C:\\Tomcat8\\webapps\\BAE_TEST\\testdata.properties";
	static Properties serverproperties1 =null;		  
	static DataSet ds=null;
	Connection connection2;
	DatabaseLoader databaseloader2 ;
//	int userId;
//	int tableId;
	String templateMode;
		
	public static void initDatabaseForTest() throws IOException
	{		  
		Properties serverproperties =null;		  
		try
		{
			ds=new DataSet();
			serverproperties = new Properties();
			InputStream is = new FileInputStream(propFileLocation);
			serverproperties.load(is);
			serverproperties1 =serverproperties;
			databasename = serverproperties.getProperty("databasename");
			InstanceName = serverproperties.getProperty("InstanceName");
			user = serverproperties.getProperty("user");
			password = serverproperties.getProperty("password");
			server = serverproperties.getProperty("server");
			port = serverproperties.getProperty("port");
			databasetype = serverproperties.getProperty("databasetype");
			Memberid=serverproperties.getProperty("memberId");
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			jdbcConnectionString = ds.getjdbcConnectionString();//"jdbc:jtds:sqlserver://"+server+":"+port;
				//	System.out.println("jdbc connection string "+jdbcConnectionString);			
		   }
		   catch(Exception exception)
		   {
			   exception.printStackTrace();
		   }
	    }
	
	   public static Connection getConnectionForTest() throws SQLException
	   {
		   Connection conn = null;			
		   try
		   {				
			   initDatabaseForTest();
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
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String Seperator = new Character((char)1).toString();
	public final static String ContentDelimeter = new Character((char)2).toString();
	int numColumns	= 0;
	int numRows		= 0;
	int tableId		= -1;
	int nhId		= -1;

	ArrayList	columnIds = null;
	ArrayList	rowIds = null;
	ArrayList	formulaIds = null;
	ArrayList	strValIds = null;
	String		formulaString = null;

	int userId = -1;
	String userName	= null;
	String userPassword =null;
	int	   memberId		= -1;
	//ArrayList formulaArray = new ArrayList();
	String view = null;//		= "LATEST"; // Will default to Latest.
	String query =null;//= "";

	// Error vector to all the Exceptions
	Vector xlErrorCells = null; //new Vector();
	// access variables
	boolean canAddRows = false;
	boolean canDeleteRows = false;
	boolean canAdministerColumns = false;

	Connection connection		= null;
	PreparedStatement stmt		= null;
	TransactionManager tm = null;
	int tid = -1;
	int MAX_RETRY_ATTEMPTS = 5;
	int RETRY_WAIT_TIME_MIN = 1000;
	int RETRY_WAIT_TIME_MAX = 3000;
//	public void exportTest() throws ServletException, IOException, SQLException{
//		service();
//	}
	@Test
	public void createTableTest() throws ServletException, IOException{
		String tblBuffer = new xlCollaborationServiceTest().service();
		//assertNotNull(tblBuffer);
		AssertJUnit.assertTrue(tblBuffer instanceof String);
		String[] buffer = tblBuffer.split(Seperator);
		AssertJUnit.assertEquals(2, buffer.length);		
	}
//	@Test
//	public void getRequestReaderTest() throws IOException{
//		BoardwalkRequestReaderTest reader = getRequestReader();
//	}
//	@Test
//	public void processHeaderTest() throws IOException{
//		BoardwalkRequestReaderTest reader = getRequestReader();
//		String header = reader.getNextContent();
//		assertNotNull("this is not null", header);
//		assertTrue(header.length() == 5);
//	}
//	@Test
//	public void processColumnsTest() throws IOException{
//		BoardwalkRequestReaderTest reader = getRequestReader();
//		reader.getNextContent();
//		String columns = reader.getNextContent();
//	}
//	@Test
//	public void processColumnData() throws IOException{
//		BoardwalkRequestReaderTest reader = getRequestReader();
//		reader.getNextContent();
//		reader.getNextContent();
//		String columnData = reader.getNextContent();
//	}
//	@Test
//	public void createRowsNewTable() throws SQLException{
//		userId = 1001;
//		templateMode = "USER";
//		tableId = 2000025;
//		rowIds = TableManager.createRowsNewTable(connection, tableId, 3000, userId, 2);
//	}
//	@Test
//	public void test1() throws SQLException, SystemException, ServletException, IOException{
//		System.out.println("inside the test1");
//		connection = getConnectionForTest();
//		new xlCollaborationServiceTest().service();
//	}
	@Test
	public void service()throws ServletException, IOException, SQLException
	{
		getElapsedTime();		
		// Failure String
		String failureReason = "";
		//String reqBuffer = getRequestBuffer(request).toString();
		//BoardwalkRequestReader reader = getRequestReader(request);
		//testing changes
		AssertJUnit.assertNotNull(getConnectionForTest());
		AssertJUnit.assertTrue(getConnectionForTest() instanceof Connection);
		connection = getConnectionForTest();
		AssertJUnit.assertNotNull(getRequestReader());
		AssertJUnit.assertTrue(getRequestReader() instanceof BoardwalkRequestReaderTest);
		BoardwalkRequestReaderTest reader = getRequestReader();
		  //assertTrue(reader == null);
		//assertEquals(new BoardwalkRequestReaderTest(), reader);
		//System.out.println(reqBuffer);
		//String[] fullTableArr = reqBuffer.split(ContentDelimeter);
		//System.out.println(fullTable);
		System.out.println("xlLinkExportService: Time to read the buffer = " + getElapsedTime());
//		try
//		{
//			MAX_RETRY_ATTEMPTS = Integer.parseInt(getServletConfig().getInitParameter("MAX_RETRY_ATTEMPTS"));
//			RETRY_WAIT_TIME_MIN = Integer.parseInt(getServletConfig().getInitParameter("RETRY_WAIT_TIME_MIN"));
//			RETRY_WAIT_TIME_MAX = Integer.parseInt(getServletConfig().getInitParameter("RETRY_WAIT_TIME_MAX"));
//			System.out.println("MAX_RETRY_ATTEMPTS=" + MAX_RETRY_ATTEMPTS);
//			System.out.println("RETRY_WAIT_TIME_MIN=" + RETRY_WAIT_TIME_MIN);
//			System.out.println("RETRY_WAIT_TIME_MAX=" + RETRY_WAIT_TIME_MAX);
//		}
//		catch (Exception e)
//		{
//			System.out.println("Deadlock parameters not set. Using defaults...");
//		}
		StringBuffer responseToUpdate = null; //new StringBuffer();
		String responseBuffer = null;
//		for (int ti = 0; ti < MAX_RETRY_ATTEMPTS; ti++)
//		{
			responseToUpdate = new StringBuffer ();
			responseBuffer = null;
			
			try
			{

				//processHeader(fullTableArr[0]);
//				System.out.println(reader.getNextContent());
//				System.out.println(reader.getNextContent());
//				System.out.println(reader.getNextContent());
//				System.out.println(reader.getNextContent());
				processHeader(reader.getNextContent());
				//Test the 
				
				//processColumns(fullTableArr[1]);
				processColumns(reader.getNextContent());
				// Add rows
				if (numRows > 0)
				{
					if (canAdministerColumns && canAddRows) {
					      rowIds = TableManager.createRowsNewTable(connection, tableId, tid, userId, numRows);
					      AssertJUnit.assertNotNull(rowIds);
					      AssertJUnit.assertTrue(rowIds instanceof ArrayList);
					      AssertJUnit.assertEquals(numRows, rowIds.size());
					     }
					     System.out.println("xlLinkExportService: Time to create rows = " + getElapsedTime());
					     
					/* PWC_ZZZ_1_29

					// Add Cells
					query =
						" INSERT INTO BW_CELL (BW_ROW_ID, BW_COLUMN_ID, CELL_TYPE, TX_ID) " +
						" SELECT BW_ROW.ID, BW_COLUMN.ID, 'STRING', ? " +
						" FROM BW_ROW, BW_COLUMN " +
						" WHERE " +
						" BW_ROW.TX_ID = ? " +
						" AND BW_COLUMN.TX_ID = ? ";
					stmt = connection.prepareStatement(query);
					stmt.setInt(1, tid);
					stmt.setInt(2, tid);
					stmt.setInt(3, tid);
					stmt.executeUpdate();
					stmt.close();
					stmt = null;
					System.out.println("xlLinkExportService: Time to insert into bw_cell table = " + getElapsedTime());

					// Create new bw cell status records
					String q2 = "INSERT INTO BW_CELL_STATUS " +
							   "  SELECT BW_CELL.ID, 1, BW_CELL.TX_ID " +
							   "  FROM BW_CELL " +
							   "  WHERE BW_CELL.TX_ID = ?";
					stmt = connection.prepareStatement(q2);
					stmt.setInt(1, tid);
					stmt.executeUpdate();
					stmt.close();
					stmt = null;
					System.out.println("xlLinkExportService: Time to create bw cell status records= " + getElapsedTime());
					*/
					// Insert into BW_RC_STRING_VALUE
					for (int i = 0; i < numColumns * 2; i = i + 2)
					{
						int columnIdx = i / 2;
						//System.out.println("Processing column num = " + columnIdx);
						//processColumnData(fullTableArr[i + 2], fullTableArr[i + 3], columnIdx);
						String cellBuff = reader.getNextContent();
						String fmlaBuff = reader.getNextContent();
						processColumnData(cellBuff, fmlaBuff, columnIdx);
						cellBuff = null;
						fmlaBuff = null;
					}
					System.out.println("xlLinkExportService: Time to insert into rcsv table = " + getElapsedTime());


					System.out.println("xlLinkExportService: xlErrorCells.size() " + xlErrorCells.size());
					if (xlErrorCells.size() > 0)
					{
						throw new BoardwalkException(12011);
					}

					query = "{CALL BW_UPD_CELL_FROM_RCSV_LINK_EXPORT(?,?,?)}";
					CallableStatement cstmt = connection.prepareCall(query);
					cstmt.setInt(1, tid);
					cstmt.setInt(2, tableId);
					cstmt.setInt(3, userId);
					int updCount = cstmt.executeUpdate();
					cstmt.close();
					cstmt = null;
					System.out.println("xlLinkExportService: Time to execute BW_UPD_CELL_FROM_RCSV_LINK_EXPORT = " + getElapsedTime());
				}
				// commit the transaction
				tm.commitTransaction();
				tm = null;
				//tm.rollbackTransaction(); // FOR NOW

				// create the response
				responseToUpdate.append("Success" + Seperator);
				responseToUpdate.append(numColumns + Seperator);
				responseToUpdate.append(numRows + Seperator);
				responseToUpdate.append(tid + ContentDelimeter);

				responseToUpdate.append(tableId + ContentDelimeter + memberId + ContentDelimeter);

				int ri = 0;
				int ci = 0;

				for (ri = 0; ri < numRows - 1; ri++)
				{
					responseToUpdate.append(rowIds.get(ri) + Seperator);
				}

				if (numRows > 0)
					responseToUpdate.append(rowIds.get(ri) + ContentDelimeter);//last rowid
				else
					responseToUpdate.append(ContentDelimeter);//last rowid

				for (ci = 0; ci < numColumns - 1; ci++)
				{
					responseToUpdate.append(columnIds.get(ci) + Seperator);
				}

				responseToUpdate.append(columnIds.get(ci) + ContentDelimeter);//last columnid

				responseToUpdate.append(formulaString + ContentDelimeter);

				//ti = MAX_RETRY_ATTEMPTS; // dont try again

				failureReason = "";

			}
			catch (SQLException sqe)
			{

				sqe.printStackTrace();
				try
				{
					if (tm != null)
						tm.rollbackTransaction();
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
				if (sqe.getErrorCode() == 1205)
				{
//					if (ti == MAX_RETRY_ATTEMPTS - 1)
//					{
//						failureReason = (new xlErrorNew(tableId, 0, 0, 13001)).buildTokenString();
//						System.out.println("xlLinkExportService: Deadlock maximum attempts exhausted. Sending server busy message to client ");
//
//						System.out.println("xlLinkExportService:failureReason = " + failureReason);
//					}
					//System.out.println("xlLinkExportService:Deadlock attempt number = " + (ti + 1) + " out of max = " + MAX_RETRY_ATTEMPTS);
					//sqe.printStackTrace();
					try
					{
						int sleepTime = RETRY_WAIT_TIME_MIN + (new Random()).nextInt(RETRY_WAIT_TIME_MAX - RETRY_WAIT_TIME_MIN);
						System.out.println("Sleeping for " + sleepTime + "ms");
						Thread.sleep(sleepTime);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					failureReason = sqe.getMessage();
					//ti = MAX_RETRY_ATTEMPTS; // dont try again
				}

			}
			catch (BoardwalkException bwe)
			{
				//ti = MAX_RETRY_ATTEMPTS; // dont try again
				bwe.printStackTrace();
				if (xlErrorCells.size() > 0)
				{
					StringBuffer errorBuffer = new StringBuffer();

					for (int errorIndex = 0; errorIndex < xlErrorCells.size(); errorIndex++)
					{
						xlErrorNew excelError = (xlErrorNew)(xlErrorCells.elementAt(errorIndex));
						errorBuffer.append(excelError.buildTokenString());
					}
					errorBuffer.append(Seperator);
					failureReason = errorBuffer.toString();
					
					try
					{
						if (tm != null)
							tm.rollbackTransaction();
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				}
			}
			catch (Exception e)
			{
				//ti = MAX_RETRY_ATTEMPTS; // dont try again
				e.printStackTrace();
				try
				{
					if (tm != null)
						tm.rollbackTransaction();
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
				failureReason = e.getMessage();
			}
			finally
			{
				// close the connection
				try
				{
					//reader.close();
					connection.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				// clean up
				numColumns = 0;
				numRows = 0;
				tableId = -1;
				nhId = -1;

				columnIds = null;
				rowIds = null;
				formulaIds = null;
				strValIds = null;
				formulaString = null;

				userId = -1;
				userName = "";
				userPassword = "";
				memberId = -1;
				//formulaArray = null;
				view = null;
				query = "";
				xlErrorCells = null;
				
				canAddRows = false;
				canDeleteRows = false;
				canAdministerColumns = false;

				connection = null;
				stmt = null;
				tm = null;
				tid = -1;
			}
		//}
		//Testing changes
		System.out.println("response to update is->");
		System.out.println(responseToUpdate.toString());
		
		// The response
//		if (failureReason.length() == 0)
//		{
//			responseBuffer = responseToUpdate.toString();
//			commitResponseBuffer(responseBuffer, response);
//			System.out.println("xlLinkExportService: Success : Time to prepare response = " + getElapsedTime());
//		}
//		else
//		{
//			failureReason = "FAILURE" + ContentDelimeter + failureReason;
//			commitResponseBuffer(failureReason, response);
//
//			System.out.println("xlLinkExportService: Failure : Time to prepare response = " + getElapsedTime());
//			System.out.println("xlLinkExportService: failureReason = " + failureReason);
//		}
	}

	public void processColumnData(String cellData, String formulaData, int columnIdx) throws SQLException 
	{
		String[] cellArr = cellData.split(Seperator);
		String[] formulaArr = formulaData.split(Seperator);
		int columnId = ((Integer)columnIds.get(columnIdx)).intValue();
		boolean emptyColumn = false;
		//System.out.println("cellArr.length = " + cellArr.length);
		if (cellArr.length == 0) // empty column
		{
			emptyColumn = true;
			//System.out.println("Column is empty");
		}
		boolean emptyFormulae = false;
		if (formulaArr.length == 0) // empty column
		{
			emptyFormulae = true;
			//System.out.println("Formulae is empty");
		}

		// insert into bw_rc_string_value 
		query = 
			" INSERT INTO BW_RC_STRING_VALUE " + 
			" (BW_ROW_ID, BW_COLUMN_ID, STRING_VALUE, FORMULA, TX_ID, CHANGE_FLAG) " +
			" VALUES " +
			" (?, ?, ?, ?, ?, ?) ";

		stmt = connection.prepareStatement(query);
		for (int i = 0; i < numRows; i++)
		{
			int rowId = ((Integer)rowIds.get(i)).intValue ();
			String cellValue = "";
			String formula = null;
			if (emptyColumn == false)
			{
				try
				{
					cellValue = cellArr[i];
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					cellValue = "";
				}
			}
			if (emptyFormulae == false)
			{
				try
				{
					formula = formulaArr[i];
					if (formula.indexOf("=") < 0)
					{
						formula = null;
					}
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					formula = null;
				}
			}
			//System.out.println("INSERT INTO BW_RC_STRING_VALUE rowId = " + rowId + " columnId = " + columnId + " cellValue = " + cellValue + " formula = " + formula);

			stmt.setInt(1, rowId);
			stmt.setInt(2, columnId);
			stmt.setString(3, cellValue);
			stmt.setString(4, formula);
			stmt.setInt(5, tid);
			stmt.setInt(6, 12);
			stmt.addBatch();
		}
		int[] rescnt = stmt.executeBatch();
		stmt.close();
		stmt = null;

	}

	public void processColumns(String columnInfo) throws BoardwalkException, SQLException
	{
		// If the user has access to add new Columns then procede forward
		if(canAdministerColumns)
		{
			columnIds = new ArrayList(numColumns);
			String[] columnNames = columnInfo.split(Seperator);
			Vector columns = new Vector();
			query = " INSERT INTO BW_COLUMN " +
					   " (NAME, BW_TBL_ID, COLUMN_TYPE, SEQUENCE_NUMBER, TX_ID) " +
					   " VALUES " +
					   " (?,?,?,?,?)";

			stmt = connection.prepareStatement(query);
			// Add columns
			for (int cni = 0; cni < numColumns; cni++)
			{
				//System.out.println("Adding column : " + columnNames[cni]);
				stmt.setString(1, columnNames[cni]);
				stmt.setInt(2, tableId);
				stmt.setString(3, "STRING");
				stmt.setFloat(4, cni + 1);
				stmt.setInt(5, tid);
				stmt.addBatch();
			}
			int[] rescnt = stmt.executeBatch();
			stmt.clearBatch();
			stmt.close();
			stmt = null;
		}

		//HashMap columnHash = new HashMap();
		ResultSet resultset = null;
		query = "select id from bw_column where tx_id = ? order by sequence_number";
		stmt = connection.prepareStatement(query);
		stmt.setInt(1, tid);
		resultset = stmt.executeQuery();
		while (resultset.next())
		{
			int columnId = resultset.getInt(1);
			//int columnIdx = resultset.getFloat (2);
			columnIds.add (new Integer(columnId));
			//columnHash.put (new Integer(columnIdx), new Integer(columnId));
			//System.out.println("columnid = " + columnId);
		}
		stmt.close();
		stmt = null;
		resultset.close();
		resultset = null;
	}

	public void processHeader(String header) throws BoardwalkException, SQLException, SystemException, IOException
	{

		//System.out.println("header = " + header);

		String[] headerInfo = header.split(Seperator);
		System.out.println(header);
		Properties props=new Properties();
		InputStream is = new FileInputStream("D://Heat//BAE_4_3//Export.properties");
		props.load(is);
		//Modified by Tekvision on 20180207 for Clear Text Password(Issue Id: 14241) - START
		userId				= 1001;//Integer.parseInt(headerInfo[0]);
		userName			= headerInfo[1];
		//userPassword        = headerInfo[2];
		memberId			= Integer.parseInt(headerInfo[2]);
		tableId				= Integer.parseInt(props.getProperty("LETableId"));//Integer.parseInt (headerInfo[4]);
		nhId				= Integer.parseInt(headerInfo[4]);
		numColumns			= Integer.parseInt(headerInfo[5]);
		numRows				= Integer.parseInt(headerInfo[6]);
		view = "LATEST";
		xlErrorCells = new Vector();
		//Modified by Tekvision on 20180207 for Clear Text Password(Issue Id: 14241) - END
		System.out.println(userId+"  "+userName+"  "+memberId+"  "+tableId+"  "+nhId+"  "+numColumns+"  "+numRows);
		// Start a connection
		// Test changes
//		DatabaseLoader databaseloader = new DatabaseLoader(serverproperties1);
//		connection = databaseloader.getConnection();
//		initDatabaseForTest();
//		connection = getConnectionForTest();
		//	Access control checks
		TableInfo tinfo = TableManager.getTableInfo(connection, userId, tableId);
		System.out.println(headerInfo);
		TableAccessList ftal = TableViewManager.getSuggestedAccess(connection, tinfo, userId, memberId, nhId);
		//if (view == null ||  view.trim().equals(""))
		//{
		//    view = ftal.getSuggestedViewPreferenceBasedOnAccess();
		//    System.out.println("Suggested view pref = " + view);
		//    if(view == null)
		//        view = "None";
		//}
		// Check access control :: TBD
		int raccess = 1;
		int ACLFromDB = ftal.getACL();
		AssertJUnit.assertNotNull(new TableAccessRequest(tableId, "LATEST", true));
		TableAccessRequest wAccess = new TableAccessRequest(tableId, "LATEST", true);
		int wACL = wAccess.getACL();
		int awACL = wACL & ACLFromDB;

		canAddRows				= ftal.canAddRow();
		canDeleteRows			= ftal.canDeleteRow();
		canAdministerColumns	= ftal.canAdministerColumn();

		// authenticate the user
		Member memberObj = UserManager.authenticateMember(connection, userName, memberId); //Modified by Tekvision on 20180207 for Clear Text Password(Issue Id: 14241)
		if (memberObj == null)
		{
			//System.out.println("Authentication failed for user : " + userName);
			xlErrorCells.add( new xlErrorNew( tableId, 0, 0, 11004));
			throw new BoardwalkException(11004);
		}
		else
		{
			//System.out.println("Authentication succeeded for user : " + userName);
			nhId = memberObj.getNeighborhoodId();
			tm = new TransactionManager(connection, userId);
			tid = tm.startTransaction("Link export new table", "Link export new table");
		}

		System.out.println("Time to authenticate user = " + getElapsedTime());

		if(canAdministerColumns == false)
		{
			// User does not have access to add columns
			xlErrorCells.add( new xlErrorNew( tableId,0,0,12010));
			System.out.println("No access to add column");
		}

		if(canAddRows == false)
		{
			xlErrorCells.add( new xlErrorNew( tableId,0,0,12012));
			System.out.println("No access to add rows");
		}
		//System.out.println("view = " + view);
		if(view.equals("None"))
		{
			xlErrorCells.add( new xlErrorNew(tableId, 0, 0, 10005));
		}
	}
//	@Test
//	public void testThis(){
//		System.out.println("testing the function");
//		assertTrue(true);
//	}
}

