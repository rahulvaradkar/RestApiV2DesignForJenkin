package boardwalk.rest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.boardwalk.database.TransactionManager;
import com.boardwalk.excel.xlColumn_import;
import com.boardwalk.excel.xlErrorNew;
import com.boardwalk.exception.BoardwalkException;
import com.boardwalk.table.ColumnManager;
import com.boardwalk.table.RowManager;
import com.boardwalk.table.TableAccessList;
import com.boardwalk.table.TableAccessRequest;
import com.boardwalk.table.TableInfo;
import com.boardwalk.table.TableManager;
import com.boardwalk.table.TableRowInfo;
import com.boardwalk.table.TableViewManager;

import boardwalk.connection.BoardwalkConnection;
import boardwalk.rest.GridManagement.GET_TBL;
import io.swagger.model.CellChain;
import io.swagger.model.CellStatus;
import io.swagger.model.Column;
import io.swagger.model.ColumnChain;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.FormulaValue;
import io.swagger.model.GridChain;
import io.swagger.model.GridInfo;
import io.swagger.model.GridTransaction;
import io.swagger.model.Row;
import io.swagger.model.RowChain;
import io.swagger.model.StringValues;
import io.swagger.model.Transaction;
import io.swagger.model.TransactionChain;

public class GridchainManagement {

	
	public GridchainManagement()
	{	
	}

    public enum GRID_CHAIN_INFO {
    	GRID_ID (0),
    	GRID_NAME (1),
    	GRID_PURPOSE (2),
    	COLLAB_ID (3),
    	WB_ID (4),
    	PEER_ACCESS (5),
    	PRIVATE_ACCESS (6),
    	FRIEND_ACCESS (7),
    	SEQUENCE_NUMBER (8),
    	NH_ID (9),
    	IS_ACTIVE (10) ,
    	VIEW_PREF (11) ,
    	TX_ID_CREATED (12) ,
    	TX_CREATED_ON (13),
    	TX_CREATED_USER_ID (14),
    	TX_CREATED_USER_EMAIL (15),
    	TX_CREATED_DESCRIPTION (16),
    	TX_CREATED_COMMENT (17),
    	IS_LOCKED (18) ,
    	TX_ID_LOCKED (19) ,
    	TX_LOCKED_ON (20),
    	TX_LOCKED_USER_ID (21),
    	TX_LOCKED_USER_EMAIL (22),
    	TX_LOCKED_DESCRIPTION (23),
    	TX_LOCKED_COMMENT (24);

    	private int colNo;

    	GRID_CHAIN_INFO(int colNo) {
            this.colNo = colNo;
        }    	
        
        public int getcolNo() {
        	return this.colNo;
        }
    }
    
    public enum COLUMN_CHAIN_INFO {
    	COLUMN_ID (0),
    	COLUMN_NAME (1),
    	GRID_ID (2),
    	COLUMN_TYPE (3),
    	SEQUENCE_NUMBER (4),
    	LOOKUP_GRID (5),
    	LOOKUP_COLUMN(6),
    	IS_ENUMERATED (7),
    	COLUMN_WIDTH (8),
    	IS_ACTIVE (9),
    	COLUMN_SOURCE (10),
    	ATTR (11),
    	CREATED_TX_ID (12) ,
    	CREATED_TX_USER_ID (13) ,
    	CREATED_TX_USER_EMAIL (14) ,
    	CREATED_ON (15) ,
    	CREATED_TX_DESC (16) ,
    	CREATED_TX_COMMENT (17) ,
    	DELETED_TX_ID (18) ,
    	DELETED_TX_USER_ID (19) ,
    	DELETED_TX_USER_EMAIL (20) ,
    	DELETED_ON (21) ,
    	DELETED_TX_DESC (22) ,
    	DELETED_TX_COMMENT (23) ;

    	private int colNo;

    	COLUMN_CHAIN_INFO(int colNo) {
            this.colNo = colNo;
        }    	
        
        public int getcolNo() {
        	return this.colNo;
        }
    }

    public enum ROW_CHAIN_INFO {
        BW_ROW_ID (0),
        GRID_ID	(1),
        SEQUENCE_NUMBER (2),
        IS_ACTIVE (3),
        OWNER_ID (4),
        OWNER_EMAIL (5),
        CREATED_TX_ID (6),	
        CREATED_USER_ID (7),	
        CREATED_USER_EMAIL (8),	
        CREATED_ON (9),	
        CREATED_DESCRIPTION (10),	
        CREATED_COMMENT (11),
        DELETED_TX_ID (12),	
        DELETED_TX_USER_ID (13),	
        DELETED_TX_USER_EMAIL (14),	
        DELETED_ON (15),	
        DELETED_TX_DESCRIPTION (16),	
        DELETED_TX_COMMENT (17);

    	private int colNo;

    	ROW_CHAIN_INFO(int colNo) {
            this.colNo = colNo;
        }    	
        
        public int getcolNo() {
        	return this.colNo;
        }
    }
    

    public enum CELL_CHAIN_INFO {
        BW_CELL_ID (0),
        GRID_ID	(1),
        BW_ROW_ID (2),
    	BW_COLUMN_ID (3),
    	CELL_TYPE (4),
    	STRING_VALUE (5),
    	FORMULA (6),
    	IS_ACTIVE (7),
    	TX_ID (8),
    	TX_USER_ID (9),
    	TX_USER_EMAIL (10),
    	TX_CREATED_ON (11),
    	TX_DESCRIPTION (12),
    	TX_COMMENT (13)	;

    	private int colNo;

    	CELL_CHAIN_INFO(int colNo) {
            this.colNo = colNo;
        }    	
        
        public int getcolNo() {
        	return this.colNo;
        }
    }

    
    public enum STRING_VALUES {
        BW_STRING_VALUE_ID (0),
    	BW_CELL_ID (1),
    	STRING_VALUE (2),
    	TX_ID (3),
    	TX_CREATED_USER_ID (4),
    	TX_CREATED_USER_EMAIL (5),
    	TX_CREATED_ON (6),
    	TX_DESCRIPTION (7),
    	TX_COMMENT (8),
    	FORMULA_ID (9);

    	private int colNo;

    	STRING_VALUES(int colNo) {
            this.colNo = colNo;
        }    	
        
        public int getcolNo() {
        	return this.colNo;
        }
    }
    
    
    public enum CELL_STATUS {
        BW_CELL_STATUS_ID (0),
    	BW_CELL_ID (1),
    	IS_ACTIVE (2),
    	TX_ID (3),
    	TX_CREATED_USER_ID (4),
    	TX_CREATED_USER_EMAIL (5),
    	TX_CREATED_ON (6),
    	TX_DESCRIPTION (7),
    	TX_COMMENT (8);

    	private int colNo;

    	CELL_STATUS(int colNo) {
            this.colNo = colNo;
        }    	
        
        public int getcolNo() {
        	return this.colNo;
        }
    }

    public enum FORMULA_VALUE {
        FORMULA_ID (0),
    	FORMULA (1),
    	FORMULA_INDEX (2),
    	TX_ID (3),
    	TX_CREATED_USER_ID (4),
    	TX_CREATED_USER_EMAIL (5),
    	TX_CREATED_ON (6),
    	TX_DESCRIPTION (7),
    	TX_COMMENT (8);

    	private int colNo;

    	FORMULA_VALUE(int colNo) {
            this.colNo = colNo;
        }    	
        
        public int getcolNo() {
        	return this.colNo;
        }
    }
   





    
    //@GET
    //@Path("/{gridId}")
	public static GridChain gridchainGridIdGet(int gridId,  ArrayList<ErrorRequestObject> ErrResps, BoardwalkConnection bwcon, ArrayList<Integer> memberNh, ArrayList<Integer> statusCode)
	{
		GridChain gc = new GridChain();

		ErrorRequestObject erb;

		// get the connection
    	Connection connection = null;
		int nhId = -1;
		int memberId = -1;
		
		connection = bwcon.getConnection();
		memberId = memberNh.get(0);
		nhId = memberNh.get(1);

		
		PreparedStatement preparedstatement = null;
		ResultSet rs = null ;
		boolean results = false;
		int rsCount = 0;
		try
		{
			String CALL_BW_GET_GRID_DATA  = null;
			CALL_BW_GET_GRID_DATA = "{CALL BW_GET_GRID_DATA(?,?)}";
			
			preparedstatement = connection.prepareStatement(CALL_BW_GET_GRID_DATA);

			preparedstatement.setInt(1, gridId);
			preparedstatement.setInt(2, memberId);
			
			results = preparedstatement.execute();

			int nCol;
			String[] row ;
			ArrayList<String[]> table;

			
			ArrayList<Transaction> gts = new ArrayList<Transaction>();
			Transaction gt = new Transaction();
			//gts.add(gt);
			
			ArrayList<Integer> TxIds = new ArrayList<Integer>();
			
			ArrayList<Integer> cellIds = new ArrayList<Integer>();
			CellChain currCC ;
			ArrayList<CellChain>  Arrclc = new ArrayList<CellChain>() ;
			int cellIndex ;
			
			do
		    {
				if (results)
				{
					rs = preparedstatement.getResultSet();

					switch(rsCount)
					{
					case 0:			//GridChain
						System.out.println("Processing " + rsCount + "th result");
						nCol = rs.getMetaData().getColumnCount();
						row = new String[nCol] ;
						table = new ArrayList<>();
						while (rs.next()) {

						    row = new String[nCol];
						    for( int iCol = 1; iCol <= nCol; iCol++ ){
						            Object obj = rs.getObject( iCol );
						            row[iCol-1] = (obj == null) ?null:obj.toString();
						    }

						    gc.setCollabId(Integer.parseInt(row[GRID_CHAIN_INFO.COLLAB_ID.getcolNo()]));
							gc.setCreationTxId(Integer.parseInt(row[GRID_CHAIN_INFO.TX_ID_CREATED.getcolNo()]));
							gc.setCreatorId(Integer.parseInt(row[GRID_CHAIN_INFO.TX_CREATED_USER_ID.getcolNo()]));
							gc.setCreatorName(row[GRID_CHAIN_INFO.TX_CREATED_USER_EMAIL.getcolNo()]);
/*							gc.setGridCells(gridCells);
							gc.setGridColumns(gridColumns);
							gc.setGridFormulas(gridFormulas);
							gc.setGridRows(gridRows);
							gc.setGridTransactions(gridTransactions);
*/							gc.setWbId(Integer.parseInt(row[GRID_CHAIN_INFO.WB_ID.getcolNo()]));

							if ( !TxIds.contains(Integer.parseInt(row[GRID_CHAIN_INFO.TX_ID_CREATED.getcolNo()])) )
							{
								System.out.println("Gridchain : Adding txId:" +  row[GRID_CHAIN_INFO.TX_ID_CREATED.getcolNo()]);
								TxIds.add(Integer.parseInt(row[GRID_CHAIN_INFO.TX_ID_CREATED.getcolNo()])) ;

								gt = new Transaction();
								//gt.setCreatedOn(Long.parseLong(row[GRID_CHAIN_INFO.TX_CREATED_ON.getcolNo()]));
								//gt.setCreatedOn(Long.valueOf(rs.getDate( Integer.parseInt(row[GRID_CHAIN_INFO.TX_CREATED_ON.getcolNo()] ))));
								
								gt.setCreatedOn(java.sql.Timestamp.valueOf(row[GRID_CHAIN_INFO.TX_CREATED_ON.getcolNo()]).getTime()) ;
								gt.setTransactionTimeUTC(java.sql.Timestamp.valueOf(row[GRID_CHAIN_INFO.TX_CREATED_ON.getcolNo()]));
								gt.setDescription(row[GRID_CHAIN_INFO.TX_CREATED_DESCRIPTION.getcolNo()]);
								gt.setId(Integer.parseInt(row[GRID_CHAIN_INFO.TX_ID_CREATED.getcolNo()]));
								// folloiwng line give java.lang.NumberFormatException: For input string: "2018-09-20 11:41:17.64" Exception
								//gt.setTransactionTimeUTC( rs.getTimestamp( Integer.parseInt(row[GRID_CHAIN_INFO.TX_CREATED_ON.getcolNo()] ) ) );
								gt.setUserId(Integer.parseInt(row[GRID_CHAIN_INFO.TX_CREATED_USER_ID.getcolNo()]));
								gt.setUserName(row[GRID_CHAIN_INFO.TX_CREATED_USER_EMAIL.getcolNo()]);
								gt.setKeyword(row[GRID_CHAIN_INFO.TX_CREATED_COMMENT.getcolNo()]);
								gts.add(gt);
				
							}
							
							if (row[GRID_CHAIN_INFO.TX_ID_LOCKED.getcolNo()] != null )
							{
								if ( !TxIds.contains(Integer.parseInt(row[GRID_CHAIN_INFO.TX_ID_LOCKED.getcolNo()])) )
								{
									System.out.println("Gridchain : Adding Locked txId:" +  row[GRID_CHAIN_INFO.TX_ID_LOCKED.getcolNo()]);
									TxIds.add(Integer.parseInt(row[GRID_CHAIN_INFO.TX_ID_LOCKED.getcolNo()])) ;

									gt = new Transaction();
									gt.setCreatedOn(java.sql.Timestamp.valueOf(row[GRID_CHAIN_INFO.TX_LOCKED_ON.getcolNo()]).getTime()) ;
									gt.setTransactionTimeUTC(java.sql.Timestamp.valueOf(row[GRID_CHAIN_INFO.TX_LOCKED_ON.getcolNo()]));
									gt.setDescription(row[GRID_CHAIN_INFO.TX_LOCKED_DESCRIPTION.getcolNo()]);
									gt.setId(Integer.parseInt(row[GRID_CHAIN_INFO.TX_ID_LOCKED.getcolNo()]));
									gt.setUserId(Integer.parseInt(row[GRID_CHAIN_INFO.TX_LOCKED_USER_ID.getcolNo()]));
									gt.setUserName(row[GRID_CHAIN_INFO.TX_LOCKED_USER_EMAIL.getcolNo()]);
									gt.setKeyword(row[GRID_CHAIN_INFO.TX_LOCKED_COMMENT.getcolNo()]);
									gts.add(gt);
								}
							}

						}
						rs.close();						
						break;
						
					case 1:			//ColumnChain
						System.out.println("Processing " + rsCount + "th result");
						nCol = rs.getMetaData().getColumnCount();
						row = new String[nCol] ;
						table = new ArrayList<>();
					    ArrayList<ColumnChain>  Arrcc = new ArrayList<ColumnChain>();
					    ColumnChain cc;
						while (rs.next()) {
						    row = new String[nCol];
						    for( int iCol = 1; iCol <= nCol; iCol++ ){
						            Object obj = rs.getObject( iCol );
						            row[iCol-1] = (obj == null) ?null:obj.toString();
						    }
						    cc = new ColumnChain();
						    cc.setColumnId(Integer.parseInt(row[COLUMN_CHAIN_INFO.COLUMN_ID.getcolNo()]));
						    cc.setColumnSource(row[COLUMN_CHAIN_INFO.COLUMN_SOURCE.getcolNo()]);
						    cc.setColumnType(row[COLUMN_CHAIN_INFO.COLUMN_TYPE.getcolNo()]);
						    cc.setColumnWidth(Integer.parseInt(row[COLUMN_CHAIN_INFO.COLUMN_WIDTH.getcolNo()]));
						    
						    //If Column is Active. DeletionTxId is null. -1 is set.
						    if (row[COLUMN_CHAIN_INFO.IS_ACTIVE.getcolNo()].equalsIgnoreCase("1"))
						    {
						    	cc.setCreationTxId(Integer.parseInt(row[COLUMN_CHAIN_INFO.CREATED_TX_ID.getcolNo()]));
						    	cc.setDeletionTxId(-1);
						    }
					    	else
					    	{
							    //If Column is NOT Active. Both CreationTxId and DeletionTxId is set.
						    	cc.setCreationTxId(Integer.parseInt(row[COLUMN_CHAIN_INFO.CREATED_TX_ID.getcolNo()]));
						    	cc.setDeletionTxId(Integer.parseInt(row[COLUMN_CHAIN_INFO.DELETED_TX_ID.getcolNo()]));
					    	}
						    
						    cc.setGridId(Integer.parseInt(row[COLUMN_CHAIN_INFO.GRID_ID.getcolNo()]));
						    cc.setIsActive(row[COLUMN_CHAIN_INFO.IS_ACTIVE.getcolNo()].equalsIgnoreCase("1"));  
						    cc.setName(row[COLUMN_CHAIN_INFO.COLUMN_NAME.getcolNo()]);
						    cc.setOwnerEmail(row[COLUMN_CHAIN_INFO.CREATED_TX_USER_EMAIL.getcolNo()]);
						    cc.setOwnerId(Integer.parseInt(row[COLUMN_CHAIN_INFO.CREATED_TX_USER_ID.getcolNo()]));
						    cc.setSeqNo(Float.parseFloat(row[COLUMN_CHAIN_INFO.SEQUENCE_NUMBER.getcolNo()]));
						    Arrcc.add(cc);
						    
						    //COLUMN_CHAIN_INFO.CREATED_TX_ID
						    //COLUMN_CHAIN_INFO.DELETED_TX_ID
							if ( !TxIds.contains(Integer.parseInt(row[COLUMN_CHAIN_INFO.CREATED_TX_ID.getcolNo()])) )
							{
								System.out.println("Columnchain : Adding txId:" +  row[COLUMN_CHAIN_INFO.CREATED_TX_ID.getcolNo()]);

								TxIds.add(Integer.parseInt(row[COLUMN_CHAIN_INFO.CREATED_TX_ID.getcolNo()])) ;
								
								gt = new Transaction();

								gt.setCreatedOn(java.sql.Timestamp.valueOf(row[COLUMN_CHAIN_INFO.CREATED_ON.getcolNo()]).getTime()) ;
								gt.setTransactionTimeUTC(java.sql.Timestamp.valueOf(row[COLUMN_CHAIN_INFO.CREATED_ON.getcolNo()]));
								gt.setDescription(row[COLUMN_CHAIN_INFO.CREATED_TX_DESC.getcolNo()]);
								gt.setId(Integer.parseInt(row[COLUMN_CHAIN_INFO.CREATED_TX_ID.getcolNo()]));
								gt.setUserId(Integer.parseInt(row[COLUMN_CHAIN_INFO.CREATED_TX_USER_ID.getcolNo()]));
								gt.setUserName(row[COLUMN_CHAIN_INFO.CREATED_TX_USER_EMAIL.getcolNo()]);
								gt.setKeyword(row[COLUMN_CHAIN_INFO.CREATED_TX_COMMENT.getcolNo()]);
								gts.add(gt);
							}

						    if (row[COLUMN_CHAIN_INFO.IS_ACTIVE.getcolNo()].equalsIgnoreCase("0"))
							{
								if ( !TxIds.contains(Integer.parseInt(row[COLUMN_CHAIN_INFO.DELETED_TX_ID.getcolNo()])) )
								{
									System.out.println("Columnchain : Adding Deleted txId:" +  row[COLUMN_CHAIN_INFO.DELETED_TX_ID.getcolNo()]);

									TxIds.add(Integer.parseInt(row[COLUMN_CHAIN_INFO.DELETED_TX_ID.getcolNo()])) ;

									gt = new Transaction();
									
									gt.setCreatedOn(java.sql.Timestamp.valueOf(row[COLUMN_CHAIN_INFO.DELETED_ON.getcolNo()]).getTime()) ;
									gt.setTransactionTimeUTC(java.sql.Timestamp.valueOf(row[COLUMN_CHAIN_INFO.DELETED_ON.getcolNo()]));
									gt.setDescription(row[COLUMN_CHAIN_INFO.DELETED_TX_DESC.getcolNo()]);
									gt.setId(Integer.parseInt(row[COLUMN_CHAIN_INFO.DELETED_TX_ID.getcolNo()]));
									gt.setUserId(Integer.parseInt(row[COLUMN_CHAIN_INFO.DELETED_TX_USER_ID.getcolNo()]));
									gt.setUserName(row[COLUMN_CHAIN_INFO.DELETED_TX_USER_EMAIL.getcolNo()]);
									gt.setKeyword(row[COLUMN_CHAIN_INFO.DELETED_TX_COMMENT.getcolNo()]);
									gts.add(gt);
								}
							}
						}
						rs.close();			
						gc.setGridColumns(Arrcc);
						break;

					case 2:			//RowChain
						System.out.println("Processing " + rsCount + "th result");
						nCol = rs.getMetaData().getColumnCount();
						row = new String[nCol] ;
						table = new ArrayList<>();
					    ArrayList<RowChain>  Arrrc = new ArrayList<RowChain>();
					    RowChain rc;
						while (rs.next()) {
						    row = new String[nCol];
						    for( int iCol = 1; iCol <= nCol; iCol++ ){
						            Object obj = rs.getObject( iCol );
						            row[iCol-1] = (obj == null) ?null:obj.toString();
						    }
						    rc = new RowChain();
						    
						    if (row[ROW_CHAIN_INFO.IS_ACTIVE.getcolNo()].equalsIgnoreCase("1"))
						    {
						    	rc.setCreationTxId(Integer.parseInt(row[ROW_CHAIN_INFO.CREATED_TX_ID.getcolNo()]));
						    	rc.setDeletionTxId(-1);
						    }
						    else
						    {
						    	rc.setCreationTxId(Integer.parseInt(row[ROW_CHAIN_INFO.CREATED_TX_ID.getcolNo()]));
						    	rc.setDeletionTxId(Integer.parseInt(row[ROW_CHAIN_INFO.DELETED_TX_ID.getcolNo()]));
						    }
						    
						    rc.setGridId(Integer.parseInt(row[ROW_CHAIN_INFO.GRID_ID.getcolNo()]));
						    rc.setIsActive(row[ROW_CHAIN_INFO.IS_ACTIVE.getcolNo()].equalsIgnoreCase("1"));
						    rc.setOwnerEmail(row[ROW_CHAIN_INFO.OWNER_EMAIL.getcolNo()]);
						    rc.setOwnerId(Integer.parseInt(row[ROW_CHAIN_INFO.OWNER_ID.getcolNo()]));
						    rc.setRowId(Integer.parseInt(row[ROW_CHAIN_INFO.BW_ROW_ID.getcolNo()]));
						    rc.setSequNo(Float.parseFloat(row[ROW_CHAIN_INFO.SEQUENCE_NUMBER.getcolNo()]));
							Arrrc.add(rc);
							
							//ROW_CHAIN_INFO.CREATE_TX_ID
							//ROW_CHAIN_INFO.DELETE_TX_ID
							if ( !TxIds.contains(Integer.parseInt(row[ROW_CHAIN_INFO.CREATED_TX_ID.getcolNo()])) )
							{
								System.out.println("Rowchain : Adding txId:" +  row[ROW_CHAIN_INFO.CREATED_TX_ID.getcolNo()]);

								TxIds.add(Integer.parseInt(row[ROW_CHAIN_INFO.CREATED_TX_ID.getcolNo()])) ;

								gt = new Transaction();

								gt.setCreatedOn(java.sql.Timestamp.valueOf(row[ROW_CHAIN_INFO.CREATED_ON.getcolNo()]).getTime()) ;
								gt.setTransactionTimeUTC(java.sql.Timestamp.valueOf(row[ROW_CHAIN_INFO.CREATED_ON.getcolNo()]));
								gt.setDescription(row[ROW_CHAIN_INFO.CREATED_DESCRIPTION.getcolNo()]);
								gt.setId(Integer.parseInt(row[ROW_CHAIN_INFO.CREATED_TX_ID.getcolNo()]));
								gt.setUserId(Integer.parseInt(row[ROW_CHAIN_INFO.CREATED_USER_ID.getcolNo()]));
								gt.setUserName(row[ROW_CHAIN_INFO.CREATED_USER_EMAIL.getcolNo()]);
								gt.setKeyword(row[ROW_CHAIN_INFO.CREATED_COMMENT.getcolNo()]);
								gts.add(gt);								
							}
							
							if (row[ROW_CHAIN_INFO.IS_ACTIVE.getcolNo()].equalsIgnoreCase("0"))
							{
								if ( !TxIds.contains(Integer.parseInt(row[ROW_CHAIN_INFO.DELETED_TX_ID.getcolNo()])) )
								{
									System.out.println("Rowchain : Adding Deleted txId:" +  row[ROW_CHAIN_INFO.DELETED_TX_ID.getcolNo()]);

									TxIds.add(Integer.parseInt(row[ROW_CHAIN_INFO.DELETED_TX_ID.getcolNo()])) ;

									gt = new Transaction();

									gt.setCreatedOn(java.sql.Timestamp.valueOf(row[ROW_CHAIN_INFO.DELETED_ON.getcolNo()]).getTime()) ;
									gt.setTransactionTimeUTC(java.sql.Timestamp.valueOf(row[ROW_CHAIN_INFO.DELETED_ON.getcolNo()]));
									gt.setDescription(row[ROW_CHAIN_INFO.DELETED_TX_DESCRIPTION.getcolNo()]);
									gt.setId(Integer.parseInt(row[ROW_CHAIN_INFO.DELETED_TX_ID.getcolNo()]));
									gt.setUserId(Integer.parseInt(row[ROW_CHAIN_INFO.DELETED_TX_USER_ID.getcolNo()]));
									gt.setUserName(row[ROW_CHAIN_INFO.DELETED_TX_USER_EMAIL.getcolNo()]);
									gt.setKeyword(row[ROW_CHAIN_INFO.DELETED_TX_COMMENT.getcolNo()]);
									gts.add(gt);									
								}
							}
						}
						rs.close();			
						gc.setGridRows(Arrrc);
						break;

					case 3:			//CellChain
						System.out.println("Processing " + rsCount + "th result");
						nCol = rs.getMetaData().getColumnCount();
						row = new String[nCol] ;
						table = new ArrayList<>();
					    Arrclc = new ArrayList<CellChain>();
					    CellChain clc;
						while (rs.next()) {
						    row = new String[nCol];
						    for( int iCol = 1; iCol <= nCol; iCol++ ){
						            Object obj = rs.getObject( iCol );
						            row[iCol-1] = (obj == null) ?null:obj.toString();
						    }
						    clc = new CellChain();
						    clc.setCellId(Integer.parseInt(row[CELL_CHAIN_INFO.BW_CELL_ID.getcolNo()]));
						    clc.setCellType(row[CELL_CHAIN_INFO.CELL_TYPE.getcolNo()]);
						    clc.setColumnId(Integer.parseInt(row[CELL_CHAIN_INFO.BW_COLUMN_ID.getcolNo()]));
						    clc.setGridId(Integer.parseInt(row[CELL_CHAIN_INFO.GRID_ID.getcolNo()]));
						    clc.setIsActive(row[CELL_CHAIN_INFO.IS_ACTIVE.getcolNo()].equalsIgnoreCase("1"));
						    clc.setRowId(Integer.parseInt(row[CELL_CHAIN_INFO.BW_ROW_ID.getcolNo()]));
						    //clc.setCellHistory();			// Set after processing StringValues
						    //clc.setCellStatus();			// Set after processing CellStatus
							Arrclc.add( clc);
							cellIds.add(Integer.parseInt(row[CELL_CHAIN_INFO.BW_CELL_ID.getcolNo()]));
							
							//CELL_CHAIN_INFO.TX_ID
							if ( !TxIds.contains(Integer.parseInt(row[CELL_CHAIN_INFO.TX_ID.getcolNo()])) )
							{
								System.out.println("Cellchain : Adding txId:" +  row[CELL_CHAIN_INFO.TX_ID.getcolNo()]);

								TxIds.add(Integer.parseInt(row[CELL_CHAIN_INFO.TX_ID.getcolNo()])) ;
								
								gt = new Transaction();
								
								gt.setCreatedOn(java.sql.Timestamp.valueOf(row[CELL_CHAIN_INFO.TX_CREATED_ON.getcolNo()]).getTime()) ;
								gt.setTransactionTimeUTC(java.sql.Timestamp.valueOf(row[CELL_CHAIN_INFO.TX_CREATED_ON.getcolNo()]));
								gt.setDescription(row[CELL_CHAIN_INFO.TX_DESCRIPTION.getcolNo()]);
								gt.setId(Integer.parseInt(row[CELL_CHAIN_INFO.TX_ID.getcolNo()]));
								gt.setUserId(Integer.parseInt(row[CELL_CHAIN_INFO.TX_USER_ID.getcolNo()]));
								gt.setUserName(row[CELL_CHAIN_INFO.TX_USER_EMAIL.getcolNo()]);
								gt.setKeyword(row[CELL_CHAIN_INFO.TX_COMMENT.getcolNo()]);
								gts.add(gt);								
							}
							
						}
						rs.close();			
						gc.setGridCells(Arrclc);
						break;

					case 4:			//StringValues
						System.out.println("Processing " + rsCount + "th result");
						nCol = rs.getMetaData().getColumnCount();
						row = new String[nCol] ;
						table = new ArrayList<>();
					    ArrayList<StringValues>  Arrsv = new ArrayList<StringValues>();
					    StringValues sv;
					    int currCellId = -1;
					    
						while (rs.next()) {
						    row = new String[nCol];
						    for( int iCol = 1; iCol <= nCol; iCol++ ){
						            Object obj = rs.getObject( iCol );
						            row[iCol-1] = (obj == null) ?null:obj.toString();
						    }
						
						    if (currCellId != Integer.parseInt(row[STRING_VALUES.BW_CELL_ID.getcolNo()]))
						    {
						    	if (currCellId != -1)
						    	{
						    		//Set Arrsv to CellChain object of the CellId
						    		cellIndex = cellIds.indexOf(currCellId);
						    		currCC = Arrclc.get(cellIndex);
						    		currCC.setCellHistory(Arrsv);
						    		Arrclc.set(cellIndex, currCC);
						    	}
						    	//Setting currCellId to new one. Resetting Arrsv for new cellId
					    		currCellId = Integer.parseInt(row[STRING_VALUES.BW_CELL_ID.getcolNo()]);
						    	Arrsv = new ArrayList<StringValues>();
						    }
						    sv = new StringValues();
						    sv.setCellId(Integer.parseInt(row[STRING_VALUES.BW_CELL_ID.getcolNo()]));
						    sv.setFormulaId( (row[STRING_VALUES.FORMULA_ID.getcolNo()]==null)?null: Integer.parseInt(row[STRING_VALUES.FORMULA_ID.getcolNo()] ));
						    sv.setStringValue(row[STRING_VALUES.STRING_VALUE.getcolNo()]);
						    sv.setStringValueId(Integer.parseInt(row[STRING_VALUES.BW_STRING_VALUE_ID.getcolNo()]));
						    sv.setTxId(Integer.parseInt(row[STRING_VALUES.TX_ID.getcolNo()]));
						    
						    Arrsv.add(sv);

						    //STRING_VALUES.TX_ID
							if ( !TxIds.contains(Integer.parseInt(row[STRING_VALUES.TX_ID.getcolNo()])) )
							{
								System.out.println("StringValues : Adding txId:" +  row[STRING_VALUES.TX_ID.getcolNo()]);

								TxIds.add(Integer.parseInt(row[STRING_VALUES.TX_ID.getcolNo()])) ;
								
								gt = new Transaction();

								gt.setCreatedOn(java.sql.Timestamp.valueOf(row[STRING_VALUES.TX_CREATED_ON.getcolNo()]).getTime()) ;
								gt.setTransactionTimeUTC(java.sql.Timestamp.valueOf(row[STRING_VALUES.TX_CREATED_ON.getcolNo()]));
								gt.setDescription(row[STRING_VALUES.TX_DESCRIPTION.getcolNo()]);
								gt.setId(Integer.parseInt(row[STRING_VALUES.TX_ID.getcolNo()]));
								gt.setUserId(Integer.parseInt(row[STRING_VALUES.TX_CREATED_USER_ID.getcolNo()]));
								gt.setUserName(row[STRING_VALUES.TX_CREATED_USER_EMAIL.getcolNo()]);
								gt.setKeyword(row[STRING_VALUES.TX_COMMENT.getcolNo()]);
								gts.add(gt);								
							}


						
						}
						//For last cellId records 
			    		cellIndex = cellIds.indexOf(currCellId);
			    		currCC = Arrclc.get(cellIndex);
			    		currCC.setCellHistory(Arrsv);
			    		Arrclc.set(cellIndex, currCC);
						rs.close();			

						gc.setGridCells(Arrclc);
						break;
					
					case 5:			//CellStatus
						System.out.println("Processing " + rsCount + "th result");
						nCol = rs.getMetaData().getColumnCount();
						row = new String[nCol] ;
						table = new ArrayList<>();
					    ArrayList<CellStatus>  Arrcs = new ArrayList<CellStatus>();
					    CellStatus cls;
					    currCellId = -1;
					    
						while (rs.next()) {
						    row = new String[nCol];
						    for( int iCol = 1; iCol <= nCol; iCol++ ){
						            Object obj = rs.getObject( iCol );
						            row[iCol-1] = (obj == null) ?null:obj.toString();
						    }
						    
						    if (currCellId != Integer.parseInt(row[CELL_STATUS.BW_CELL_ID.getcolNo()]))
						    {
						    	if (currCellId != -1)
						    	{
						    		//Set Arrsv to CellChain object of the CellId
						    		cellIndex = cellIds.indexOf(currCellId);
						    		currCC = Arrclc.get(cellIndex);
						    		currCC.setCellStatus(Arrcs);
						    		Arrclc.set(cellIndex, currCC);
						    	}
						    	//Setting currCellId to new one. Resetting Arrsv for new cellId
					    		currCellId = Integer.parseInt(row[CELL_STATUS.BW_CELL_ID.getcolNo()]);
					    		Arrcs = new ArrayList<CellStatus>();
						    }

						    cls = new CellStatus();
						    cls.setCellId(Integer.parseInt(row[CELL_STATUS.BW_CELL_ID.getcolNo()]));
						    cls.setCellStatusId(Integer.parseInt(row[CELL_STATUS.BW_CELL_STATUS_ID.getcolNo()]));
						    //(row[STRING_VALUES.FORMULA_ID.getcolNo()]==null)?null: Integer.parseInt(row[STRING_VALUES.FORMULA_ID.getcolNo()] ));						    
						    
						    System.out.println("row[CELL_STATUS.IS_ACTIVE.getcolNo()] ------->>>"  + row[CELL_STATUS.IS_ACTIVE.getcolNo()]);
						    cls.setIsActive( (row[CELL_STATUS.IS_ACTIVE.getcolNo()]=="true")? 1: 0);
						    cls.setTxId(Integer.parseInt(row[CELL_STATUS.TX_ID.getcolNo()]));
						    
						    Arrcs.add(cls);
						    
						    //CELL_STATUS.TX_ID
							if ( !TxIds.contains(Integer.parseInt(row[CELL_STATUS.TX_ID.getcolNo()])) )
							{
								System.out.println("CellStatus : Adding txId:" +  row[CELL_STATUS.TX_ID.getcolNo()]);

								TxIds.add(Integer.parseInt(row[CELL_STATUS.TX_ID.getcolNo()])) ;
								
								gt = new Transaction();

								gt.setCreatedOn(java.sql.Timestamp.valueOf(row[CELL_STATUS.TX_CREATED_ON.getcolNo()]).getTime()) ;
								gt.setTransactionTimeUTC(java.sql.Timestamp.valueOf(row[CELL_STATUS.TX_CREATED_ON.getcolNo()]));
								gt.setDescription(row[CELL_STATUS.TX_DESCRIPTION.getcolNo()]);
								gt.setId(Integer.parseInt(row[CELL_STATUS.TX_ID.getcolNo()]));
								gt.setUserId(Integer.parseInt(row[CELL_STATUS.TX_CREATED_USER_ID.getcolNo()]));
								gt.setUserName(row[CELL_STATUS.TX_CREATED_USER_EMAIL.getcolNo()]);
								gt.setKeyword(row[CELL_STATUS.TX_COMMENT.getcolNo()]);
								gts.add(gt);								
							}
						}
						//for last cellId record
			    		cellIndex = cellIds.indexOf(currCellId);
			    		currCC = Arrclc.get(cellIndex);
			    		currCC.setCellStatus(Arrcs);
			    		Arrclc.set(cellIndex, currCC);

			    		rs.close();			

						gc.setGridCells(Arrclc);
						break;

					case 6:			//FormualValues
						System.out.println("Processing " + rsCount + "th result FormualValues");
						nCol = rs.getMetaData().getColumnCount();
						row = new String[nCol] ;
						table = new ArrayList<>();
					    ArrayList<FormulaValue>  Arrfmlas = new ArrayList<FormulaValue>();
					    FormulaValue fmla;
						while (rs.next()) {
						    row = new String[nCol];
						    for( int iCol = 1; iCol <= nCol; iCol++ ){
						            Object obj = rs.getObject( iCol );
						            row[iCol-1] = (obj == null) ?null:obj.toString();
						    }

						    fmla = new FormulaValue();
						    fmla.setFormulaId(Integer.parseInt(row[FORMULA_VALUE.FORMULA_ID.getcolNo()]));
						    fmla.setFormula(row[FORMULA_VALUE.FORMULA.getcolNo()]);
						    fmla.setFormulaIndex(row[FORMULA_VALUE.FORMULA_INDEX.getcolNo()]);
						    fmla.setTxId(Integer.parseInt(row[FORMULA_VALUE.TX_ID.getcolNo()]));
						    
						    Arrfmlas.add(fmla);
						    
						    //FORMULA_VALUE.TX_ID
							if ( !TxIds.contains(Integer.parseInt(row[FORMULA_VALUE.TX_ID.getcolNo()])) )
							{
								System.out.println("FormulaValues : Adding txId:" +  row[FORMULA_VALUE.TX_ID.getcolNo()]);

								TxIds.add(Integer.parseInt(row[FORMULA_VALUE.TX_ID.getcolNo()])) ;
								
								gt = new Transaction();

								gt.setCreatedOn(java.sql.Timestamp.valueOf(row[FORMULA_VALUE.TX_CREATED_ON.getcolNo()]).getTime()) ;
								gt.setTransactionTimeUTC(java.sql.Timestamp.valueOf(row[FORMULA_VALUE.TX_CREATED_ON.getcolNo()]));
								gt.setDescription(row[FORMULA_VALUE.TX_DESCRIPTION.getcolNo()]);
								gt.setId(Integer.parseInt(row[FORMULA_VALUE.TX_ID.getcolNo()]));
								gt.setUserId(Integer.parseInt(row[FORMULA_VALUE.TX_CREATED_USER_ID.getcolNo()]));
								gt.setUserName(row[FORMULA_VALUE.TX_CREATED_USER_EMAIL.getcolNo()]);
								gt.setKeyword(row[FORMULA_VALUE.TX_COMMENT.getcolNo()]);
								gts.add(gt);								
							}

						    
						}
						rs.close();			
						gc.setGridFormulas(Arrfmlas);
						break;
					}
					rsCount++;
					rs.close();
					System.out.println("rsCount ----------" + rsCount);
				}
				results = preparedstatement.getMoreResults(); 
		    }while (results);
		    gc.setGridTransactions(gts);

		    preparedstatement.close();
		}
		catch( SQLException sql1 )
		{

		}
		finally
		{
			try
			{
				preparedstatement.close();
				if (rs != null)
					rs.close();
			}
			catch( SQLException sql2 )
			{
				sql2.printStackTrace();
			}
		}
		return gc;
	}
}
