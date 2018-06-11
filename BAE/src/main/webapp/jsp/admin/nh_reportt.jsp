<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.table.*,com.boardwalk.neighborhood.*,java.io.*, java.util.*" %>

<%
Hashtable nhNames = new Hashtable();

Vector nhRel;
String head;
int cBox;
int Cnt = -1;
Integer  cBoxfromserver;
int nhId = -1;
String nhIdName="";


Vector nhTree = (Vector)request.getAttribute("nhTree");
String nhtitle = (String)request.getAttribute("nhtitle");
head = (String)request.getAttribute("head");
cBoxfromserver = (Integer)request.getAttribute("check");
nhIdName = (String)request.getAttribute("nhIdName");

try 
{
	Integer NHID = (Integer)request.getAttribute("nhId");
	if ( NHID != null )
	nhId = NHID.intValue();
	
//	System.out.println("NH ID"+nhId);
	
	nhRel = (Vector)request.getAttribute("nhRel");
}
catch (Exception e) {
	System.out.println("No Neighborhood Id");
	nhId = -1;
	nhRel = null;
}

if ( nhIdName != null &&! nhIdName.trim().equals("") )
{
	//request.setAttribute("heading", "Neighborhood :  "+nhIdName  );
}
else
{
	request.setAttribute("heading", "Welcome to Boardwalk: Please Join a Neighborhood to continue");
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

Hashtable relationshipToNeighborhoods = new Hashtable();

if ( nhRel != null )
{
	Iterator neighIter = nhRel.iterator();
	
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
}



%>

<html>
<form method="post" action="BW_Neighborhoods">
<head>
<input type="hidden" name="action" value="">
<input type="hidden" name="noRels" value="0">
<input type="hidden" name="nhId" value="<%=nhId%>">
<input type="hidden" name="selNhid" value="<%=nhId%>">
<input type="hidden" name="selectedRelation" value="-1">


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
		
		function newRelation(){
			document.forms[0].action.value = "newRelation";
			document.forms[0].submit();
			
			//window.open('','relEditor','status=no,toolbar=no,location=no,scrollbar=yes,resizable=yes,height=800,width=600,menubar=no', 'false');
			//var set_timeout1=setTimeout("document.forms[0].target='relEditor';",100);
			//var set_timeout=setTimeout("document.forms[0].submit();",200);
		}
		
		function editRelation(){
			document.forms[0].action.value = "editRelation";
			document.forms[0].submit();
			//window.open('','relEditor','status=no,toolbar=no,location=no,scrollbar=yes,resizable=yes,height=800,width=600,menubar=no', 'false');
			//var set_timeout1=setTimeout("document.forms[0].target='relEditor';",100);
			//var set_timeout=setTimeout("document.forms[0].submit();",200);
		}
		function deleteRelation(){
					document.forms[0].action.value = "deleteRelation";
					document.forms[0].submit();
		}
		
		function setforDetails(n){
			document.forms[0].selNhid.value = n;
			document.forms[0].noRels.value = noRels;
			document.forms[0].nhId.value = n;
			document.forms[0].action.value = "";
			document.forms[0].submit();

		}
		// Start ...................
		
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
			     if(doNxt == "deleteNH")
				 {
					if(MsgOkCancel() == true)
						validateNhid(obj.value)
					else
						return;
			    }
				else
					{
					document.forms[0].submit();
					return;
					}
			   }			   
			}
			if ( doNxt !='createNH'   &&  foundChecked == false )
			{
				alert( 'Please make a selection before making a request')
			}
			else
			{
				if ( doNxt !='deleteNH')
				{
				document.forms[0].selNhid.value = -1;
				document.forms[0].action.value = doNxt;
			     document.forms[0].submit();
				}
			
			}
      }

	function validateNhid(selNhid)
	{
		var url = "jsp/admin/getErrorsForNHCollabWB.jsp?nhid="+selNhid+"&typeNumber=1";
		xmlHttp = GetXmlHttpObject(stateChangedForNH);
		xmlHttp.open("GET", url , true) ;
		xmlHttp.send(null) ;
	}

	function stateChangedForNH() 
	{ 
		if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
		{ 
			callBackvalidateNhid(xmlHttp.responseText);
		} 
	} 

	function callBackvalidateNhid(asResponse)
	{

		var retvalues = asResponse.split("^");
		if(trim(retvalues[1]) == "delete") 
		{
			document.forms[0].submit();
			return;
		}
		else 
		{
			if(trim(retvalues[0]) == "nh")
			{
				alert("You do not have the Admin Access to Delete the Neighborhood")
				return;
			}
		}
	}
	
	function trim(str) 
	{ 
		str = str.replace(/^\s*/, '').replace(/\s*$/, ''); 
		return str;
	} 

	function GetXmlHttpObject(handler)
	{ 
		var objXmlHttp=null;
		if (navigator.userAgent.indexOf("Opera")>=0)
		{
			alert("This example doesn't work in Opera") ;
			return ;
		}
		if (navigator.userAgent.indexOf("MSIE")>=0)
		{ 
			var strName="Msxml2.XMLHTTP";
			if (navigator.appVersion.indexOf("MSIE 5.5")>=0)
			{
				strName="Microsoft.XMLHTTP";
			} 
			try
			{ 
				objXmlHttp=new ActiveXObject(strName);
				objXmlHttp.onreadystatechange=handler ;
				return objXmlHttp;
			} 
			catch(e)
			{ 
				alert("Error. Scripting for ActiveX might be disabled") 
				return 
			} 
		} 
		if (navigator.userAgent.indexOf("Mozilla")>=0)
		{
			objXmlHttp=new XMLHttpRequest();
			objXmlHttp.onload=handler;
			objXmlHttp.onerror=handler ;
			return objXmlHttp;
		}
	}


// End.............................
               
               function checkNHSelect(doNxt) {
                       var cBoxCount = 0;
                       for (i=0;;i++) {
			  var obj = document.forms[0].elements[i];
			  if (obj == null) {
				alert("No More Objs");
				break;
                          }
                          alert("Obj = " + obj.name);
                      }
               }


function checkCheckBoxsetActionAndSubmit(doNext)
{
    if ( currentCheckboxCount == 1 )
    {
    	document.forms[0].action.value = doNext
    	document.forms[0].submit()
    }
    else
    {
    	alert("Please make a selection before executing this function")

    }

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
    foldersTree = gFld("<b><%=head%></b>", "javascript:undefined()")
</script>
<%
    // iterate over the neighborhood tree
    Iterator nhIter = nhTree.iterator();
    while (nhIter.hasNext()) {
	NHTree nht = (NHTree)nhIter.next();
	int nhid = nht.getNeighborhood().getId();
	String nhName = nht.getName();
//	System.out.println("Level 0 : " + nhName);
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
	//	System.out.println("Level 1 : " + nh1Name);
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
	//		System.out.println("Level 2 : " + nh2Name);
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



		<table cellpadding=0 cellspacing=0 border=0 width=772><tr><td width=772>
		

		<table cellpadding=0 cellspacing=0 border=0 width=772>

		<tr>
			<td width=178 valign=top>
			<table cellpadding=4 cellspacing=0 border=0 width=100%><tr><td bgcolor=#ECECD9>
			    <br> 
				<table cellspacing=0 cellpadding=2 border=0 width=100%>
				   
    					<tr>
    				        <td class="Command" >
    				       
    				        <a href="javascript:setNHSubmit('createNH')" > Create </a>|
    				        <a href="javascript:setNHSubmit('deleteNH')" > Delete </a>|  
    				        <a href="javascript:setNHSubmit('membersNH')" > Members</a>     
    				        
    				        
    				        </td>
				    
				    </tr>
				
				<tr><td bgcolor=white>



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
		<td bgcolor=white valign=top>
		<div class=body>
		<b>
		<%
		
		if ( nhIdName != null )
		{
		%>
			<%=nhIdName %>
	<%
		}
		
		%>
	
		</b>
		</div>
		<table  cellpadding=0 cellspacing=0 border=0 width=100%>
		    <tr valign="top" bgcolor="#FFCC66"> 
		      <td height="2" width="100%"><img src="images/clear.gif" width="100%" height="2"></td>
                    </tr> 
                    
			<tr valign="top"> 
			<td class="body" align=center> 
			    <br><br>

				<% if ( nhRel != null ) { %>
				
				<table border="1" cellspacing="0" cellpadding="2" >
				
				<tr>
				<th width=1%></th>
				<th class="BWTableHead" >Relationship</th>
				<th class="BWTableHead" >Neighborhoods</th>
				</tr>
				
				
				
				
				
				
				
		<%
		 		no = 0;   
				Vector fixedRelations = new Vector();
				fixedRelations.add("PRIVATE");
				fixedRelations.add("PARENT");
				fixedRelations.add("DOMAIN");
				fixedRelations.add("PEER");
				fixedRelations.add("CHILDREN");
				String fixedRelation = "";
				String displayRelationshipName ="";
				
				for ( int f = 0; f < fixedRelations.size(); f++ )
				{
					
					 fixedRelation = (String)fixedRelations.elementAt(f);
					 
					  displayRelationshipName = fixedRelation;
					
					if (fixedRelation.equals("PRIVATE") )
					{
						displayRelationshipName = "Your Team";
					}
					if (fixedRelation.equals("PARENT") )
					{
					displayRelationshipName = "Managing Department";
					}
					if (fixedRelation.equals("DOMAIN") )
					{
						displayRelationshipName = "Company";
					}
					if (fixedRelation.equals("PEER") )
					{
						displayRelationshipName = "Peer Depts";
					}
					if (fixedRelation.equals("CHILDREN") )
					{
						displayRelationshipName = "Your Teams";
					}
					             
					
					
					
					Vector targetNhidsForFixedRelationship = (Vector)relationshipToNeighborhoods.get( fixedRelation );
					if ( targetNhidsForFixedRelationship != null && targetNhidsForFixedRelationship.size() > 0 )
					{
						no = no + 1;
					
		%>
					<tr>
					<td width=1%> </td>				        
				        <td name="<%=no%>"><%=displayRelationshipName%></td>
					
					<td>										
						<select class="InputBox"  >

						<%
							
							for ( int fn = 0; fn <  targetNhidsForFixedRelationship.size() ; fn++ )
							{
								int fixedRel_targetNhId = ((Integer) targetNhidsForFixedRelationship.elementAt(fn)).intValue();
						
						%>
								 	<option name="nhid<%=no%>"  value="<%=fixedRel_targetNhId%>"> <%=(String)nhNames.get(new Integer(fixedRel_targetNhId))%> </option>

						<%
						
							}							
						%>

						</select>									        
				        </td>
				        </tr>
				<%      
				       } 
				       
				       relationshipToNeighborhoods.remove(fixedRelation);
				}
				
				
				
		
				Enumeration  nhRelations = relationshipToNeighborhoods.keys();
		    		
		    		while (nhRelations.hasMoreElements()) 
		    		{
					String reln = (String)nhRelations.nextElement();
					no = no + 1;
		%>
		                    <tr>
		 		                    	<td width=1%>
		                    	<input onclick="document.forms[0].selectedRelation.value=this.name" type="checkbox" name="<%=reln%>" >
					</td>
					<td name="<%=no%>">
					
					<%=reln%>
					</td>
					
				
					<td>
					
					<select class="InputBox"  >
					
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
				<tr     bgcolor=white> 
				<td  colspan="10" class="body"  > 
				 <b>
			             <a href="javascript:newRelation()">New Relation</a> |
			            <a href="javascript:editRelation()">Edit Relation</a> | 
			            <a href="javascript:deleteRelation()">Delete Relation</a> 
			            </td>
			           </tr>
			           
			       

				</table>
				<td width="2" valign="top" bgcolor="#FFCC66"><img src="images/clear.gif" width="2" height="2"></td>				
				<%}%>
				</table>

				</td></tr></table><%@include file='/jsp/common/commonparameters.jsp' %> </form>

</body>
<%@include file='/jsp/common/footer.jsp' %>
</html>
