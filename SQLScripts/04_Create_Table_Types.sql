/****************************************
--Replace 'BAE_4_4' with the desired name
****************************************/
USE [BAE_4_4]
GO
/****** Object:  UserDefinedTableType [dbo].[BW_COL_SET]    Script Date: 06/03/2015 05:44:30 ******/
CREATE TYPE [dbo].[BW_COL_SET] AS TABLE(
	[BW_COL_ID] [int] NULL
)
GO
/****** Object:  UserDefinedTableType [dbo].[BW_ROW_SET]    Script Date: 06/03/2015 05:44:30 ******/
CREATE TYPE [dbo].[BW_ROW_SET] AS TABLE(
	[BW_ROW_ID] [int] NULL
)
GO
/****** Object:  UserDefinedTableType [dbo].[HISTORY_TAG_COLLECTION]    Script Date: 06/03/2015 05:44:30 ******/
CREATE TYPE [dbo].[HISTORY_TAG_COLLECTION] AS TABLE(
	[NAME] [nvarchar](max) NULL,
	[TAG_ID] [int] NULL,
	[ROW_CRITERIA] [nvarchar](max) NULL,
	[TX_ID] [int] NULL
)
GO
/****** Object:  UserDefinedTableType [dbo].[HISTORY_TAG_LIST]    Script Date: 06/03/2015 05:44:30 ******/
CREATE TYPE [dbo].[HISTORY_TAG_LIST] AS TABLE(
	[NAME] [nvarchar](max) NULL,
	[CONDITION] [int] NULL,
	[KEYNAME] [nvarchar](max) NULL,
	[VALUE] [nvarchar](max) NULL,
	[TX_ID] [int] NULL,
	[PK_KEY] [int] NULL
)
GO
