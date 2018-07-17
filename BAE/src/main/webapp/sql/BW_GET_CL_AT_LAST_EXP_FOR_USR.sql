select  BW_cell.id as CELL_ID, 
	BW_cell.CELL_TYPE,
	BW_string_value.string_value as CELL_STRING_VALUE, 
	-1 CELL_INTEGER_VALUE,
	1.1 CELL_DOUBLE_VALUE,
	-1 CELL_TBL_VALUE,
	BW_string_value.TX_ID
from 
	(
	select BW_string_value.BW_cell_id, max ( BW_string_value.id ) as  max_value_id
	from 
		BW_CELL ,
		BW_row, 
		BW_string_value, 
		BW_TXS  
	where BW_cell.BW_row_id = BW_row.id
	      and BW_row.BW_TBL_id = ?
	      And BW_cell.id = BW_string_value.BW_cell_id
	      AND BW_string_value.TX_ID >= ?      
	      AND BW_string_value.TX_id = BW_TXS.TX_ID
	      AND BW_TXS.CREATED_BY = ?
	      and BW_row.IS_ACTIVE = 1       
	group by BW_string_value.BW_cell_id 
	)
	as maxValueTbl, 
	BW_CELL ,
	BW_row, 
	BW_string_value
where BW_cell.BW_row_id = BW_row.id
      and BW_row.BW_TBL_id = ?
      and BW_cell.id = BW_string_value.BW_cell_id
      and BW_cell.id = maxValueTbl.BW_cell_id
      and BW_string_value.id = maxValueTbl.max_value_id
      AND BW_cell.CELL_TYPE='STRING'	
UNION ALL
select  BW_cell.id as CELL_ID, 
	BW_cell.CELL_TYPE,
	' ' CELL_STRING_VALUE, 
	BW_integer_value.integer_value as CELL_INTEGER_VALUE,
	1.1 CELL_DOUBLE_VALUE,
	-1 CELL_TBL_VALUE,
	BW_integer_value.TX_ID
from 
	(
	select BW_integer_value.BW_cell_id, max ( BW_integer_value.id ) as max_value_id
	from 
	BW_CELL ,
	BW_row, 
	BW_INTEGER_value,
	BW_TXS  
	where BW_cell.BW_row_id = BW_row.id
	      and BW_row.BW_TBL_id = ?
	      and BW_cell.id = BW_integer_value.BW_cell_id
	      and BW_integer_value.TX_id >= ?
	       and BW_row.IS_ACTIVE = 1
	      AND BW_integer_value.TX_id = BW_TXS.TX_ID
	      AND BW_TXS.CREATED_BY = ?     
	group by BW_integer_value.BW_cell_id 
	)
	as maxValueTbl,
	BW_CELL ,
	BW_INTEGER_value,
	BW_row
where 	BW_cell.BW_row_id = BW_row.id
	and BW_row.BW_TBL_id = ?
	and BW_cell.id = BW_integer_value.BW_cell_id
	and BW_cell.id = maxValueTbl.BW_cell_id      
	and BW_integer_value.id = maxValueTbl.max_value_id
	AND BW_cell.CELL_TYPE='INTEGER'	
UNION ALL 
select  BW_cell.id as CELL_ID, 
	BW_cell.CELL_TYPE,
	' ' CELL_STRING_VALUE, 
	1 CELL_INTEGER_VALUE,
	BW_double_value.DOUBLE_VALUE    CELL_DOUBLE_VALUE,
	-1 CELL_TBL_VALUE,	
	BW_double_value.TX_ID
from 
	(
	select BW_double_value.BW_cell_id, max ( BW_double_value.TX_id ) as max_value_id
	from 
	BW_CELL ,
	BW_row, 
	BW_DOUBLE_value ,
	BW_TXS  
	where BW_cell.BW_row_id = BW_row.id
	      and BW_row.BW_TBL_id = ?
	      and BW_cell.id = BW_double_value.BW_cell_id
	      and BW_double_value.TX_id >= ?
	      and BW_row.IS_ACTIVE = 1
	       AND BW_double_value.TX_id = BW_TXS.TX_ID
	      AND BW_TXS.CREATED_BY = ?      
	group by BW_double_value.BW_cell_id 
	)
	as maxValueTbl,
	BW_CELL ,
	BW_row, 
	BW_DOUBLE_value 
where 	BW_cell.BW_row_id = BW_row.id
	and BW_row.BW_TBL_id = ?
	and BW_cell.id = BW_double_value.BW_cell_id
	and BW_cell.id = maxValueTbl.BW_cell_id      
	and BW_double_value.id = maxValueTbl.max_value_id
	AND BW_cell.CELL_TYPE='FLOAT'