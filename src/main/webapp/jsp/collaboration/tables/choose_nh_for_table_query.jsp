<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.neighborhood.*,java.io.*, java.util.*" %>

<%
Hashtable nhNames = new Hashtable();

Vector nhRel;
String head;
int cBox;
int Cnt = -1;
Integer  cBoxfromserver;
int nhId;
boolean checked = false;

Vector nhTree = (Vector)request.getAttribute("nhTree");
String nhtitle = (String)request.getAttribute("nhtitle");
head = (String)request.getAttribute("head");
cBoxfromserver = (Integer)request.getAttribute("check");


String ViewPreference = (String)request.getAttribute("ViewPreference");
String QueryPreference =(String)request.getAttribute("QueryPreference");
int tableId =    ((Integer)(request.getAttribute("tableId"))).intValue();
Integer baselineIdInteger = (Integer)request.getAttribute("baselineId");
int baselineId = -1;

if ( baselineIdInteger != null )
{
	baselineId = baselineIdInteger.intValue();	
}




try 
{
	nhId = ((Integer)(request.getAttribute("nhId"))).intValue();
	System.out.println("NH ID"+nhId);
	nhRel = (Vector)request.getAttribute("nhRel");
}
catch (Exception e) {
	System.out.println("No Neighborhood Id");
	nhId = -1;
	nhRel = null;
}


if (nhtitle == null)
	nhtitle = "Select Neighborhoods";

if (head ==  null)
	head = "Available Neighborhoods";


if ( cBoxfromserver == null )
{

	System.out.println("No check box");
	cBox = 1;
}
else
{
	cBox = cBoxfromserver.intValue();

}



%>

<html>
<form method="post" action="MyTables">
<head>

<input type="hidden" name="action" value="">
<input type="hidden" name="nhId" value="<%=nhId%>">
<input type="hidden" name="selNhid" value="<%=nhId%>">
<input type="hidden" name="ViewPreference" value="<%=ViewPreference%>">
<input type="hidden" name="QueryPreference" value="<%=QueryPreference%>">
<input type="hidden" name="queryOnChildrenOfNh" value="false">
<input type="hidden" name="tableId" value="<%=tableId%>">
<input type="hidden" id="baselineId" name="baselineId"  value="<%=baselineId%>">







<title><%=nhtitle%></title>

<style>
   /* styles for the tree */
   SPAN.TreeviewSpanArea A {
        font-size: 10pt;
        font-family: verdana,helvetica;
        text-decoration: none;
        color: black
   }
   SPAN.TreeviewSpanArea A:hover {
        color: '#820082';
   }
   /* rest of the document */
   BODY {background-color: white}
   TD {
        font-size: 10pt;
        font-family: verdana,helvetica;
   }
</style>


<!-- SECTION 2: Replace everything (HTML, JavaScript, etc.) from here until the beginning
of SECTION 3 with the pieces of the head section that are needed for your site  -->

<script>

var isCheckboxChecked = true;

          function getTableReportBySpecificNh(queryOnChildrenOfNh) 
	  {    		
	  	  document.forms[0].action.value = 'displayInPlainHTML'
	  	  document.forms[0].queryOnChildrenOfNh.value =queryOnChildrenOfNh
	  	 document.forms[0].submit()
	}
		
	
</script>

<!-- SECTION 3: These four scripts define the tree, do not remove-->
<script>
<%@ include file="/jsp/admin/ua.js" %>
</script>
<script>
<%@ include file="/jsp/admin/ftiens4.js" %>
</script>
<script>
USETEXTLINKS = 1
STARTALLOPEN = 1
USEFRAMES = 0
USEICONS = 0
WRAPTEXT = 0
ICONPATH = "images/"
PERSERVESTATE = 1
HIGHLIGHT = 0
HIGHLIGHT_COLOR = 'white'
HIGHLIGHT_BG ='white'


</script>
<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>


<%
    int no = 0;
    if ((nhTree!= null) && (!nhTree.isEmpty())) {
%>
<script>
    foldersTree = gFld("<b><%=head%></b>", "")
</script>
<%


    // iterate over the neighborhood tree
    Iterator nhIter = nhTree.iterator();
    while (nhIter.hasNext()) {
	NHTree nht = (NHTree)nhIter.next();
	int nhid = nht.getNeighborhood().getId();
	String nhName = nht.getName();
	System.out.println("Level 0 : " + nhName);
	Vector nh1Tree = nht.getChildren();
	no = no + 1;
	nhNames.put( new Integer(nhid), nhName);
	
	
	if (!nh1Tree.isEmpty()) {
%>
<script>
		isCheckboxChecked = <%=checked%>
  		aux = insFld(foldersTree,
				gFldCB(
				"<%=nhName%>"
				, ""
				,"selNH<%=nhid%>"
				,"<%=nhid%>"
				,"<%=cBox%>"
				, ""
				,isCheckboxChecked
				 ))
		
</script>
<%
	}
	else
	{
%>
<script>
		isCheckboxChecked = <%=checked%>
		insDoc(foldersTree,
			gLnkCB("S",
                        	"<%=nhName%>"
				, ""
				,"selNH<%=nhid%>"
				,"<%=nhid%>"
				,"<%=cBox%>"
				, ""
				,isCheckboxChecked
				 ))
	   
</script>
<%
	} // end if
	 // iterate over the neighborhood tree
    	Iterator nh1Iter = nh1Tree.iterator();
    	while (nh1Iter.hasNext()) {
		NHTree nh1t = (NHTree)nh1Iter.next();
		int nh1id = nh1t.getNeighborhood().getId();
		String nh1Name = nh1t.getName();
		System.out.println("Level 1 : " + nh1Name);
        	Vector nh2Tree = nh1t.getChildren();
		no = no + 1;
		nhNames.put( new Integer(nh1id), nhName+"/"+nh1Name);
		
        	if (!nh2Tree.isEmpty()) {
%>

<script>
		isCheckboxChecked = <%=checked%>
		aux1 = insFld(aux,
				gFldCB(
				"<%=nh1Name%>"
				, ""
				,"selNH<%=nh1id%>"
				,"<%=nh1id%>"
				,"<%=cBox%>"
				, ""
				,isCheckboxChecked
				 ))
		
</script>
<%
		}
		else
		{
%>
<script>
			isCheckboxChecked = <%=checked%>
			insDoc(aux,
				gLnkCB("S",
                        	"<%=nh1Name%>"
				, ""
				,"selNH<%=nh1id%>"
				,"<%=nh1id%>"
				,"<%=cBox%>"
				, ""
				,isCheckboxChecked
				 ))
		
</script>
<%
		} // end if
                Iterator nh2Iter = nh2Tree.iterator();
	    	while (nh2Iter.hasNext()) {
			NHTree nh2t = (NHTree)nh2Iter.next();
			int nh2id = nh2t.getNeighborhood().getId();
			String nh2Name = nh2t.getName();
			System.out.println("Level 2 : " + nh2Name);
        		Vector nh3Tree = nh2t.getChildren();
			no = no + 1;
			nhNames.put( new Integer(nh2id), nhName+"/"+nh1Name+"/"+nh2Name);
			
        		if (!nh3Tree.isEmpty()) {
%>
<script>
				isCheckboxChecked = <%=checked%>
				aux2 = insFld(aux1,
					gFldCB(
					"<%=nh2Name%>"
					, ""
					,"selNH<%=nh2id%>"
					,"<%=nh2id%>"
					,"<%=cBox%>"
					, ""
					,isCheckboxChecked
				 ))
				 
</script>
<%
			}
			
			
			
			else
			{
%>
<script>
				isCheckboxChecked = <%=checked%>
				insDoc(aux1,
					gLnkCB("S",
                        		"<%=nh2Name%>"
					, ""
					,"selNH<%=nh2id%>"
					,"<%=nh2id%>"
					,"<%=cBox%>"
					, ""
					,isCheckboxChecked
				 	))
				

</script>
<%
			} // end if
			Iterator nh3Iter = nh3Tree.iterator();
			while (nh3Iter.hasNext()) {
				NHTree nh3t = (NHTree)nh3Iter.next();
				int nh3id = nh3t.getNeighborhood().getId();
				String nh3Name = nh3t.getName();
				no = no + 1;
				nhNames.put( new Integer(nh3id), nhName+"/"+nh1Name+"/"+nh2Name+"/"+nh3Name);
			
				
%>
<script>
				isCheckboxChecked = <%=checked%>
				insDoc(aux2,
					gLnkCB("S",
                        		"<%=nh3Name%>"
					, ""
					,"selNH<%=nh3id%>"
					,"<%=nh3id%>"
					,"<%=cBox%>"
					, ""
					,isCheckboxChecked
				 	))
			
</script>
<%
			} // end while nh3
	    	} // end while nh2
        } // END WHILE nh1
	// Print out the level 0 Neighborhoods
    } // end while NH 0
    

    
}//end if
%>
<script>
noNhds = <%=no%>
</script>
</head>


<!-- SECTION 4: Change the body tag to fit your site -->
<body bgcolor=white leftmargin=0 topmargin=0 marginheight="0" marginwidth="0" onResize="if (navigator.family == 'nn4') window.location.reload()">


<!-- SECTION 5: Replace all the HTML from here until the beginning of SECTION 6 with the pieces of the head section that are needed for your site  -->



		<table align=center cellpadding=0 cellspacing=0 border=0><tr><td >
		

		<table align=center cellpadding=0 cellspacing=0 border=0 >
		<tr>
			<td valign=top>
			<table cellpadding=4 cellspacing=0 border=0 width=100%><tr><td bgcolor=#ECECD9>
			    <img src=http://www.treeview.net/treemenu/layout/t.gif width=170 height=1><br>
				<table cellspacing=0 cellpadding=2 border=0 width=100%><tr><td bgcolor=white>

<!-- SECTION 6: Build the tree. -->

<!-- By making any changes to this code you are violating your user agreement.
     Corporate users or any others that want to remove the link should check
	 the online FAQ for instructions on how to obtain a version without the link -->
<!-- Removing this link will make the script stop from working -->

<table border=0><tr><td><font size=-2><a style="font-size:7pt;text-decoration:none;color:silver" href="http://www.treemenu.net/" target=_blank>JavaScript Tree Menu</a></font></td></tr></table>
<span class=TreeviewSpanArea>
<b>Choose a Neighborhood and click on a specific report</b>

<br>
<br>

<script>initializeDocument()</script>
<noscript>
A tree for site navigation will open here if you enable JavaScript in your browser.
</noscript>
</span>

<!-- SECTION 7: Continuation of the body of the page, after the tree. Replace whole section with
your site's HTML. -->

		<tr>
		 <td> <a href="javascript:getTableReportBySpecificNh('false')">Consolidate For this Neighborhood</a> |
		  <a href="javascript:getTableReportBySpecificNh('true')">Consolidate For Children of this Neighborhood</a> </td>
		</tr>
		</td></tr></table></table>
		</tr></table>
<%@include file='/jsp/common/commonparameters.jsp' %> </form>
</body>
</html>
