package boardwalk.rest;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
//import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import com.boardwalk.database.Transaction;
import com.boardwalk.database.TransactionManager;
import com.boardwalk.excel.newXLRow;
import com.boardwalk.excel.xlColumn_import;
import com.boardwalk.excel.xlErrorNew;
import com.boardwalk.exception.BoardwalkException;
import com.boardwalk.exception.BoardwalkMessage;
import com.boardwalk.exception.SystemException;
import com.boardwalk.member.MemberNode;
import com.boardwalk.neighborhood.NeighborhoodManager;
import com.boardwalk.query.QueryMaker;
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
import io.swagger.model.Cell;
import io.swagger.model.CellBuffer;
import io.swagger.model.Column;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.Grid;
import io.swagger.model.GridChangeBuffer;
import io.swagger.model.GridChanges;
import io.swagger.model.GridInfo;
import io.swagger.model.NeighborhoodPath;
import io.swagger.model.Row;
import io.swagger.model.SequencedCellArray;
import io.swagger.model.StatusChange;

public class GridManagement {

    private static String CALL_BW_GET_ALL_MEMBERSHIPS_INFO = "{CALL BW_GET_ALL_MEMBERSHIPS_INFO}";
    private static int OPERATION_LINK_EXPORT = 1;
    private static int OPERATION_SUBMIT = 2;
    		

	public GridManagement()
	{	
	}

		
    //@GET
    //@Path("/{gridId}")
	public static CellBuffer gridGridIdGet(int gridId, int importTid, String view, int mode, int baselineId , ArrayList<ErrorRequestObject> ErrResps, String authBase64String, BoardwalkConnection bwcon, ArrayList<Integer> memberNh, ArrayList<Integer> statusCode )
	{
		//CellBuffer  cellBufferRequest = new CellBuffer();
		CellBuffer cbfReturn = new CellBuffer();
		ErrorRequestObject erb;

		// get the connection
    	Connection connection = null;
		
		int nhId = -1;
		int memberId = -1;
		connection = bwcon.getConnection();
		memberId = memberNh.get(0);
		nhId = memberNh.get(1);

		//System.out.println("MemberNode -> nhId :" + nhId);
		//System.out.println("MemberNode -> memberId :" + memberId);
		
    	Vector<xlErrorNew> xlErrorCells = new Vector<xlErrorNew>(); 
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
    		ArrayList<Integer> deletedColumnArray = new ArrayList<Integer>();
    		ArrayList<Integer> deletedRowArray = new ArrayList<Integer>();
    		ArrayList<Cell> gridCells  = new ArrayList<Cell>();
    		Cell gridCell;

    		int columnId;
    		
    		int tid = -1;

    		//System.out.println("GRID/PUT REST-API CALL :  memberId : " + memberId);
    		//System.out.println("GRID/PUT REST-API CALL :  gridId : " + gridId);
    		//System.out.println("GRID/PUT REST-API CALL :  gridName : " + gridName);
    		System.out.println("GRID/PUT REST-API CALL :  view : " + view);

    		// Error vector to all the Exceptions
    		//Vector xlErrorCells = new Vector(); //new Vector();
    		// access variables
    		boolean canAddRows = false;
    		boolean canDeleteRows = false;
    		boolean canAdministerColumns = false;

/*    		ArrayList<?>	columnIds = null;
    		ArrayList<?>	rowIds = null;
    		ArrayList<?>	formulaIds = null;
    		ArrayList<?>	strValIds = null;
    		String		formulaString = null;*/
    		
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
				cbfReturn = getGridRefresh  (connection, gridId, importTid, userId, memberId, nhId,  view, mode, baselineId, synch , ErrResps, statusCode);
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
				//BoardwalkMessage( 10005 -> "ACCESS EXCEPTION", 5 ,"You dont have the priviliges to execute this action ",  "Please contact the owner of the table to setup necessary access control"));
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
				HashSet<?> restrColumnList = new HashSet<Object>();
				if (accessTableId > 0) 
				{
					System.out.println("Using access table id = " + accessTableId);
					// read the access for the user
					restrColumnList = TableViewManager.getRestrictedColumnsForImport(connection, gridId, accessTableId, userId);
				}
				// Get the columns
				Vector<?> colv = ColumnManager.getXlColumnsForImport(connection, gridId, userId, memberId);
				Iterator<?> ci = colv.iterator();
				
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
				gcb.setNewColumnArray( gridCols);				//gcb is part of LINK IMPORT CELL BUFFER
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
				
				//System.out.println("RowManager.getTableRows....userId:" + userId + "nhId:" + nhId + "baselineId:" + baselineId + "view:"+ view);
				/* Condition Added By Asfak - START - 29 Jun 2014 */
				if (criteriaTableId == -1 && !viewIsDynamic) {
					System.out.println("RowManager.getTableRows....1");
					tbrowInfo = RowManager.getTableRows(connection, gridId, userId, nhId, baselineId, view, 1, -1, -1);
				} else if (criteriaTableId > 0 && viewIsDynamic) {
					System.out.println("RowManager.getTableRows....2");
					tbrowInfo = RowManager.getTableRows(connection, gridId, userId, nhId, baselineId, view, 1, -1, -1);
				} else {
					System.out.println("RowManager.getTableRows....3");
					tbrowInfo = RowManager.getTableRows(connection, gridId, userId, nhId, baselineId, view, 1, -1, -1);
				}
				Vector<?> rowv = tbrowInfo.getRowVector();
				// rows
				gridRows = new ArrayList <Row>();
				rowArray = new ArrayList <Integer>();
				
				int previousRowid = -1;
				int previousRowSequence = -1;
				for (int r = 0; r < rowv.size(); r++) 
				{
					com.boardwalk.table.Row rowObject = (com.boardwalk.table.Row) rowv.elementAt(r);
					rowArray.add(rowObject.getId());

					gridRow = new Row();
					gridRow.setCreationTid(rowObject.getCreationTid());
					gridRow.setCreaterId(rowObject.getCreatorUserId());
					gridRow.setOwnerName(rowObject.getOwnerName());
					gridRow.setOwnershipAssignedTid(rowObject.getOwnershipAssignedTid());
					gridRow.setOwnerName(rowObject.getOwnerName());
					gridRow.setOwnerId(rowObject.getOwnerUserId());
					gridRow.setActive((rowObject.getIsActive()== 1? true : false));
					gridRow.setRowName(rowObject.getName()); 	
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
				gcb.setNewRowArray(gridRows);
	    		//cellBufferRequest.setRows(gridRows);
	    		//cellBufferRequest.setRowArray(rowArray);
	    		
				// Get the cells
				String q = null;
				String rowQuery = null;
				boolean results = false;
				if (criteriaTableId == -1 && !viewIsDynamic) {
					//System.out.println("CALL BW_GET_TBL_LNK_IMPORT ---> " + gridId + "," + userId + "," + memberId + "," + nhId + "," + view);
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
					//System.out.println("CALL BW_GET_FiltredTableCELLS_DYNAMIC ---> " + gridId + "," + userId + "," + memberId + "," + view);
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
					//System.out.println("CALL BW_GET_FiltredTableCELLS_DYNAMIC ---> " + gridId + "," + userId + "," + memberId + "," + view);
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
									com.boardwalk.table.Row rowObject = (com.boardwalk.table.Row) rowv.elementAt(r);
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
										
									//Adding Cells in Link Import.
						    		//ArrayList<Cell> gridCells  = new ArrayList<Cell>();
						    		gridCell = new Cell();
									gridCell.setAccess(cellAccess);
									gridCell.setActive(true );
									gridCell.setCellFormula(cellFormula);
									gridCell.setCellValue(cellval);
									gridCell.setChangeFlag(0);
									gridCell.setColId(columnId);
									gridCell.setColSequence(colSequence);
									//gridCell.setId(id);	NOT AVAILABLE
									gridCell.setRowId(rowObject.getId()); 
									gridCell.setRowSequence((int) rowObject.getSequenceNumber());
									gridCell.setTid(celltid);
									gridCells.add(gridCell);
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
				Vector<?> NhPathIds = com.boardwalk.neighborhood.NeighborhoodManager.getBoardwalkPathIds( connection , nhId );
				
				NeighborhoodPath nhpath = new NeighborhoodPath();
				int nhLevels = NhPathIds.size()-1;
				nhpath.setLevels(nhLevels);

				String nhPathId = (String)NhPathIds.elementAt(0);
				String nhpathArr[] = nhPathId.split("\\\\");

				System.out.println("nhPathId : " + nhPathId);

				switch (nhLevels)
				{
					case 3:
						nhpath.setNhLevel0(Integer.parseInt(nhpathArr[0]) );
						nhpath.setNhLevel1(Integer.parseInt(nhpathArr[1]) );
						nhpath.setNhLevel2(Integer.parseInt(nhpathArr[2]) );
						nhpath.setNhLevel3(Integer.parseInt(nhpathArr[3]) );
						break;
						
					case 2:
						nhpath.setNhLevel0(Integer.parseInt(nhpathArr[0]) );
						nhpath.setNhLevel1(Integer.parseInt(nhpathArr[1]) );
						nhpath.setNhLevel2(Integer.parseInt(nhpathArr[2]) );
						nhpath.setNhLevel3(-1);
						break;
						
					case 1:
						nhpath.setNhLevel0(Integer.parseInt(nhpathArr[0]) );
						nhpath.setNhLevel1(Integer.parseInt(nhpathArr[1]) );
						nhpath.setNhLevel2(-1);
						nhpath.setNhLevel3(-1);
						break;
						
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
				ginfo.setFilter("");
				ginfo.setAsOfTid(maxTransactionId);
				ginfo.setBaselineId(baselineId);
				ginfo.setServerName("");
				ginfo.setServerURL("");

			} 	//GET TABLE BUFFER TRY ENDS
			catch (Exception e) 
			{
				e.printStackTrace();
				erb = new ErrorRequestObject();
				erb.setError("Exception:" + e.getMessage() );
				erb.setPath("GridManagement.gridGridIdGet: getTableBuffer Block");
				erb.setProposedSolution("Contact Administrator");
				ErrResps.add(erb);
				statusCode.add(500);	//500: Server-side Error. Exception thrown from GridManagement.gridGridIdGet [getTableBuffer] Block
				return cbfReturn ;
			}
		
		
			gcb.setDeletedColumnArray(deletedColumnArray);	//gcb added in LINK IMPORT CELLBUFFER
			gcb.setDeletedRowArray(deletedRowArray);		//gcb added in LINK IMPORT CELLBUFFER
			gcb.setCritical(-1);
			gcb.setCriticalLevel(-1);
			cbfReturn.setCells(gridCells);					//gridCells added in LINK IMPORT CELLBUFFER
			cbfReturn.setColumnArray(columnArray );
			cbfReturn.setColumnCellArrays(scas);
			cbfReturn.setColumns(gridCols);
			cbfReturn.setGridChangeBuffer(gcb);
			cbfReturn.setInfo(ginfo);
			cbfReturn.setRowArray(rowArray);
			cbfReturn.setRows(gridRows);
			cbfReturn.setGridChangeBuffer(gcb);				
        	statusCode.add(200);		//200: Success. Returns cellBuffer
			return cbfReturn ;
		}
		//Custom code Ends
		catch (BoardwalkException bwe)
		{
			StringBuffer  errorBuffer  = new StringBuffer();
			if( xlErrorCells.size() > 0 )
			{
				for ( int errorIndex = 0; errorIndex< xlErrorCells.size(); errorIndex++ )
				{
					xlErrorNew excelError = (xlErrorNew)(xlErrorCells.elementAt(errorIndex));
					errorBuffer.append( excelError.buildTokenString() );
				}
			}
        	erb = new ErrorRequestObject();
        	erb.setError("TABLE ACCESS EXCEPTION (10005). " + bwe.getMessage());
        	erb.setPath(errorBuffer.toString());
			erb.setProposedSolution(bwe.getPotentialSolution());
        	ErrResps.add(erb);
			//BoardwalkMessage( 10005 -> "ACCESS EXCEPTION", 5 ,"You don't have the privileges to execute this action ",  "Please contact the owner of the table to setup necessary access control"));
        	statusCode.add(403);		//403: Forbidden. User don't have the privileges to execute this action.
			return cbfReturn ;
		}
		catch ( SystemException s)
		{
			System.out.println("SystemException thrown in GridManagement.gridGridIdGet:TableManager.getTableInfo()");
			s.printStackTrace();
			erb = new ErrorRequestObject();
			erb.setError("SystemException: " + s.getErrorMessage());
			erb.setPath("GridManagement.gridGridIdGet:TableManager.getTableInfo");
			erb.setProposedSolution(s.getPotentialSolution());
			ErrResps.add(erb);
			statusCode.add(500);	//500: Server-side Error. SystemException thrown from GridManagement.gridGridIdGet:TableManager.getTableInfo
			return cbfReturn ;
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
//		System.out.println("cbfReturn.toString() as follows...................");
//		System.out.println(cbfReturn.toString());
//		System.out.println("End of PRINTING cbfReturn.toString() ...................END");
		//System.out.println("authBase64String ..................." + authBase64String);
		// Decode data on other side, by processing encoded data
		//byte[] valueDecoded = Base64.decodeBase64(authBase64String);
		//System.out.println("Decoded value is " + new String(valueDecoded));		
		//return cbfReturn ;
	}	// end of GET GRID LINK IMPORT
		

	//Returns Grid Refresh CellBuffer 
	public static CellBuffer getGridRefresh(Connection connection, int gridId, int importTid, int userId, int memberId, int nhId,  String view, int mode, int baselineId, int synch , ArrayList<ErrorRequestObject> ErrResps, ArrayList<Integer> statusCode)
	{
		ErrorRequestObject erb;
		CellBuffer cbfReturn = new CellBuffer();
		Vector<xlErrorNew> xlErrorCells = new Vector<xlErrorNew>(); 
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

		//All Rows and Columns in Grid
		ArrayList<Column> allColumnArray = new ArrayList<Column>();
		ArrayList<Row> allRowArray = new ArrayList<Row>();

		ArrayList<String> cellValues  = new ArrayList<String>();
		ArrayList<String> cellfmla  = new ArrayList<String>();
		ArrayList<Integer> colCellAccess  = new ArrayList<Integer>();
		ArrayList <SequencedCellArray> scas = new ArrayList<SequencedCellArray>();
		GridChangeBuffer gcb ;
		GridInfo ginfo = new GridInfo();
	
		int rowCount;		//Number of Rows in Grid
		int colCount;		//Number of Columns in Grid
    	try
    	{

			int maxTransactionId = importTid;
			int criteriaTableId = TableViewManager.getCriteriaTable(connection, gridId, userId);
			//System.out.println("Using criterea table id = " + criteriaTableId);
	
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
			HashSet<?> restrColumnList = new HashSet<Object>();
			if (accessTableId > 0)
			{
				//System.out.println("Using access table id = " + accessTableId);
				// read the access for the user
				restrColumnList = TableViewManager.getRestrictedColumnsForImport(connection, gridId, accessTableId, userId);
			}
	
			// Get the columns
			Vector<?> colv = ColumnManager.getXlColumnsForImport(connection, gridId, userId, memberId);
			HashMap<Integer, xlColumn_import> colHash = new HashMap<Integer, xlColumn_import>();
			//ColObjsByColId = new HashMap();
			Iterator<?> ci = colv.iterator();
			//while (ci.hasNext())
			//{
			//    xlColumn_import coli = (xlColumn_import)ci.next();
			//    ColObjsByColId.put(new Integer(coli.getId()), coli);
			//}
			// columns
	
			newGridCols = new ArrayList<Column>();
			Column gridCol;
			Column allGridCol;
			int previousColId =-1;
			int previousColSequence = -1;
			
			colCount = colv.size();
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

				columnArray.add(col.getId());

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

				//For all Columns Creation TxId is used.
				allGridCol = new Column();
				allGridCol.setActive(true);
				allGridCol.setId(col.getId());
				allGridCol.setName(col.getName());
				allGridCol.setPreviousColumnid(previousColId);
				allGridCol.setPreviousColumnSequence(new BigDecimal(previousColSequence));
				allGridCol.setSeqNo(new BigDecimal(col.getSequenceNumber()));
				allGridCol.setTid(col.getCreationTid());
				allColumnArray.add(allGridCol);
				
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
			Vector<?> rowv = tbrowInfo.getRowVector();
	
			Hashtable<?, ?> rowHash = tbrowInfo.getRowHash();
	
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
			Row allGridRow;
			
			rowCount = rowv.size();
			int previousRowid = -1;
			int previousRowSequence = -1;
			for (int r = 0; r < rowv.size(); r++)
			{
				com.boardwalk.table.Row rowObject = (com.boardwalk.table.Row) rowv.elementAt(r);
				int rowId = rowObject.getId();
				rowArray.add(rowId);
			
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
					gridRow.setOwnerId(rowObject.getOwnerUserId());
					gridRow.setActive((rowObject.getIsActive()== 1? true : false));
					gridRow.setRowName(rowObject.getName()); 	
		    		gridRow.setId(rowObject.getId()); 
		    		gridRow.setPreviousRowid(  previousRowid);
		    		gridRow.setPreviousRowSequence(previousRowSequence);
		    		gridRow.setTid((rowObject.getCreationTid() >= rowObject.getOwnershipAssignedTid())   ? rowObject.getCreationTid() : rowObject.getOwnershipAssignedTid());
		    		Float obj = new Float(rowObject.getSequenceNumber());
		    		gridRow.setSeqNo(obj.intValue()); 
		    		newGridRows.add(gridRow);
				}
				else
				{
					//Added for CELLBUFFER.ROWS will contain all Rows of Grid
					//resData.append(rowId + Seperator);
					//resData.append(Seperator);
					numSrvrRows++;
				}

				//Adding into All Rows Array
				allGridRow = new Row();
				allGridRow.setCreationTid(rowObject.getCreationTid());
				allGridRow.setCreaterId(rowObject.getCreatorUserId());
				allGridRow.setOwnerName(rowObject.getOwnerName());
				allGridRow.setOwnershipAssignedTid(rowObject.getOwnershipAssignedTid());
				allGridRow.setOwnerId(rowObject.getOwnerUserId());
				allGridRow.setActive((rowObject.getIsActive()== 1? true : false));
				allGridRow.setRowName(rowObject.getName()); 	
				allGridRow.setId(rowObject.getId()); 
				allGridRow.setPreviousRowid(  previousRowid);
				allGridRow.setPreviousRowSequence(previousRowSequence); 
				allGridRow.setTid((rowObject.getCreationTid() >= rowObject.getOwnershipAssignedTid())   ? rowObject.getCreationTid() : rowObject.getOwnershipAssignedTid());
	    		Float obj = new Float(rowObject.getSequenceNumber());
	    		allGridRow.setSeqNo(obj.intValue()); 
	    		allRowArray.add(allGridRow);
				
				//Float obj = new Float(rowObject.getSequenceNumber());
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
	
			//System.out.println("tableId = " + gridId);
			//System.out.println("userId = " + userId);
			//System.out.println("memberId = " + memberId);
			//System.out.println("nhId = " + nhId);
			//System.out.println("view = " + view);
			//System.out.println("importTid = " + importTid);
			//System.out.println("newTid = " + tid);
			//System.out.println("mode = " + mode);
			//System.out.println("synch = " + synch);
	
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
				if (rowHash.get(new Integer(rowId)) == null)
					continue;
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
				cl.setActive(true );
				//cl.setRowSequence(rowSequence);
				//cl.setColSequence((int) col.getSequenceNumber());
				//cl.setId(id);
				cl.setChangeFlag(-1);
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
					{
						if (rowHash.get(new Integer(rs1.getInt(1))) == null)
							deletedRowArray.add(rs1.getInt(1));
						else
							continue;
					}
					else if(rs1.getString(2).equals("C"))
					{
						
						//Added to fix Refresh issue when the Column Access is applied from Template side 2016/11/16
						if (restrColumnList != null && restrColumnList.size() > 0)
						{
							if (restrColumnList.contains(new Integer(rs1.getInt(1))))
								System.out.println("Skip restricted column :" + rs1.getInt(1));
							else
								deletedColumnArray.add(rs1.getInt(1)); 
						}
						else
							deletedColumnArray.add(rs1.getInt(1)); 
/*						xlColumn_import col = (xlColumn_import)colHash.get(new Integer(rs1.getInt(1)));
						if (col == null)
							deletedColumnArray.add(rs1.getInt(1)); 
						else
							continue;  */					
					}
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

			NeighborhoodPath nhpath = new NeighborhoodPath();
			nhpath = getNeighborhoodPath(connection, nhId);
			ginfo.setNeighborhoodHeirarchy( nhpath);
			
			ginfo.setNhId(nhId);
			ginfo.setCriteriaTableId(criteriaTableId);
			ginfo.setMode(mode);
			ginfo.setCollabId(tinfo.getCollaborationId());
			ginfo.setWbId(tinfo.getWhiteboardId());
			ginfo.setMaxTxId(maxTransactionId);
			ginfo.setImportTid(maxTransactionId);
			ginfo.setRowCount(rowCount);
			ginfo.setColCount(colCount);
			ginfo.setServerName("");
			ginfo.setServerURL("");
			ginfo.setFilter("");
			ginfo.setBaselineId(-1);
			ginfo.setAsOfTid(maxTransactionId);
			
	    }
		catch (BoardwalkException bwe)
		{
			//BoardwalkException(10005);
			StringBuffer  errorBuffer  = new StringBuffer();
			if( xlErrorCells.size() > 0 )
			{
				for ( int errorIndex = 0; errorIndex< xlErrorCells.size(); errorIndex++ )
				{
					xlErrorNew excelError = (xlErrorNew)(xlErrorCells.elementAt(errorIndex));
					errorBuffer.append( excelError.buildTokenString() );
				}
			}
        	erb = new ErrorRequestObject();
        	erb.setError("TABLE ACCESS EXCEPTION (10005). " + bwe.getMessage());
        	erb.setPath(errorBuffer.toString());
			erb.setProposedSolution(bwe.getPotentialSolution());
        	ErrResps.add(erb);
			//BoardwalkMessage( 10005 -> "ACCESS EXCEPTION", 5 ,"You don't have the privileges to execute this action ",  "Please contact the owner of the table to setup necessary access control"));
        	statusCode.add(403);		//403: Forbidden. User don't have the privileges to execute this action.
			return cbfReturn ;
		}	
    	catch (SQLException sqe)
		{
    		System.out.println("SQLException in GridManagement.getGridRefresh");
			erb = new ErrorRequestObject();
			erb.setError("SQLException:" + sqe.getErrorCode() + ", Cause:"+ sqe.getMessage());
			erb.setPath("GridManagement.getGridRefresh:: getCriteriaTable | StartTransaction | BW_IMPORT_CHANGES ");
			erb.setProposedSolution("Contact System Administrator");
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
			statusCode.add(500);			//500 : Server Error. SQLException thrown from GridManagement.gridGridIdGet::getGridRefresh -> getCriteriaTable | StartTransaction | BW_IMPORT_CHANGES 
			return cbfReturn;
		}
		catch ( SystemException s)
		{
			System.out.println("SystemException thrown in GridManagement.gridGet: Possibly from getAllMemberships() OR TableManager.getTableInfo()");
			s.printStackTrace();
			erb = new ErrorRequestObject();
			erb.setError("SystemException: " + s.getErrorMessage());
			erb.setPath("GridManagement.getGridRefresh:: TableManager.getTableInfo | ColumnManager.getXlColumnsForImport | RowManager.getTableRows");
			erb.setProposedSolution(s.getPotentialSolution());
			ErrResps.add(erb);
			statusCode.add(500);			//500 : Server Error. SystemException thrown from GridManagement.gridGridIdGet::getGridRefresh -> TableManager.getTableInfo | ColumnManager.getXlColumnsForImport | RowManager.getTableRows
			return cbfReturn;
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
		gcb.setCritical(-1);
		gcb.setCriticalLevel(-1);
		gcb.setDeletedColumnArray(deletedColumnArray);	//deleted column ids
		gcb.setDeletedRowArray(deletedRowArray);		//deleted row ids
		gcb.setNewColumnArray( newGridCols);
		gcb.setNewRowArray(newGridRows);
		
		cbfReturn.setInfo(ginfo);
		cbfReturn.setCells( gridCells);							//changed cells
		cbfReturn.setRowArray(rowArray);
		cbfReturn.setColumnArray(columnArray );
		cbfReturn.setColumnCellArrays(scas);
		cbfReturn.setColumns(allColumnArray);
		cbfReturn.setGridChangeBuffer(gcb);
		cbfReturn.setRows(allRowArray);

		statusCode.add(200);			//200 : Success. cellBuffer Returned
		return cbfReturn;
	}	// end of GET GRID REFRESH
	
    //@PUT
    //@Path("/{gridId}")
	public static CellBuffer gridPut(int gridId, CellBuffer  cellBufferRequest, ArrayList<ErrorRequestObject> ErrResps, String authBase64String, BoardwalkConnection bwcon, ArrayList<Integer> memberNh, ArrayList<Integer> statusCode)
	{
		CellBuffer cbfReturn = new CellBuffer();
		ErrorRequestObject erb;
		
		// get the connection
    	Connection connection = null;
		
		int nhId = -1;
		int memberId = -1;
		int userId = -1;

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
    		GridInfo ginfo = cellBufferRequest.getInfo();

    		if (ginfo == null)
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("Missing element info:{}");
    			erb.setPath("GridManagement.gridPut::cellBufferRequest.getInfo()");
    			erb.setProposedSolution("Add info:{} in gridPut Request");
    			ErrResps.add(erb);
    		    System.out.println("Missing element info:{}");
    		    //return cbfReturn;
    		}
    		
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

    		if (gcb == null)
    		{
    			erb = new ErrorRequestObject();
    			erb.setError("Missing element GridChangeBuffer:[]");
    			erb.setPath("GridManagement.gridPut::cellBufferRequest.getGridChangeBuffer()");
    			erb.setProposedSolution("Add GridChangeBuffer:[] in gridPut Request");
    			ErrResps.add(erb);
    		    System.out.println("Missing element GridChangeBuffer:[]");
    		    //return cbfReturn;
    		}

	    	if (ErrResps.size() > 0) 
	    	{
	    		statusCode.add(404);	//404 : Bad Request. Missing elements info | cells | rowArray | columnArray | rows | columns | columnCellArrays | GridChangeBuffer 
	    		return cbfReturn;
	    	}

    		//cellBufferRequest.getGridChangeBuffer()   -------- need to be decided
    		
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
    			int baselineId = -1;
    			processSubmitRequest(connection, userId, memberId, nhId, gridId, baselineId, cellBufferRequest, ErrResps, cbfReturn, statusCode);
    			return cbfReturn;
			}	    	

  			//OPERATION_LINK_EXPORT 
    		ArrayList<Column> newColumns = (ArrayList<Column>) gcb.getNewColumnArray();
    		ArrayList<Row> newRows = (ArrayList<Row>) gcb.getNewRowArray();
  			
    		//int numColumns = gridColumns.size();
    		//int numRows = gridRows.size();
    		
    		//numColumns and numRows are taken from GridChangeBuffer NewRowArray and NewColumnArray size.
    		int numColumns = newColumns.size();
    		int numRows = newRows.size();

    		System.out.println("GRID/PUT REST-API CALL :  numColumns : " + numColumns);
    		System.out.println("GRID/PUT REST-API CALL :  numRows : " + numRows);
    		Column cl;

    		int collabId = ginfo.getCollabId();
    		String gridName = ginfo.getName();
    		String view = "LATEST";
    		int tid = -1;

    		//System.out.println("GRID/PUT REST-API CALL :  memberId : " + memberId);
    		//System.out.println("GRID/PUT REST-API CALL :  collabId : " + collabId);
    		//System.out.println("GRID/PUT REST-API CALL :  gridId : " + gridId);
    		//System.out.println("GRID/PUT REST-API CALL :  gridName : " + gridName);
    		//System.out.println("GRID/PUT REST-API CALL :  view : " + view);
    		
    		// Error vector to all the Exceptions
    		Vector<xlErrorNew> xlErrorCells = new Vector<xlErrorNew>(); //new Vector();
    		// access variables
    		boolean canAddRows = false;
    		boolean canDeleteRows = false;
    		boolean canAdministerColumns = false;

    		ArrayList<Integer>	columnIds = null;
    		ArrayList<?>	rowIds = null;
    		ArrayList<?>	formulaIds = null;
    		ArrayList<?>	strValIds = null;
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
	    		statusCode.add(403);	//403 : Forbidden. User don't have the privileges to execute this action.
    		    return cbfReturn;
	    	}

	    	
	    	//Checking if ColumnNames are blank;	IssueId: 14288
			for (int cni = 0; cni < numColumns; cni++)
			{
				//cl = gridColumns.get(cni);
				cl = newColumns.get(cni);
				String colname = cl.getName();
				if (colname.trim().equals(""))
				{
					System.out.println("Blank Column Name ");
					erb = new ErrorRequestObject();
					erb.setError("Column Name cannot be Blank.");
					erb.setPath("Blank Grid Column at position:" + cni);
					erb.setProposedSolution("Enter Unique NonEmpty Column Name.");
					ErrResps.add(erb);
					statusCode.add(404);		//404 : Bad Request. Blank Column Name
					return cbfReturn;
				}
			}
			
			//Adding Column Start
			if(canAdministerColumns)
			{
				try 
				{
					columnIds = new ArrayList<Integer>(numColumns);
//						String[] columnNames = columnInfo.split(Seperator);
					Vector<?> columns = new Vector<Object>();
					query = " INSERT INTO BW_COLUMN " +
							   " (NAME, BW_TBL_ID, COLUMN_TYPE, SEQUENCE_NUMBER, TX_ID) " +
							   " VALUES " +
							   " (?,?,?,?,?)";

					stmt = connection.prepareStatement(query);
					// Add columns...Ignoring BWID so Starting from 1. numColumns was sent from client Ignoring bwid, so adding 1 
					for (int cni = 0; cni < numColumns; cni++)
					{
						//cl = gridColumns.get(cni);
						cl = newColumns.get(cni);
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
					statusCode.add(412);		//412 : Precondition Failed. Column Already Exists in Grid (Duplicate)
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
				gcb.getNewColumnArray().set(index, col);			//Refilling NewColumn Detailsin GridChangeBuffer with Ids
			
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
					
					int columnId = ((Integer)columnIds.get(iCol)).intValue();
					colCellArr.get(iCol).setColumnId(columnId);
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
					rw.setPreviousRowSequence(prevRowSeq);
					rw.setSeqNo(iRow+1);
					rw.setTid(tid);
					prevRowId = (Integer) rowIds.get(iRow);
					prevRowSeq = iRow;
					gridRows.set(iRow, rw);				// updating gridRows in-Place
					
					gcb.getNewRowArray().set(iRow, rw);			//Refilling NewRow Details in GridChangeBuffer with Ids
					
					System.out.println("Adding to rowArr: iRow: " + iRow + " | rowIds : " + rowIds.get(iRow));
		    		rowArr.add(iRow,(Integer) rowIds.get(iRow));		// Recreating rowArr by Adding each id into rowArr
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
			
			//GridChangeBuffer gcbRet = new GridChangeBuffer();
			gcb.setCritical(-1);
			gcb.setCriticalLevel(0);

			ginfo.setAsOfTid(tid);
			ginfo.setBaselineId(-1);
			ginfo.setColCount(numColumns);
			ginfo.setCollabId(tinfo.getCollaborationId());
			ginfo.setCriteriaTableId(-1);
			ginfo.setExportTid(tid);
			ginfo.setFilter("");
			ginfo.setId(gridId);
			ginfo.setImportTid(tid);
			ginfo.setMaxTxId(tid);
			ginfo.setMemberId(memberId);
			ginfo.setMode(1);
			ginfo.setName(tinfo.getTableName());
			NeighborhoodPath nhpath = new NeighborhoodPath();
			nhpath = getNeighborhoodPath(connection, nhId);
			ginfo.setNeighborhoodHeirarchy( nhpath);
			ginfo.setNhId(nhId);
			ginfo.setPurpose(tinfo.getTablePurpose());
			ginfo.setRowCount(numRows);
			ginfo.setServerName("");
			ginfo.setServerURL("");
			ginfo.setUserId(userId);
			ginfo.setView(view);
			ginfo.setWbId(tinfo.getWhiteboardId());
			
    		cbfReturn.setInfo(ginfo);
    		cbfReturn.setRowArray(rowArr);
    		cbfReturn.setColumnArray(colArr);
    		cbfReturn.setColumnCellArrays(colCellArr);
    		cbfReturn.setColumns(gridColumns);
    		cbfReturn.setRows(gridRows);
    		cbfReturn.setCells(cellArr);
    		cbfReturn.setGridChangeBuffer(gcb);
    		
    		statusCode.add(200);	//200 : Success. Returns cellBuffer
    		return cbfReturn;
    		//Custom code Ends
    	}
       catch ( SystemException s)
		{
        	System.out.println("SystemException MAY BE thrown by TableManager.getTableInfo");
        	s.printStackTrace();
        	erb = new ErrorRequestObject();
        	erb.setError("SystemException: " + s.getErrorMessage());
        	erb.setPath("GridManagement.gridPut:: TableManager.getTableInfo");
			erb.setProposedSolution(s.getPotentialSolution());
        	ErrResps.add(erb);
        	statusCode.add(500);			//500 : Server Error. SystemException: Failed to get TableManager.getTableInfo.
        	return cbfReturn;
		}      	
		catch (SQLException sqe)
		{
			erb = new ErrorRequestObject();
			erb.setError("SQLException:" + sqe.getCause());
			erb.setPath("GridManagement.gridPut");
			erb.setProposedSolution("Get DBConnection failed. Contact Boardwalk System Administrator");
			ErrResps.add(erb);
			sqe.printStackTrace();
        	statusCode.add(500);			//500 : Server Error. SQLException on Server
        	return cbfReturn;
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
	}//End of PUT GRID for LINK EXPORT

	//Returns nhPath used gridInfo.NeighborhoodHeirarchy property
	public static  NeighborhoodPath getNeighborhoodPath(Connection connection , int nhId)
	{
		System.out.println("Inside getNeighborhoodPath:" + nhId);
		Vector<?> NhPathIds = com.boardwalk.neighborhood.NeighborhoodManager.getBoardwalkPathIds( connection , nhId );
		
		NeighborhoodPath nhpath = new NeighborhoodPath();
		int nhLevels = NhPathIds.size()-1;
		nhpath.setLevels(nhLevels);

		//System.out.println("Inside getNeighborhoodPath nhLevels:" + nhLevels);

		String nhPathId = (String)NhPathIds.elementAt(0);
		String nhpathArr[] = nhPathId.split("\\\\");

		//System.out.println("nhPathId : " + nhPathId);
		
		switch (nhLevels)
		{
			case 3:
				nhpath.setNhLevel0(Integer.parseInt(nhpathArr[0]) );
				nhpath.setNhLevel1(Integer.parseInt(nhpathArr[1]) );
				nhpath.setNhLevel2(Integer.parseInt(nhpathArr[2]) );
				nhpath.setNhLevel3(Integer.parseInt(nhpathArr[3]) );
				break;
				
			case 2:
				nhpath.setNhLevel0(Integer.parseInt(nhpathArr[0]) );
				nhpath.setNhLevel1(Integer.parseInt(nhpathArr[1]) );
				nhpath.setNhLevel2(Integer.parseInt(nhpathArr[2]) );
				nhpath.setNhLevel3(-1);
				break;
				
			case 1:
				nhpath.setNhLevel0(Integer.parseInt(nhpathArr[0]) );
				nhpath.setNhLevel1(Integer.parseInt(nhpathArr[1]) );
				nhpath.setNhLevel2(-1);
				nhpath.setNhLevel3(-1);
				break;
				
			case 0:
				nhpath.setNhLevel0(Integer.parseInt(nhpathArr[0]));
				nhpath.setNhLevel1(-1);
				nhpath.setNhLevel2(-1);
				nhpath.setNhLevel3(-1);
		}
		//System.out.println("Inside getNeighborhoodPath nhpath.getLevels:" + nhpath.getLevels());
		//System.out.println("Inside getNeighborhoodPath nhpath.getNhLevel0:" + nhpath.getNhLevel0());
		//System.out.println("Inside getNeighborhoodPath nhpath.getNhLevel1:" + nhpath.getNhLevel1());
		//System.out.println("Inside getNeighborhoodPath nhpath.getNhLevel2:" + nhpath.getNhLevel2());
		//System.out.println("Inside getNeighborhoodPath nhpath.getNhLevel3:" + nhpath.getNhLevel3());

		return nhpath;
	}
	
//	public static void processLinkExportColumnData(Connection connection, String cellData, String formulaData, int columnIdx, ArrayList rowIds, ArrayList columnIds, int numRows, int tid) throws SQLException 
	public static void processLinkExportColumnData(Connection connection, ArrayList<Cell> cellArr,  SequencedCellArray sca, SequencedCellArray scaFmla, int columnIdx, ArrayList<?> rowIds, ArrayList<Integer> columnIds, int numRows, int tid) throws SQLException 
	{
		PreparedStatement stmt		= null;
		ArrayList<String> seqColValues = (ArrayList<String>) sca.getCellValues();
		ArrayList<String> seqColFmlas = (ArrayList<String>) scaFmla.getCellFormulas();
		
		//cellArr.clear();	This was storing cells of only last column. THIS WAS A BUG
		
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
	public static void processSubmitRequest(Connection connection, int userId, int memberId, int nhId, int gridId, int baselineId, CellBuffer  cellBufferRequest, ArrayList<ErrorRequestObject> ErrResps, CellBuffer cbfReturn, ArrayList<Integer> statusCode)
	{

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

		Vector<xlErrorNew> xlErrorCells = new Vector<xlErrorNew>();
		Vector<Integer> xlDeleteRows = new Vector<Integer>();
		HashMap<Integer, Integer> rowIdHash = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> colIdHash = new HashMap<Integer, Integer>();
		HashMap<?, ?> accCols = new HashMap<Object, Object>();
		HashMap<Integer, HashMap<String, Comparable>> newRowsHash = new HashMap<Integer, HashMap<String, Comparable>>();
		HashMap<?, ?> newColumnsHash = new HashMap<Object, Object>();

		ArrayList<?> columnIds = null;
		ArrayList<?> columnNames = null;
		
		boolean colsDeleted = false;
		boolean rowsDeleted = false;
		boolean newRowsAdded = false;
		int defaultAccess = 2;
		
		ArrayList<Cell> cellArr = (ArrayList<Cell>) cellBufferRequest.getCells();
		GridChangeBuffer gcb = cellBufferRequest.getGridChangeBuffer();
		
		ArrayList<Integer> delColArray = (ArrayList<Integer>)  gcb.getDeletedColumnArray();
		ArrayList<Integer> delRowArray = (ArrayList<Integer>)  gcb.getDeletedRowArray();
		ArrayList<Column> newColArray = (ArrayList<Column>)  gcb.getNewColumnArray();
		ArrayList<Row> newRowArray = (ArrayList<Row>) gcb.getNewRowArray();

		if (cellArr == null | delColArray == null | delRowArray == null | newColArray == null | newRowArray == null )
		{
			erb = new ErrorRequestObject(); erb.setError("Grid Change Element Missing in the Request."); 
			erb.setPath("newRowArray or newColumnArray or deletedRowArray or deletedColumnArray or cells is Missing."); 
			erb.setProposedSolution("The Request must contain newRowArray, newColumnArray, deletedRowArray, deletedColumnArray, cells");
			ErrResps.add(erb);
			statusCode.add(404);		//404 : Bad Request. Missing Grid Change Elements
			return;
		}
		
		if (cellArr.isEmpty() & delColArray.isEmpty() & delRowArray.isEmpty() & newColArray.isEmpty() & newRowArray.isEmpty())
		{
			erb = new ErrorRequestObject(); erb.setError("The Request does not contain any Changes to Update the Grid"); 
			erb.setPath("Empty newRowArray, newColumnArray, deletedRowArray, deletedColumnArray, cells"); 
			erb.setProposedSolution("No Change detail Found in Request. So no changes made to the Grid on Server.");
			ErrResps.add(erb);
			statusCode.add(404);		//404 : Bad Request. Missing Grid Change Elements
			return;
		}
		
		int intCritical = gcb.getCritical();
		int intCriticalLevel = gcb.getCriticalLevel();
		
		Column cl;
		Row rw;
		
		GridInfo ginfo = cellBufferRequest.getInfo();
		
		String view = ginfo.getView();
		int importTid = ginfo.getImportTid();
		int exportTid = ginfo.getExportTid();
		
		//processHeader functionality FOR SUBMIT
		
		if (validateMemberShip(connection, memberId , nhId, userId, ErrResps, statusCode) == false)
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
		        	statusCode.add(409);			//409 : Conflict.  Critical Updates on Server.
		        	return;
				}
			}
			System.out.println("No Critical updates Found after the last import ----g2");
			
			//	Access control checks

			//System.out.println("Calling TableManager.getTableInfo : userId:" + userId  + ", gridId:" + gridId);
			tinfo = TableManager.getTableInfo(connection, userId, gridId);

			//System.out.println("Calling TableViewManager.getSuggestedAccess : tinfo:" + tinfo + " userId:" + userId  + ", memberId:" + memberId  + ", nhId:" + nhId);
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
		        	statusCode.add(403);			//403 : Forbidden.  User don't have the privileges to execute this action.
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

			System.out.println("canAddRows : " + canAddRows);
			System.out.println("canDeleteRows : " + canDeleteRows);
			System.out.println("canAdministerColumns : " + canAdministerColumns);
			
			System.out.println("wACL : " + wACL);
			System.out.println("awACL : " + awACL);
			
			// No Access to Table
			if ( awACL != wACL && canAddRows == false && canDeleteRows == false && canAdministerColumns == false)
			{
				xlErrorCells.add( new xlErrorNew(gridId, 0, 0, 10005));
				//throw new BoardwalkException(10005);
	        	erb = new ErrorRequestObject();
	        	erb.setError("ACCESS EXCEPTION (10005).  canAddRows - False, canDeleteRows - False, canAdministerColumn - False ");
	        	erb.setPath("GridManagement.gridPut::GridManagement.TableAccessRequest");
				erb.setProposedSolution("You dont have the priviliges to execute this action, Please contact the owner of the table to setup necessary access control");
	        	ErrResps.add(erb);
	        	statusCode.add(403);			//403 : Forbidden.  User don't have the privileges to execute this action.
	        	return;
			}

			
			if (newRowArray.size() > 0 && canAddRows == false)
			{
				erb = new ErrorRequestObject();
				erb.setError("TABLE UPDATE EXCEPTION (12012): No access to Add a Row" );
				erb.setPath("GridManagement.gridPut::canAddRows=FALSE, newRowArray.size()>0");
				erb.setProposedSolution("You don't have access to add a new row,  Please resolve errors and try again");
				ErrResps.add(erb);
	        	statusCode.add(403);			//403 : Forbidden.  User don't have the privileges to execute this action.
				System.out.println("No access to add rows");
			}
			
			if (delRowArray.size() > 0 && canDeleteRows == false)
			{
				erb = new ErrorRequestObject();
				erb.setError("TABLE UPDATE EXCEPTION (12010): No access to Delete a Row" );
				erb.setPath("GridManagement.gridPut::canDeleteRows=FALSE, delRowArray.size()>0");
				erb.setProposedSolution("You don't access to Delete a row, Please contact the owner of the Table");
				ErrResps.add(erb);
	        	statusCode.add(403);			//403 : Forbidden.  User don't have the privileges to execute this action.
				System.out.println("No access to Delete rows");
			}
			
			if (newColArray.size() > 0 && canAdministerColumns == false)
			{
				erb = new ErrorRequestObject();
				erb.setError("TABLE UPDATE EXCEPTION (12010): No access to add a new Column" );
				erb.setPath("GridManagement.gridPut::canAdministerColumns=FALSE, newColArray.Size()>0");
				erb.setProposedSolution("You don't access to add a new column, Please contact the owner of the Table");
				ErrResps.add(erb);
	        	statusCode.add(403);			//403 : Forbidden.  User don't have the privileges to execute this action.
				System.out.println("TABLE UPDATE EXCEPTION (12010): No access to Add a new Column");
			}
			
			if (delColArray.size() > 0 && canAdministerColumns == false)
			{
				erb = new ErrorRequestObject();
				erb.setError("TABLE UPDATE EXCEPTION (12010): No access to Delete a Column" );
				erb.setPath("GridManagement.gridPut::canAdministerColumns=FALSE, delColArray.size()>0");
				erb.setProposedSolution("You don't access to Delete a column, Please contact the owner of the Table");
				ErrResps.add(erb);
	        	statusCode.add(403);			//403 : Forbidden.  User don't have the privileges to execute this action.
				System.out.println("TABLE UPDATE EXCEPTION (12010): No access to Delete a Column." );
			}

	    	if (ErrResps.size() > 0) 
	    	{
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

			HashMap<?, ?> colCellAccess = new HashMap<Object, Object>();
			HashMap<String, HashSet<Integer>> accessQueryXrowSet = new HashMap<String, HashSet<Integer>>();
//			int defaultAccess = 2;

			if (accessTableId > 0)
			{
				Integer defAccess = new Integer(2);
				colCellAccess = TableViewManager.getColumnAcccess( connection, gridId, accessTableId, userId);
				Iterator<?> columnConditionalAccessIter = colCellAccess.keySet().iterator();
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

							HashSet<Integer> rowSet = (HashSet<Integer>) accessQueryXrowSet.get(accessInstr);
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
										rowSet = new HashSet<Integer>();
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

	    	//Checking if ColumnNames are blank;	IssueId: 14288
			for (int cn = 0; cn < newColArray.size(); cn = cn + 1)
			{
				newco = newColArray.get(cn);
				if (newco.getName().trim().equals(""))
				{
					System.out.println("Blank Column Name ");
					erb = new ErrorRequestObject();
					erb.setError("Column Name cannot be Blank.");
					erb.setPath("Blank Grid Column at position:" + cn);
					erb.setProposedSolution("Enter Unique NonEmpty Column Name.");
					ErrResps.add(erb);
		        	return;					
				}
			}
			
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
							statusCode.add(423);		//423 : Locked. The resource that is being accessed is locked. The table is being updated by another user, Please try later
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
							statusCode.add(412);		//412 : Precondition Failed. Columns are not Unique. 
							return;
						}
					}
					/*else
					{
						xlErrorCells.add(new xlErrorNew(gridId, 0, prevColId, 12010));
						erb = new ErrorRequestObject();
						erb.setError("TABLE UPDATE EXCEPTION (12010): No access to add a new Column" );
						erb.setPath("GridManagement.gridPut::canAdministerColumns=FALSE");
						erb.setProposedSolution("You don't access to add a new column, Please contact the owner of the Table");
						ErrResps.add(erb);
						return;
					}*/
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
/*				if (canAdministerColumns == false)
				{
					for(int id = 0; id < delColArray.size(); id=id+1 )
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
						System.out.println("TABLE UPDATE EXCEPTION (12010): No access to Delete a Column: ClumnId :" + delColId );
						return;
					}
				}
				else
				{
*/					for(int id = 0; id < delColArray.size(); id=id+1 )
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
//				}
			}
			// End of Columns Delete FOR SUBMITS ENDS

			//START OF NEW ROWS FOR SUBMIT
			if (newRowArray.size() > 0) 		// If new Rows exists. Send from Client array of -1s
			{
				// Adding New Rows
/*				if (canAddRows == false)
				{
					xlErrorCells.add(new xlErrorNew(gridId,  -1, -1, 12012));
					ExceptionAddRows = true;
					System.out.println("No access to add rows");
					erb = new ErrorRequestObject();
					erb.setError("TABLE UPDATE EXCEPTION (12012): No access to Add a Row" );
					erb.setPath("GridManagement.gridPut::canAddRows=FALSE");
					erb.setProposedSolution("You don't have access to add a new row,  Please resolve errors and try again");
					ErrResps.add(erb);
					return;
				}
				else
				{
*/					int prevNewRowId = -1;
					int prevRowId = -1;
					int prOffset = 1;
					int rwSeq = -1;
					Vector<newXLRow> nrv = new Vector<newXLRow>();
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
								statusCode.add(423);		//423 : Locked. The resource that is being accessed is locked. The table is being updated by another user, Please try later
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
						Iterator<newXLRow> nri = nrv.iterator();
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

						HashMap<String, Comparable> rdata;
						
						while (rs.next())
						{
							int rid = rs.getInt(1);
							int ridx = Integer.parseInt(rs.getString(2));
							System.out.println("Adding row into rowIdHash ridx :" + ridx + " ..... rid:" + rid);
							rowIdHash.put(new Integer(ridx), new Integer(rid));

							rdata = new HashMap<String, Comparable>();
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
			//	}	// End of canAddRows=true
			}		// end of newRowArray.size() 			END OF NEW ROWS FOR SUBMIT

			// delete the rows STARTS for SUBMIT
			System.out.println("delRowArray.size() :" + delRowArray.size() );
			if (delRowArray.size() > 0 )
			{
/*				if (canDeleteRows == false)
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
				{*/
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
//				}
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
					if (xlFormula.indexOf("=") != 0)					//Fix for IssueId: 
					{
						xlFormula = null;
					}
					
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
									if (((HashSet<?>) accessQueryXrowSet.get(accessInstr)).contains(new Integer(xlRowId)))
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
					        	statusCode.add(403);	//403 : Forbidden.  User don't have the privileges to execute this action. Add/Delete Row | Administer Columns
					        	return; //return was not there earlier
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
					        	statusCode.add(403);	//403 : Forbidden.  User don't have the privileges to execute this action. Add/Delete Row | Administer Columns
					        	return;		//return was not there earlier
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
			HashSet<Integer> delRowsOnServer = new HashSet<Integer>();				

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
				HashSet<Integer> accessibleRowsAfterSubmit = new HashSet<Integer>();
				
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
				
				Iterator<Integer> rowIdsFromClientIter = rowIdHash.keySet().iterator();
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
			        	erb.setPath("GridManagement.processSubmitRequest::accessibleRowsAfterSubmit, delRowsOnServer, xlDeleteRows does not contain Rows sent by client.");
						erb.setProposedSolution("Access Filter violation, You have either updated columns used for access filters or trying to update a row that is no longer accessible. Please contact your process admin Or the owner of the table to setup necessary access control");
			        	ErrResps.add(erb);
			        	statusCode.add(403);		//403 : Forbidden.  User don't have the privileges to execute this action. Add/Delete Row | Administer Columns
			        	return;
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
					HashMap<?, ?> rowdata = (HashMap<?, ?>) newRowsHash.get(rw.getSeqNo());
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
			retGcb.setCritical(-1);
			retGcb.setCriticalLevel(-1);

			ftal = TableViewManager.getSuggestedAccess(connection, tinfo, userId, memberId, nhId);
			
			GenerateGridRowAndRowArray (connection,  gridId, userId, memberId, nhId,  baselineId, view, criteriaTableId, retGridRows, retRowArr,  ErrResps);
			GenerateGridColumnAndColumnArray(connection, gridId, userId, memberId,  baselineId, view, ftal, retGridColumns, retColArr,  ErrResps);
			
//			(Connection connection, int gridId, int userId, int memberId, int nhId,  int baselineId, String view, int criteriaTableId, ArrayList <Row> gridRows, ArrayList <Integer> rowArray,  ArrayList<ErrorRequestObject> ErrResps)
			
			ginfo.setAsOfTid(tid);
			
			ginfo.setBaselineId(-1);
			ginfo.setColCount(retColArr.size());
			ginfo.setCollabId(tinfo.getCollaborationId());
			ginfo.setCriteriaTableId(criteriaTableId);
			ginfo.setExportTid(tid);
			ginfo.setFilter("");
			ginfo.setId(gridId);
			ginfo.setImportTid(importTid);
			ginfo.setMaxTxId(tid);
			ginfo.setMemberId(memberId);
			ginfo.setMode(1);
			ginfo.setName(tinfo.getTableName());
			NeighborhoodPath nhpath = new NeighborhoodPath();
			nhpath = getNeighborhoodPath(connection, nhId);
			ginfo.setNeighborhoodHeirarchy( nhpath);
			ginfo.setNhId(nhId);
			ginfo.setPurpose(tinfo.getTablePurpose());
			ginfo.setRowCount(retRowArr.size());
			ginfo.setServerName("");
			ginfo.setServerURL("");
			ginfo.setUserId(userId);
			ginfo.setView(view);
			ginfo.setWbId(tinfo.getWhiteboardId());
			

//			int mode = ginfo.getMode();
//			int baselineId = ginfo.getBaselineId();
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
        	statusCode.add(400);			//400 : Bad Request . Too many errors in payload
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
			statusCode.add(500);		 //500 : Server Error. SystemException on Server on Submit
        }
		catch (SQLException sqe)
		{
			erb = new ErrorRequestObject();
			erb.setError("SQLException:" + sqe.getErrorCode());
			erb.setPath("GridManagement.gridPut::TableViewManager.getCriteriaTable OR TableViewManager.getColumnAcccess");
			erb.setProposedSolution(sqe.getMessage() + ", " + sqe.getCause());
			ErrResps.add(erb);
			statusCode.add(500);		 //500 : Server Error. SQLException on Server on Submit
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
	}		//End of processSubmitRequest

	
	//Validate membership
	public static boolean validateMemberShip(Connection connection, int memberId, int nhId, int userId, ArrayList<ErrorRequestObject> ErrResps, ArrayList<Integer> statusCode )
	{
		boolean blnReturn = false;
		ErrorRequestObject erb;
		try
		{
			Hashtable<Integer, MemberNode> members = getAllMemberships(connection);

			if (!members.containsKey(memberId))
			{
				//System.out.println("members.containsKey(memberId) memberId : " + memberId + " --> is FALSE");
	        	erb = new ErrorRequestObject();
	        	erb.setError("Membership is Not Valid");
	        	erb.setPath("GridManagement.gridPut.validateMemberShip [SUBMIT]::GridManagement.getAllMemberships");
				erb.setProposedSolution("Request with Exiting Member ID");
	        	ErrResps.add(erb);
	        	statusCode.add(404);		//404 : Bad Request. Membership Not found in validateMembership
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
        	System.out.println("SystemException thrown by getAllMemberships");
        	s.printStackTrace();
        	erb = new ErrorRequestObject();
        	erb.setError("SystemException: " + s.getErrorMessage());
        	erb.setPath("GridManagement.gridPut.validateMemberShip [SUBMIT]::GridManagement.getAllMemberships");
			erb.setProposedSolution(s.getPotentialSolution());
        	ErrResps.add(erb);
        	statusCode.add(500);		//500 : Server Error. SystemException on Server in validateMembership
		}    		
		return blnReturn;
	}
	
	
    //@POST
	public static int gridPost(Grid grid, ArrayList <ErrorRequestObject> ErrResps, String authBase64String, BoardwalkConnection bwcon, ArrayList<Integer> memberNh, ArrayList<Integer> statusCode) 
	{
		int gridId = -1;
		ErrorRequestObject erb;
		// get the connection
    	Connection connection = null;
		
		int nhId = -1;
		int memberId = -1;
		int userId = -1;

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
			Vector<?> wv = bcn.getWhiteboards();
			Iterator<?> wvi = wv.iterator();
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
	        	erb.setPath("GridManagement.gridPost::bcn.getWhiteboards");
				erb.setProposedSolution("Request with Exiting Whiteboard ID");
	        	ErrResps.add(erb);
	        	statusCode.add(404);		//404: Not found.	Whiteboard not found.
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
		            Vector<NewTableAccessList> accessLists = new Vector<NewTableAccessList>();
		            Hashtable<?, ?>  relationships = NeighborhoodManager.getNeighborhoodRelationships(connection, nhId );
					Enumeration<?> relationKeys = relationships.keys();
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
					erb.setPath("GridManagement.gridPost::TableManager.createTable block");
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
					statusCode.add(412);		//412: Precondition Failed.	Grid already exists in whiteboard. 
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
        	statusCode.add(404);		//404: Not found. Collaboration Id not found.
        	return gridId;
		}
        catch ( SystemException s)
		{
        	System.out.println("SystemException thrown by NeighborhoodManager.getNeighborhoodRelationships");
        	s.printStackTrace();
        	erb = new ErrorRequestObject();
        	erb.setError("SystemException: " + s.getErrorMessage());
        	erb.setPath("GridManagement.gridPost::NeighborhoodManager.getNeighborhoodRelationships");
			erb.setProposedSolution(s.getPotentialSolution());
        	ErrResps.add(erb);
        	statusCode.add(500);		//500: Server Error. Failed to get Neighborhood Relationships.
        	return gridId;
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
	}

    static public Hashtable<Integer, MemberNode> getAllMemberships(Connection connection) throws SystemException
    {
    	Hashtable<Integer, MemberNode> ht = new Hashtable<Integer, MemberNode>();
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
                //System.out.println("MemberNode-> memberId:" + memberId + ", userId:" + userId + ", firstName:" + firstName + ", lastName:" + lastName + ", Email:" + Email + ", nhId:" + nhId + ", nhLevel:" + nhLevel);            
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
    public static void GenerateGridColumnAndColumnArray(Connection connection, int gridId, int userId, int memberId, int baselineId, String view, TableAccessList ftal, ArrayList <Column> gridCols, ArrayList <Integer> columnArray,  ArrayList<ErrorRequestObject> ErrResps)
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
		//System.out.println("mode = " + mode);

    	try
    	{

			int criteriaTableId = TableViewManager.getCriteriaTable(connection, gridId, userId);
			System.out.println("Using criterea table id = " + criteriaTableId);
			int accessTableId = TableViewManager.getAccessTable(connection, gridId, userId);
			HashSet<?> restrColumnList = new HashSet<Object>();
			if (accessTableId > 0) 
			{
				System.out.println("Using access table id = " + accessTableId);
				// read the access for the user
				restrColumnList = TableViewManager.getRestrictedColumnsForImport(connection, gridId, accessTableId, userId);
			}
			// Get the columns
			Vector<?> colv = ColumnManager.getXlColumnsForImport(connection, gridId, userId, memberId);
			Iterator<?> ci = colv.iterator();
			
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
//    public static void GenerateGridRowAndRowArray(Connection connection, int gridId, int userId, int memberId, int nhId, int mode, int baselineId, String view, TableAccessList ftal, int criteriaTableId, ArrayList <Row> gridRows, ArrayList <Integer> rowArray,  ArrayList<ErrorRequestObject> ErrResps)
    public static void GenerateGridRowAndRowArray(Connection connection, int gridId, int userId, int memberId, int nhId,  int baselineId, String view, int criteriaTableId, ArrayList <Row> gridRows, ArrayList <Integer> rowArray,  ArrayList<ErrorRequestObject> ErrResps)
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
			Vector<?> rowv = tbrowInfo.getRowVector();
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
	    		gridRow.setId(rowObject.getId()); 
	    		gridRow.setPreviousRowid(  previousRowid);
	    		gridRow.setPreviousRowSequence(previousRowSequence); 
	    		Float obj = new Float(rowObject.getSequenceNumber());
	    		gridRow.setSeqNo(obj.intValue()); 

				rowArray.add(rowObject.getId());
	    		
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
			Vector<?> rowv = tbrowInfo.getRowVector();
			
			
			int accessTableId = TableViewManager.getAccessTable(connection, gridId, userId);
			HashSet<?> restrColumnList = new HashSet<Object>();
			if (accessTableId > 0) 
			{
				System.out.println("Using access table id = " + accessTableId);
				// read the access for the user
				restrColumnList = TableViewManager.getRestrictedColumnsForImport(connection, gridId, accessTableId, userId);
			}
	
			Vector<?> colv = ColumnManager.getXlColumnsForImport(connection, gridId, userId, memberId);
			Iterator<?> ci = colv.iterator();
			
			String q = null;
			String rowQuery = null;
			boolean results = false;
			if (criteriaTableId == -1 && !viewIsDynamic) {
				//System.out.println("CALL BW_GET_TBL_LNK_IMPORT ---> " + gridId + "," + userId + "," + memberId + "," + nhId + "," + view);
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
				//System.out.println("CALL BW_GET_FiltredTableCELLS_DYNAMIC ---> " + gridId + "," + userId + "," + memberId + "," + view);
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
				//System.out.println("CALL BW_GET_FiltredTableCELLS_DYNAMIC ---> " + gridId + "," + userId + "," + memberId + "," + view);
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

    public enum GET_TBL {
    	COLUMN_ID (0),
    	ROW_ID (1),
    	COLUMN_SEQUENCE (2),
    	ROW_SEQUENCE (3),
    	STRING_VALUE (4),
    	FORMULA (5),
    	COLUMN_NAME (6) ;
    	
        private int colNo;

        GET_TBL(int colNo) {
            this.colNo = colNo;
        }    	
        
        public int getcolNo() {
        	return this.colNo;
        }
    }
    
    public enum GET_CHANGES_FOR_TX {
    	ROW_ID (0),
    	COLUMN_ID (1),
    	CELL_STRING_VALUE (2),
    	TX_CREATED_BY (3),
    	CREATED_ON (4),
    	COMMENT (5),
    	EMAIL_ADDRESS (6),
    	FORMULA (7),
    	COLUMN_NAME (8),
    	COLUMN_SEQUENCE (9),
    	ROW_SEQUENCE (10);
    	
        private int colNo;

    	GET_CHANGES_FOR_TX(int colNo) {
            this.colNo = colNo;
        }    	
        
        public int getcolNo() {
        	return this.colNo;
        }
    }
    
    public enum GET_CHANGE_STATUS_FOR_TX {
    	ROW_ID (0),
    	COLUMN_ID (1),
    	ACTIVE (2),
    	TX_CREATED_BY (3),
    	CREATED_ON (4),
    	COMMENT (5),
    	EMAIL_ADDRESS (6),
    	COLUMN_SEQUENCE (7),
    	ROW_SEQUENCE (8);
    	
        private int colNo;

        GET_CHANGE_STATUS_FOR_TX(int colNo) {
            this.colNo = colNo;
        }    	
        
        public int getcolNo() {
        	return this.colNo;
        }
    }
    
    //@GET
    //@Path("/{gridId}/{transactionId}/changes")
    public static GridChanges gridGridIdTransactionIdChangesGet(Integer gridId, long transactionId, long difference_in_MiliSec, String viewPref, ArrayList<ErrorRequestObject> ErrResps, String authBase64String, BoardwalkConnection bwcon, ArrayList<Integer> memberNh, ArrayList<Integer> statusCode ) 
    {
    	
    	Connection connection = null;
		int nhId = -1;
		int memberId = -1;
		int userId = -1;
		connection = bwcon.getConnection();
		memberId = memberNh.get(0);
		nhId = memberNh.get(1);
		userId = bwcon.getUserId();
		
    	//CompleteTableWithChanges
		ErrorRequestObject erb;
		GridChanges gcReturn = new GridChanges();
		
		ArrayList<Integer> rowArray = new ArrayList<Integer>();
		ArrayList<Integer> columnArray  = new ArrayList<Integer>();
		ArrayList<String> cellValues  = new ArrayList<String>();
		ArrayList<String> cellfmla  = new ArrayList<String>();
		ArrayList <SequencedCellArray> scas = new ArrayList<SequencedCellArray>();
		SequencedCellArray sca;
		ArrayList<Row> gridRows = new ArrayList<Row>();
		Row gridRow = new Row();
		ArrayList <Column> gridCols = new ArrayList<Column>();
		Column gridCol ;
		ArrayList<Cell> gridCells  = new ArrayList<Cell>();
		GridInfo ginfo = new GridInfo();

		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try
		{
			
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			
			// Collect the Active column IDs first
			System.out.println("calling...........BW_GET_TBL_BEF_T..........");
			System.out.println("1.tableid " +  gridId);
			System.out.println("2. asofTxid " +  transactionId);
			System.out.println("3. userId " +  userId);
			System.out.println("4. memberId " +  memberId);
			System.out.println("5. nhId " +  nhId);
			System.out.println("6. view " + viewPref);

			int criteriaTableId = TableViewManager.getCriteriaTable(connection, gridId, userId);
			System.out.println("Using criteriaTableId = " + criteriaTableId);
			String rowQuery = "";

			if (criteriaTableId > -1)
				rowQuery = TableViewManager.getRowQuery(connection, gridId, userId, criteriaTableId, false, viewPref, "TABLE"); //Modified by Lakshman on 20171108 to avoid self joins on BW_CELL for performance gain
//				System.out.println("rowQuery = " + rowQuery);

			if(criteriaTableId == -1)
			{
				stmt = connection.prepareStatement("{CALL BW_GET_TBL_BEF_T(?,?,?,?,?,?)}");
				stmt.setInt(1, gridId);
				stmt.setInt(2, (int) transactionId);
				stmt.setInt(3, userId);
				stmt.setInt(4, memberId);
				stmt.setInt(5, nhId);
				stmt.setString(6, viewPref);
			}
			else
			{
				String lsSql = QueryMaker.getFiltredQueryBefXL(rowQuery);
				stmt = connection.prepareStatement(lsSql);
//				System.out.println ("BW_GET_TBL_BEF_T = " + lsSql);
				/*Modified by Lakshman on 20171107 for fixing the Table history for a Link Export TX_ID: Issue ID = 4060 - START*/
				stmt.setInt(1, (int) transactionId);
				
				stmt.setInt(2, gridId);
				stmt.setInt(3, userId);
				stmt.setInt(4, memberId);
				stmt.setInt(5, (int) transactionId);

				stmt.setInt(6, gridId);
				stmt.setInt(7, userId);
				stmt.setInt(8, memberId);
				stmt.setInt(9, (int) transactionId);

				stmt.setInt(10, (int) transactionId);
				
				stmt.setInt(11, gridId);
				stmt.setInt(12, userId);
				stmt.setInt(13, memberId);
				stmt.setInt(14, (int) transactionId);

				stmt.setInt(15, gridId);
				stmt.setInt(16, userId);
				stmt.setInt(17, memberId);
				stmt.setInt(18, (int) transactionId);
				/*Modified by Lakshman on 20171107 for fixing the Table history for a Link Export TX_ID: Issue ID = 4060 - END*/
			}

			rs = stmt.executeQuery();

			ArrayList <Integer> Cols = new ArrayList<Integer>();
			ArrayList <Integer> Rows = new ArrayList<Integer>();
			
			TableInfo tinfo = TableManager.getTableInfo(connection, userId, gridId);
			ginfo.setCollabId(tinfo.getCollaborationId());
			ginfo.setWbId(tinfo.getWhiteboardId());
			ginfo.setId(gridId);
			ginfo.setName(tinfo.getTableName());
			ginfo.setPurpose(tinfo.getTablePurpose());
			ginfo.setView(tinfo.getTableDefaultViewPreference());
			ginfo.setMemberId(memberId);
			ginfo.setUserId(userId);
			
			//Initializing SequenceCellArray()
			sca = new SequencedCellArray();
    		cellValues =  new ArrayList<String>();
    		cellfmla = new ArrayList<String>();
			
			int prevRowId = -1;
			int prevColId = -1;
			
			int nCol = rs.getMetaData().getColumnCount();
			String[] row = new String[nCol] ;

			ArrayList<String[]> table = new ArrayList<>();
			while( rs.next()) {
			    row = new String[nCol];
			    for( int iCol = 1; iCol <= nCol; iCol++ ){
			            Object obj = rs.getObject( iCol );
			            row[iCol-1] = (obj == null) ?null:obj.toString();
			    }
			    
			    if(!Cols.contains(Integer.parseInt(row[GET_TBL.COLUMN_ID.getcolNo()])))
			    {
			    	Cols.add(Integer.parseInt(row[GET_TBL.COLUMN_ID.getcolNo()]));
			    	gridCol = new Column();
			    	gridCol.setId(Integer.parseInt(row[GET_TBL.COLUMN_ID.getcolNo()]));
			    	gridCol.setName(row[GET_TBL.COLUMN_NAME.getcolNo()]);
			    	gridCol.setSeqNo(new BigDecimal( row[GET_TBL.COLUMN_SEQUENCE.getcolNo()]));
			    	gridCol.setPreviousColumnid(prevColId);
			    	gridCols.add(gridCol);
			    	
			    	columnArray.add(Integer.parseInt(row[GET_TBL.COLUMN_ID.getcolNo()]));

			    	if (prevColId != -1 & prevColId != Integer.parseInt(row[GET_TBL.COLUMN_ID.getcolNo()]) )
			    	{
			    		//Adding SequenceCellArray() into collections of columns SequenceCellArray() with columnId and columnSequence
			    		sca.setCellValues(cellValues);
			    		sca.setCellFormulas(cellfmla);
			    		//sca.setColSequence((int)   Double.parseDouble(row[GET_TBL.COLUMN_SEQUENCE.getcolNo()]));
			    		//sca.setColumnId(Integer.parseInt(row[GET_TBL.COLUMN_ID.getcolNo()]));
			    		System.out.println("adding into scas for colId: " + prevColId + "  ##@#$#$^%$^%$&&**");
			    		scas.add(sca);
			    		
			    		//Initializing SequenceCellArray() for next column
			    		sca = new SequencedCellArray();
			    		cellValues =  new ArrayList<String>();
			    		cellfmla = new ArrayList<String>();
			    		sca.setColSequence((int)   Double.parseDouble(row[GET_TBL.COLUMN_SEQUENCE.getcolNo()]));
			    		sca.setColumnId(Integer.parseInt(row[GET_TBL.COLUMN_ID.getcolNo()]));
			    		//cellValues =  new ArrayList<String>();
			    		//cellfmla = new ArrayList<String>();
			    	}
			    	else
			    	{
			    		System.out.println("First time $@#@@$#@@#$@$@#$@$");
			    		sca.setColSequence((int)   Double.parseDouble(row[GET_TBL.COLUMN_SEQUENCE.getcolNo()]));
			    		sca.setColumnId(Integer.parseInt(row[GET_TBL.COLUMN_ID.getcolNo()]));
			    	}
			    	
			    	
			    	prevColId = Integer.parseInt(row[GET_TBL.COLUMN_ID.getcolNo()]);
			    }

			    if(!Rows.contains(Integer.parseInt(row[GET_TBL.ROW_ID.getcolNo()])))
			    {
			    	Rows.add(Integer.parseInt(row[GET_TBL.ROW_ID.getcolNo()]));
					gridRow = new Row();
					gridRow.setId(Integer.parseInt(row[GET_TBL.ROW_ID.getcolNo()]));
					gridRow.setSeqNo((int)   Double.parseDouble(row[GET_TBL.ROW_SEQUENCE.getcolNo()]));
					gridRow.setPreviousRowid(prevRowId);
					gridRows.add(gridRow);
					prevRowId = Integer.parseInt(row[GET_TBL.ROW_ID.getcolNo()]);
					
			    	rowArray.add(Integer.parseInt(row[GET_TBL.ROW_ID.getcolNo()]));
			    }
			    
/*			    gridCell = new Cell();
			    gridCell.setRowId(Integer.parseInt(row[GET_TBL.ROW_ID.getcolNo()]));
			    gridCell.setRowSequence((int)   Double.parseDouble(row[GET_TBL.ROW_SEQUENCE.getcolNo()]));
			    gridCell.setColId(Integer.parseInt(row[GET_TBL.COLUMN_ID.getcolNo()]));
			    gridCell.setColSequence((int)   Double.parseDouble(row[GET_TBL.COLUMN_SEQUENCE.getcolNo()]));
			    gridCell.setCellFormula(row[GET_TBL.FORMULA.getcolNo()]);
			    gridCell.setCellValue(row[GET_TBL.STRING_VALUE.getcolNo()]);
			    gridCells.add(gridCell);
*/
			    cellValues.add(row[GET_TBL.STRING_VALUE.getcolNo()]);
			    cellfmla.add(row[GET_TBL.FORMULA.getcolNo()]);
			    
			    table.add( row );
			}
			//Adding last Sequence column Arrau
    		sca.setCellValues(cellValues);
    		sca.setCellFormulas(cellfmla);
    		sca.setColSequence((int)   Double.parseDouble(row[GET_TBL.COLUMN_SEQUENCE.getcolNo()]));
    		sca.setColumnId(Integer.parseInt(row[GET_TBL.COLUMN_ID.getcolNo()]));
    		scas.add(sca);

			gcReturn.setColumnArray(columnArray);
			gcReturn.setColumnCellArrays(scas);
			gcReturn.setColumns(gridCols);
			gcReturn.setInfo(ginfo);
			gcReturn.setRowArray(rowArray);
			gcReturn.setRows(gridRows);
			
			// print result
			for( String[] roww: table ){
				
				System.out.print( "COLUMN_ID " + roww[GET_TBL.COLUMN_ID.getcolNo()]);
				
			    for( String s: roww ){
			        System.out.print( " " + s );
			    }
			    System.out.println();
			}

			stmt.close();
			rs.close();
			stmt	= null;
			rs		= null;

			System.out.println("calling...........BW_GET_VALUE_CHANGES_FOR_TID..........");
			System.out.println("1.tableid " +  gridId);
			System.out.println("2. asofTxid " +  (int) transactionId);
			System.out.println("3. userId " +  userId);
			System.out.println("4. memberId " +  memberId);
			System.out.println("5. nhId " +  nhId);
			System.out.println("6. view " + viewPref);

			if(criteriaTableId == -1)
			{
				stmt = connection.prepareStatement("{CALL BW_GET_VALUE_CHANGES_FOR_TID(?,?,?,?,?,?)}");
				stmt.setInt(1, gridId);
				stmt.setInt(2, (int) transactionId);
				stmt.setInt(3, userId);
				stmt.setInt(4, memberId);
				stmt.setInt(5, nhId);
				stmt.setString(6, viewPref);
			}
			else
			{
				String lsSql = QueryMaker.getFiltredQueryValueChangesXL(rowQuery);
				stmt = connection.prepareStatement(lsSql);

				stmt.setInt(1, gridId);
				stmt.setInt(2, userId);
				stmt.setInt(3, memberId);

				stmt.setInt(4, (int) transactionId);
				stmt.setInt(5, (int) transactionId);

				stmt.setInt(6, gridId);
				stmt.setInt(7, userId);
				stmt.setInt(8, memberId);

				stmt.setInt(9, (int) transactionId);
				stmt.setInt(10, (int) transactionId);
			}

			rs = stmt.executeQuery();

			nCol = rs.getMetaData().getColumnCount();
			row = new String[nCol] ;
			table = new ArrayList<>();
			Cell cl;
			gridCells = new ArrayList<Cell>();
			while(rs.next())
			{
			    row = new String[nCol];
			    for( int iCol = 1; iCol <= nCol; iCol++ ){
			            Object obj = rs.getObject( iCol );
			            row[iCol-1] = (obj == null) ?null:obj.toString();
			    }
			    cl = new Cell();
			    cl.setRowId(Integer.parseInt(row[GET_CHANGES_FOR_TX.ROW_ID.getcolNo()]));
			    cl.setColId(Integer.parseInt(row[GET_CHANGES_FOR_TX.COLUMN_ID.getcolNo()]));
			    cl.setCellValue(row[GET_CHANGES_FOR_TX.CELL_STRING_VALUE.getcolNo()]);
			    cl.setCellFormula(row[GET_CHANGES_FOR_TX.FORMULA.getcolNo()]);
			    cl.setRowSequence((int) Double.parseDouble(row[GET_CHANGES_FOR_TX.ROW_SEQUENCE.getcolNo()]));
			    cl.setColSequence((int) Double.parseDouble(row[GET_CHANGES_FOR_TX.COLUMN_SEQUENCE.getcolNo()]));
			    
			    gridCells.add(cl);
				table.add( row );
			}
			stmt.close();
			rs.close();
			stmt	= null;
			rs		= null;

			gcReturn.setCells(gridCells);

			System.out.println("calling...........BW_GET_STATUS_CHANGES_FOR_TID..........");
			System.out.println("1.tableid " +  gridId);
			System.out.println("2. asofTxid " +  (int) transactionId);
			System.out.println("3. userId " +  userId);
			System.out.println("4. memberId " +  memberId);
			System.out.println("5. nhId " +  nhId);
			System.out.println("6. view " + viewPref);

			if(criteriaTableId == -1)
			{
				stmt = connection.prepareStatement("{CALL BW_GET_STATUS_CHANGES_FOR_TID(?,?,?,?,?,?)}");
				stmt.setInt(1, gridId);
				stmt.setInt(2, (int) transactionId);
				stmt.setInt(3, userId);
				stmt.setInt(4, memberId);
				stmt.setInt(5, nhId);
				stmt.setString(6, viewPref);
			}
			else
			{
				String lsSql = QueryMaker.getFiltredQueryStatusChangesXL(rowQuery);
				stmt = connection.prepareStatement(lsSql);
				//System.out.println(" << lsSql >>"+lsSql);

				stmt.setInt(1, gridId);
				stmt.setInt(2, userId);
				stmt.setInt(3, memberId);
				stmt.setInt(4, (int) transactionId);

				stmt.setInt(5, gridId);
				stmt.setInt(6, userId);
				stmt.setInt(7, memberId);
				stmt.setInt(8, (int) transactionId);
			}

			rs = stmt.executeQuery();

			nCol = rs.getMetaData().getColumnCount();
			row = new String[nCol] ;
			table = new ArrayList<>();
			ArrayList<StatusChange> Scc = new ArrayList<StatusChange>(); 
			StatusChange sc;
			while(rs.next())
			{
			    row = new String[nCol];
			    for( int iCol = 1; iCol <= nCol; iCol++ ){
			            Object obj = rs.getObject( iCol );
			            row[iCol-1] = (obj == null) ?null:obj.toString();
			    }
			    System.out.println("Active:" + row[GET_CHANGE_STATUS_FOR_TX.ACTIVE.getcolNo()]);
			    sc = new StatusChange();
			    sc.setRowId(Long.parseLong(row[GET_CHANGE_STATUS_FOR_TX.ROW_ID.getcolNo()]));
			    sc.setColumnId(Long.parseLong(row[GET_CHANGE_STATUS_FOR_TX.COLUMN_ID.getcolNo()]));
			    sc.setActive(Boolean.valueOf(row[GET_CHANGE_STATUS_FOR_TX.ACTIVE.getcolNo()]));
			    sc.setTxId(transactionId);
			    sc.setColumnSeq((int) Double.parseDouble(row[GET_CHANGE_STATUS_FOR_TX.COLUMN_SEQUENCE.getcolNo()]));
			    sc.setComment((row[GET_CHANGE_STATUS_FOR_TX.COMMENT.getcolNo()]));
			   // sc.setCreatedDateTime(new BigDecimal(row[GET_CHANGE_STATUS_FOR_TX.CREATED_ON.getcolNo()]));
			    sc.setRowSeq((int) Double.parseDouble(row[GET_CHANGE_STATUS_FOR_TX.ROW_SEQUENCE.getcolNo()]));
			    sc.setUserEmail((row[GET_CHANGE_STATUS_FOR_TX.EMAIL_ADDRESS.getcolNo()]));
			    
			    Scc.add(sc);
				table.add( row );
			}

			gcReturn.setStatusChanges(Scc);

			stmt.close();
			rs.close();
			stmt = null;
			rs = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			if (stmt != null)
			{
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stmt = null;
			}
			if (rs != null)
			{
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rs = null;
			}
		}
		statusCode.add(200);
    	return gcReturn;
    }

    //@GET
    //@Path("/{gridId}/transactions")
    public static ArrayList<io.swagger.model.GridTransaction> gridGridIdTransactionsGet(Integer gridId, String reportType, long importTid, 
			long startDate, long endDate, long difference_in_MiliSec, String viewPref , ArrayList<ErrorRequestObject> ErrResps, String authBase64String, BoardwalkConnection bwcon, ArrayList<Integer> memberNh, ArrayList<Integer> statusCode ) 
    {

    	
/*    	long difference_in_MiliSec;

		Calendar cal_GMT = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		long server_Millis = cal_GMT.getTimeInMillis();
		difference_in_MiliSec = local_offset - server_Millis;
		System.out.println("Local Server (gmt) in miliSeconds is " + server_Millis );
		System.out.println("The difference in Server and Clietnis " + (local_offset - server_Millis ));
  */  	
    	// TODO Auto-generated method stub
		ErrorRequestObject erb;
   		
		//ArrayList<io.swagger.model.Transaction> txs = new ArrayList<io.swagger.model.Transaction>();
		
		ArrayList<io.swagger.model.GridTransaction> txs = new ArrayList<io.swagger.model.GridTransaction>();
		
		io.swagger.model.GridTransaction tx ;
		
		// get the connection
    	Connection connection = null;

		int nhId = -1;
		int memberId = -1;
		
		connection = bwcon.getConnection();
		memberId = memberNh.get(0);
		nhId = memberNh.get(1);
		int userId = bwcon.getUserId();
		//String viewPref = "LATEST";

	    com.boardwalk.database.Transaction ts, t;
	    
		//long endDate = startTime - difference_in_MiliSec;
		//long startDate = Long.parseLong(endTime) - difference_in_MiliSec;
		try
		{
			Hashtable<?, ?> transactionList = null; ;

			if (reportType.toUpperCase().equals("DURATION"))
			{
				transactionList = TableManager.getTransactionList(connection,
					      gridId,
					      -1,
					      -1,
					      startDate,
					      endDate,
					      userId,
					      nhId,
					      viewPref,
					      true);
			}
			else if (reportType.toUpperCase().equals("AFTERIMPORT"))
			{
				transactionList = TableManager.getTransactionListAfterImport(connection,
					      gridId.intValue(),
					      (int) importTid,
					      userId,
					      nhId,
					      viewPref);
			}

			System.out.println("Number of transactions = " + transactionList.size());

			Vector<?> tvec = new Vector<Object>(transactionList.keySet());
			//Collections.sort(tvec);
		    Iterator<?> i = tvec.iterator();
		    String descr;
	    
		    Date longDate ;
		    while (i.hasNext())
			{
		    	Integer tid = (Integer)i.next();
		    	System.out.println("TransactionList..ID : " + tid);

			    Vector<?>  vt = (Vector<?>)transactionList.get(tid);
			    t = (Transaction)vt.elementAt(0);
			    Iterator<?> j = vt.iterator();
			    String checkImage = "";

			    tx = new io.swagger.model.GridTransaction();
			    
			    tx.setId(tid);
				tx.setBaselineAdded(false);
				tx.setCellUpdated(false);
				tx.setColumnAdded(false);
				tx.setFormulaUpdated(false);
				tx.setRowAdded(false);
				tx.setRowDeleted(false);

				tx.setComment(t.getComment());
				tx.setTransactionTime(new BigDecimal(t.getCreatedOnTime()));
				tx.setUpdatedBy(t.getCreatedByUserAddress());

				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss a");

				Calendar gmtCal = Calendar.getInstance();
				gmtCal.setTimeInMillis(t.getCreatedOnTime());
				
				java.util.Date  theLocalServerConversionDate =  gmtCal.getTime();
			    String strTheLocalServerConversionDate = sdfDate.format(theLocalServerConversionDate);
				System.out.println("Setting strTheLocalServerConversionDate with Format ^&^&^&^&^&^&^&^&^&^ : " + strTheLocalServerConversionDate);
				
				tx.setCreatedOnTime(sdfDate.format(theLocalServerConversionDate));
				
				Calendar gmtaCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				//after considering Client's locale difference to get GMT Time
				gmtaCal.setTimeInMillis(t.getCreatedOnTime() - difference_in_MiliSec);
				java.util.Date  theGMTConversionDate =  gmtaCal.getTime();
			    String strTheGMTConversionDate = sdfDate.format(theGMTConversionDate);
				System.out.println("Setting strTheGMTConversionDate with Format ^&^&^&^&^&^&^&^&^&^ : " + strTheGMTConversionDate);
				
				tx.setCreatedOnTimeGMT(sdfDate.format(theGMTConversionDate));

				
			    while (j.hasNext())
			    {
			    	ts = (Transaction)j.next();
			    	descr = ts.getDescription();
			    	//System.out.println("descr=" + descr);
			    	if (descr.toUpperCase().startsWith("ROWADD"))
			    		tx.setRowAdded(true);
			    	else if (descr.toUpperCase().startsWith("ROWDEL"))
			    		tx.setRowDeleted(true);
			    	else if (descr.toUpperCase().startsWith("COLADD"))
						tx.setColumnAdded(true);
			    	else if (descr.toUpperCase().startsWith("CELLUPD"))
						tx.setCellUpdated(true);
			    	else if (descr.toUpperCase().startsWith("FRMUPD"))
						tx.setFormulaUpdated(true);
			    	else if (descr.toUpperCase().startsWith("BLNADD"))
						tx.setBaselineAdded(true);
			    }
				txs.add(tx);			    	
				System.out.println("End of vt.iterator");
			}
			System.out.println("End of tvec.iterator");

		}
		catch (SQLException sql)
		{
			sql.printStackTrace();

		}
		finally
		{
		  try
		  {
			if ( connection != null )
				connection.close();
		  }
		  catch ( SQLException sql )
		  {
			sql.printStackTrace();
		  }
		}
		
    	return txs;
	}
}
