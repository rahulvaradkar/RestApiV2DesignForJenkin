package boardwalk.rest;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.codec.binary.Base64;

import com.boardwalk.database.DatabaseLoader;
import com.boardwalk.database.TransactionManager;
import com.boardwalk.excel.newXLRow;
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
import com.boardwalk.user.UserManager;

import boardwalk.collaboration.BoardwalkCollaborationManager;
import boardwalk.collaboration.BoardwalkCollaborationNode;
import boardwalk.collaboration.BoardwalkWhiteboardNode;
import boardwalk.connection.BoardwalkConnection;
import boardwalk.connection.BoardwalkConnectionManager;
import io.swagger.api.NotFoundException;
import io.swagger.model.Cell;
import io.swagger.model.CellBuffer;
import io.swagger.model.CellChangeDetails;
import io.swagger.model.Column;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.Grid;
import io.swagger.model.GridChangeBuffer;
import io.swagger.model.GridInfo;
import io.swagger.model.LoginInfo;
import io.swagger.model.NeighborhoodPath;
import io.swagger.model.Row;
import io.swagger.model.SequencedCellArray;
import io.swagger.model.Whiteboard;
import io.swagger.models.Info;

public class GridManagement {

    private static String CALL_BW_GET_ALL_MEMBERSHIPS_INFO = "{CALL BW_GET_ALL_MEMBERSHIPS_INFO}";
    private static int OPERATION_LINK_EXPORT = 1;
    private static int OPERATION_SUBMIT = 2;
    		

	public GridManagement()
	{	
	}

    //@GET
    //@Path("/{gridId}")
	public static CellBuffer gridGridIdGet(int gridId, int importTid, String view, int mode, int baselineId , ArrayList<ErrorRequestObject> ErrResps, String authBase64String)
	{
		//CellBuffer  cellBufferRequest = new CellBuffer();
		CellBuffer cbfReturn = new CellBuffer();
		ErrorRequestObject erb;

		// get the connection
    	Connection connection = null;
		BoardwalkConnection bwcon = null;
		
		int nhId = -1;
		int memberId = -1;
		
		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
				
		if (!ErrResps.isEmpty())
		{
			return cbfReturn;
		}

		connection = bwcon.getConnection();
		memberId = memberNh.get(0);
		nhId = memberNh.get(1);

		System.out.println("MemberNode -> nhId :" + nhId);
		System.out.println("MemberNode -> memberId :" + memberId);
		
    	Vector xlErrorCells = new Vector(); 
    	try
    	{
    		
    		int userId = bwcon.getUserId();
    		
    		//System.out.println("cellBufferRequest.toString() as follows...................");
    		//System.out.println(cellBufferRequest.toString());

    		GridInfo ginfo = new GridInfo();

    		int wbId;
    		int collabId;
    		
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
    		
    		int tid = -1;

    		System.out.println("GRID/PUT REST-API CALL :  memberId : " + memberId);
    		System.out.println("GRID/PUT REST-API CALL :  gridId : " + gridId);
    		//System.out.println("GRID/PUT REST-API CALL :  gridName : " + gridName);
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

			int synch = 0;
			
			if (importTid > 0)
			{
				cbfReturn = getGridRefresh  (connection, gridId, importTid, userId, memberId, nhId,  view, mode, baselineId, synch , ErrResps);
				return cbfReturn;
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
	    		//cellBufferRequest.setColumns(gridCols);
	    		//cellBufferRequest.setColumnArray(columnArray);
	    		
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
	    		//cellBufferRequest.setRows(gridRows);
	    		//cellBufferRequest.setRowArray(rowArray);
	    		
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
							//cellBufferRequest.setColumnCellArrays(scas);
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

				System.out.println("nhId : " + nhId);
				System.out.println("connection : " + connection);
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
		//}
		
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
		System.out.println("authBase64String ..................." + authBase64String);
		// Decode data on other side, by processing encoded data
		byte[] valueDecoded = Base64.decodeBase64(authBase64String);
		System.out.println("Decoded value is " + new String(valueDecoded));		
		return cbfReturn ;
	}
		

	//Returns Grid Refresh CellBuffer 
	public static CellBuffer getGridRefresh(Connection connection, int gridId, int importTid, int userId, int memberId, int nhId,  String view, int mode, int baselineId, int synch , ArrayList<ErrorRequestObject> ErrResps)
	{
		ErrorRequestObject erb;
		CellBuffer cbfReturn = new CellBuffer();
		Vector xlErrorCells = new Vector(); 
		TransactionManager tm = null ;
		ArrayList<Cell> gridCells = new ArrayList<Cell>();
		ArrayList <Column> newGridCols = new ArrayList <Column>();
		ArrayList <Row> newGridRows = new ArrayList <Row>();

		ArrayList <Integer> columnArray = new ArrayList <Integer>();
		ArrayList<Integer> rowArray = new ArrayList<Integer>();

		ArrayList<Integer> deletedColumnArray = new ArrayList<Integer>();
		ArrayList<Integer> deletedRowArray = new ArrayList<Integer>();
		ArrayList<Column> newColumnArray = new ArrayList<Column>();
		ArrayList<Row> newRowArray = new ArrayList<Row>();
		
		ArrayList<String> cellValues  = new ArrayList<String>();
		ArrayList<String> cellfmla  = new ArrayList<String>();
		ArrayList<Integer> colCellAccess  = new ArrayList<Integer>();
		ArrayList <SequencedCellArray> scas = new ArrayList<SequencedCellArray>();
		GridChangeBuffer gcb ;
		GridInfo ginfo = new GridInfo();
	
    	try
    	{

			int maxTransactionId = importTid;
			int criteriaTableId = TableViewManager.getCriteriaTable(connection, gridId, userId);
			System.out.println("Using criterea table id = " + criteriaTableId);
	
			String lsRowQuery = TableViewManager.getRowQuery(connection, gridId, userId, criteriaTableId, true, view, "TABLE");
			
			// Check access control :: TBD
			TableInfo tinfo = TableManager.getTableInfo(connection, userId, gridId);
			TableAccessList ftal = TableViewManager.getSuggestedAccess(connection, tinfo, userId, memberId, nhId);

			if (view == null || view.trim().equals(""))
			{
				view = ftal.getSuggestedViewPreferenceBasedOnAccess();
				System.out.println("Suggested view pref = " + view);
				if (view == null)
				{
					xlErrorCells.add(new xlErrorNew(gridId, 0, 0, 10005));
					throw new BoardwalkException(10005);
				}
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
	
			//Added to fix Refresh issue when the Column Access is applied from Template side 2016/11/16
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
			HashMap colHash = new HashMap();
			//ColObjsByColId = new HashMap();
			Iterator ci = colv.iterator();
			//while (ci.hasNext())
			//{
			//    xlColumn_import coli = (xlColumn_import)ci.next();
			//    ColObjsByColId.put(new Integer(coli.getId()), coli);
			//}
			// columns
	
			newGridCols = new ArrayList<Column>();
			Column gridCol;
			int previousColId =-1;
			int previousColSequence = -1;
			
			for (int c = 0; c < colv.size(); c++)
			{
				xlColumn_import col = (xlColumn_import)colv.elementAt(c);
				//Added to fix Refresh issue when the Column Access is applied from Template side 2016/11/16
				if (restrColumnList != null && restrColumnList.size() > 0)
				{
					if (restrColumnList.contains(new Integer(col.getId())))
					{
						System.out.println("Skip restricted column " + col.getName());
						continue;
					}
				}
				if (maxTransactionId < col.getCreationTid())
				{
					maxTransactionId = col.getCreationTid();
				}
	
				if (maxTransactionId < col.getAccessTid())
				{
					maxTransactionId = col.getAccessTid();
				}
				
				//resData.append(col.getId() + Seperator);				//USE FOR REST API BUFFER
				//resData.append(col.getName() + Seperator);				//USE FOR REST API BUFFER
				// Mark New Columns, or Deleted Columns
				if (col.getCreationTid() > importTid)
				{
					//resData.append("N" + Seperator);					//USE FOR REST API BUFFER   NEW COLUMN
					gridCol = new Column();
					gridCol.setActive(true);
					gridCol.setId(col.getId());
					gridCol.setName(col.getName());
					gridCol.setPreviousColumnid(previousColId);
					gridCol.setPreviousColumnSequence(new BigDecimal(previousColSequence));
					gridCol.setSeqNo(new BigDecimal(col.getSequenceNumber()));
					gridCol.setTid(col.getCreationTid());
					newGridCols.add(gridCol);
				}
				else if (col.getAccessTid() > importTid && col.getAccess() > 0)
				{
					//resData.append("N" + Seperator);					//USE FOR REST API BUFFER	NEW COLUMN
					gridCol = new Column();
					gridCol.setActive(true);
					gridCol.setId(col.getId());
					gridCol.setName(col.getName());
					gridCol.setPreviousColumnid(previousColId);
					gridCol.setPreviousColumnSequence(new BigDecimal(previousColSequence));
					gridCol.setSeqNo(new BigDecimal(col.getSequenceNumber()));
					gridCol.setTid(col.getAccessTid());
					newGridCols.add(gridCol);
				}
				
				colHash.put(new Integer(col.getId()), col);
				previousColId = col.getId();
				previousColSequence = Math.round(col.getSequenceNumber());
			}
	
			//System.out.println(resData.toString());
			// Get the rows
			boolean viewIsDynamic = false;
			if (view.indexOf("?") == 0)
			{
				System.out.println("View is dynamic = " + view);
				viewIsDynamic = true;
			}
			TableRowInfo tbrowInfo = null;
			if (criteriaTableId == -1 && !viewIsDynamic)
			{
				tbrowInfo = RowManager.getTableRows(connection, gridId, userId, nhId, baselineId, view, 1, -1, -1);
			}
			//condition added ashishB
			if (criteriaTableId > 0 && viewIsDynamic)
			{
				tbrowInfo = RowManager.getTableRows(connection, gridId, userId, nhId, baselineId, view, 1, -1, -1);
			}
			else
			{
				tbrowInfo = RowManager.getTableRows(connection, gridId, userId, nhId, baselineId, view, 1, -1, -1);
			}
			Vector rowv = tbrowInfo.getRowVector();
	
			Hashtable rowHash = tbrowInfo.getRowHash();
	
			// rows
			//System.out.println("transaction start");
			tm = new TransactionManager(connection, userId);
			int tid = tm.startTransaction("Import changes for table id = " + gridId, "");
			tm.commitTransaction();
			tm = null;
			//System.out.println("transaction commit");
	//		stmt = connection.prepareStatement("UPDATE BW_ROW SET OWNER_TID = ? WHERE ID = ?");
			int numNewRows = 0;
			int numSrvrRows = 0; //Added by Jeetendra on 20171205 to fix the Issue Id: 14025
			
			Row gridRow = new Row();
			newGridRows = new ArrayList <Row>();
			
			int previousRowid = -1;
			int previousRowSequence = -1;
			for (int r = 0; r < rowv.size(); r++)
			{
				com.boardwalk.table.Row rowObject = (com.boardwalk.table.Row) rowv.elementAt(r);
				int rowId = rowObject.getId();
				if (maxTransactionId < rowObject.getCreationTid())
				{
					maxTransactionId = rowObject.getCreationTid();
				}
	
				if (maxTransactionId < rowObject.getOwnershipAssignedTid())
				{
					maxTransactionId = rowObject.getOwnershipAssignedTid();
				}
				//Modified by Jeetendra on 20171205 to fix the Issue Id: 14025 - START
				//resData.append(rowId + Seperator);
	
				if (synch != 0 || importTid < rowObject.getCreationTid() || importTid < rowObject.getOwnershipAssignedTid()) {		//THIS CONDITION IS NEEDED IN REST API
					//resData.append(rowId + Seperator);
					//resData.append("N" + Seperator);
					numNewRows++;
					numSrvrRows++;
					
					gridRow = new Row();
					gridRow.setCreationTid(rowObject.getCreationTid());
					gridRow.setCreaterId(rowObject.getCreatorUserId());
					gridRow.setOwnerName(rowObject.getOwnerName());
					gridRow.setOwnershipAssignedTid(rowObject.getOwnershipAssignedTid());
					gridRow.setOwnerName(rowObject.getOwnerName());
					gridRow.setOwnerId(rowObject.getOwnerUserId());
					gridRow.setActive((rowObject.getIsActive()== 1? true : false));
					gridRow.setRowName(rowObject.getName()); 	
		    		gridRow.setActive((rowObject.getIsActive() == 1? true: false)); 
		    		gridRow.setId(rowObject.getId()); 
		    		gridRow.setPreviousRowid(  previousRowid);
		    		gridRow.setPreviousRowSequence(previousRowSequence); 
		    		Float obj = new Float(rowObject.getSequenceNumber());
		    		gridRow.setSeqNo(obj.intValue()); 
		    		newGridRows.add(gridRow);
					
				}
				else
				{
					//resData.append(rowId + Seperator);
					//resData.append(Seperator);
					numSrvrRows++;
				}
	    		Float obj = new Float(rowObject.getSequenceNumber());
	    		previousRowid = rowObject.getId() ;
	    		previousRowSequence = obj.intValue();
			}
	
			//System.out.println(resData.toString());
			// Get the cells TBD : views other than latest
			String q = null;
			if (synch == 0)
			{
				q = "{CALL BW_IMPORT_CHANGES(?,?,?,?,?,?,?)}";
				System.out.println("Calling BW_IMPORT_CHANGES ");
			}
			else
			{
				q = "{CALL BW_IMPORT_CHANGES_ALL(?,?,?,?,?,?,?)}";
				System.out.println("Calling BW_IMPORT_CHANGES_ALL ");
			}
			PreparedStatement stmt		= null;
			//cellv = TableManager.getLatestCellsForTable(connection, m_tableid, userId, memberId, nhId, ViewPreference);
			stmt = connection.prepareStatement(q);
			stmt.setInt(1, gridId);
			stmt.setInt(2, userId);
			stmt.setInt(3, memberId);
			stmt.setInt(4, nhId);
			stmt.setString(5, view);
			stmt.setInt(6, importTid);
			stmt.setInt(7, tid);
	
			System.out.println("tableId = " + gridId);
			System.out.println("userId = " + userId);
			System.out.println("memberId = " + memberId);
			System.out.println("nhId = " + nhId);
			System.out.println("view = " + view);
			System.out.println("importTid = " + importTid);
			System.out.println("newTid = " + tid);
			System.out.println("mode = " + mode);
			System.out.println("synch = " + synch);
	
			System.out.println("before stmt.executeQuery :" + q);
			ResultSet rs = stmt.executeQuery ();
			System.out.println("after stmt.executeQuery :" + q);
	
			gridCells = new ArrayList<Cell>();
			Cell cl;
			
			System.out.println("before processing rs:" );
			while (rs.next())
			{
				System.out.println("inside while:" );
				String sval = rs.getString(1);
				String fmla = rs.getString(2);
				int rowId = rs.getInt(3);
				int colId = rs.getInt(4);
				if (maxTransactionId < rs.getInt(5))
				{
					maxTransactionId = rs.getInt(5);
				}
//				if (rowHash.get(new Integer(rowId)) == null)
//					continue;
				xlColumn_import col = (xlColumn_import)colHash.get(new Integer(colId));
				if (col == null)
					continue;
				if (fmla == null || fmla.indexOf("=") < 0 || mode == 1)
				{
					fmla = "";
				}
				else
				{
					fmla = fmla.trim();
				}
	
				//System.out.println("Got column for id = " + colId);
				int colAccess = col.getAccess();
				int cellAccess = java.lang.Math.min(raccess, colAccess);
				
				cl = new Cell();
				cl.setColId(colId);
				cl.setRowId(rowId);
				cl.setCellFormula(fmla);
				cl.setCellValue(sval);
				cl.setAccess(cellAccess);
				cl.setTid(tid);
				//cl.setActive(active);
				//cl.setRowSequence(rowSequence);
				//cl.setColSequence(colSequence);
				//cl.setId(id);
				//cl.setChangeFlag(changeFlag);
	
				System.out.println("colId:"+colId+"rowId:"+rowId+"Formula:"+fmla+"sval:"+sval+"tid:"+tid+"cellAccess:"+cellAccess);
				gridCells.add(cl);
				
				//resData.append(rowId + Seperator + colId + Seperator + sval.trim() + Seperator + fmla + Seperator + cellAccess + Seperator);
				System.out.println("before end block of while:" );
			}
			System.out.println("after processing rs:" );

			//System.out.println(resData.toString());
			//resData.replace(resData.length() - 1, resData.length(), ContentDelimeter);
			//System.out.println(resData.toString());
			stmt.close();
			rs.close();
			rs = null;
			stmt = null;
			//System.out.println("Time(sec) to fetch changed cells = " + getElapsedTime());
	
			int maxdeletedcell_tid;
			maxdeletedcell_tid = 0;
			ResultSet rs1 = null;
	
			try
			{
				stmt = connection.prepareStatement("SELECT MAX(BW_ROW.TX_ID) FROM BW_ROW WHERE BW_ROW.TX_ID > ? AND BW_ROW.BW_TBL_ID = ? AND BW_ROW.IS_ACTIVE = 0 UNION SELECT MAX(BW_COLUMN.TX_ID) FROM BW_COLUMN WHERE BW_COLUMN.TX_ID > ? AND BW_COLUMN.BW_TBL_ID = ? AND BW_COLUMN.IS_ACTIVE = 0");
				stmt.setInt(1, importTid);
				stmt.setInt(2, gridId);
				stmt.setInt(3, importTid);
				stmt.setInt(4, gridId);
				rs1 = stmt.executeQuery ();
				while( rs1.next() )
				{
					if(rs1.getInt(1) > maxdeletedcell_tid)
						maxdeletedcell_tid = rs1.getInt(1);
				}
				rs1.close();
				stmt.close();
				stmt = null;
			}
			catch(Exception e11)
			{	e11.printStackTrace();
				try
				{
					rs1.close();
					stmt.close();
					stmt = null;
				}
				catch (Exception e12)
				{
					e12.printStackTrace();
				}
			}
			if ( maxdeletedcell_tid > maxTransactionId )
			{
				maxTransactionId = maxdeletedcell_tid;
				System.out.println("maxtid reset by cell deactivation to = " + maxTransactionId);
			}
	
			//Get Deleted Rows and Deleted Columns
			deletedColumnArray = new ArrayList<Integer>();
			deletedRowArray = new ArrayList<Integer>();
	
			try
			{
				stmt = connection.prepareStatement("SELECT BW_ROW.ID, 'R' FROM BW_ROW WHERE BW_ROW.TX_ID > ? AND BW_ROW.BW_TBL_ID = ? AND BW_ROW.IS_ACTIVE = 0 UNION SELECT BW_COLUMN.ID, 'C'  FROM BW_COLUMN WHERE BW_COLUMN.TX_ID > ? AND BW_COLUMN.BW_TBL_ID = ? AND BW_COLUMN.IS_ACTIVE = 0");
				stmt.setInt(1, importTid);
				stmt.setInt(2, gridId);
				stmt.setInt(3, importTid);
				stmt.setInt(4, gridId);
				rs1 = stmt.executeQuery ();
				while( rs1.next() )
				{
					if(rs1.getString(2).equals("R"))
						deletedRowArray.add(rs1.getInt(1));
					else if(rs1.getString(2).equals("C"))
						deletedColumnArray.add(rs1.getInt(1)); 
				}
				rs1.close();
				stmt.close();
				stmt = null;
			}
			catch(Exception e11)
			{	e11.printStackTrace();
				try
				{
					rs1.close();
					stmt.close();
					stmt = null;
				}
				catch (Exception e12)
				{
					e12.printStackTrace();
				}
			}
			if ( maxdeletedcell_tid > maxTransactionId )
			{
				maxTransactionId = maxdeletedcell_tid;
				System.out.println("maxtid reset by cell deactivation to = " + maxTransactionId);
			}

			ginfo.setId(gridId);
			ginfo.setName(tinfo.getTableName());
			ginfo.setPurpose(tinfo.getTablePurpose());
			ginfo.setView(view);
			ginfo.setMemberId(memberId);
			ginfo.setUserId(userId);		
			ginfo.setNhId(nhId);
			ginfo.setCriteriaTableId(criteriaTableId);
			ginfo.setMode(mode);
			ginfo.setCollabId(tinfo.getCollaborationId());
			ginfo.setWbId(tinfo.getWhiteboardId());
			ginfo.setMaxTxId(maxTransactionId);
			ginfo.setImportTid(maxTransactionId);

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
		}	catch (SQLException sqe)
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
	    	
		gcb = new GridChangeBuffer();
	//	gcb.setCritical(critical);
	//	gcb.setCriticalLevel(criticalLevel);
		gcb.setDeletedColumnArray(deletedColumnArray);	//deleted column ids
		gcb.setDeletedRowArray(deletedRowArray);		//deleted row ids
		gcb.setNewColumnArray( newGridCols);
		gcb.setNewRowArray(newGridRows);
		
		cbfReturn.setInfo(ginfo);
		cbfReturn.setCells( gridCells);							//changed cells
		cbfReturn.setRowArray(rowArray);
		cbfReturn.setColumnArray(columnArray );
		cbfReturn.setColumnCellArrays(scas);
		cbfReturn.setColumns(newGridCols);
		cbfReturn.setGridChangeBuffer(gcb);
		cbfReturn.setRows(newGridRows);
			
		return cbfReturn;
	}
	
    //@PUT
    //@Path("/{gridId}")
	public static CellBuffer gridPut(int gridId, CellBuffer  cellBufferRequest, ArrayList<ErrorRequestObject> ErrResps, String authBase64String)
	{
		CellBuffer cbfReturn = new CellBuffer();
		ErrorRequestObject erb;
		
		// get the connection
    	Connection connection = null;
		BoardwalkConnection bwcon = null;
		
		int nhId = -1;
		int memberId = -1;
		int userId = -1;

		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
				
		if (!ErrResps.isEmpty())
		{
			return cbfReturn;
		}

		connection = bwcon.getConnection();
		memberId = memberNh.get(0);
		nhId = memberNh.get(1);
		userId = bwcon.getUserId();

    	try
    	{
    		//Custom Code Starts
    		//reading cellBufferRequest
    		
    		//GridInfo gi = cellBufferRequest.getInfo();
    		ArrayList<Integer> rowArr =  (ArrayList<Integer>) cellBufferRequest.getRowArray();
    		ArrayList<Integer> colArr =  (ArrayList<Integer>) cellBufferRequest.getColumnArray(); 
    		ArrayList<Cell> cellArr = (ArrayList<Cell>) cellBufferRequest.getCells();
    		
    		ArrayList<Row> gridRows = (ArrayList<Row>) cellBufferRequest.getRows();
    		ArrayList<Column> gridColumns = (ArrayList<Column>) cellBufferRequest.getColumns();
    		ArrayList<SequencedCellArray> colCellArr = (ArrayList<SequencedCellArray>) cellBufferRequest.getColumnCellArrays();
    		GridChangeBuffer gcb = cellBufferRequest.getGridChangeBuffer();
    		
    		if (cellArr == null)
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("Missing element cells:[]");
    			erb.setPath("GridManagement.gridPut::cellBufferRequest.getCells()");
    			erb.setProposedSolution("Add cells:[] in gridPut Request");
    			ErrResps.add(erb);
    		    System.out.println("Missing element cells:[]");
    		    //return cbfReturn;
    		}

    		if (rowArr == null)
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("Missing element rowArray:[]");
    			erb.setPath("GridManagement.gridPut::cellBufferRequest.getRowArray()");
    			erb.setProposedSolution("Add rowArray:[] in gridPut Request");
    			ErrResps.add(erb);
    		    System.out.println("Missing element rowArray:[]");
    		    //return cbfReturn;
    		}

    		if (colArr == null)
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("Missing element columnArray:[]");
    			erb.setPath("GridManagement.gridPut::cellBufferRequest.getColumnArray()");
    			erb.setProposedSolution("Add columnArray:[] in gridPut Request");
    			ErrResps.add(erb);
    		    System.out.println("Missing element columnArray:[]");
    		    //return cbfReturn;
    		}

    		if (gridRows == null)
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("Missing element rows:[]");
    			erb.setPath("GridManagement.gridPut::cellBufferRequest.getRows()");
    			erb.setProposedSolution("Add rows:[] in gridPut Request");
    			ErrResps.add(erb);
    		    System.out.println("Missing element rows:[]");
    		    //return cbfReturn;
    		}

    		if (gridColumns == null)
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("Missing element columns:[]");
    			erb.setPath("GridManagement.gridPut::cellBufferRequest.getColumns()");
    			erb.setProposedSolution("Add columns:[] in gridPut Request");
    			ErrResps.add(erb);
    		    System.out.println("Missing element columns:[]");
    		    //return cbfReturn;
    		}
    		
    		if (colCellArr == null)
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("Missing element columnCellArrays:[]");
    			erb.setPath("GridManagement.gridPut::cellBufferRequest.getColumnCellArrays()");
    			erb.setProposedSolution("Add columnCellArrays:[] in gridPut Request");
    			ErrResps.add(erb);
    		    System.out.println("Missing element columnCellArrays:[]");
    		    //return cbfReturn;
    		}

	    	if (ErrResps.size() > 0) 
	    	{
    		    return cbfReturn;
	    	}

    		//cellBufferRequest.getGridChangeBuffer()   -------- need to be decided
    		GridInfo ginfo = cellBufferRequest.getInfo();
    		
    		int importTid = ginfo.getImportTid();
    		
    		int operType;
    		if (importTid == -1)
    		{
    			operType = OPERATION_LINK_EXPORT ;
    			System.out.println("This is Link Export Operation");
    		}
    		else
    		{
    			operType = OPERATION_SUBMIT;
    			System.out.println("This is Submit Operation");
    		}

/*			SUBMIT code DIVERSION HERE    		*/
  			if (operType == OPERATION_SUBMIT)
			{
    			System.out.println("This is Submit Operation");
    			processSubmitRequest(connection, userId, memberId, nhId, gridId, cellBufferRequest, ErrResps, cbfReturn);
    			return cbfReturn;
			}	    	
    		
    		int numColumns = gridColumns.size();
    		int numRows = gridRows.size();
    		
    		System.out.println("GRID/PUT REST-API CALL :  numColumns : " + numColumns);
    		System.out.println("GRID/PUT REST-API CALL :  numRows : " + numRows);
    		Column cl;

    		int collabId = ginfo.getCollabId();
    		String gridName = ginfo.getName();
    		String view = "LATEST";
    		int tid = -1;

    		System.out.println("GRID/PUT REST-API CALL :  memberId : " + memberId);
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

    			erb = new ErrorRequestObject();
    			erb.setError("TABLE UPDATE EXCEPTION: No access to add column");
    			erb.setPath("GridManagement.gridPut::ftal.canAdministerColumn=FALSE");
    			erb.setProposedSolution("You don't access to add a new column, Please contact the owner of the Table");
    			ErrResps.add(erb);
			}

			if(canAddRows == false)
			{
				xlErrorCells.add( new xlErrorNew( gridId,0,0,12012));
				System.out.println("No access to add rows");
    			erb = new ErrorRequestObject();
    			erb.setError("TABLE UPDATE EXCEPTION: No access to add rows");
    			erb.setPath("GridManagement.gridPut::ftal.canAddRow=FALSE");
    			erb.setProposedSolution("You don't have access to add a new row,  Please resolve errors and try again");
    			ErrResps.add(erb);
			}
			System.out.println("view = " + view);
			if(view.equals("None"))
			{
				xlErrorCells.add( new xlErrorNew(gridId, 0, 0, 10005));
				
    			erb = new ErrorRequestObject();
    			erb.setError("ACCESS EXCEPTION: No priviliges to execute this action");
    			erb.setPath("GridManagement.gridPut::view.equals(None)");
    			erb.setProposedSolution("You dont have the priviliges to execute this action,  Please contact the owner of the table to setup necessary access control");
    			ErrResps.add(erb);
			}

	    	if (ErrResps.size() > 0) 
	    	{
    		    return cbfReturn;
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
			
			GridChangeBuffer gcbRet = new GridChangeBuffer();
			
			ginfo.setAsOfTid(tid);
			ginfo.setExportTid(tid);

    		cbfReturn.setInfo(ginfo);
    		cbfReturn.setRowArray(rowArr);
    		cbfReturn.setColumnArray(colArr);
    		cbfReturn.setColumnCellArrays(colCellArr);
    		cbfReturn.setColumns(gridColumns);
    		cbfReturn.setRows(gridRows);
    		cbfReturn.setCells(cellArr);
    		cbfReturn.setGridChangeBuffer(gcbRet);
    		//Custom code Ends
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
			
			cl.setRowId(rowId); cl.setColId(columnId); cl.setCellValue(cellValue); cl.setCellFormula(formula);
			cl.setTid(tid); cl.setChangeFlag(12);cl.setActive(true); cl.setColSequence(columnIdx); cl.setRowSequence(i+1);
			//cl.setAccess(access); 
    		cellArr.add(cl);
		}
		int[] rescnt = stmt.executeBatch();
		stmt.close();
		stmt = null;
	}
	
	
	//Submit chnages using PUT GRID REST API CALL
	public static void processSubmitRequest(Connection connection, int userId, int memberId, int nhId, int gridId, CellBuffer  cellBufferRequest, ArrayList<ErrorRequestObject> ErrResps, CellBuffer cbfReturn )
	{

/*		cbfReturn.setCells(cellBufferRequest.getCells());
		cbfReturn.setColumnArray(cellBufferRequest.getColumnArray());
		cbfReturn.setColumnCellArrays(cellBufferRequest.getColumnCellArrays());
		cbfReturn.setColumns(cellBufferRequest.getColumns());
		cbfReturn.setGridChangeBuffer(cellBufferRequest.getGridChangeBuffer());
		cbfReturn.setInfo(cellBufferRequest.getInfo());
		cbfReturn.setRowArray(cellBufferRequest.getRowArray());
		cbfReturn.setRows(cellBufferRequest.getRows());
*/		
		TransactionManager tm = null;
		int tid = -1;
		
		ErrorRequestObject erb;
 
		TableInfo tinfo ;
		TableAccessList ftal ;
		boolean canAddRows = false;
		boolean canDeleteRows = false;
		boolean canAdministerColumns = false;

		boolean ExceptionAddRows = false;
		boolean ExceptionDeleteRows = false;

		Vector xlErrorCells = new Vector();
		Vector xlDeleteRows = new Vector();
		HashMap rowIdHash = new HashMap();
		HashMap colIdHash = new HashMap();
		HashMap accCols = new HashMap();
		HashMap newRowsHash = new HashMap();
		HashMap newColumnsHash = new HashMap();

		ArrayList columnIds = null;
		ArrayList columnNames = null;
		

		boolean colsDeleted = false;
		boolean rowsDeleted = false;
		boolean newRowsAdded = false;
		int defaultAccess = 2;
		
//		ArrayList<Integer> rowArr =  (ArrayList<Integer>) cellBufferRequest.getRowArray();
//		ArrayList<Integer> colArr =  (ArrayList<Integer>) cellBufferRequest.getColumnArray(); 
		ArrayList<Cell> cellArr = (ArrayList<Cell>) cellBufferRequest.getCells();
		
//		ArrayList<Row> gridRows = (ArrayList<Row>) cellBufferRequest.getRows();
//		ArrayList<Column> gridColumns = (ArrayList<Column>) cellBufferRequest.getColumns();
//		ArrayList<SequencedCellArray> colCellArr = (ArrayList<SequencedCellArray>) cellBufferRequest.getColumnCellArrays();
		GridChangeBuffer gcb = cellBufferRequest.getGridChangeBuffer();
		
//		ArrayList<CellChangeDetails> cellChangesArray =  (ArrayList<CellChangeDetails>)  gcb.getCellChangesArray();
		ArrayList<Integer> delColArray = (ArrayList<Integer>)  gcb.getDeletedColumnArray();
		ArrayList<Integer> delRowArray = (ArrayList<Integer>)  gcb.getDeletedRowArray();
		ArrayList<Column> newColArray = (ArrayList<Column>)  gcb.getNewColumnArray();
		ArrayList<Row> newRowArray = (ArrayList<Row>) gcb.getNewRowArray();
		
		int intCritical = gcb.getCritical();
		int intCriticalLevel = gcb.getCriticalLevel();
		
		Column cl;
		Row rw;
		
		GridInfo ginfo = cellBufferRequest.getInfo();
		
		String view = ginfo.getView();
		int importTid = ginfo.getImportTid();
		int exportTid = ginfo.getExportTid();
		
		//processHeader functionality FOR SUBMIT
		
		if (validateMemberShip(connection, memberId , nhId, userId, ErrResps) == false)
		{
			xlErrorCells.add( new xlErrorNew( gridId, 0, 0, 11005));
			return;
		}
		
		try 
		{
			int criteriaTableId;
			// try the old sheet check
			criteriaTableId = TableViewManager.getCriteriaTable(connection, gridId, userId);
			System.out.println("Using criterea table id = " + criteriaTableId);
	
			//added on 20150620 by Sohum Team
			String lsRowQuery = TableViewManager.getRowQuery(connection, gridId, userId, criteriaTableId, true, view, "TABLE"); //Modified by Lakshman on 20171108 to avoid self joins on BW_CELL for performance gain
	
			//Old sheet check not required.
			
			//Critical Updates
			//Change For Critical Update Start
			PreparedStatement preparedstatement1 = null;
			PreparedStatement criticalcheckstament = null;
			ResultSet resultset1 = null;
			ResultSet criticalresult = null;
			// check if there are any critical updates since last import
			// Added a check on the users so that we do not process critical update of Same user
			String criticalqry="";
			int iscritical_check=-1;
			criticalqry ="SELECT Count(*)  FROM  BW_SIGNIFICANT_TXS  " +
				   "	WHERE     CREATED_BY <> " + userId +
				   " 	AND BW_TBL_ID = " + gridId +
				   "	AND TX_ID > " + importTid ;
	
			criticalcheckstament = connection.prepareStatement(criticalqry);
			criticalresult = criticalcheckstament.executeQuery();
	
			if (criticalresult.next())
			{
				iscritical_check = criticalresult.getInt(1);
			}
			criticalcheckstament.close();
			criticalcheckstament = null;
			if (criticalresult != null)
			{
				criticalresult.close();
				criticalresult = null;
			}
	
			System.out.println("Result of critical check" +  iscritical_check);

			if (iscritical_check>0)
			{
				int critTid1 = -1;
				String query1 = "";
				if (lsRowQuery == null){
					query1 =" SELECT MAX(BW_SIGNIFICANT_TXS.TX_ID)" +
							   " FROM BW_SIGNIFICANT_TXS " +
							   " WHERE " +
							   " BW_TBL_ID = ? AND BW_SIGNIFICANT_TXS.CREATED_BY <> " + userId +
							   " GROUP BY  BW_TBL_ID ";
					//Modified by Ashish for Performance Fix 
					preparedstatement1 = connection.prepareStatement(query1);
					preparedstatement1.setInt(1, gridId);
			   }else{
					//Modified by Ashish for Performance Fix 
					query1 = "{CALL BW_CHECK_SIGNIFICANT_UPDATES(?,?,?,?)}" ;
					preparedstatement1 = connection.prepareStatement(query1);
					preparedstatement1.setInt(1, gridId);
					preparedstatement1.setInt(2, userId);
					preparedstatement1.setInt(3, importTid);
					preparedstatement1.setString(4, view);
				}
				//Change For Critical Update end
				resultset1 = preparedstatement1.executeQuery();
				String lsResponseStr = null;
				
				if (resultset1.next())
				{
					critTid1 = resultset1.getInt(1);
				}
				preparedstatement1.close();
				preparedstatement1 = null;
				if (resultset1 != null)
				{
					resultset1.close();
					resultset1 = null;
				}
		
				if (importTid < critTid1)
				{
					System.out.println("Found Critical updates after the last import ----g2");
					//lsResponseStr = getSignificantUpdateIds(connection, tableId, importTid, userId, nhId, view, memberId);
					//throw new BoardwalkException(12017);
		        	erb = new ErrorRequestObject();
		        	erb.setError("TABLE UPDATE EXCEPTION ((12017)");
		        	erb.setPath("GridManagement.gridPut::GridManagement.BW_CHECK_SIGNIFICANT_UPDATES");
					erb.setProposedSolution("There has been Critical update(s) to this table after your last import, please refresh first.");
		        	ErrResps.add(erb);
		        	return;
				}
			}
			System.out.println("No Critical updates Found after the last import ----g2");
			
			//	Access control checks

			System.out.println("Calling TableManager.getTableInfo : userId:" + userId  + ", gridId:" + gridId);
			tinfo = TableManager.getTableInfo(connection, userId, gridId);

			System.out.println("Calling TableViewManager.getSuggestedAccess : tinfo:" + tinfo + " userId:" + userId  + ", memberId:" + memberId  + ", nhId:" + nhId);
			ftal = TableViewManager.getSuggestedAccess(connection, tinfo, userId, memberId, nhId);
			if (view == null || view.trim().equals(""))
			{
				view = ftal.getSuggestedViewPreferenceBasedOnAccess();
				System.out.println("Suggested view pref = " + view);
				if (view == null)
				{
					xlErrorCells.add(new xlErrorNew(gridId, 0, 0, 10005));
					//throw new BoardwalkException(10005);
		            //errorMessages.put( new Integer( 10005), new BoardwalkMessage( 10005, "ACCESS EXCEPTION", 5 ,"You dont have the priviliges to execute this action ",  "Please contact the owner of the table to setup necessary access control"));
		        	erb = new ErrorRequestObject();
		        	erb.setError("ACCESS EXCEPTION (10005)");
		        	erb.setPath("GridManagement.gridPut::GridManagement.TableViewManager.getSuggestedAccess");
					erb.setProposedSolution("You dont have the priviliges to execute this action, Please contact the owner of the table to setup necessary access control");
		        	ErrResps.add(erb);
		        	return;
				}
			}
			// Check access control :: TBD
			int raccess = 1;
			int ACLFromDB = ftal.getACL();

			System.out.println("Calling TableAccessRequest(gridId, view, true) : gridId:" + gridId + " view:" + view);
			TableAccessRequest wAccess = new TableAccessRequest(gridId, view, true);
			int wACL = wAccess.getACL();
			int awACL = wACL & ACLFromDB;

			canAddRows				= ftal.canAddRow();
			canDeleteRows			= ftal.canDeleteRow();
			canAdministerColumns	= ftal.canAdministerColumn();

			// No Access to Table
			if ( awACL != wACL && canAddRows == false && canDeleteRows == false && canAdministerColumns == false)
			{
				xlErrorCells.add( new xlErrorNew(gridId, 0, 0, 10005));
				//throw new BoardwalkException(10005);
	        	erb = new ErrorRequestObject();
	        	erb.setError("ACCESS EXCEPTION (10005)");
	        	erb.setPath("GridManagement.gridPut::GridManagement.TableAccessRequest");
				erb.setProposedSolution("You dont have the priviliges to execute this action, Please contact the owner of the table to setup necessary access control");
	        	ErrResps.add(erb);
	        	return;
			}
			
			// see if there is a criterea table associated with this table
			criteriaTableId = TableViewManager.getCriteriaTable(connection, gridId, userId);
			System.out.println("Using criterea table id = " + criteriaTableId);
			
			int accessTableId = -1;
			try {
				accessTableId = TableViewManager.getAccessTable(connection, gridId, userId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			HashMap colCellAccess = new HashMap();
			HashMap accessQueryXrowSet = new HashMap();
//			int defaultAccess = 2;

			if (accessTableId > 0)
			{
				Integer defAccess = new Integer(2);
				colCellAccess = TableViewManager.getColumnAcccess( connection, gridId, accessTableId, userId);
				Iterator columnConditionalAccessIter = colCellAccess.keySet().iterator();
				while (columnConditionalAccessIter.hasNext())
				{
					Integer colId = (Integer) columnConditionalAccessIter.next();
					if (colCellAccess.get(colId) instanceof String){
						String accessString = (String) colCellAccess.get(colId);
						Pattern pattern = Pattern.compile("(\\d)(\\?.*)");
						Matcher matcher = pattern.matcher(accessString);
						if(matcher.matches())
						{
							int access = Integer.parseInt(matcher.group(1));
							String accessInstr = matcher.group(2);
							System.out.println("column acess for colid = " + colId + " is " + access +
									" if row matches accessQuery = " + accessInstr);

							HashSet rowSet = (HashSet) accessQueryXrowSet.get(accessInstr);
							if (rowSet == null)
							{
								String rowQuery = TableViewManager.getRowQuery(connection, TableViewManager.getCriteriaForDynamicView(accessInstr), gridId, true, "RESULTSET");
								rowQuery = rowQuery + " IF OBJECT_ID('tempdb..#CELL_TEMP') IS NOT NULL DROP TABLE #CELL_TEMP "; //Modified by Lakshman on 20171108 to avoid self joins on BW_CELL for performance gain
			
								PreparedStatement stmt = connection.prepareStatement(rowQuery);
								ResultSet rs = stmt.executeQuery();
								while (rs.next())
								{
									int rowId = rs.getInt(1);
									if (rowSet != null) {
										rowSet.add(new Integer(rowId));
									}
									else
									{
										rowSet = new HashSet();
										rowSet.add(new Integer(rowId));
										accessQueryXrowSet.put(accessInstr, rowSet);
									}
								}
								System.out.println("rowSet = " + rowSet);
							}
						}
					}
				}
				defaultAccess = (Integer) colCellAccess.get(new Integer(-1));
				System.out.println("processHeader():defaultAccess = " + defaultAccess);
			}		//end of  (accessTableId > 0)
			//Checking Access is done.  End of processHeader FOR SUBMIT ENDS HERE
			
			// Start a transaction
			tm = new TransactionManager(connection, userId);
			tid = tm.startTransaction("Export Changes on GridId = " + gridId, "PUT GRID");
		
			//Processing Columns Arrays i.e. gridColumns
			boolean setDefaultAccess = false;
			boolean ExceptionAdministerColumns = false;
			boolean newColsAdded = false;

			System.out.println("Processing newColArray()");
			
			Column newco;
			int newColid =-1;
			String newColName = null;
			int prevNewColId = -1;
			int prevColId = -1;
			int prevColOffset = -1;
			int newColSeq = -1;
			for (int cn = 0; cn < newColArray.size(); cn = cn + 1)
			{
				newco = newColArray.get(cn);
				
				prevColId = newco.getPreviousColumnid();
				if (prevColId == -1)
					prevColId = prevNewColId;
				
				prevColOffset = newco.getPreviousColumnSequence().intValue();
				newColName = newco.getName();
				newColid = newco.getId();
				newColSeq = newco.getSeqNo().intValue();
				
				if (newColid == -1)
				{
					if (canAdministerColumns)
					{
						try
						{
							TableManager.lockTableForUpdate(connection, gridId);
						}
						catch (SQLException sq)
						{
							xlErrorCells.add(new xlErrorNew(gridId, 0, 0, 12008));
							System.out.println("Exception in TableManager.lockTableForUpdate");
							erb = new ErrorRequestObject();
							erb.setError("TABLE UPDATE EXCEPTION (12008)" );
							erb.setPath("GridManagement.gridPut::TableManager.lockTableForUpdate");
							erb.setProposedSolution("The table is being updated by another user, Please try later");
							ErrResps.add(erb);
							return;
						}
						
						try
						{
							System.out.println(" TableManager.createColumnXL-  prevColId:" + prevColId + ", newColName:" + newColName +  ", prevColId:" + prevColId + ", prevColOffset:" + prevColOffset + ", tid:" + tid);
							newColid = TableManager.createColumnXL(
													connection,
													gridId,
													newColName,
													prevColId,
													prevColOffset,
													tid
													);
							newColsAdded = true;
							//pcOffset++;
							prevNewColId = newColid;
						}
						catch (Exception e)
						{
							// unique column violation
							xlErrorCells.add(new xlErrorNew(gridId, 0, prevColId, 12001));
							//System.out.println("unique column violation");
							//throw new BoardwalkException(12001);
							//TABLE UPDATE EXCEPTION", 5 ,"Columns are not unqiue",  "Please make sure the columns are unique"));

							erb = new ErrorRequestObject();
							erb.setError("TABLE UPDATE EXCEPTION : Unique Column violation" );
							erb.setPath("GridManagement.gridPut::TableManager.createColumnXL");
							erb.setProposedSolution("Columns are not unqiue\",  \"Please make sure the columns are unique");
							ErrResps.add(erb);
							return;
						}
					}
					else
					{
						xlErrorCells.add(new xlErrorNew(gridId, 0, prevColId, 12010));
						erb = new ErrorRequestObject();
						erb.setError("TABLE UPDATE EXCEPTION (12010): No access to add a new Column" );
						erb.setPath("GridManagement.gridPut::canAdministerColumns=FALSE");
						erb.setProposedSolution("You don't access to add a new column, Please contact the owner of the Table");
						ErrResps.add(erb);
						return;
					}
				}
				
				if (colIdHash.get(new Integer(newColSeq)) == null)
				{
					System.out.println("Adding colIdHash : newColSeq : " + newColSeq + ", newColid:" + newColid);
					colIdHash.put(new Integer(newColSeq), new Integer(newColid));
				}
			}
			
			
			if (newColsAdded == true)
			{
				if (intCritical <= 0)
				{
					if ((intCriticalLevel & (1 << 1)) == (1 << 1))
					{
						intCritical = 1;
						System.out.println("Transaction critical because columns added");
					}
				}
				TableManager.resequenceColumns(connection, gridId);
			}
			
			ArrayList<Integer> retDelColArray = new ArrayList<Integer>();
			ArrayList<Integer> retDelRowArray = new ArrayList<Integer>();
			
			System.out.println("Processing delColArray: of size " + delColArray.size());
			//Delete Columns	FOR SUBMIT STARTS
			if (delColArray.size() > 0 ) 
			{
				if (canAdministerColumns == false)
				{
					for(int id = 0; id < delColArray.size()-1; id=id+1 )
					{
						int delColId = delColArray.get(id);
						xlErrorCells.add(new xlErrorNew(gridId, 0, delColId, 12010));
						if (ExceptionAdministerColumns == false)
							ExceptionAdministerColumns = true;
						
						erb = new ErrorRequestObject();
						erb.setError("TABLE UPDATE EXCEPTION (12010): No access to Delete a Column" );
						erb.setPath("GridManagement.gridPut::canAdministerColumns=FALSE");
						erb.setProposedSolution("You don't access to Delete a column, Please contact the owner of the Table");
						ErrResps.add(erb);
						//return;
						System.out.println("TABLE UPDATE EXCEPTION (12010): No access to Delete a Column: ClumnId :" + delColId );
					}
				}
				else
				{
					for(int id = 0; id < delColArray.size(); id=id+1 )
					{
						int delColId = delColArray.get(id);
						if (colIdHash.get(new Integer(delColId)) != null)		// NOT REQUIRED AS THERE IS NO HASH OF EXISTING COLUMNS Rahul 15-JUNE-2018
							colIdHash.remove(new Integer(delColId));			// NOT REQUIRED AS THERE IS NO HASH OF EXISTING COLUMNS Rahul 15-JUNE-2018

						System.out.println("Calling ColumnManager.deleteColumn : delColId :" + delColId  + ", tid:" + tid);
						colsDeleted = ColumnManager.deleteColumn(connection, delColId, tid);
						System.out.println("After ColumnManager.deleteColumn : colsDeleted :" + colsDeleted);
						if (colsDeleted)
							retDelColArray.add(delColId);

					}
					
					if (intCritical <= 0)
					{
						if ((intCriticalLevel & (1 << 2)) == (1 << 2))
						{
							intCritical = 1;
							System.out.println("Transaction critical because columns deleted");
						}
					}
				}
			}
			// End of Columns Delete FOR SUBMITS ENDS

			//START OF NEW ROWS FOR SUBMIT
			if (newRowArray.size() > 0) 		// If new Rows exists. Send from Client array of -1s
			{
				// Adding New Rows
				if (canAddRows == false)
				{
					xlErrorCells.add(new xlErrorNew(gridId,  -1, -1, 12012));
					ExceptionAddRows = true;
					System.out.println("No access to add rows");
					erb = new ErrorRequestObject();
					erb.setError("TABLE UPDATE EXCEPTION (12012): No access to Add a Row" );
					erb.setPath("GridManagement.gridPut::canAddRows=FALSE");
					erb.setProposedSolution("You don't have access to add a new row,  Please resolve errors and try again");
					ErrResps.add(erb);
					//return;
				}
				else
				{
					int prevNewRowId = -1;
					int prevRowId = -1;
					int prOffset = 1;
					int rwSeq = -1;
					Vector nrv = new Vector();
					boolean isDeletedRow = false;

					for (int rni = 0; rni < newRowArray.size(); rni = rni + 1)
					{
						rw = newRowArray.get(rni);
						int rwId = rw.getId();
						prOffset = rw.getPreviousRowSequence();
						prevRowId = rw.getPreviousRowid();
						rwSeq = rw.getSeqNo();
						
						if (rwId == -1)
						{
							try
							{
								TableManager.lockTableForUpdate(connection, gridId);
							}
							catch (SQLException sq)
							{
								xlErrorCells.add(new xlErrorNew(gridId, 0, 0, 12008));
								//throw new BoardwalkException(12008);
								System.out.println("Exception in TableManager.lockTableForUpdate");
								erb = new ErrorRequestObject();
								erb.setError("TABLE UPDATE EXCEPTION (12008)" );
								erb.setPath("GridManagement.gridPut::TableManager.lockTableForUpdate");
								erb.setProposedSolution("The table is being updated by another user, Please try later");
								ErrResps.add(erb);
								return;
							}
	
							newXLRow nr = new newXLRow(prevRowId, prOffset, rwSeq);
							nrv.addElement(nr);
							newRowsAdded = true;
							prOffset++;
						}
						
					}	// End of ForLOOP newRowArray			/// ORINIVALLY gridRows.size()

					//Processing Newly Added Rows
					if (newRowsAdded == true)
					{
						System.out.println("Calling INSERT INTO BW_NEW_ROW ..one by one");
						// add the new rows
						String query =
							"INSERT INTO BW_NEW_ROW " +
							"(PREV_ROW_ID, TX_ID, OFFSET) " +
							"VALUES " +
							"(?, ?, ?) ";
						PreparedStatement stmt = connection.prepareStatement(query);
						Iterator nri = nrv.iterator();
						while (nri.hasNext())
						{
							newXLRow nr = (newXLRow)nri.next();
							stmt.setInt(1, nr.getPreviousRowId());
							stmt.setInt(2, tid);
							stmt.setInt(3, nr.getIndex());
							stmt.addBatch();
							System.out.println("Calling INSERT INTO BW_NEW_ROW  --> nr.getPreviousRowId():" + nr.getPreviousRowId() + ", tid:" + tid + "nr.getIndex():" + nr.getIndex() );
						}
						int[] rescnt = stmt.executeBatch();
						stmt.clearBatch();
						stmt.close();
						stmt = null;
						//System.out.println("Time to create new rows in tmp table = " + getElapsedTime());
						System.out.println("Calling {CALL BW_CR_ROWS_XL(?,?,?)}");

						query = "{CALL BW_CR_ROWS_XL(?,?,?)}";
						CallableStatement cstmt = connection.prepareCall(query);
						cstmt.setInt(1, gridId);
						cstmt.setInt(2, userId);
						cstmt.setInt(3, tid);
						cstmt.executeUpdate();
						cstmt.close();
						cstmt = null;
						//System.out.println("Time to create new rows = " + getElapsedTime());

						// resequence the rows
						TableManager.resequenceRows(connection, gridId);
						//System.out.println("Time to resequence rows = " + getElapsedTime());

						
						System.out.println("Calling SELECT BW_ROW.ID, BW_ROW.NAME FROM BW_ROW WHERE TX_ID = " + tid);
						// create the buffer
						//query = "SELECT BW_ROW.ID, BW_ROW.NAME FROM BW_ROW WHERE TX_ID = ?";
						query = "SELECT BW_ROW.ID, BW_ROW.NAME, BW_ROW.BW_TBL_ID, BW_ROW.TX_ID, BW_ROW.SEQUENCE_NUMBER, BW_ROW.IS_ACTIVE, BW_ROW.OWNER_ID, BW_ROW.OWNER_TID FROM BW_ROW WHERE TX_ID = ?";
						stmt = connection.prepareStatement(query);
						stmt.setInt(1, tid);
						ResultSet rs = stmt.executeQuery();

						HashMap rdata;
						
						while (rs.next())
						{
							int rid = rs.getInt(1);
							int ridx = Integer.parseInt(rs.getString(2));
							System.out.println("Adding row into rowIdHash ridx :" + ridx + " ..... rid:" + rid);
							rowIdHash.put(new Integer(ridx), new Integer(rid));

							rdata = new HashMap();
							rdata.put("rowId", rs.getInt(1));
							rdata.put("name", rs.getString(2));
							rdata.put("txId", rs.getInt(4));
							rdata.put("sequenceNumber", rs.getFloat(5));
							rdata.put("isActive", rs.getInt(6));
							rdata.put("ownerId", rs.getInt(7));
							rdata.put("ownerTid", rs.getInt(8));
							newRowsHash.put(new Integer(ridx), rdata);
						}

						stmt.close();
						rs.close();
						stmt = null;
						rs = null;
//						System.out.println("Time to read back new rows = " + getElapsedTime());

						// resequence the rows   -- RESEQUENCE IS MOVE BEFORE CREATING newRwsHash to get correct SequenceNumber. It was returning 1 less than actual value as Float converted to Math.round()
//						TableManager.resequenceRows(connection, gridId);
						//System.out.println("Time to resequence rows = " + getElapsedTime());

						if (intCritical <= 0)
						{
							System.out.println("critical level = " + intCriticalLevel);
							System.out.println("(1<<3) = " + (1 << 3));
							System.out.println("(criticalLevel & (1 << 3)) = " + (intCriticalLevel & (1 << 3)));
							if ((intCriticalLevel & (1 << 3)) == (1 << 3))
							{
								intCritical = 1;
								System.out.println("Transaction critical because rows added");
							}
						}
					}	// End of Processing newly added Rows
				}	// End of canAddRows=true
			}		// end of newRowArray.size() 			END OF NEW ROWS FOR SUBMIT

			// delete the rows STARTS for SUBMIT
			System.out.println("delRowArray.size() :" + delRowArray.size() );
			if (delRowArray.size() > 0 )
			{
				if (canDeleteRows == false)
				{
					for (int drc=0; drc < delRowArray.size(); drc=drc+1  )
					{
						xlErrorCells.add(new xlErrorNew(gridId, delRowArray.get(drc), 0, 12013));
						ExceptionDeleteRows = true;
					}
					System.out.println("No access to Delete rows");
					erb = new ErrorRequestObject();
					erb.setError("TABLE UPDATE EXCEPTION (12010): No access to Delete a Row" );
					erb.setPath("GridManagement.gridPut::canDeleteRows=FALSE");
					erb.setProposedSolution("You don't access to Delete a row, Please contact the owner of the Table");
					ErrResps.add(erb);
					return;
				}
				else
				{
					for (int drc=0; drc < delRowArray.size(); drc=drc+1  )
					{
						System.out.println("Adding to xlDeleteRows  :" + delRowArray.get(drc)  );
						xlDeleteRows.addElement( delRowArray.get(drc) );
					}

					rowsDeleted = RowManager.deactivateRows( connection, xlDeleteRows, tid);
					System.out.println("rowsDeleted  :" + rowsDeleted);

					if (rowsDeleted)
					{
						for (int drc=0; drc < delRowArray.size(); drc=drc+1  )
						{
							System.out.println("Adding to retDelRowArray :" + delRowArray.get(drc));
							retDelRowArray.add(delRowArray.get(drc));
						}
					}
				
					if (intCritical <= 0)
					{
						if ((intCriticalLevel & (1 << 4)) == (1 << 4))
						{
							intCritical = 1;
							System.out.println("Transaction critical because rows deleted");
						}
					}
				}
			}	// End of Delete Rows FOR SUBMIT
			
			// Start of Process Cells
			
			
			if (cellArr.size() > 0 )
			{
				int numCellsChanged = 0;
				Cell ccd;

				String query = " INSERT INTO BW_RC_STRING_VALUE VALUES(?, ?, ?, ?, ?, ?) ";
				PreparedStatement stmt = connection.prepareStatement(query);

				int batchSize = 10000;
				int batchCounter = 0;

				String xlcellval = null;
				String xlFormula = null;
				int xlRowIdx = -1;
				int xlColIdx = -1;
				int xlRowId = -1;
				int xlColId = -1;
				int cellChangeFlag = 1;
				int ccount = 0;
				
				for (int ccdc=0; ccdc < cellArr.size(); ccdc=ccdc+1)
				{
					ccd = cellArr.get(ccdc);
					xlRowIdx = ccd.getRowSequence();
					xlColIdx = ccd.getColSequence();
					xlcellval = ccd.getCellValue();
					xlFormula = ccd.getCellFormula();
					cellChangeFlag = ccd.getChangeFlag();
					
					System.out.println("Processing Cells: xlRowIdx:"+ xlRowIdx + ", xlColIdx:" + xlColIdx + ", xlcellval:" + xlcellval + ", xlFormula:" + xlFormula + ", cellChangeFlag:" + cellChangeFlag) ;
					switch(cellChangeFlag)
					{
						case 1:			//value change
							xlRowId = ccd.getRowId();
							xlColId = ccd.getColId();
							break;
							
						case 2:			//formula change
							xlRowId = ccd.getRowId();
							xlColId = ccd.getColId();
							break;
							
						case 4:			//new row
							xlRowId = ((Integer)rowIdHash.get(new Integer(xlRowIdx))).intValue();
							xlColId = ccd.getColId();
							//ccd.setRowId(xlRowId);
							break;
							
						case 8:			//new column
							xlRowId = ccd.getRowId();
							if (xlRowId == -1)			// Cell at the intersection of newly added row and column
							{
								xlRowId = ((Integer)rowIdHash.get(new Integer(xlRowIdx))).intValue();
								ccd.setRowId(xlRowId);
							}
							xlColId = ((Integer)colIdHash.get(new Integer(xlColIdx))).intValue();
							//ccd.setColId(xlColId);
							break;
							
						case 16:		//row deleted		NOT PART OF CELL ARRAY		
						case 32:		//col deleted		NOT PART OF CELL ARRAY
					}

					System.out.println("@@@@@@@  Cell Details: xlRowId:" + xlRowId + ", xlColId:" + xlColId + ", xlRowIdx:" + xlRowIdx + ", xlColIdx:" + xlColIdx );
					
					int ColAcess = defaultAccess; // assuming column access is implemented in client
					if (accCols.get(new Integer(xlColId)) != null)
						ColAcess = ((Integer)accCols.get(new Integer(xlColId))).intValue();
					
					// override access from access table
					if (colCellAccess != null && colCellAccess.size() > 0)
					{ 
						Object cA = colCellAccess.get(new Integer(xlColId));
						if (cA != null)
						{
							if (cA instanceof Integer)
							{
								ColAcess = ((Integer)cA).intValue();
								System.out.println("column acess for colid = " + xlColId + " is " + ColAcess);
							}
							else
							{
								String accessString = (String)cA;
								System.out.println(accessString);
								Pattern pattern = Pattern.compile("(\\d)(\\?.*)");
								Matcher matcher = pattern.matcher(accessString);
								System.out.println("match count = " + matcher.groupCount());
								if(matcher.matches())
								{
									int access = Integer.parseInt(matcher.group(1));
									String accessInstr = matcher.group(2);
									System.out.println("column access for colid = " + xlColId + " is " + access +
											" if row matches accessQuery = " + accessInstr);
									System.out.println("Otherwise using defaultAccess = " + defaultAccess);
									System.out.println(accessQueryXrowSet.toString());
									if (((HashSet) accessQueryXrowSet.get(accessInstr)).contains(new Integer(xlRowId)))
									{
										ColAcess = access;
										System.out.println("Using access = " + ColAcess + "for cell with rowId = " + xlRowId + "matching condition " + accessInstr);
									}
								}
							}
						}
					}	// end of IF

					// If anything other change other than value change/formula change (row added, column added etc)
					if (cellChangeFlag > 2 && xlRowId > 0 && xlColId > 0)
					{
						stmt.setInt(1, xlRowId);
						stmt.setInt(2, xlColId);
						stmt.setString(3, xlcellval);
						stmt.setString(4, xlFormula);
						stmt.setInt(5, tid);
						stmt.setInt(6, cellChangeFlag);
						stmt.addBatch();
						//Updateing cells[] in Submit Cell Buffer
						//ccd.setRowId(xlRowId);
						//ccd.setColId(xlColId);
						//ccd.setTid(tid);

						cellArr.get(ccdc).setRowId(xlRowId);
						cellArr.get(ccdc).setColId(xlColId);
						cellArr.get(ccdc).setTid(tid);
						cellArr.get(ccdc).setAccess(ColAcess);
						
						numCellsChanged = numCellsChanged + 1;
						batchCounter = batchCounter + 1;
						if (batchCounter == batchSize)
						{
							int[] rescnt = stmt.executeBatch();
							stmt.clearBatch();
							batchCounter = 0;
						}
					}
					else if ((ColAcess == 2 && xlRowId > 0 && xlColId > 0) ||
							 (ColAcess == 1 && xlRowId > 0 && xlColId > 0 && xlFormula != null && cellChangeFlag == 1)) 
						// value or formula changed by user, cellChangeFlag = 1 or 2 ||
						// value changed by formula, cellChangeFlag = 1, access = 1
					{

						stmt.setInt(1, xlRowId);
						stmt.setInt(2, xlColId);
						stmt.setString(3, xlcellval);
						stmt.setString(4, xlFormula);
						stmt.setInt(5, tid);
						stmt.setInt(6, cellChangeFlag);
						stmt.addBatch();
						//Updateing cells[] in Submit Cell Buffer

						cellArr.get(ccdc).setRowId(xlRowId);
						cellArr.get(ccdc).setColId(xlColId);
						cellArr.get(ccdc).setTid(tid);
						cellArr.get(ccdc).setAccess(ColAcess);
						//ccd.setRowId(xlRowId);
						//ccd.setColId(xlColId);
						//ccd.setTid(tid);

						numCellsChanged = numCellsChanged + 1;
						batchCounter = batchCounter + 1;
						if (batchCounter == batchSize)
						{
							int[] rescnt = stmt.executeBatch();
							stmt.clearBatch();
							batchCounter = 0;
						}
					}
					else if (ColAcess == 0 || ColAcess == 1)
					{
						if (ColAcess == 1)
						{
							System.out.println("processCells():Cell access violation " +
									"rowId=" + xlRowId + " colId=" + xlColId +
									"value=" + xlcellval +
									"frmla=" + xlFormula +
									"cellChangeFlag=" + cellChangeFlag);
							if (xlRowId != -1)
							{
								xlErrorCells.add(new xlErrorNew(gridId, xlRowId, xlColId, 12016));
								//12016, "TABLE UPDATE EXCEPTION", 5 ,"You are trying to update a cell for which you do not have access"								
					        	erb = new ErrorRequestObject();
					        	erb.setError("TABLE UPDATE EXCEPTION (12016): You are trying to update a cell for which you do not have access");
					        	erb.setPath("GridManagement.gridPut::GridManagement.processSubmitRequest. GridId:" + gridId + ", RowId:" + xlRowId + ",CoumnId:" + xlColId );
								erb.setProposedSolution("You dont have the priviliges to execute this action, Please contact the owner of the table to setup necessary access control");
					        	ErrResps.add(erb);
							}
						}
						else
						{
							System.out.println("New Column without Access Right Added");
							if (xlColId != -1 || xlRowId != -1)
							{
								xlErrorCells.add(new xlErrorNew(gridId, xlRowId, xlColId, 12016));
					        	erb = new ErrorRequestObject();
					        	erb.setError("TABLE UPDATE EXCEPTION (12016): New Column without Access Right Added");
					        	erb.setPath("GridManagement.gridPut::GridManagement.processSubmitRequest");
								erb.setProposedSolution("New Column without Access Right Added, Please contact the owner of the table to setup necessary access control");
					        	ErrResps.add(erb);
							}
						}
					}
					ccount++;
				}	// end of For

				//if(numCellsChanged > 0 && ExceptionDeleteRows == false && ExceptionAddRows == false && ExceptionAdministerColumns == false)
				if (batchCounter > 0) // the last batch
				{
					int[] rescnt = stmt.executeBatch();
					stmt.clearBatch();
					System.out.print(".");
				}
				stmt.close();
				stmt = null;
				query = null;
				if (numCellsChanged > 0)
				{
					if (intCritical <= 0)
					{
						if ((intCriticalLevel & (1 << 5)) == (1 << 5))
						{
							intCritical = 1;
							System.out.println("Transaction critical because cells changed");
						}
					}
				}

			}	//End of  IF( cellChangesArray.size().......	
			//End of Process Cell FOR SUBMIT

			String query= null;
			query = "{CALL BW_UPD_CELL_FROM_RCSV(?,?,?,?)}";
			CallableStatement cstmt = connection.prepareCall(query);
			cstmt.setInt(1, tid);
			cstmt.setInt(2, importTid);
			cstmt.setInt(3, gridId);
			cstmt.setInt(4, userId);
			int updCount = cstmt.executeUpdate();
			cstmt.close();
			cstmt = null;
			
			PreparedStatement stmt = null;
			ResultSet rs = null;

			//Added by Jeetendra on 20171111 to fix Issue ID: 5120 - START
			//there could be some rows already deleted on server. We want to skip those 
			HashSet delRowsOnServer = new HashSet();				

			query = "SELECT BW_ROW.ID FROM BW_ROW WHERE BW_TBL_ID = ? AND BW_ROW.TX_ID > ? AND IS_ACTIVE = 0";
			stmt = connection.prepareStatement(query);
			stmt.setInt(1, gridId);
			stmt.setInt(2, importTid >= exportTid ? exportTid : importTid);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				delRowsOnServer.add(new Integer(rs.getInt(1)));
			}

			stmt.close();
			rs.close();
			stmt = null;
			rs = null;

			//Added by Jeetendra on 20171111 to fix Issue ID: 5120 - END
			// filter consistency check
			if (criteriaTableId > 0)
			{
				HashSet accessibleRowsAfterSubmit = new HashSet();
				
				lsRowQuery  = TableViewManager.getRowQuery(connection, gridId, userId, criteriaTableId, true, "LATEST", "RESULTSET");
				lsRowQuery = lsRowQuery + " IF OBJECT_ID('tempdb..#CELL_TEMP') IS NOT NULL DROP TABLE #CELL_TEMP "; //Modified by Lakshman on 20171108 to avoid self joins on BW_CELL for performance gain

				stmt = connection.prepareStatement(lsRowQuery);
				rs = stmt.executeQuery();
				while (rs.next())
				{
					accessibleRowsAfterSubmit.add(new Integer(rs.getInt(1)));
				}
				
				rs.close();
				stmt.close();
				
				Iterator rowIdsFromClientIter = rowIdHash.keySet().iterator();
				while (rowIdsFromClientIter.hasNext())
				{
					//System.out.println ("xlExpoortChangesService::service(): xlDeleteRows = " + xlDeleteRows.toString());
					Integer rowIdFromClient = (Integer) rowIdHash.get(rowIdsFromClientIter.next());
					if (rowIdFromClient > 0 && !accessibleRowsAfterSubmit.contains(rowIdFromClient) && !delRowsOnServer.contains(rowIdFromClient) //Modified by Jeetendra on 20171111 to fix Issue ID: 5120
							&& !xlDeleteRows.contains(rowIdFromClient)) // deleted rows will not be fetched
					{
						System.out.println("The row id = " + rowIdFromClient + " is not accessible for user");
						xlErrorCells.add(new xlErrorNew(gridId, rowIdFromClient.intValue(), 0, 12018));
						//throw new BoardwalkException(12018, "Access Filter violation");
			        	erb = new ErrorRequestObject();
			        	erb.setError("Access Filter violation ((12018)");
			        	erb.setPath("GridManagement.gridPut::");
						erb.setProposedSolution("Access Filter violation, Please contact the owner of the table to setup necessary access control");
			        	ErrResps.add(erb);
					}
				}
			}

			System.out.println("xlExportChangesService: xlErrorCells.size() " + xlErrorCells.size());
			if (xlErrorCells.size() > 0)
			{
				//throw new BoardwalkException(12011);
				// errorMessages.put( new Integer( 12011 ), new BoardwalkMessage( 12011, "TABLE UPDATE EXCEPTION", 5 ,"There are many errors",  "Please resolve errors and try again"));
	        	erb = new ErrorRequestObject();
	        	erb.setError("TABLE UPDATE EXCEPTION ((12011)");
	        	erb.setPath("GridManagement.gridPut::xlErrorCells.Size()>0");
				erb.setProposedSolution("There are many errors\",  \"Please resolve errors and try again");
	        	ErrResps.add(erb);

	        	throw new BoardwalkException(12011);
			
			}

			// if transaction is to be made critical
			if (intCritical > 0)
				tm.addSigTransaction("REST-API GRID PUT", gridId, tid);

			tm.commitTransaction();

			//////////////// GENERATE CELLBUFFER TO RETURN FOR SUBMIT REST API CALL ///////////////////////
			ArrayList<Integer> retRowArr = new ArrayList<Integer>();
			ArrayList<Integer> retColArr =  new ArrayList<Integer>();
			ArrayList<Cell> retCellArr = new ArrayList<Cell>();
			ArrayList<Row> retGridRows = new ArrayList<Row>();
			ArrayList<Column> retGridColumns = new ArrayList<Column>();
			ArrayList<SequencedCellArray> retColCellArr = new ArrayList<SequencedCellArray>();
			
			GridChangeBuffer retGcb = new GridChangeBuffer();
			//ArrayList<CellChangeDetails> retCellChangesArray =  new ArrayList<CellChangeDetails>();
			ArrayList<Column> retNewColArray = new ArrayList<Column>();
			ArrayList<Row> retNewRowArray = new ArrayList<Row>();
			
			
			if (newColArray.size() > 0)
			{
				for (int cn = 0; cn < newColArray.size(); cn = cn + 1)
				{
					newco = newColArray.get(cn);
					System.out.println("retGcb Process : newco.getSeqNo() :" + newco.getSeqNo());
					int newColId = (Integer) colIdHash.get(new Integer( newco.getSeqNo().intValue() ) );
					newColArray.get(cn).setId(newColId);
					newColArray.get(cn).setTid(tid);
				}
				retGcb.setNewColumnArray(newColArray);
			}
			else
				retGcb.setNewColumnArray(newColArray);
				

			if (newRowArray.size() > 0)
			{
				for (int rn = 0; rn < newRowArray.size(); rn = rn + 1)
				{
					rw = newRowArray.get(rn);
					int newRowId = (Integer) rowIdHash.get(rw.getSeqNo());

					HashMap rowdata = (HashMap) newRowsHash.get(rw.getSeqNo());
					
/*					rdata.put("rowId", Integer.parseInt(rs.getString(1)));
					rdata.put("name", rs.getString(2));
					rdata.put("txId", rs.getString(3));
					rdata.put("sequenceNumber", rs.getString(4));
					rdata.put("isActive", rs.getString(5));
					rdata.put("ownerId", rs.getString(6));
					rdata.put("ownerTid", rs.getString(7));
					newRowsHash.put(new Integer(ridx), rdata);
*/					
					
					rw.setId(newRowId);
					rw.setActive(  (Integer) rowdata.get("isActive") == 1 ? true : false);
					rw.setCreaterId((Integer) rowdata.get("ownerId"));
					rw.setCreationTid((Integer) rowdata.get("ownerTid"));
					rw.setOwnerId((Integer) rowdata.get("ownerId"));
					rw.setOwnershipAssignedTid((Integer) rowdata.get("ownerTid"));
					rw.setRowName((String) rowdata.get("name"));
					rw.setSeqNo(  (int) Math.round( (float) rowdata.get("sequenceNumber") ));
					rw.setTid((Integer) rowdata.get("txId"));
					
					retNewRowArray.add(rn, rw);
				}
				retGcb.setNewRowArray(retNewRowArray); 
			}
			else
				retGcb.setNewRowArray(retNewRowArray); 

			
			retGcb.setDeletedColumnArray(retDelColArray);
			retGcb.setDeletedRowArray(retDelRowArray);
			
			ginfo.setAsOfTid(tid);
			ginfo.setExportTid(tid);

			int mode = ginfo.getMode();
			int baselineId = ginfo.getBaselineId();
			//COMMENTING THIS LINE
			//GenerateGridColumnAndColumnArray(connection, gridId, userId, memberId, mode, baselineId, view, ftal,  retGridColumns, retColArr,   ErrResps);
			cbfReturn.setColumns(retGridColumns );
			cbfReturn.setColumnArray(retColArr);
			cbfReturn.setCells(cellArr);
			//cbfReturn.setCells(retCellArr);
			cbfReturn.setInfo(ginfo);
			cbfReturn.setRowArray(retRowArr);
			cbfReturn.setColumnCellArrays(retColCellArr);
			cbfReturn.setRows(retGridRows);
			cbfReturn.setGridChangeBuffer(retGcb);
			
			////////////////
			
		}
		catch (BoardwalkException bwe)
		{
			if (xlErrorCells.size() <= 0)
			{
				xlErrorCells.add(new xlErrorNew(gridId, 0, 0, bwe.getErrorCode()));
			}
			StringBuffer errorBuffer = new StringBuffer();

			for (int errorIndex = 0; errorIndex < xlErrorCells.size(); errorIndex++)
			{
				xlErrorNew excelError = (xlErrorNew)(xlErrorCells.elementAt(errorIndex));
				errorBuffer.append(excelError.buildTokenString());
			}
        	erb = new ErrorRequestObject();
        	erb.setError("Error Cells causing TABLE UPDATE EXCEPTION ((12011)");
        	erb.setPath(errorBuffer.toString());
			erb.setProposedSolution("There are many errors\",  \"Please resolve errors and try again");
        	ErrResps.add(erb);
        	
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
        catch (SystemException se)
		{
			erb = new ErrorRequestObject();
			erb.setError("SystemException:" + se.getErrorMessage() );
			erb.setPath("GridManagement.gridPut::TableManager.getTableInfo");
			erb.setProposedSolution(se.getMessage() + ", " + se.getPotentialSolution());
			ErrResps.add(erb);
			se.printStackTrace();
        }
		catch (SQLException sqe)
		{
			erb = new ErrorRequestObject();
			erb.setError("SQLException:" + sqe.getErrorCode());
			erb.setPath("GridManagement.gridPut::TableViewManager.getCriteriaTable OR TableViewManager.getColumnAcccess");
			erb.setProposedSolution(sqe.getMessage() + ", " + sqe.getCause());
			ErrResps.add(erb);
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
		}
	}

	
	//Validate membership
	public static boolean validateMemberShip(Connection connection, int memberId, int nhId, int userId, ArrayList<ErrorRequestObject> ErrResps)
	{
		boolean blnReturn = false;
		ErrorRequestObject erb;
		try
		{
			Hashtable members = getAllMemberships(connection);

			if (!members.containsKey(memberId))
			{
				System.out.println("members.containsKey(memberId) memberId : " + memberId + " --> is FALSE");
	        	erb = new ErrorRequestObject();
	        	erb.setError("Membership ID NOT FOUND");
	        	erb.setPath("GridManagement.gridPut.validateMemberShip [SUBMIT]::GridManagement.getAllMemberships");
				erb.setProposedSolution("Request with Exiting Member ID");
	        	ErrResps.add(erb);
			}
			else
			{
				MemberNode mn = (MemberNode) members.get(memberId);
				nhId = mn.getNhId();
				userId = mn.getUserId();
				blnReturn = true;
			}
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
		
		return blnReturn;
	}
	
	
    //@POST
	public static int gridPost(Grid grid, ArrayList <ErrorRequestObject> ErrResps, String authBase64String) 
	{
		int gridId = -1;
		ErrorRequestObject erb;

		// get the connection
    	Connection connection = null;
		BoardwalkConnection bwcon = null;
		
		int nhId = -1;
		int memberId = -1;
		int userId = -1;

		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
				
		if (!ErrResps.isEmpty())
		{
			return gridId;
		}

		connection = bwcon.getConnection();
		memberId = memberNh.get(0);
		nhId = memberNh.get(1);
		userId = bwcon.getUserId();

		grid.setMemberId(memberId);
	    
		int wbId = grid.getWbId();
		int collabId = grid.getCollabId();
		String tableDesc = grid.getDescription();
		String tableName = grid.getName();

		try
		{
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

    //Generate ArrayList<Column> GridColumns. ArrayList<Integer> ColArr
    public static void GenerateGridColumnAndColumnArray(Connection connection, int gridId, int userId, int memberId, int mode, int baselineId, String view, TableAccessList ftal, ArrayList <Column> gridCols, ArrayList <Integer> columnArray,  ArrayList<ErrorRequestObject> ErrResps)
    {
    	ErrorRequestObject erb ;
		int maxTransactionId = -1;
		int exportTid = -1;
		Column gridCol ;
		
		if (view == null || view.trim().equals("")) 
		{
			view = ftal.getSuggestedViewPreferenceBasedOnAccess();
			System.out.println("Suggested view pref = " + view);
		}
		System.out.println("view = " + view);
		System.out.println("baselineId = " + baselineId);
		System.out.println("mode = " + mode);

    	try
    	{

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
	//		gridCols = new ArrayList <Column>();
	//		columnArray = new ArrayList <Integer>();
		
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
    	
		//cellBufferRequest.setColumns(gridCols);
		//cellBufferRequest.setColumnArray(columnArray);
    }
    
    //Generate ArrayList<Column> GridRows. ArrayList<Integer> rowArr
    public static void GenerateGridRowAndRowArray(Connection connection, int gridId, int userId, int memberId, int nhId, int mode, int baselineId, String view, TableAccessList ftal, int criteriaTableId, ArrayList <Row> gridRows, ArrayList <Integer> rowArray,  ArrayList<ErrorRequestObject> ErrResps)
    {
    	ErrorRequestObject erb ;
    	Row gridRow;
		int maxTransactionId = -1;
    	
		// Get the rows
		String lsRowQuery = ""; // Row query String
		// is the view dynamic (not in the criteria table), starts with ?
		boolean viewIsDynamic = false;
		if (view.indexOf("?") == 0) {
			System.out.println("View is dynamic = " + view);
			viewIsDynamic = true;
		}
		TableRowInfo tbrowInfo = null;

    	try
    	{
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
			//gridRows = new ArrayList <Row>();
			//rowArray = new ArrayList <Integer>();
			
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
	//cellBufferRequest.setRows(gridRows);
	//cellBufferRequest.setRowArray(rowArray);
    }

    //Generate ArrayList <SequencedCellArray> 
    public static void GenerateGridSequencedCellArray(Connection connection, int gridId, int userId, int memberId, int nhId, int mode, int baselineId, String view,  ArrayList <SequencedCellArray> scas ,  ArrayList<ErrorRequestObject> ErrResps)
    {

    	ErrorRequestObject erb ;
    	SequencedCellArray sca;
		int maxTransactionId = -1;
		int exportTid;

		ArrayList<String> cellValues ; 
		ArrayList<String> cellfmla ;
		int columnId;
		ArrayList<Integer> colCellAccess;
		
    	PreparedStatement stmt		= null;

    	try 
    	{
    		
			TableInfo tinfo = TableManager.getTableInfo(connection, userId, gridId);
			TableAccessList ftal = TableViewManager.getSuggestedAccess(connection, tinfo, userId, memberId, nhId);
	
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
	
			
			int criteriaTableId = TableViewManager.getCriteriaTable(connection, gridId, userId);
	 		System.out.println("Using criterea table id = " + criteriaTableId);
	
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
			
			
			int accessTableId = TableViewManager.getAccessTable(connection, gridId, userId);
			HashSet restrColumnList = new HashSet();
			if (accessTableId > 0) 
			{
				System.out.println("Using access table id = " + accessTableId);
				// read the access for the user
				restrColumnList = TableViewManager.getRestrictedColumnsForImport(connection, gridId, accessTableId, userId);
			}
	
			Vector colv = ColumnManager.getXlColumnsForImport(connection, gridId, userId, memberId);
			Iterator ci = colv.iterator();
			
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
						//cellBufferRequest.setColumnCellArrays(scas);
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

	}
}
