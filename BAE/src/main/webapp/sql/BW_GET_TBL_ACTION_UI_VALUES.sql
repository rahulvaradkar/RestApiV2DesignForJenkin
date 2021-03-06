SELECT 	
	BW_OBJECT_ACTIONS.ID, 			
	BW_OBJECT_ACTIONS.OBJECT_TYPE, 
	BW_OBJECT_ACTIONS.OBJECT_ID,
	BW_OBJECT_ACTIONS.ACTION_UI_STRING,
	BW_DEFAULT_ACTION_TYPES.ACTION,
	BW_DEFAULT_ACTION_TYPES.ID   ACTION_ID
FROM  
     BW_OBJECT_ACTIONS, BW_DEFAULT_ACTION_TYPES
WHERE 
     	BW_OBJECT_ACTIONS.OBJECT_ID = ?
   AND  BW_OBJECT_ACTIONS.OBJECT_TYPE = 'TABLE'
   AND  BW_DEFAULT_ACTION_TYPES.OBJECT_TYPE = BW_OBJECT_ACTIONS.OBJECT_TYPE
   AND  BW_DEFAULT_ACTION_TYPES.ID = BW_OBJECT_ACTIONS.ACTION_ID   
UNION
SELECT 	
	-1 ID, 			
	'TABLE' OBJECT_TYPE, 
	-1 OBJECT_ID,
	BW_DEFAULT_ACTION_TYPES.ACTION   ACTION_UI_STRING,
	BW_DEFAULT_ACTION_TYPES.ACTION,
	BW_DEFAULT_ACTION_TYPES.ID   ACTION_ID
FROM  
        BW_DEFAULT_ACTION_TYPES
WHERE   BW_DEFAULT_ACTION_TYPES.OBJECT_TYPE = 'TABLE'
	AND BW_DEFAULT_ACTION_TYPES.ID
	NOT IN
	( 
	SELECT 	BW_OBJECT_ACTIONS.ACTION_ID
	FROM  BW_OBJECT_ACTIONS
	WHERE BW_OBJECT_ACTIONS.OBJECT_TYPE = 'TABLE'
		 AND BW_OBJECT_ACTIONS.OBJECT_ID = ?
	)
