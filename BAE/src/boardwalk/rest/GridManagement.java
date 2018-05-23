package boardwalk.rest;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
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
import com.boardwalk.excel.xlColumn_import;
import com.boardwalk.excel.xlErrorNew;
import com.boardwalk.exception.BoardwalkException;
import com.boardwalk.exception.SystemException;
import com.boardwalk.member.MemberNode;
import com.boardwalk.neighborhood.NeighborhoodManager;
import com.boardwalk.table.ColumnManager;
import com.boardwalk.table.NewTableAccessList;
import com.boardwalk.table.RowManager;
import com.boardwalk.table.TableAccessList;
import com.boardwalk.table.TableAccessRequest;
import com.boardwalk.table.TableInfo;
import com.boardwalk.table.TableManager;
import com.boardwalk.table.TableRowInfo;
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
import io.swagger.model.GridChangeBuffer;
import io.swagger.model.GridInfo;
import io.swagger.model.NeighborhoodPath;
import io.swagger.model.Row;
import io.swagger.model.SequencedCellArray;
import io.swagger.model.Whiteboard;
import io.swagger.models.Info;

public class GridManagement {

    private static String CALL_BW_GET_ALL_MEMBERSHIPS_INFO = "{CALL BW_GET_ALL_MEMBERSHIPS_INFO}";

	public GridManagement()
	{	
	}

    //@GET
    //@Path("/{gridId}")
	public static CellBuffer gridGridIdGet(int gridId, CellBuffer  cellBufferRequest, ArrayList<ErrorRequestObject> ErrResps)
	{
		CellBuffer cbfReturn = new CellBuffer();
		ErrorRequestObject erb;
        // get the connection
    	Connection connection = null;
    	Vector xlErrorCells = new Vector(); 
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
    			erb.setPath("GridManagement.gridGet::getBoradwalkConnection");
    			erb.setProposedSolution("Authentication/Connection Failed. Contact Boardwalk System Administrator");
    			ErrResps.add(erb);
    		    System.out.println("Authentication/Connection Failed");
    		    return cbfReturn;
    		}
    		//Custom Code Starts

    		//cellBufferRequest.getGridChangeBuffer()   -------- need to be decided
    		
    		System.out.println("cellBufferRequest.toString() as follows...................");
    		System.out.println(cellBufferRequest.toString());
    		
    		GridInfo ginfo = cellBufferRequest.getInfo();
    		
    		if (ginfo.getMemberId() == null)
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("MemberId is missing in GET Request");
    			erb.setPath("GridManagement.gridGet::GridInfo.getMemberId()");
    			erb.setProposedSolution("MemberId is mandetory. Provide MemberId to get the Grid Status");
    			ErrResps.add(erb);
    			return cbfReturn;
    		}
    		
    		if (ginfo.getMemberId() < 0)
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("MemberId must be a Positive Number");
    			erb.setPath("GridManagement.gridGet::GridInfo.getMemberId()");
    			erb.setProposedSolution("You must provide existing MemberId is to get the Grid Status. MemberId must be a Positive Number");
    			ErrResps.add(erb);
    			return cbfReturn;
    		}

    		
    		if (ginfo.getMode() == null)
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("Mode is missing in GET Request");
    			erb.setPath("GridManagement.gridGet::GridInfo.getMode()");
    			erb.setProposedSolution("Mode is mandetory. Provide Mode as 1 or 0 to get the Grid Status");
    			ErrResps.add(erb);
    			return cbfReturn;
    		}
    		
    		if ((ginfo.getMode() != 0) && (ginfo.getMode() != 1))
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("Invalid Mode in GET Request");
    			erb.setPath("GridManagement.gridGet::GridInfo.getMode()");
    			erb.setProposedSolution("Valid Mode is 1 or 0 to get the Grid Status");
    			ErrResps.add(erb);
    			return cbfReturn;
    		}
    		
    		if (ginfo.getView() == null)
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("View is missing in GET Request");
    			erb.setPath("GridManagement.gridGet::GridInfo.getView()");
    			erb.setProposedSolution("View is mandetory. Valid View values are [ MY_ROWS |LATEST | DESIGN | LATEST_BY_USER | LATEST_VIEW_OF_ALL_USERS | LATEST_VIEW_OF_ALL_CHILDREN | LATEST_VIEW_OF_ALL_USERS_IN_ANY_NH | LATEST_VIEW_OF_ALL_USERS_IN_ANY_CHILDREN_NH | LATEST_ROWS_OF_ALL_USERS_IN_MY_NH | LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_IMM_CHD | LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_ALL_CHD ]");
    			ErrResps.add(erb);
    			return cbfReturn;
    		}
    		
    		if (ginfo.getView().equals("MY_ROWS") || 
    			ginfo.getView().equals("LATEST") || 
    			ginfo.getView().equals("DESIGN") || 
    			ginfo.getView().equals("LATEST_BY_USER") || 
    			ginfo.getView().equals("LATEST_VIEW_OF_ALL_USERS") || 
    			ginfo.getView().equals("LATEST_VIEW_OF_ALL_CHILDREN") || 
    			ginfo.getView().equals("LATEST_VIEW_OF_ALL_USERS_IN_ANY_NH") || 
    			ginfo.getView().equals("LATEST_VIEW_OF_ALL_USERS_IN_ANY_CHILDREN_NH") || 
    			ginfo.getView().equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH") || 
    			ginfo.getView().equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_IMM_CHD") || 
    			ginfo.getView().equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_ALL_CHD") || ginfo.getView().indexOf("?") == 0 )
    		{    			
        		System.out.println("view : " + ginfo.getView());
    		}
    		else
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("Invalid View in GET Request");
    			erb.setPath("GridManagement.gridGet::GridInfo.getView()");
    			erb.setProposedSolution("Invalid View. Valid View values are [ LATEST | DESIGN | MY_ROWS | LATEST_BY_USER | LATEST_VIEW_OF_ALL_USERS | LATEST_VIEW_OF_ALL_CHILDREN | LATEST_VIEW_OF_ALL_USERS_IN_ANY_NH | LATEST_VIEW_OF_ALL_USERS_IN_ANY_CHILDREN_NH | LATEST_ROWS_OF_ALL_USERS_IN_MY_NH | LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_IMM_CHD | LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_ALL_CHD ] ");
    			ErrResps.add(erb);
    			return cbfReturn;
    		}
    		
    		int memberId = ginfo.getMemberId();
    		int wbId;
    		int collabId;
    		//int wbId = ginfo.getWbId();
    		//int collabId = ginfo.getCollabId();
    		String gridName = ginfo.getName();
    		String view = ginfo.getView();
    		int baselineId = ginfo.getBaselineId();
    		int mode = ginfo.getMode();
    		
    		
    		//GridInfo
/*    		ginfo.setAsOfTid(asOfTid); ginfo.setBaselineId(baselineId); ginfo.setCollabId(collabId);
    		ginfo.setExportTid(exportTid); ginfo.setFilter(filter); ginfo.setId(id); ginfo.setImportTid(importTid);
    		ginfo.setMemberId(memberId); ginfo.setName(name); ginfo.setNeighborhoodHeirarchy(neighborhoodHeirarchy);
    		ginfo.setServerName(serverName); ginfo.setServerURL(serverURL); ginfo.setView(view); ginfo.setWbId(wbId);
*/    		
    		
    		ArrayList<Integer> rowArray = new ArrayList<Integer>();
    		ArrayList<Integer> columnArray  = new ArrayList<Integer>();
    		ArrayList<String> cellValues  = new ArrayList<String>();
    		ArrayList<String> cellfmla  = new ArrayList<String>();
    		ArrayList<Integer> colCellAccess  = new ArrayList<Integer>();
    		ArrayList <SequencedCellArray> scas = new ArrayList<SequencedCellArray>();
    		SequencedCellArray sca;
    		ArrayList<Row> gridRows = new ArrayList<Row>();
    		Row gridRow = new Row();
    		ArrayList <Column> gridCols = new ArrayList<Column>();
    		Column gridCol ;
    		GridChangeBuffer gcb = new GridChangeBuffer();
    		ArrayList<Cell> gridCells  = new ArrayList<Cell>();
    		Cell gridCell;

    		int columnId;
    		
    		//rowArray[]
/*    		int arg0;
    		ArrayList<Integer> rowArray = new ArrayList <Integer>();
    		rowArray.add(arg0);
    		cellBufferRequest.setRowArray(rowArray);	// DONE
*/    		
    		// columArray[]
/*    		ArrayList<Integer> columnArray = new ArrayList <Integer>();
    		columnArray.add(arg0);
    		cellBufferRequest.setColumnArray(columnArray); // DONE
*/    		
    		//columnCellArrays
/*    		ArrayList<String> cellValues = new ArrayList <String>();
    		ArrayList<String> cellfmla = new ArrayList <String>();
    		ArrayList<Integer> colCellAccess = new ArrayList <Integer>();
    		int columnId;
    		ArrayList <SequencedCellArray> scas = new ArrayList <SequencedCellArray>();
    		SequencedCellArray sca = new SequencedCellArray();
    		sca.setCellValues(  cellValues);
    		sca.setColumnId(columnId);
    		scas.add(sca);
    		cellBufferRequest.setColumnCellArrays(scas);  // in process
*/    		
    		//rows: array<Row>
/*    		ArrayList<Row> gridRows = new ArrayList <Row>();
    		Row gridRow = new Row();
    		gridRow.setActive(active); gridRow.setId(id); gridRow.setPreviousRowid(previousRowid);
    		gridRow.setPreviousRowSequence(previousRowSequence); gridRow.setSeqNo(seqNo); gridRow.setTid(tid);
    		gridRows.add(gridRow);
    		cellBufferRequest.setRows(gridRows);	//DONE
*/
    		//columns: array<Column>
/*    		ArrayList <Column> gridCols = new ArrayList <Column>();
    		Column gridCol = new Column();
    		gridCol.setActive(active); gridCol.setId(id); gridCol.setName(name); gridCol.setPreviousColumnid(previousColumnid);
    		gridCol.setPreviousColumnSequence(previousColumnSequence); gridCol.setSeqNo(seqNo); gridCol.setTid(tid);
    		gridCols.add(gridCol);
    		cellBufferRequest.setColumns(gridCols);  // DONE
*/    		
    		//cells: array<Cell>
/*    		ArrayList<Cell> gridCells = new ArrayList <Cell>(); 
    		Cell gridCell = new Cell();
    		gridCell.setAccess(access);; gridCell.setActive(active); gridCell.setChangeFlag(changeFlag);
    		gridCell.setColId(colId); gridCell.setColSequence( colSequence); gridCell.setFormula(formula);
    		gridCell.setId(id); gridCell.setRowId(rowId); gridCell.setRowSequence(rowSequence); gridCell.setTid(tid);
    		gridCell.setValue(value);
    		cellBufferRequest.setCells(gridCells );
*/    		
    		int nhId ;
    		int userId ;
    		int tid = -1;

    		System.out.println("GRID/PUT REST-API CALL :  memberId : " + memberId);
 //   		System.out.println("GRID/PUT REST-API CALL :  wbId : " + wbId);
//    		System.out.println("GRID/PUT REST-API CALL :  collabId : " + collabId);
    		System.out.println("GRID/PUT REST-API CALL :  gridId : " + gridId);
    		System.out.println("GRID/PUT REST-API CALL :  gridName : " + gridName);
    		System.out.println("GRID/PUT REST-API CALL :  view : " + view);

    		// Error vector to all the Exceptions
    		//Vector xlErrorCells = new Vector(); //new Vector();
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

//    		try
  //  		{
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
					collabId = tinfo.getCollaborationId();
					wbId = tinfo.getWhiteboardId();

					if (view == null || view.trim().equals(""))
					{
						view = ftal.getSuggestedViewPreferenceBasedOnAccess();
						System.out.println("Suggested view pref = " + view);
						if(view == null)
							view = "None";
					}
					// Check access control :: TBD
					int raccess = 1;
					int ACLFromDB = ftal.getACL();
					TableAccessRequest wAccess = new TableAccessRequest(gridId, view, true);
					int wACL = wAccess.getACL();
					int awACL = wACL & ACLFromDB;
					if (awACL == wACL)
					{
						raccess = 2;
						System.out.println("Rows have write access");
					}
					else
					{
						System.out.println("Rows are readonly");
					}
		
					// authenticate the user
				/*	Member memberObj = UserManager.authenticateMember(connection, userName, memberId); //Modified by Tekvision on 20180207 for Clear Text Password(Issue Id: 14241)
					if (memberObj == null)
					{
						//System.out.println("Authentication failed for user : " + userName);
						responseBuffer = "Failure";//TBD : Description of the Error
						xlErrorCells.add( new xlErrorNew( tableId, 0, 0, 11004));
						throw new BoardwalkException(11004);
					}
					else
					{
						//System.out.println("Authentication succeeded for user : " + userName);
						nhId = memberObj.getNeighborhoodId();
						
					}

					System.out.println("Time to authenticate user = " + getElapsedTime());*/

					
					// This happens only when the user has no access to the said table
					// But in this case the user does not have provision to select the table
					if(view.equals("None"))
					{
						xlErrorCells.add( new xlErrorNew(gridId, 0, 0, 10005));
						throw new BoardwalkException(10005);		//it was 11005.
					}

					// get the tableBuffer
					//
					//responseBuffer = TableViewManager.getTableBuffer(connection, gridId, userId, memberId, nhId, baselineId, view, mode);
					
					try 
					{		// TRY OF getTableBuffer STARTS
						int maxTransactionId = -1;
						int exportTid = -1;

						if (view == null || view.trim().equals("")) 
						{
							view = ftal.getSuggestedViewPreferenceBasedOnAccess();
							System.out.println("Suggested view pref = " + view);
						}
						System.out.println("view = " + view);
						System.out.println("baselineId = " + baselineId);
						System.out.println("mode = " + mode);

						int criteriaTableId = TableViewManager.getCriteriaTable(connection, gridId, userId);
						System.out.println("Using criterea table id = " + criteriaTableId);
						int accessTableId = TableViewManager.getAccessTable(connection, gridId, userId);
						HashSet restrColumnList = new HashSet();
						if (accessTableId > 0) 
						{
							System.out.println("Using access table id = " + accessTableId);
							// read the access for the user
							restrColumnList = TableViewManager.getRestrictedColumnsForImport(connection, gridId, accessTableId, userId);
						}
						// Get the columns
						Vector colv = ColumnManager.getXlColumnsForImport(connection, gridId, userId, memberId);
						Iterator ci = colv.iterator();
						
						// columns
						float previousColumnSequence = -1;
						int previousColumnid = -1;
						gridCols = new ArrayList <Column>();
						columnArray = new ArrayList <Integer>();
						
						for (int c = 0; c < colv.size(); c++) 
						{
							xlColumn_import col = (xlColumn_import) colv.elementAt(c);
							if (restrColumnList != null && restrColumnList.size() > 0) 	{
								if (restrColumnList.contains(new Integer(col.getId()))) 
								{
									System.out.println("Skip restricted column " + col.getName());
									continue;
								}
							}
							if (maxTransactionId < col.getCreationTid()) {
								maxTransactionId = col.getCreationTid();
							}

							if (maxTransactionId < col.getAccessTid()) {
								maxTransactionId = col.getAccessTid();
							}
							gridCol = new Column();
							gridCol.setActive(true);
							gridCol.setId(col.getId()); 
							gridCol.setName(col.getName()); 
							gridCol.setPreviousColumnid(previousColumnid);
							gridCol.setPreviousColumnSequence( new BigDecimal(previousColumnSequence));
							gridCol.setSeqNo(  new BigDecimal(col.getSequenceNumber())); 
							gridCol.setTid(col.getCreationTid());
							gridCols.add(gridCol);
							columnArray.add(col.getId());
							
							previousColumnSequence = col.getSequenceNumber();
							previousColumnid = col.getId();
						}
			    		cellBufferRequest.setColumns(gridCols);
			    		cellBufferRequest.setColumnArray(columnArray);
			    		
						// Get the rows
						String lsRowQuery = ""; // Row query String
						// is the view dynamic (not in the criteria table), starts with ?
						boolean viewIsDynamic = false;
						if (view.indexOf("?") == 0) {
							System.out.println("View is dynamic = " + view);
							viewIsDynamic = true;
						}
						TableRowInfo tbrowInfo = null;
						
						/* Condition Added By Asfak - START - 29 Jun 2014 */
						if (criteriaTableId == -1 && !viewIsDynamic) {
							tbrowInfo = RowManager.getTableRows(connection, gridId, userId, nhId, baselineId, view, 1, -1, -1);
						} else if (criteriaTableId > 0 && viewIsDynamic) {
							tbrowInfo = RowManager.getTableRows(connection, gridId, userId, nhId, baselineId, view, 1, -1, -1);
						} else {
							tbrowInfo = RowManager.getTableRows(connection, gridId, userId, nhId, baselineId, view, 1, -1, -1);
						}
						Vector rowv = tbrowInfo.getRowVector();
						// rows
						gridRows = new ArrayList <Row>();
						rowArray = new ArrayList <Integer>();
						
						int previousRowid = -1;
						int previousRowSequence = -1;
						for (int r = 0; r < rowv.size(); r++) 
						{
							com.boardwalk.table.Row rowObject = (com.boardwalk.table.Row) rowv.elementAt(r);

							gridRow = new Row();
							gridRow.setCreationTid(rowObject.getCreationTid());
							gridRow.setCreaterId(rowObject.getCreatorUserId());
							gridRow.setOwnerName(rowObject.getOwnerName());
							gridRow.setOwnershipAssignedTid(rowObject.getOwnershipAssignedTid());
							gridRow.setOwnerName(rowObject.getOwnerName());
							gridRow.setOwnerId(rowObject.getOwnerUserId());
							
							gridRow.setActive((rowObject.getIsActive()== 1? true : false));
							gridRow.setRowName(rowObject.getName()); 	
							

							rowArray.add(rowObject.getId());
				    		gridRow = new Row();
				    		gridRow.setActive((rowObject.getIsActive() == 1? true: false)); 
				    		gridRow.setId(rowObject.getId()); 
				    		gridRow.setPreviousRowid(  previousRowid);
				    		gridRow.setPreviousRowSequence(previousRowSequence); 
				    		Float obj = new Float(rowObject.getSequenceNumber());
				    		gridRow.setSeqNo(obj.intValue()); 
				    		
				    		previousRowid = rowObject.getId() ;
				    		previousRowSequence = obj.intValue();
							
							if (maxTransactionId < rowObject.getCreationTid()) {
								maxTransactionId = rowObject.getCreationTid();
							}

							if (maxTransactionId < rowObject.getOwnershipAssignedTid()) {
								maxTransactionId = rowObject.getOwnershipAssignedTid();
							}
				    		gridRow.setTid(maxTransactionId);

				    		gridRows.add(gridRow);
						}
			    		cellBufferRequest.setRows(gridRows);
			    		cellBufferRequest.setRowArray(rowArray);
			    		
						// Get the cells
						String q = null;
						String rowQuery = null;
						boolean results = false;
						if (criteriaTableId == -1 && !viewIsDynamic) {
							System.out.println("CALL BW_GET_TBL_LNK_IMPORT ---> " + gridId + "," + userId + "," + memberId + "," + nhId + "," + view);
							q = "{CALL BW_GET_TBL_LNK_IMPORT(?,?,?,?,?)}";
							stmt = connection.prepareStatement(q);
							stmt.setInt(1, gridId);
							stmt.setInt(2, userId);
							stmt.setInt(3, memberId);
							stmt.setInt(4, nhId);
							stmt.setString(5, view);
							results = stmt.execute();
						} 
						else if (criteriaTableId > 0 && viewIsDynamic) /* Condition Added By Asfak - START - 29 Jun 2014 */ 
						{
							System.out.println("CALL BW_GET_FiltredTableCELLS_DYNAMIC ---> " + gridId + "," + userId + "," + memberId + "," + view);
							q = "{CALL BW_GET_FiltredTableCELLS_DYNAMIC(?,?,?,?)}";
							stmt = connection.prepareStatement(q);
							stmt.setInt(1, gridId);
							stmt.setInt(2, userId);
							stmt.setInt(3, memberId);
							stmt.setString(4, view);
							results = stmt.execute();
						} 
						else 
						{
							System.out.println("CALL BW_GET_FiltredTableCELLS_DYNAMIC ---> " + gridId + "," + userId + "," + memberId + "," + view);
							q = "{CALL BW_GET_FiltredTableCELLS_DYNAMIC(?,?,?,?)}";
							stmt = connection.prepareStatement(q);
							stmt.setInt(1, gridId);
							stmt.setInt(2, userId);
							stmt.setInt(3, memberId);
							stmt.setString(4, view);
							results = stmt.execute();
						} /* Condition Added By Asfak - END - 29 Jun 2014 */
						int rsCount = 0;
						int updcount = 0;
						//Loop through the available result sets.
						do 
						{
							if (results) {
								ResultSet rs = stmt.getResultSet();
								if (rsCount == 0) // max txid
								{
									if (rs.next()) {
										exportTid = rs.getInt(1);
										System.out.println("exportTid = " + exportTid);
										if (maxTransactionId < exportTid)
											maxTransactionId = exportTid;
									}
								} 
								else // cell data
								{
									System.out.println("colv size = " + colv.size());
									System.out.println("rowv size = " + rowv.size());
									
									scas = new ArrayList <SequencedCellArray>();									
									for (int c = 0; c < colv.size(); c++) 
									{
										xlColumn_import col = (xlColumn_import) colv.elementAt(c);
										if (restrColumnList != null && restrColumnList.size() > 0 && restrColumnList.contains(new Integer(col.getId()))) 
										{
											System.out.println("Skipping data for restricted column = " + col.getId());
											for (int r = 0; r < rowv.size(); r++) 
											{
												rs.next();
											}
											continue;
										}
										System.out.println("getting column data for id = " + col.getId());
										int colAccess = col.getAccess();
										
										sca = new SequencedCellArray();
										cellValues = new ArrayList <String>();
										cellfmla = new ArrayList <String>();
										columnId = col.getId();
										Float obj = new Float(col.getSequenceNumber());
										int colSequence = obj.intValue();
										colCellAccess = new ArrayList <Integer>();

										System.out.println("Processing Rows....columnId : " + columnId + " , rowv.size():" + rowv.size());
										for (int r = 0; r < rowv.size(); r++) 
										{
											rs.next();
											String cellval = rs.getString(1);
											if (cellval.length() > 0) 
											{
												cellval = cellval.trim();
											}

											String cellFormula = rs.getString(2);
											int celltid = rs.getInt(3);
											if (maxTransactionId < celltid)
												maxTransactionId = celltid;
											int cellAccess = java.lang.Math.min(raccess, colAccess);

											if (cellFormula == null || mode == 1) {
												cellFormula = "";
											} else {
												cellFormula = cellFormula.trim();
											}

											System.out.println("columnId : " + columnId + " -- cellFormula :" + cellFormula + " -- cellAccess:" + cellAccess);
											cellValues.add(cellval);
											cellfmla.add(cellFormula);
											colCellAccess.add(cellAccess);
										}
										
										sca.setCellValues(cellValues);
										sca.setColumnId(columnId);
										sca.setCellFormulas(cellfmla);
										sca.setCellAccess(colCellAccess);
										sca.setColSequence(colSequence);
										
										scas.add(sca);
									}
									cellBufferRequest.setColumnCellArrays(scas);
								}
								rsCount++;
								rs.close();
								System.out.println("rsCount ----------" + rsCount);
							} 
							else 
							{
								updcount = stmt.getUpdateCount();
								if (updcount >= 0) 
								{
									System.out.println("Update data displayed here. + updcount " + updcount);
								} else 
								{
									System.out.println("No more results to process.");
								}
							}
							results = stmt.getMoreResults();
						} 
						while (results || updcount != -1);
						stmt.close();

						int maxdeletedcell_tid;
						maxdeletedcell_tid = 0;
						try {
							stmt = connection.prepareStatement("SELECT MAX(BW_ROW.TX_ID) FROM BW_ROW WHERE BW_ROW.BW_TBL_ID = ? AND BW_ROW.IS_ACTIVE = 0 UNION SELECT MAX(BW_COLUMN.TX_ID) FROM BW_COLUMN WHERE BW_COLUMN.BW_TBL_ID = ? AND BW_COLUMN.IS_ACTIVE = 0");
							stmt.setInt(1, gridId);
							stmt.setInt(2, gridId);
							ResultSet rs1 = stmt.executeQuery();
							while (rs1.next()) {
								if (rs1.getInt(1) > maxdeletedcell_tid)
								maxdeletedcell_tid = rs1.getInt(1);
							}
							rs1.close();
							stmt.close();
							stmt = null;
							rs1 = null;
						} 
						catch (Exception e11) {
							e11.printStackTrace();
						}

						if (maxdeletedcell_tid > maxTransactionId) {
							maxTransactionId = maxdeletedcell_tid;
							System.out.println("maxtid reset by cell deactivation to = " + maxTransactionId);
						}

						ginfo.setId(gridId);
						ginfo.setName(tinfo.getTableName());
						ginfo.setPurpose(tinfo.getTablePurpose());
						ginfo.setView(view);
						ginfo.setMemberId(memberId);
						ginfo.setUserId(userId);

						Vector NhPathIds = com.boardwalk.neighborhood.NeighborhoodManager.getBoardwalkPathIds( connection , nhId );
						
						NeighborhoodPath nhpath = new NeighborhoodPath();
						int nhLevels = NhPathIds.size()-1;
						nhpath.setLevels(nhLevels);

						String nhPathId = (String)NhPathIds.elementAt(0);
						String nhpathArr[] = nhPathId.split("\\\\");

						switch (nhLevels)
						{
							case 3:
								nhpath.setNhLevel0(Integer.parseInt(nhpathArr[0]) );
								nhpath.setNhLevel1(Integer.parseInt(nhpathArr[1]) );
								nhpath.setNhLevel2(Integer.parseInt(nhpathArr[2]) );
								nhpath.setNhLevel3(Integer.parseInt(nhpathArr[3]) );
								
							case 2:
								nhpath.setNhLevel0(Integer.parseInt(nhpathArr[0]) );
								nhpath.setNhLevel1(Integer.parseInt(nhpathArr[1]) );
								nhpath.setNhLevel2(Integer.parseInt(nhpathArr[2]) );
								nhpath.setNhLevel3(-1);
								
							case 1:
								nhpath.setNhLevel0(Integer.parseInt(nhpathArr[0]) );
								nhpath.setNhLevel1(Integer.parseInt(nhpathArr[1]) );
								nhpath.setNhLevel2(-1);
								nhpath.setNhLevel3(-1);
								
							case 0:
								nhpath.setNhLevel0(Integer.parseInt(nhpathArr[0]));
								nhpath.setNhLevel1(-1);
								nhpath.setNhLevel2(-1);
								nhpath.setNhLevel3(-1);
						}
						ginfo.setNeighborhoodHeirarchy(nhpath );
						
						ginfo.setNhId(nhId);
						ginfo.setRowCount(rowv.size());
						ginfo.setColCount(colv.size());
						ginfo.setMaxTxId(maxTransactionId);
						ginfo.setImportTid(maxTransactionId);
						ginfo.setExportTid(exportTid);
						ginfo.setCriteriaTableId(criteriaTableId);
						ginfo.setMode(mode);
						ginfo.setCollabId(collabId);
						ginfo.setWbId(wbId);

					} 	//GET TABLE BUFFER TRY ENDS
					catch (Exception e) 
					{
						e.printStackTrace();
						return null;
					}
				}
				
				cbfReturn.setCells(gridCells);
				cbfReturn.setColumnArray(columnArray );
				cbfReturn.setColumnCellArrays(scas);
				cbfReturn.setColumns(gridCols);
				cbfReturn.setGridChangeBuffer(gcb);
				cbfReturn.setInfo(ginfo);
				cbfReturn.setRowArray(rowArray);
				cbfReturn.setRows(gridRows);
				cbfReturn.setGridChangeBuffer(gcb);				
    		}
			//catch (Exception e) 
			//{
			//	e.printStackTrace();
			//	return null;
			//}
    //	}	
    		//Custom code Ends
					
			catch (BoardwalkException bwe)
			{
				if( xlErrorCells.size() > 0 )
				{
					StringBuffer  errorBuffer  = new StringBuffer();
	
					for ( int errorIndex = 0; errorIndex< xlErrorCells.size(); errorIndex++ )
					{
						xlErrorNew excelError = (xlErrorNew)(xlErrorCells.elementAt(errorIndex));
						errorBuffer.append( excelError.buildTokenString() );
					}
				}
			}
	        catch ( SystemException s)
			{
	        	System.out.println("SystemException thrown in GridManagement.gridGet: Possibly from getAllMemberships() OR TableManager.getTableInfo()");
	        	s.printStackTrace();
	        	erb = new ErrorRequestObject();
	        	erb.setError("SystemException: " + s.getErrorMessage());
	        	erb.setPath("GridManagement.gridGet::GridManagement.getAllMemberships OR TableManager.getTableInfo");
				erb.setProposedSolution(s.getPotentialSolution());
	        	ErrResps.add(erb);
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
		System.out.println("cbfReturn.toString() as follows...................");
		System.out.println(cbfReturn.toString());
		System.out.println("End of PRINTING cbfReturn.toString() ...................END");

    	return cbfReturn ;
	}
		
	 	
    //@PUT
    //@Path("/{gridId}")
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

    		
    		//int wbId = ginfo.getWbId();			// OPTIONAL
    		int collabId = ginfo.getCollabId();
    		//int griId = ginfo.getId();
    		String gridName = ginfo.getName();
    		String view = "LATEST";
    		int nhId ;
    		int userId ;
    		int tid = -1;

    		System.out.println("GRID/PUT REST-API CALL :  memberId : " + memberId);
 //   		System.out.println("GRID/PUT REST-API CALL :  wbId : " + wbId);
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
						
						try 
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
								float colSeq =  cl.getSeqNo().floatValue();
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
						catch (SQLException sqe)
						{
							System.out.println("Exception in INSERT INTO BW_COLUMN ");
							erb = new ErrorRequestObject();
							erb.setError("SQLException:" + sqe.getErrorCode() + ", Cause:"+ sqe.getMessage());
							erb.setPath("GridManagement.gridPut::stmt(INSERT INTO BW_COLUMN)");
							erb.setProposedSolution("Column Exists Grid. Try different Column Name");
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
							return cbfReturn;
						}
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
						col.setSeqNo(new BigDecimal( resultset.getInt(3)));
						col.setTid(resultset.getInt(4));
						col.setActive(resultset.getBoolean(5));
						col.setPreviousColumnid(prevColId);
						col.setPreviousColumnSequence(new BigDecimal(prevColSeq));
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
