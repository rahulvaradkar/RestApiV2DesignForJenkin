<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.neighborhood.*,java.io.*, java.util.*" %>

<%
 Hashtable nhNames = new Hashtable();
Vector nhTree = (Vector)request.getAttribute("nhTree");
Vector nhRel = (Vector)request.getAttribute("nhRel");
String head;
int cBox;
// select lookup_table.js
int nhId;
int Cnt = -1;
String nhtitle = (String)request.getAttribute("nhtitle");
if (nhtitle == null)
	nhtitle = "Select Neighborhoods";
head = (String)request.getAttribute("head");
String parentName = (String)request.getAttribute("parentName");
if (head ==  null)
	head = "Available Neighborhoods";
try {
	cBox = ((Integer)(request.getAttribute("check"))).intValue();
}
catch (Exception e) {
	System.out.println("No check box");
	cBox = 0;
}

try {
	nhId = ((Integer)(request.getAttribute("nhId"))).intValue();
	System.out.println("NH ID"+nhId);
	nhRel = (Vector)request.getAttribute("nhRel");
}
catch (Exception e) {
	System.out.println("No Neighborhood Id");
	nhId = -1;
}

Iterator neighIter = nhRel.iterator();
Hashtable relationshipToNeighborhoods = new Hashtable();
while (neighIter.hasNext()) 
{
	NeighborhoodRelation nhr = (NeighborhoodRelation)neighIter.next();
	int trg_nhid = nhr.getTargetNhId();
	String reln = nhr.getRelation();
	Vector neighborhoods =(Vector) relationshipToNeighborhoods.get(reln);
	if ( neighborhoods == null )
	{
		neighborhoods = new Vector();
		relationshipToNeighborhoods.put(reln,neighborhoods);
	}
	neighborhoods.add(new Integer(trg_nhid));
}
	
	
request.setAttribute("heading", "Add/Delete Neighborhood Relations for "+nhId);
%>
<html>
<form>
<head>
<input type="hidden" name="action" value="">
<input type="hidden" name="noRels" value="0">
<input type="hidden" name="nhId" value="<%=nhId%>">

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
		//This script is not related with the tree itself, just used for my example
		function getQueryString(index)
		{
			var paramExpressions;
			var param
			var val
			paramExpressions = window.location.search.substr(1).split("&");
			if (index < paramExpressions.length)
			{
				param = paramExpressions[index];
				if (param.length > 0) {
					return eval(unescape(param));
				}
			}
			return ""
		}
                var noRels = -1;
		function getnhIndex(id) {
			for (i=0;i<=nhCnt;i++) {
				if (id == nhids[i])
					return(i);
			}
			return(1);
		}
		function setforRelation(id){
			noRels = noRels + 1;
			document.forms[0].nhood1.value=fullnhNames[noRels];
			var s = eval("document.forms[0].nhood"+noRels);
			var idx = getnhIndex(id);
			s.value = fullnhNames[idx];
		}
		function setNHood(i,id){
			var s = eval("document.forms[0].nhood"+i);
			var idx = getnhIndex(id);
			s.value = fullnhNames[idx];
		}

                function setforDetails(n){
			document.forms[0].action.value = "NHDetails";
			document.forms[0].noRels.value = noRels;
			document.forms[0].nhId.value = n;
    			document.forms[0].submit();
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
<%@ include file="ua.js" %>
</script>
<script>
<%@ include file="ftiens4.js" %>
</script>
<script>
USETEXTLINKS =
STARTALLOPEN = 0
USEFRAMES = 0
USEICONS = 0
WRAPTEXT = 0
ICONPATH = "images/"
PERSERVESTATE = 1
var fullnhNames = new Array();
var nhids = new Array();
var nhCnt = -1;
</script>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>



<title><%=nhtitle%></title>
<% if ((nhTree!= null) && (!nhTree.isEmpty())) {
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
	System.out.println("adding nh id " + nhid +" nhname " + nhName );
	nhNames.put( new Integer(nhid), nhName);
%>
<script>
	nhCnt = nhCnt + 1;
	fullnhNames[nhCnt] = "<%=nhName%>";
        nhids[nhCnt] = <%=nhid%>;
</script>
<%
	Cnt = Cnt + 1;
	if (!nh1Tree.isEmpty()) {
%>
<script>
		aux = insFld(foldersTree,
				gFldCB(
				"<%=nhName%>"
				, "javascript:setforDetails(<%=nhid%>)"
				,"<%=nhid%>"
				,"<%=cBox%>"
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
				, "javascript:setforRelation(<%=nhid%>)"
				,"<%=nhid%>"
				,"<%=cBox%>"
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
        	nhNames.put( new Integer(nh1id), nhName+"/"+nh1Name);
      %>
<script>
		nhCnt = nhCnt + 1;
		fullnhNames[nhCnt] = "<%=nhName%>/<%=nh1Name%>";
        	nhids[nhCnt] = <%=nh1id%>;
</script>
<%
        	Cnt = Cnt + 1;
		if (!nh2Tree.isEmpty()) {
%>
<script>
		aux1 = insFld(aux,
				gFldCB(
				"<%=nh1Name%>"
				, "javascript:setforRelation(<%=nh1id%>)"
				,"<%=nh1id%>"
				,"<%=cBox%>"
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
				, "javascript:setforRelation(<%=nh1id%>)"
				,"<%=nh1id%>"
				,"<%=cBox%>"
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
        		nhNames.put( new Integer(nh2id), nhName+"/"+nh1Name+"/"+nh2Name);
%>
<script>
	nhCnt = nhCnt + 1;
	fullnhNames[nhCnt] = "<%=nhName%>/<%=nh1Name%>/<%=nh2Name%>";
        nhids[nhCnt] = <%=nh2id%>;
</script>
<%
			Cnt = Cnt + 1;
        		if (!nh3Tree.isEmpty()) {
%>
<script>
				aux2 = insFld(aux1,
					gFldCB(
					"<%=nh2Name%>"
					, "javascript:setforRelation(<%=nh2id%>)"
					,"<%=nh2id%>"
					,"<%=cBox%>"
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
					, "javascript:setforRelation(<%=nh2id%>)"
					,"<%=nh2id%>"
					,"<%=cBox%>"
				 	))

</script>
<%
			} // end if
			Iterator nh3Iter = nh3Tree.iterator();
			while (nh3Iter.hasNext()) {
				NHTree nh3t = (NHTree)nh3Iter.next();
				int nh3id = nh3t.getNeighborhood().getId();
				String nh3Name = nh3t.getName();
				Cnt = Cnt + 1;
				nhNames.put( new Integer(nh3id), nhName+"/"+nh1Name+"/"+nh2Name+"/"+nh3Name);
%>
<script>
				nhCnt = nhCnt + 1;
				fullnhNames[nhCnt] = "<%=nhName%>/<%=nh1Name%>/<%=nh2Name%>/<%=nh3Name%>";
				nhids[nhCnt] = <%=nh3id%>;
				insDoc(aux1,
					gLnkCB("S",
                        		"<%=nh3Name%>"
					, "javascript:setforRelation(<%=nh3id%>)"
					,"<%=nh3id%>"
					,"<%=cBox%>"
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

</head>


<!-- SECTION 4: Change the body tag to fit your site -->
<body bgcolor=white leftmargin=0 topmargin=0 marginheight="0" marginwidth="0" onResize="if (navigator.family == 'nn4') window.location.reload()">


<!-- SECTION 5: Replace all the HTML from here until the beginning of SECTION 6 with the pieces of the head section that are needed for your site  -->



		<table cellpadding=0 cellspacing=0 border=0 width=772><tr><td width=772>


		<table cellpadding=0 cellspacing=0 border=0 width=772>
		<tr>
			<td width=178 valign=top>
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
<script>initializeDocument()</script>
<noscript>
A tree for site navigation will open here if you enable JavaScript in your browser.
</noscript>
</span>

<!-- SECTION 7: Continuation of the body of the page, after the tree. Replace whole section with
your site's HTML. -->

		</td></tr></table></table>
		</td>
		<td bgcolor=white valign=top>

		<table  cellpadding=10 cellspacing=0 border=0 width=100%><tr><td>

		<h4>Friends</h4>
		<table border = 1 col=3>
		
		<tr>
		<th class="BWTableHead" >Relationship</th>
		<th class="BWTableHead" >Neighborhoods</th>
		</tr>
<%
		Enumeration nhNameEnum = nhNames.keys();
		while( nhNameEnum.hasMoreElements() )
		{
			Integer nhIdKey = (Integer)nhNameEnum.nextElement();
			System.out.println(" nhId = " + nhIdKey.intValue() + " nhname = " + (String)nhNames.get(nhIdKey)       );
		
		}
		



		Enumeration  nhRelations = relationshipToNeighborhoods.keys();
                int no = 0;   		
    		
    		
    		
    		
    		
    		while (nhRelations.hasMoreElements()) 
    		{
			String reln = (String)nhRelations.nextElement();
			no = no + 1;
%>
                    <tr>
			<td name="<%=no%>">
			
			<input type="hidden" name="rel<%=no%>" value="<%=reln%>">
			
			<SPAN CONTENTEDITABLE   onfocus="changeRelName(this.parentElement)"  STYLE="height: 100%; width: 100%;">
			<%=reln%></SPAN>

			</td>
			
			<td>
			
			<select class="LookupList"   STYLE="height: 100%; width: 100%;" >
			
			<%
				Vector targetNhids = (Vector)relationshipToNeighborhoods.get( reln );
				if  ( targetNhids.size() > 0 )
				{

						for ( int n = 0; n <  targetNhids.size() ; n++ )
						{
							int targetNhId = ((Integer) targetNhids.elementAt(n)).intValue();
			%>
					 <option name="nhid<%=no%>"  value="<%=targetNhId%>"> <%=(String)nhNames.get(new Integer(targetNhId))%> </option>
			
			<%
						}
					}
			%>
		        
		        </select>
		        
		        </td>
			
                        
			
		    </tr>
<script>
			noRels = noRels + 1;
			
</script>
<%
    		}

		while (no <= Cnt) {
                   no = no + 1;
%>
                     <tr>
			<td>
			<input name="rel<%=no%>" value="">
			</td>
			<td>
			<input name="nhood<%=no%>" value="">
		        </td>
			<input  type="hidden" name="nhid<%=no%>" value="">
			
		    </tr>
<%

		}
%>

		</table>
		<tr>

		 <input onClick="javascript:setforDelete()" type="button" name="Delete" value="Delete Relation(s)">
		<tr>
                <input onClick="javascript:setActionAndSubmit('commitRelations')" type="button" name="Commit" value="Commit Changes">
                <input onClick="javascript:setforDetails(<%=nhId%>)" type="button" name="Cancel" value="Cancel Changes">
		</td></tr></table></td></tr></table>

</body>
<%@include file='/jsp/common/commonparameters.jsp' %> </form>
</html>
