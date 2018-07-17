<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.neighborhood.*,java.io.*, java.util.*,com.boardwalk.collaboration.*" %>

<%

Hashtable nhNames = new Hashtable();

String head;
int cBox;
int Cnt = -1;
Integer  cBoxfromserver;
int nhId = -1;



Vector nhTree = (Vector)request.getAttribute("nhTree");
String nhtitle = (String)request.getAttribute("nhtitle");
head = (String)request.getAttribute("head");
cBoxfromserver = (Integer)request.getAttribute("check");
String nhIdName = (String)request.getAttribute("nhIdName");
System.out.println("nhIdName" + nhIdName );




try 
{
	Integer NHID = (Integer)request.getAttribute("nhId");	
	if ( NHID != null )
	nhId = NHID.intValue();
	
	System.out.println("NH ID"+nhId);	
	
}
catch (Exception e) {
	System.out.println("No Neighborhood Id");
	nhId = -1;
	
}

/*

if ( nhIdName != null &&! nhIdName.trim().equals("") )
{
	request.setAttribute("heading", nhIdName+ " Collaborations " );
}
else
{
	request.setAttribute("heading", "Welcome to Boardwalk");
}

*/


if (head ==  null)
	head = "Available Neighborhoods";


cBox = 0;

if (nhtitle == null)
	nhtitle = "Select Neighborhoods";

%>

<html>
<form method="post" action="MyCollaborations">
<head>
<input type="hidden" name="nhId" value="<%=nhId%>">
<input id="selNhid" type="hidden" name="selNhid" value="<%=nhId%>">

<%
System.out.println( "Selected Nh Id ============" + nhId ); 
%>

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
                var noNhds;
		
				
		var noRels = -1;
		function getnhIndex(id) {
			for (i=0;i<=nhCnt;i++) {
				if (id == nhids[i])
					return(i);
			}
			return(1);
		}
		
		
		function setforDetails(n){
			document.forms[0].selNhid.value = n;
			document.forms[0].nhId.value = n;
			document.forms[0].action.value = "collaborationTree";
			document.forms[0].submit();
		}
		
		
		function setNHSubmit(doNxt) 
		{
		      var foundChecked = false;
		    
		       
                       for (i=1;i<=noNhds;i++) 
                       {
			  var obj = eval("document.forms[0].selNH"+i);
			  
			   
                           if (obj != null && obj.checked ) 
			   {
			   	 foundChecked = true;			  
				document.forms[0].selNhid.value = obj.value;
				document.forms[0].action.value = doNxt;
			        document.forms[0].submit();
			        return;
			   }			   
			}
			if (  foundChecked == false )
			{
				alert( 'Please make a selection before making a request')
			}
			else
			{
				
				document.forms[0].selNhid.value = -1;
				document.forms[0].action.value = doNxt;
			       document.forms[0].submit();
			}
               }
               
  



	
	       
		
		var prevTD, prevTDValue, click;
		click = false;
		
		function changeRelName(currTD)
		{
			// change value for previous cell
			//alert ("click = " + click)
			//alert ("currTD.name = " + currTD.name)
			//alert ("currTD.innerText = " + currTD.innerText)
			if (click == true)
			{ // not the first click, set previous cell
				prevHdnCell = "rel" + prevTD.name
				oldValue = prevTDValue				
				var newValue = prevTD.innerText
				
				
				if ( oldValue != newValue )
				{
					document.all.item(prevHdnCell).value = newValue				
					prevTD.style.color = "red"
				}
				
			}
			
			if (currTD != null) {
				prevTD =  currTD
				prevTDValue = currTD.innerText
			}		
		
			click = true;
			
			return
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
STARTALLOPEN = 0
USEFRAMES = 0
USEICONS = 0
WRAPTEXT = 0
ICONPATH = "images/"
PERSERVESTATE = 1
</script>

<br>
<br>


<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>
<%
    int no = 0;
    if ((nhTree!= null) && (!nhTree.isEmpty())) 
    {
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
  		aux = insFld(foldersTree,
				gFldCB(
				"<%=nhName%>"
				, "javascript:setforDetails(<%=nhid%>)"
				,"selNH<%=no%>"
				,"<%=nhid%>"
				,"<%=cBox%>",''
				 ))
</script>
<%
	}
	else
	{
%>
<script>
		insDoc(foldersTree,
			gLnkCB("S",
                        	"<%=nhName%>"
				, "javascript:setforDetails(<%=nhid%>)"
				,"selNH<%=no%>"
				,"<%=nhid%>"
				,"<%=cBox%>", ''
				
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
		aux1 = insFld(aux,
				gFldCB(
				"<%=nh1Name%>"
				, "javascript:setforDetails(<%=nh1id%>)"
				,"selNH<%=no%>"
				,"<%=nh1id%>"
				,"<%=cBox%>", ''
				 ))
</script>
<%
		}
		else
		{
%>
<script>
			insDoc(aux,
				gLnkCB("S",
                        	"<%=nh1Name%>"
				, "javascript:setforDetails(<%=nh1id%>)"
				,"selNH<%=no%>"
				,"<%=nh1id%>"
				,"<%=cBox%>", ''
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
				aux2 = insFld(aux1,
					gFldCB(
					"<%=nh2Name%>"
					, "javascript:setforDetails(<%=nh2id%>)"
					,"selNH<%=no%>"
					,"<%=nh2id%>"
					,"<%=cBox%>", ''
				 ))
</script>
<%
			}
			else
			{
%>
<script>
				insDoc(aux1,
					gLnkCB("S",
                        		"<%=nh2Name%>"
					, "javascript:setforDetails(<%=nh2id%>)"
					,"selNH<%=no%>"
					,"<%=nh2id%>"
					,"<%=cBox%>", ''
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

				insDoc(aux2,
					gLnkCB("S",
                        		"<%=nh3Name%>"
					, "javascript:setforDetails(<%=nh3id%>)"
					,"selNH<%=no%>"
					,"<%=nh3id%>"
					//"javascript:checkBoxCheck(this)"
					,"<%=cBox%>"	, ''
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




		<table cellpadding=0 cellspacing=0  ><tr><td >
		

		<table cellpadding=0 cellspacing=0 border=0 >
		<tr>
			<td  valign=top>
			<table cellpadding=4 cellspacing=0 border=0 width=100%>
			
			<tr><td bgcolor=#ECECD9>
			
			
			
			    <br> 
				<table align="left"   valign="top"  cellspacing=0 cellpadding=2 border=0 width=100%>				   
    					
				<tr ><td bgcolor=white>





<!-- SECTION 6: Build the tree. -->

<!-- By making any changes to this code you are violating your user agreement.
     Corporate users or any others that want to remove the link should check
	 the online FAQ for instructions on how to obtain a version without the link -->
<!-- Removing this link will make the script stop from working -->
<table border=0><tr><td><font size=-2><a style="font-size:7pt;text-decoration:none;color:silver" href="http://www.treemenu.net/" target=_blank>JavaScript Tree Menu</a></font></td></tr></table>
<span class=TreeviewSpanArea>
<br>





 <%   
if ((nhTree!= null) && (!nhTree.isEmpty())) 
{
%>
<script>initializeDocument()</script>
<%}%>


<noscript>
A tree for site navigation will open here if you enable JavaScript in your browser.
</noscript>
</span>




<!-- SECTION 7: Continuation of the body of the page, after the tree. Replace whole section with
your site's HTML. -->

		</td></tr></table></table>
		</td>


		
		<td align="left"  valign="top"  >

		<table  align="left"  valign="top" cellpadding=0 cellspacing=0 border=0>
		
		<tr align="left"  >
		<td>
		<%

			if ( nhIdName != null &&! nhIdName.trim().equals("") )
			{
			%>
				<b> Collaborations in <%=nhIdName%>  </b>
			<%
			}

		%>
		</td>
                </tr>
		
		<tr align="left"   valign="top" bgcolor="#FFCC66"> 
		<td bgcolor="#FFCC66" height="2" ><img src="images/clear.gif" height="2"></td>
	   	</tr>
	   	
		<%@ include file="/jsp/collaboration/includecollablist.jsp" %>


		</table>
		
			</td>
	       <%@include file='/jsp/common/commonparameters.jsp' %> </form>						
				     
      <%@include file='/jsp/common/footer.jsp' %>	
 

