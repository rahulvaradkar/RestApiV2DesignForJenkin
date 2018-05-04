SELECT  
	BWROW.NAME   ROW_NAME, 
	BWROW.ID   ROW_ID, 
	BWROW.SEQUENCE_NUMBER   ROW_SEQUENCE_NUMBER, 
	BWCOLUMN.ID   COLUMN_ID,  
	BWCOLUMN.SEQUENCE_NUMBER   COLUMN_SEQUENCE_NUMBER, 
	BWCOLUMN.NAME   COLUMN_NAME,
	BWCELL.ID   CELL_ID, 
	BWCELL.CELL_TYPE   CELL_TYPE,
	BW_SV.STRING_VALUE   CELL_STRING_VALUE, 
	-1 CELL_INTEGER_VALUE, 
	1.1 CELL_DOUBLE_VALUE,
	-1 CELL_TBL_VALUE,
	'Not Set' CELL_TBL_NAME,
	BW_TXS.TX_ID, 
	BW_TXS.CREATED_BY   TX_CREATED_BY,
	BW_TXS.CREATED_ON,
	BW_TXS.DESCRIPTION,
	BW_TXS.COMMENT_,
	BW_USER.EMAIL_ADDRESS,
	BWROW.OWNER_ID, BWROW.IS_ACTIVE,
	BWROW.TX_ID   ROW_TID, 
	BWCOLUMN.TX_ID   COL_TID, 
	BWCOLUMN.IS_ACTIVE   COL_ACTIVE,
	BWROW.OWNER_TID   ROW_OWNER_TID,
	BW_ROW_CREATOR_TX.CREATED_BY   ROW_CREATOR_ID,  
	BW_ROW_OWNER.EMAIL_ADDRESS   ROW_OWNER_NAME
	
FROM    BW_ROW   BWROW, 
	BW_COLUMN   BWCOLUMN, 
	BW_CELL   BWCELL,
	BW_BL_ROW   BWB_ROW,
	BW_BL_COLUMN   BWB_COLUMN,
	BW_BL_CELL   BWB_CELL,
	BW_STRING_VALUE     BW_SV, 
	BW_TXS,
	BW_USER, 
	BW_TXS   BW_ROW_CREATOR_TX,	
	BW_USER   BW_ROW_OWNER
WHERE	
	  	BWROW.BW_TBL_ID = ?
	AND    	BWCOLUMN.BW_TBL_ID = ?
	AND     BWB_ROW.BASELINE_ID = ?
	AND     BWB_COLUMN.BASELINE_ID = ?
	AND     BWB_ROW.ROW_ID = BWROW.ID
	AND     BWB_COLUMN.COLUMN_ID = BWCOLUMN.ID
	AND 	BWCELL.BW_ROW_ID = BWROW.ID
	AND 	BWCELL.BW_COLUMN_ID = BWCOLUMN.ID
	AND 	BWCELL.CELL_TYPE = 'STRING'
	AND     BWB_CELL.CELL_ID = BWCELL.ID
	AND     BWB_CELL.BASELINE_ID = ?
	AND  	BWB_CELL.STRING_VALUE_ID = BW_SV.ID
	AND 	BW_TXS.TX_ID = BW_SV.TX_ID
	AND 	BW_TXS.CREATED_BY = BW_USER.ID
	AND   	BW_ROW_CREATOR_TX.TX_ID = BWROW.TX_ID
	AND     BW_ROW_OWNER.ID = BWROW.OWNER_ID
UNION ALL
SELECT  
	BWROW.NAME   ROW_NAME, 
	BWROW.ID   ROW_ID, 
	BWROW.SEQUENCE_NUMBER   ROW_SEQUENCE_NUMBER, 
	BWCOLUMN.ID   COLUMN_ID,  
	BWCOLUMN.SEQUENCE_NUMBER   COLUMN_SEQUENCE_NUMBER, 
	BWCOLUMN.NAME   COLUMN_NAME,
	BWCELL.ID   CELL_ID, 
	BWCELL.CELL_TYPE   CELL_TYPE,
	'_' CELL_STRING_VALUE, 
	BW_IV.INTEGER_VALUE   CELL_INTEGER_VALUE,
	1.1 CELL_DOUBLE_VALUE,
	-1 CELL_TBL_VALUE,
	'Not Set' CELL_TBL_NAME,
	BW_TXS.TX_ID, 
	BW_TXS.CREATED_BY   TX_CREATED_BY,
	BW_TXS.CREATED_ON,
	BW_TXS.DESCRIPTION,
	BW_TXS.COMMENT_,
	BW_USER.EMAIL_ADDRESS,
	BWROW.OWNER_ID, 
	BWROW.IS_ACTIVE,
	BWROW.TX_ID   ROW_TID, 
	BWCOLUMN.TX_ID   COL_TID, 
	BWCOLUMN.IS_ACTIVE   COL_ACTIVE,
	BWROW.OWNER_TID   ROW_OWNER_TID,
	BW_ROW_CREATOR_TX.CREATED_BY   ROW_CREATOR_ID,  
	BW_ROW_OWNER.EMAIL_ADDRESS   ROW_OWNER_NAME
	
FROM    BW_ROW   BWROW, 
	BW_COLUMN   BWCOLUMN, 
	BW_CELL   BWCELL,
	BW_BL_ROW   BWB_ROW,
	BW_BL_COLUMN   BWB_COLUMN,
	BW_BL_CELL   BWB_CELL,
	BW_INTEGER_VALUE     BW_IV, 
	BW_TXS,
	BW_USER, 
	BW_TXS   BW_ROW_CREATOR_TX,	
	BW_USER   BW_ROW_OWNER
WHERE	
	  	BWROW.BW_TBL_ID = ?
	AND    	BWCOLUMN.BW_TBL_ID = ?
	AND     BWB_ROW.BASELINE_ID = ?
	AND     BWB_COLUMN.BASELINE_ID = ?
	AND     BWB_ROW.ROW_ID = BWROW.ID
	AND     BWB_COLUMN.COLUMN_ID = BWCOLUMN.ID
	AND 	BWCELL.BW_ROW_ID = BWROW.ID
	AND 	BWCELL.BW_COLUMN_ID = BWCOLUMN.ID
	AND 	BWCELL.CELL_TYPE = 'INTEGER'
	AND     BWB_CELL.CELL_ID = BWCELL.ID
	AND     BWB_CELL.BASELINE_ID = ?
	AND  	BWB_CELL.INTEGER_VALUE_ID = BW_IV.ID
	AND 	BW_TXS.TX_ID = BW_IV.TX_ID
	AND 	BW_TXS.CREATED_BY = BW_USER.ID
	AND   	BW_ROW_CREATOR_TX.TX_ID = BWROW.TX_ID
	AND     BW_ROW_OWNER.ID = BWROW.OWNER_ID
UNION ALL
SELECT  
	BWROW.NAME   ROW_NAME, 
	BWROW.ID   ROW_ID, 
	BWROW.SEQUENCE_NUMBER   ROW_SEQUENCE_NUMBER, 
	BWCOLUMN.ID   COLUMN_ID,  
	BWCOLUMN.SEQUENCE_NUMBER   COLUMN_SEQUENCE_NUMBER, 
	BWCOLUMN.NAME   COLUMN_NAME,
	BWCELL.ID   CELL_ID, 
	BWCELL.CELL_TYPE   CELL_TYPE,
	'_' CELL_STRING_VALUE, 
	-1 CELL_INTEGER_VALUE,
	BW_DV.DOUBLE_VALUE   CELL_DOUBLE_VALUE,
	-1 CELL_TBL_VALUE,
	'Not Set' CELL_TBL_NAME,
	BW_TXS.TX_ID, 
	BW_TXS.CREATED_BY   TX_CREATED_BY,
	BW_TXS.CREATED_ON,
	BW_TXS.DESCRIPTION,
	BW_TXS.COMMENT_,
	BW_USER.EMAIL_ADDRESS,
	BWROW.OWNER_ID, 
	BWROW.IS_ACTIVE,BWROW.TX_ID   ROW_TID,
	BWCOLUMN.TX_ID   COL_TID, 
	BWCOLUMN.IS_ACTIVE   COL_ACTIVE,
	BWROW.OWNER_TID   ROW_OWNER_TID,
	BW_ROW_CREATOR_TX.CREATED_BY   ROW_CREATOR_ID, 
	BW_ROW_OWNER.EMAIL_ADDRESS   ROW_OWNER_NAME
	
FROM    BW_ROW   BWROW, 
	BW_COLUMN   BWCOLUMN, 
	BW_CELL   BWCELL,
	BW_BL_ROW   BWB_ROW,
	BW_BL_COLUMN   BWB_COLUMN,
	BW_BL_CELL   BWB_CELL,
	BW_DOUBLE_VALUE     BW_DV, 
	BW_TXS,
	BW_USER, 
	BW_TXS   BW_ROW_CREATOR_TX,	
	BW_USER   BW_ROW_OWNER
WHERE	
	  	BWROW.BW_TBL_ID = ?
	AND    	BWCOLUMN.BW_TBL_ID = ?
	AND     BWB_ROW.BASELINE_ID = ?
	AND     BWB_COLUMN.BASELINE_ID = ?
	AND     BWB_ROW.ROW_ID = BWROW.ID
	AND     BWB_COLUMN.COLUMN_ID = BWCOLUMN.ID
	AND 	BWCELL.BW_ROW_ID = BWROW.ID
	AND 	BWCELL.BW_COLUMN_ID = BWCOLUMN.ID
	AND 	BWCELL.CELL_TYPE = 'FLOAT'
	AND     BWB_CELL.CELL_ID = BWCELL.ID
	AND     BWB_CELL.BASELINE_ID = ?
	AND  	BWB_CELL.DOUBLE_VALUE_ID = BW_DV.ID
	AND 	BW_TXS.TX_ID = BW_DV.TX_ID
	AND 	BW_TXS.CREATED_BY = BW_USER.ID
	AND   	BW_ROW_CREATOR_TX.TX_ID = BWROW.TX_ID
	AND     BW_ROW_OWNER.ID = BWROW.OWNER_ID
ORDER BY  ROW_SEQUENCE_NUMBER, COLUMN_ID