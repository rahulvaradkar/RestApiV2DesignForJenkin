
<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer,java.util.regex.*, com.boardwalk.database.Transaction, com.boardwalk.table.*,java.io.*, java.util.*,com.boardwalk.query.*"%>


<%
	FormDefinition fd = (FormDefinition) request.getAttribute("FormDefinition");
	String title = fd.getTitle();
	String instr = fd.getInstructions();

	Hashtable sectionsByLevel = fd.getSections();
	int numLevels = sectionsByLevel.size();
	int horizGrid = fd.getMaxSectionsAtLevel();

	System.out.println("NumLevels = " + numLevels);
	System.out.println("horizGrid = " + horizGrid);

	TableContents tbcon = (TableContents)request.getAttribute("TableContents");
	Vector rowIds = tbcon.getRowIds();
	Hashtable cells;

%>
<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>

<LINK REL=STYLESHEET TYPE="text/css" HREF="../css/stylesheet.css" TITLE="stylesheet">
<%@include file='/jsp/common/error_message.jsp' %>

<table width="700" border="0">
<!--
  <tr>
    <td>Logo image</td>
  </tr>
 -->
  <tr>
    <td><br>
      Title : <%=title%>
      <br> </td>
  </tr>
  <tr>
    <td> Instructions : <%=instr%> <br> </td>
  </tr>
  <tr>
    <td><table border="0">
<%
	int iL;
	int iH;
	for (iL = 0; iL < numLevels; iL++)
	{
		System.out.println("Processing level : " + iL);
		Vector sections = (Vector)sectionsByLevel.get(new Integer(iL + 1));
		System.out.println(" sections = " + sections);
		int numSectionsAtLevel = sections.size();
%>
	<tr>

<%
		for (iH = 0; iH < horizGrid; iH++)
		{
			if (iH < sections.size())
			{
				FormSection section = (FormSection)sections.elementAt(iH);
%>
	<td valign="top" > <table width="100%" border="1" cellpadding="2" cellspacing="0" bordercolor="#eeeeee">
		<tr bgcolor="#eeeeee">
			<td colspan=2><%=section.getName()%></td>
		</tr>
<%
				Vector fields = section.getFields();
				Iterator fldI = fields.iterator();
				// go through the list of cells Excel has sent
				while(fldI.hasNext())
				{
					FormField field = (FormField)fldI.next();
					// get the field properties
					String type =  ((FormFieldProperty)field.getPropertyByName().get("Type")).getValue();
					String label = ((FormFieldProperty)field.getPropertyByName().get("Label")).getValue();
%>
			<tr>
					<td><%=label%></td>
					<td><input type="text" name="<%=field.getName()%>" id="<%=field.getName()%>"></td>
			</tr>
<%
				} // while there are fields
			} // if there is section
			else // nothing
			{
%>
			<td>&nbsp;
<%

			}// blank section
%>
	</table></td>
<%
		}// for iH
%>
	</tr>
<%
	}// for iL
%>

  <tr>
    <td><input type="submit" name="Submit" value="Submit">
      &nbsp;
      <input type="submit" name="Submit2" value="Reset"> </td>
  </tr>
</table>
<SCRIPT src="/jsp/common/footer.jsp"> </SCRIPT>

</body>
</html>