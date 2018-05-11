package boardwalk.rest;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.SecurityContext;

import com.boardwalk.database.DatabaseLoader;
import com.boardwalk.database.TransactionManager;
import com.boardwalk.excel.xlErrorNew;
import com.boardwalk.exception.BoardwalkException;
import com.boardwalk.exception.SystemException;
import com.boardwalk.member.MemberNode;
import com.boardwalk.neighborhood.NeighborhoodManager;
import com.boardwalk.table.NewTableAccessList;
import com.boardwalk.table.TableAccessList;
import com.boardwalk.table.TableAccessRequest;
import com.boardwalk.table.TableInfo;
import com.boardwalk.table.TableManager;
import com.boardwalk.table.TableViewManager;

import boardwalk.collaboration.BoardwalkCollaborationManager;
import boardwalk.collaboration.BoardwalkCollaborationNode;
import boardwalk.collaboration.BoardwalkWhiteboardNode;
import boardwalk.connection.BoardwalkConnection;
import boardwalk.connection.BoardwalkConnectionManager;
import io.swagger.api.NotFoundException;
import io.swagger.model.Cell;
import io.swagger.model.CellBuffer;
import io.swagger.model.Column;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.Grid;
import io.swagger.model.GridInfo;
import io.swagger.model.Row;
import io.swagger.model.SequencedCellArray;
import io.swagger.model.Whiteboard;

public class GridManagement {

    private static String CALL_BW_GET_ALL_MEMBERSHIPS_INFO = "{CALL BW_GET_ALL_MEMBERSHIPS_INFO}";

	public GridManagement()
	{	
	}

    //@PUT
    //@Path("/{tableId}")
	public static CellBuffer gridPut(int gridId, CellBuffer  cellBufferRequest, ArrayList<ErrorRequestObject> ErrResps)
	{
		CellBuffer cbfReturn = new CellBuffer();
		ErrorRequestObject erb;
        // get the connection
    	Connection connection = null;
    	try
    	{
    		// Start a connection
    		DatabaseLoader databaseloader = new DatabaseLoader(new Properties());
    		connection = databaseloader.getConnection();
    		// Get an authenticated boardwalk connection
    		BoardwalkConnection bwcon = null;
    		try
    		{
    			String loginName = "admin";
    			String loginPwd = "0";
    		    bwcon = BoardwalkConnectionManager.getBoardwalkConnection(connection, loginName, loginPwd, -1);
    		    System.out.println("Successfully obtained authenticated Boardwalk connection");
    		}
    		catch(BoardwalkException bwe)
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("Authentication_Connection_Failure");
    			erb.setPath("GridManagement.gridPut::getBoradwalkConnection");
    			erb.setProposedSolution("Authentication/Connection Failed. Contact Boardwalk System Administrator");
    			ErrResps.add(erb);
    		    System.out.println("Authentication/Connection Failed");
    		    return cbfReturn;
    		}
    		//Custom Code Starts
    		//reading cellBufferRequest
    		
    		//GridInfo gi = cellBufferRequest.getInfo();
    		ArrayList<Integer> rowArr =  (ArrayList<Integer>) cellBufferRequest.getRowArray();
    		ArrayList<Integer> colArr =  (ArrayList<Integer>) cellBufferRequest.getColumnArray(); 
    		ArrayList<Cell> cellArr = (ArrayList<Cell>) cellBufferRequest.getCells();
    		
    		ArrayList<Row> gridRows = (ArrayList<Row>) cellBufferRequest.getRows();
    		ArrayList<Column> gridColumns = (ArrayList<Column>) cellBufferRequest.getColumns();
    		ArrayList<SequencedCellArray> colCellArr = (ArrayList<SequencedCellArray>) cellBufferRequest.getColumnCellArrays();
    		
    		
    		
    		//cellBufferRequest.getGridChangeBuffer()   -------- need to be decided
    		GridInfo ginfo = cellBufferRequest.getInfo();
    		
    		int numColumns = gridColumns.size();
    		int numRows = gridRows.size();
    		
    		System.out.println("GRID/PUT REST-API CALL :  numColumns : " + numColumns);
    		System.out.println("GRID/PUT REST-API CALL :  numRows : " + numRows);
    		Column cl;

    		
 /*    		SequencedCellArray sca;
    		sca = colCellArr.get(0);
    		int seqColId = sca.getColumnId(); 		// colSequence is added in Swaggger.
    		ArrayList<String> seqColValues = (ArrayList<String>) sca.getCellValues();
    		sca.setColumnId(columnId);
*/    		
//    		Cell cl;
//    		cl.getAccess(); cl.getChangeFlag(); cl.getColId(); cl.getColSequence(); cl.getFormula(); cl.getId(); cl.getRowId(); cl.getRowSequence(); cl.getTid(); cl.getValue();
    		
/*    		gridColumns
    		Column cl = gridColumns.get(1 );
    		cl.getId();
    		cl.getName()
    		cl.getPreviousColumnid()
    		cl.getPreviousColumnSequence()
    		cl.getSeqNo()
    		cl.getTid()
*/
    	//	gridRows
/*    		Row  rw = gridRows.get(1);
    		rw.getId();
    		rw.getPreviousRowid();
    		rw.getPreviousRowSequence();
    		rw.getSeqNo();
    		rw.getTid();
    		
    		rw.setActive(active);
    		rw.setId(id);
    		rw.setPreviousRowid(previousRowid);
    		rw.setPreviousRowSequence(previousRowSequence);
    		rw.setSeqNo(seqNo);
    		rw.setTid(tid);
  */  		
    		//GridInfo
    /*		gi.getAsOfTid()
    		gi.getBaselineId()
    		gi.getExportTid()
    		gi.getFilter()
    		gi.getId()
    		gi.getImportTid()
    		gi.getName()
    		gi.getNeighborhoodHeirarchy()
    		gi.getServerURL()
    		gi.getServerName()
    		gi.getView()  
    		gi.getMemberId()
    		gi.getCollabId() */


/*    		gi.setAsOfTid(asOfTid);
    		gi.setBaselineId(baselineId);
    		gi.setExportTid(exportTid);
    		gi.setFilter(filter);
    		gi.setId(id);
    		gi.setImportTid(importTid);
*/
    		
    		int memberId = ginfo.getMemberId();
    		int wbId = ginfo.getWbId();
    		int collabId = ginfo.getCollabId();
    		//int griId = ginfo.getId();
    		String gridName = ginfo.getName();
    		String view = "LATEST";
    		int nhId ;
    		int userId ;
    		int tid = -1;

    		System.out.println("GRID/PUT REST-API CALL :  memberId : " + memberId);
    		System.out.println("GRID/PUT REST-API CALL :  wbId : " + wbId);
    		System.out.println("GRID/PUT REST-API CALL :  collabId : " + collabId);
    		System.out.println("GRID/PUT REST-API CALL :  gridId : " + gridId);
    		System.out.println("GRID/PUT REST-API CALL :  gridName : " + gridName);
    		System.out.println("GRID/PUT REST-API CALL :  view : " + view);
    		
    		// Error vector to all the Exceptions
    		Vector xlErrorCells = new Vector(); //new Vector();
    		// access variables
    		boolean canAddRows = false;
    		boolean canDeleteRows = false;
    		boolean canAdministerColumns = false;

    		ArrayList	columnIds = null;
    		ArrayList	rowIds = null;
    		ArrayList	formulaIds = null;
    		ArrayList	strValIds = null;
    		String		formulaString = null;
    		
    		PreparedStatement stmt		= null;
    		TransactionManager tm = null;
    		String query = null;
    		
    		try
    		{
				Hashtable members = getAllMemberships(connection);

				if (!members.containsKey(memberId))
				{
					//Error Membership Not Found
					System.out.println("members.containsKey(memberId) memberId : " + memberId + " --> is FALSE");
		        	erb = new ErrorRequestObject();
		        	erb.setError("Membership ID NOT FOUND");
		        	erb.setPath("GridManagement.gridPut::GridManagement.getAllMemberships");
					erb.setProposedSolution("Request with Exiting Member ID");
		        	ErrResps.add(erb);
				}
				
				if (members.containsKey(memberId))
				{
					System.out.println("members.containsKey(memberId) memberId : " + memberId + " --> is TRUE");
					MemberNode mn = (MemberNode) members.get(memberId);
					nhId = mn.getNhId();
					userId = mn.getUserId();
					//nhName not identified....NOT NEEDED
					
					//	Access control checks
					TableInfo tinfo = TableManager.getTableInfo(connection, userId, gridId);
					TableAccessList ftal = TableViewManager.getSuggestedAccess(connection, tinfo, userId, memberId, nhId);
					
					int raccess = 1;
					int ACLFromDB = ftal.getACL();
					TableAccessRequest wAccess = new TableAccessRequest(gridId, "LATEST", true);
					int wACL = wAccess.getACL();
					int awACL = wACL & ACLFromDB;

					canAddRows				= ftal.canAddRow();
					canDeleteRows			= ftal.canDeleteRow();
					canAdministerColumns	= ftal.canAdministerColumn();

					tm = new TransactionManager(connection, userId);
					tid = tm.startTransaction("GridManagement::gridPut New table", "GridManagement::gridPut -> new table");

					if(canAdministerColumns == false)
					{
						// User does not have access to add columns
						xlErrorCells.add( new xlErrorNew( gridId,0,0,12010));
						System.out.println("No access to add column");
					}

					if(canAddRows == false)
					{
						xlErrorCells.add( new xlErrorNew( gridId,0,0,12012));
						System.out.println("No access to add rows");
					}
					System.out.println("view = " + view);
					if(view.equals("None"))
					{
						xlErrorCells.add( new xlErrorNew(gridId, 0, 0, 10005));
					}
					
					//Adding Column Start
					if(canAdministerColumns)
					{
						columnIds = new ArrayList(numColumns);
//						String[] columnNames = columnInfo.split(Seperator);
						Vector columns = new Vector();
						query = " INSERT INTO BW_COLUMN " +
								   " (NAME, BW_TBL_ID, COLUMN_TYPE, SEQUENCE_NUMBER, TX_ID) " +
								   " VALUES " +
								   " (?,?,?,?,?)";

						stmt = connection.prepareStatement(query);
						// Add columns...Ignoring BWID so Starting from 1. numColumns was sent from client Ignoring bwid, so adding 1 
						for (int cni = 0; cni < numColumns; cni++)
						{
							cl = gridColumns.get(cni);
							String colname = cl.getName();
							int colSeq = cl.getSeqNo();
							stmt.setString(1, colname);
							stmt.setInt(2, gridId);
							stmt.setString(3, "STRING");
							stmt.setFloat(4, colSeq);
							stmt.setInt(5, tid);
							stmt.addBatch();
							System.out.println("Adding column : " + colname);
						}
						int[] rescnt = stmt.executeBatch();
						stmt.clearBatch();
						stmt.close();
						stmt = null;
					}
					
					//HashMap columnHash = new HashMap();
					ResultSet resultset = null;
					query = "select id, name, sequence_number, tx_id, is_active from bw_column where tx_id = ? order by sequence_number";
					stmt = connection.prepareStatement(query);
					stmt.setInt(1, tid);
					resultset = stmt.executeQuery();

					//Updating gridColumns in-Place
		    		colArr.clear();						// Clearing CellBuffer.colArr
					int prevColId = -1;
					int prevColSeq = -1;
					int index = 0;
					Column col ;
					while (resultset.next())
					{
						int columnId = resultset.getInt(1);
						columnIds.add (new Integer(columnId));
						
						col = new Column();
						col.setId(resultset.getInt(1));
						col.setName(resultset.getString(2));
						col.setSeqNo(resultset.getInt(3));
						col.setTid(resultset.getInt(4));
						col.setActive(resultset.getBoolean(5));
						col.setPreviousColumnid(prevColId);
						col.setPreviousColumnSequence(prevColSeq);
						prevColId = resultset.getInt(1);
						prevColSeq = resultset.getInt(3);
						gridColumns.set(index, col);

						System.out.println("Adding to colArr: index: " + index + " | columnId : " + columnId);
			    		colArr.add(index, columnId);				// Recreating colArr by Adding each ColumnId into colArr
						index += 1;
					}
					stmt.close();
					stmt = null;
					resultset.close();
					resultset = null;
					//Adding Column Ends  here

					// Add rows Starts here
					
					if (numRows > 0)
					{
						if (canAdministerColumns && canAddRows)
							rowIds = TableManager.createRowsNewTable(connection, gridId, tid, userId, numRows);
						
						for (int iCol = 0; iCol < numColumns ; iCol=iCol+1)
						{
							System.out.println("Processing column num = " + iCol);
							System.out.println("rowIds : " + rowIds);
							System.out.println("columnIds : " + columnIds);
							System.out.println("colCellArr.get(iCol) : " + colCellArr.get(iCol));
							
							processLinkExportColumnData(connection, cellArr, colCellArr.get(iCol), colCellArr.get(iCol), iCol, rowIds, columnIds, numRows, tid);
							//processLinkExportColumnData(connection, cellBuff, fmlaBuff, i, rowIds, columnIds, numRows, tid);
						}
						//System.out.println("xlLinkExportService: Time to insert into rcsv table = " + getElapsedTime());

						//Updating gridRows in cellBufferRequest
			    		rowArr.clear();				//Clearing Cellbuffer.rowArray
						int prevRowId = -1;
						int prevRowSeq = -1;
						Row rw ;
						for (int iRow=0; iRow < gridRows.size(); iRow=iRow+1)
						{
							rw = new Row();
							rw.setActive(true);
							rw.setId((Integer) rowIds.get(iRow));
							rw.setPreviousRowid(prevRowId);
							rw.setPreviousRowSequence(prevRowId);
							rw.setSeqNo(iRow+1);
							rw.setTid(tid);
							prevRowId = (Integer) rowIds.get(iRow);
							prevRowSeq = iRow;
							gridRows.set(iRow, rw);				// updating gridRows in-Place
							
							System.out.println("Adding to rowArr: iRow: " + iRow + " | rowIds : " + rowIds.get(iRow));
				    		rowArr.add(iRow,(Integer) rowIds.get(iRow));		// Recreating rowArr by Adding each id into rowArr
						}
						
						System.out.println("GridManagement::gridPut -> xlErrorCells.size() " + xlErrorCells.size());
						if (xlErrorCells.size() > 0)
						{
							throw new BoardwalkException(12011);
						}
						query = "{CALL BW_UPD_CELL_FROM_RCSV_LINK_EXPORT(?,?,?)}";
						CallableStatement cstmt = connection.prepareCall(query);
						cstmt.setInt(1, tid);
						cstmt.setInt(2, gridId);
						cstmt.setInt(3, userId);
						int updCount = cstmt.executeUpdate();
						cstmt.close();
						cstmt = null;
						System.out.println("GridManagement::gridPut -> Execute BW_UPD_CELL_FROM_RCSV_LINK_EXPORT .....DONE ");
					}
					// commit the transaction
					tm.commitTransaction();
					tm = null;
					// Add rows Ends here
					
					ginfo.setAsOfTid(tid);
					ginfo.setExportTid(tid);

		    		cbfReturn.setInfo(ginfo);
		    		cbfReturn.setRowArray(rowArr);
		    		cbfReturn.setColumnArray(colArr);
		    		cbfReturn.setColumnCellArrays(colCellArr);
		    		cbfReturn.setColumns(gridColumns);
		    		cbfReturn.setRows(gridRows);
		    		cbfReturn.setCells(cellArr);

				}
    		}
			catch (BoardwalkException bwe)
			{
	        	System.out.println("Collaboration Id not found");
	        	erb = new ErrorRequestObject();
	        	erb.setError("Collaboration ID NOT FOUND");
	        	erb.setPath("GridManagement.gridPut::BoardwalkCollaborationManager.getCollaborationTree");
				erb.setProposedSolution("Boardwalk Exception. ErrorCode:" + bwe.getErrorCode() + ", Error Msg:" + bwe.getMessage() + ", Solution:" +bwe.getPotentialSolution());
	        	ErrResps.add(erb);
	        	System.out.println("Boardwalk Exception. ErrorCode:" + bwe.getErrorCode() + ", Error Msg:" + bwe.getMessage() + ", Solution:" +bwe.getPotentialSolution());
			}
	        catch ( SystemException s)
			{
	        	System.out.println("SystemException MAY BE thrown by getAllMemberships");
	        	s.printStackTrace();
	        	erb = new ErrorRequestObject();
	        	erb.setError("SystemException: " + s.getErrorMessage());
	        	erb.setPath("GridManagement.gridPut::GridManagement.getAllMemberships");
				erb.setProposedSolution(s.getPotentialSolution());
	        	ErrResps.add(erb);
			}    		

    		//Custom code Ends
    	}
		catch (SQLException sqe)
		{
			erb = new ErrorRequestObject();
			erb.setError("SQLException:" + sqe.getCause());
			erb.setPath("GridManagement.gridPut::getConnection");
			erb.setProposedSolution("Get DBConnection failed. Contact Boardwalk System Administrator");
			ErrResps.add(erb);
			sqe.printStackTrace();
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}    		 		    		
		return cbfReturn;
	}

//	public static void processLinkExportColumnData(Connection connection, String cellData, String formulaData, int columnIdx, ArrayList rowIds, ArrayList columnIds, int numRows, int tid) throws SQLException 
	public static void processLinkExportColumnData(Connection connection, ArrayList<Cell> cellArr,  SequencedCellArray sca, SequencedCellArray scaFmla, int columnIdx, ArrayList rowIds, ArrayList columnIds, int numRows, int tid) throws SQLException 
	{
		PreparedStatement stmt		= null;
		ArrayList<String> seqColValues = (ArrayList<String>) sca.getCellValues();
		ArrayList<String> seqColFmlas = (ArrayList<String>) scaFmla.getCellValues();
		cellArr.clear();
		
		int columnId = ((Integer)columnIds.get(columnIdx)).intValue();
	
		boolean emptyColumn = false;
		
		System.out.println("seqColValues.size = " + seqColValues.size());
		if (seqColValues.size() == 0) // empty column
		{
			emptyColumn = true;
			System.out.println("Column is empty");
		}
		boolean emptyFormulae = false;
		if (seqColFmlas.size() == 0) // empty column
		{
			emptyFormulae = true;
			System.out.println("Formulae is empty");
		}
		// insert into bw_rc_string_value 
		String query = 
			" INSERT INTO BW_RC_STRING_VALUE " + 
			" (BW_ROW_ID, BW_COLUMN_ID, STRING_VALUE, FORMULA, TX_ID, CHANGE_FLAG) " +
			" VALUES " +
			" (?, ?, ?, ?, ?, ?) ";

		int prevColId = -1;
		int prevRowId = -1;
		int colSequence = -1;
		
		if (columnIdx == 0)
			prevColId = -1;
		else
			prevColId = (Integer) columnIds.get(columnIdx-1);

		
		//start here
		Cell cl;
		//cl.setAccess(access); cl.setActive(active); cl.setChangeFlag(changeFlag); cl.setColId(colId);
		//cl.setColSequence(colSequence); cl.setFormula(formula); cl.setId(id); cl.setRowId(rowId); cl.setRowSequence(rowSequence);
		//cl.setTid(tid); cl.setValue(value);
		
		
		stmt = connection.prepareStatement(query);
		for (int i = 0; i < numRows; i++)
		{
			cl = new Cell();

			int rowId = ((Integer)rowIds.get(i)).intValue ();
			String cellValue = "";
			String formula = null;
			if (emptyColumn == false)
			{
				try
				{
					//cellValue = cellArr[i];
					cellValue = seqColValues.get(i);
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
					//formula = formulaArr[i];
					formula = seqColFmlas.get(i);
					//if (formula.indexOf("=") < 0)					//Fix for IssueId: 
					if (formula.indexOf("=") != 0)					//Fix for IssueId: 
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
			
			cl.setRowId(rowId); cl.setColId(columnId); cl.setValue(cellValue); cl.setFormula(formula);
			cl.setTid(tid); cl.setChangeFlag(12);cl.setActive(true); cl.setColSequence(columnIdx); cl.setRowSequence(i+1);
			//cl.setAccess(access); 
    		cellArr.add(cl);
		}
		int[] rescnt = stmt.executeBatch();
		stmt.close();
		stmt = null;
	}
	
	
	
    //@POST
	public static int gridPost(Grid grid, ArrayList <ErrorRequestObject> ErrResps) 
	{
		int gridId = -1;
		ErrorRequestObject erb;
        // get the connection
    	Connection connection = null;
    	try
    	{
    		// Start a connection
    		DatabaseLoader databaseloader = new DatabaseLoader(new Properties());
    		connection = databaseloader.getConnection();
    		// Get an authenticated boardwalk connection
    		BoardwalkConnection bwcon = null;
    		try
    		{
    			String loginName = "admin";
    			String loginPwd = "0";
    		    bwcon = BoardwalkConnectionManager.getBoardwalkConnection(connection, loginName, loginPwd, -1);
    		    System.out.println("Successfully obtained authenticated Boardwalk connection");
    		}
    		catch(BoardwalkException bwe)
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("Authentication_Connection_Failure");
    			erb.setPath("GridManagement.gridPost::getBoradwalkConnection");
    			erb.setProposedSolution("Authentication/Connection Failed. Contact Boardwalk System Administrator");
    			ErrResps.add(erb);
    		    System.out.println("Authentication/Connection Failed");
    		    return gridId;
    		}
    		//Custom Code Starts

    		int memberId = grid.getMemberId();
    		int wbId = grid.getWbId();
    		int collabId = grid.getCollabId();
    		String tableDesc = grid.getDescription();
    		String tableName = grid.getName();

    		try
    		{
				Hashtable members = getAllMemberships(connection);
			
				if (members.containsKey(memberId))
				{
					System.out.println("members.containsKey(memberId) memberId : " + memberId + " --> is TRUE");
					MemberNode mn = (MemberNode) members.get(memberId);
					int nhId = mn.getNhId();
					int userId = mn.getUserId();
					BoardwalkCollaborationNode bcn = null;
					bcn = BoardwalkCollaborationManager.getCollaborationTree(bwcon, collabId);
	    			if (bcn == null) 
	    			{
						//throw new NoSuchElementException("Collaboration Id NOT FOUND") ;
	    				throw new BoardwalkException( 10018 );
	    			}    		
	    			//com.boardwalk.whiteboard.Whiteboard wb  
	    			boolean wbValid = false;
					Vector wv = bcn.getWhiteboards();
					Iterator wvi = wv.iterator();
					while ( wvi.hasNext())
					{
						BoardwalkWhiteboardNode bwn = (BoardwalkWhiteboardNode)wvi.next();
						System.out.println("\tWhiteboard = " + bwn.getName());
						if (bwn.getId() == grid.getWbId())
						{
							wbValid = true;
							break;
						}
					}
					if (wbValid == false)
					{
			        	System.out.println("Whiteboard Id not found");
			        	erb = new ErrorRequestObject();
			        	erb.setError("Whitebaord ID NOT FOUND");
			        	erb.setPath("GridManagement.gridPost::BoardwalkCollaborationManager.getCollaborationTree");
						erb.setProposedSolution("Request with Exiting Whiteboard ID");
			        	ErrResps.add(erb);
			        	return gridId;
					}
					else
					{
						TransactionManager tm = null;
						try
						{
							tm = new TransactionManager(connection, userId);
							int tid = tm.startTransaction();
							gridId = TableManager.createTable( connection, wbId, tableName, tableDesc, 2, 1, 1,"LATEST", memberId, tid, 1);
							// set default access control
				            Vector accessLists = new Vector();
				            Hashtable  relationships = NeighborhoodManager.getNeighborhoodRelationships(connection, nhId );
							Enumeration relationKeys = relationships.keys();
							if ( relationships.size() > 0 )
							{
								while ( relationKeys.hasMoreElements() )
								{
									String relationship = (String)relationKeys.nextElement();
									NewTableAccessList accessList = new NewTableAccessList(-1, gridId,relationship);
									if ( relationship.equals("PRIVATE") )
									{
										accessList.setAddRow();
										accessList.setDeleteRow();
										accessList.setReadLatestOfTable();
										accessList.setWriteLatestOfTable();
										accessList.setReadWriteLatestOfMyRows();
									}
									accessLists.add(accessList );
								}
							}
							NewTableAccessList publicAccessList = new NewTableAccessList(-1, gridId, "PUBLIC");
							NewTableAccessList creatorAccessList = new NewTableAccessList(-1, gridId, "CREATOR");
							creatorAccessList.setAdministerTable();
							creatorAccessList.setAdministerColumn();
							creatorAccessList.setAddRow();
							creatorAccessList.setDeleteRow();
							creatorAccessList.setReadLatestOfTable();
							creatorAccessList.setWriteLatestOfTable();
							creatorAccessList.setReadWriteLatestOfMyRows();
							accessLists.add( creatorAccessList);
							accessLists.add( publicAccessList);
							if ( accessLists.size()  > 0 )
							{
								TableManager.addAccesstoTable(connection, gridId, accessLists,tid);
							}
							tm.commitTransaction();
							System.out.println("Committed Transaction successfuly");
							return gridId ;
						}
						catch (SQLException sqe)
						{
							System.out.println("Exception in TableManager.createTable() TRY BLOCK");
							erb = new ErrorRequestObject();
							erb.setError("SQLException:" + sqe.getErrorCode() + ", Cause:"+ sqe.getMessage());
							erb.setPath("GridManagement.gridPost::TableManager.createTable");
							erb.setProposedSolution("Grid Already Exists in Whiteboard. Try different Grid Name");
							ErrResps.add(erb);
					
							try
							{
								System.out.println("Rollbacking Transaction");
								tm.rollbackTransaction();
							}
							catch (SQLException sqe1)
							{
								sqe1.printStackTrace();
							}
							System.out.println("After Rollback Transaction. Returning gridId:" + gridId);
							return gridId ;
						}
					}
				}
				else
				{
					//Error Membership Not Found
					System.out.println("members.containsKey(memberId) memberId : " + memberId + " --> is FALSE");
		        	erb = new ErrorRequestObject();
		        	erb.setError("Membership ID NOT FOUND");
		        	erb.setPath("GridManagement.gridPost::GridManagement.getAllMemberships");
					erb.setProposedSolution("Request with Exiting Member ID");
		        	ErrResps.add(erb);
		        	return gridId;
				}
    		}
    		catch (BoardwalkException bwe)
    		{
            	System.out.println("Collaboration Id not found");
            	erb = new ErrorRequestObject();
            	erb.setError("Collaboration ID NOT FOUND");
            	erb.setPath("GridManagement.gridPost::BoardwalkCollaborationManager.getCollaborationTree");
    			erb.setProposedSolution("Boardwalk Exception. ErrorCode:" + bwe.getErrorCode() + ", Error Msg:" + bwe.getMessage() + ", Solution:" +bwe.getPotentialSolution());
            	ErrResps.add(erb);
            	System.out.println("Boardwalk Exception. ErrorCode:" + bwe.getErrorCode() + ", Error Msg:" + bwe.getMessage() + ", Solution:" +bwe.getPotentialSolution());
    		}
            catch ( SystemException s)
    		{
            	System.out.println("SystemException MAY BE thrown by getAllMemberships");
            	s.printStackTrace();
            	erb = new ErrorRequestObject();
            	erb.setError("SystemException: " + s.getErrorMessage());
            	erb.setPath("GridManagement.gridPost::GridManagement.getAllMemberships");
    			erb.setProposedSolution(s.getPotentialSolution());
            	ErrResps.add(erb);
    		}
    		//Custom code Ends
    	}
		catch (SQLException sqe)
		{
			erb = new ErrorRequestObject();
			erb.setError("SQLException:" + sqe.getCause());
			erb.setPath("GridManagement.gridPost::getConnection");
			erb.setProposedSolution("Get DBConnection failed. Contact Boardwalk System Administrator");
			ErrResps.add(erb);
			sqe.printStackTrace();
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}    		 		    		
		return gridId;
	}

    static public Hashtable getAllMemberships(Connection connection) throws SystemException
    {
    	Hashtable ht = new Hashtable();
        ResultSet rs = null;
        CallableStatement cs  = null;

        try
        {
			cs = connection.prepareCall(CALL_BW_GET_ALL_MEMBERSHIPS_INFO);

			System.out.println("before calling CALL_BW_GET_ALL_MEMBERSHIPS_INFO i.e. "  + CALL_BW_GET_ALL_MEMBERSHIPS_INFO);
			cs.execute();
            rs = cs.getResultSet();
			System.out.println("after calling CALL_BW_GET_ALL_MEMBERSHIPS_INFO");

			int memberId, userId, nhId, nhLevel;
			String firstName, lastName, Email;

			//System.out.println("before while rs loop");

            while ( rs.next() )
            {
            	//System.out.println("inside while rs loop");
                memberId = rs.getInt("MemberId");
                userId = rs.getInt("UserId");
                firstName = rs.getString("FirstName");
                lastName = rs.getString("LastName");
                Email = rs.getString("Email_Address");
                nhId = rs.getInt("NhId");
                nhLevel = rs.getInt("NhLevel");
                System.out.println("MemberNode-> memberId:" + memberId + ", userId:" + userId + ", firstName:" + firstName + ", lastName:" + lastName + ", Email:" + Email + ", nhId:" + nhId + ", nhLevel:" + nhLevel);            
                ht.put(memberId, new MemberNode(memberId, userId, firstName, lastName, Email, nhId, nhLevel));
            }
			System.out.println("outside while rs loop");
        }
        catch(SQLException sqlexception)
        {
			System.out.println(sqlexception.toString());
            throw new SystemException(sqlexception);
        }
        finally
        {
            try
            {
				if ( rs != null )
					rs.close();
				if ( cs != null )
					cs.close();
            }
            catch(SQLException sqlexception1) {
				System.out.println("throwing  sqlexception1");
                throw new SystemException(sqlexception1);
            }
        }
        return ht;
    }

	
}
