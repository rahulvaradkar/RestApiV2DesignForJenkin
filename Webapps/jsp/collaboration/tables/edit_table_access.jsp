<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.table.*,java.io.*, java.util.*,com.boardwalk.neighborhood.*" %>

<%
	// get the wbid parameter

	int tableId = ((Integer)request.getAttribute("tableId")).intValue();
	String tableName = (String)request.getAttribute("tableName");

	Hashtable relationships =(Hashtable) request.getAttribute("relationships");
	System.out.println("relationships size " + relationships.size() );


	Vector PrivateRel   = (Vector)relationships.get("PRIVATE");
        NeighborhoodId privateNhId = (NeighborhoodId)PrivateRel.elementAt(0);


	request.setAttribute("heading", " Access Control for " +tableName +  " from the perspective of Neighborhood " + privateNhId.getName());


	Hashtable accessLists =(Hashtable) request.getAttribute("accessControlLists");
	System.out.println("accessControlLists size " + accessLists.size() );
	String ViewPreference = (String)request.getAttribute("ViewPreference");

	System.out.println("ViewPreference" + ViewPreference );


%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<br>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>



<script LANGUAGE=JAVASCRIPT TYPE="TEXT/JAVASCRIPT">

function checkAccessSettings(relationship, actionname)
{

	var canAdministerTable = document.all.item(relationship+'_canAdministerTable').checked
	var canAdministerColumn = document.all.item(relationship+'_canAdministerColumn').checked
	var canAddRow = document.all.item(relationship+'_canAddRow').checked
	var canDeleteRow = document.all.item(relationship+'_canDeleteRow').checked
	var canReadWriteOnMyLatestView = document.all.item(relationship+'_canReadWriteOnMyLatestView').checked
	var canReadLatestViewOfAll = document.all.item(relationship+'_canReadLatestViewOfAll').checked
	var canReadLatestViewOfAllChildren = document.all.item(relationship+'_canReadLatestViewOfAllChildren').checked
	var canReadLatestOfTable = document.all.item(relationship+'_canReadLatestOfTable').checked
	var canWriteLatestOfTable = document.all.item(relationship+'_canWriteLatestOfTable').checked
	var canReadWriteLatestOfMyRows = document.all.item(relationship+'_canReadWriteLatestOfMyRows').checked
	var canReadLatestofMyGroup = document.all.item(relationship+'_canReadLatestofMyGroup').checked
	var canReadWriteLatestofMyGroup = document.all.item(relationship+'_canReadWriteLatestofMyGroup').checked
	var canReadLatestofMyGroupAndImmediateChildren = document.all.item(relationship+'_canReadLatestofMyGroupAndImmediateChildren').checked
	var canReadWriteLatestofMyGroupAndImmediateChildren = document.all.item(relationship+'_canReadWriteLatestofMyGroupAndImmediateChildren').checked
	var canReadLatestofMyGroupAndAllChildren = document.all.item(relationship+'_canReadLatestofMyGroupAndAllChildren').checked
	var canReadWriteLatestofMyGroupAndAllChildren = document.all.item(relationship+'_canReadWriteLatestofMyGroupAndAllChildren').checked



	if ( document.all.item(relationship+'_'+actionname).checked == true )
	{
		document.all.item(relationship+'_ACL_'+ actionname).value = "on"
	}
	else
	{
		document.all.item(relationship+'_ACL_'+ actionname).value = "off"
	}


	if ( canReadLatestViewOfAll == true)
	{
		document.all.item(relationship+'_canReadLatestViewOfAllChildren').checked = true
		document.all.item(relationship+'_ACL_canReadLatestViewOfAllChildren').value = "on"

	}
	if ( canWriteLatestOfTable == true)
	{

		document.all.item(relationship+'_canReadLatestOfTable').checked = true
		document.all.item(relationship+'_ACL_canReadLatestOfTable').value = "on"
	}

	if ( canReadWriteLatestofMyGroup == true)
	{
		document.all.item(relationship+'_canReadLatestofMyGroup').checked = true
		document.all.item(relationship+'_ACL_canReadLatestofMyGroup').value = "on"

		document.all.item(relationship+'_canReadWriteLatestOfMyRows').checked = true
		document.all.item(relationship+'_ACL_canReadWriteLatestOfMyRows').value = "on"

	}


	if ( canReadWriteLatestofMyGroupAndImmediateChildren == true)
	{
			document.all.item(relationship+'_canReadLatestofMyGroupAndImmediateChildren').checked = true
			document.all.item(relationship+'_ACL_canReadLatestofMyGroupAndImmediateChildren').value = "on"


			document.all.item(relationship+'_canReadLatestofMyGroup').checked = true
			document.all.item(relationship+'_ACL_canReadLatestofMyGroup').value = "on"


			document.all.item(relationship+'_canReadWriteLatestofMyGroup').checked = true
			document.all.item(relationship+'_ACL_canReadWriteLatestofMyGroup').value = "on"

			document.all.item(relationship+'_canReadWriteLatestOfMyRows').checked = true
			document.all.item(relationship+'_ACL_canReadWriteLatestOfMyRows').value = "on"

	}


	if ( canReadLatestofMyGroupAndImmediateChildren == true)
		{

				document.all.item(relationship+'_canReadLatestofMyGroup').checked = true
				document.all.item(relationship+'_ACL_canReadLatestofMyGroup').value = "on"

	}

	if ( canReadWriteLatestofMyGroupAndAllChildren == true)
	{
			document.all.item(relationship+'_canReadLatestofMyGroupAndAllChildren').checked = true
			document.all.item(relationship+'_ACL_canReadLatestofMyGroupAndAllChildren').value = "on"


			document.all.item(relationship+'_canReadLatestofMyGroupAndImmediateChildren').checked = true
			document.all.item(relationship+'_ACL_canReadLatestofMyGroupAndImmediateChildren').value = "on"

			document.all.item(relationship+'_canReadWriteLatestofMyGroupAndImmediateChildren').checked = true
			document.all.item(relationship+'_ACL_canReadWriteLatestofMyGroupAndImmediateChildren').value = "on"


			document.all.item(relationship+'_canReadLatestofMyGroup').checked = true
			document.all.item(relationship+'_ACL_canReadLatestofMyGroup').value = "on"


			document.all.item(relationship+'_canReadWriteLatestofMyGroup').checked = true
			document.all.item(relationship+'_ACL_canReadWriteLatestofMyGroup').value = "on"

			document.all.item(relationship+'_canReadWriteLatestOfMyRows').checked = true
			document.all.item(relationship+'_ACL_canReadWriteLatestOfMyRows').value = "on"

	}

	if ( canReadLatestofMyGroupAndAllChildren == true)
		{

				document.all.item(relationship+'_canReadLatestofMyGroupAndImmediateChildren').checked = true
				document.all.item(relationship+'_ACL_canReadLatestofMyGroupAndImmediateChildren').value = "on"

				document.all.item(relationship+'_canReadLatestofMyGroup').checked = true
				document.all.item(relationship+'_ACL_canReadLatestofMyGroup').value = "on"


	}









}



</script>





<tr>
    <td>

        <!--start main page table-->
        <table border="0" cellspacing="0" cellpadding="0" align="center" class="body">

          <tr>
            <td valign="top">
                <!--start main page table 2 -->

		<table border="0" cellspacing="0" cellpadding="0" align="center" valign="top">
                    <tr valign="top" bgcolor="#FFCC66">
                      <td height="2" ><img src="images/clear.gif"  height="2"></td>
                    </tr>

                    <tr valign="top">
                        <td class="body" align=center>
                            <br>

                            <br>
                  <table border="0" cellspacing="2" cellpadding="2" align="center">
       <form method="post" action="MyTables?tableid=<%=tableId%>">
          <tr valign="top">
            <td>
              <table class="body" border="0" cellspacing="2" cellpadding="2">
                <tr bgcolor="#fddeb9">
                  <td class="body"  colspan="17"><b>Table Level Access Control Settings</b></td>
                </tr>
                </table>
              <b>
              </b></td>
          </tr>



          <tr bgcolor="#eeeeee" valign="top">
            <td>
              <table class="AccessTable"  border="1" cellspacing="1" cellpadding="2" align="center" valign="top" >

                <tr border="1"  >
		                  <th colspan=2 class="BWTableWhiteHead"><b>Users</b></th>
		                  <th colspan=4 class="BWTableGreyHead"><b>Administrative</b></th>
		                  <th colspan=3 class="BWTableWhiteHead"><b>Table Granularity  </b></th>
		                  <th colspan=9 class="BWTableGreyHead"><b>Row Granularity</b></th>



		</tr>

		 <tr border="1"  >
				  <th colspan=2 class="BWTableWhiteHead"><b></b></th>
				  <th colspan=2 class="BWTableGreyHead"><b>Admin</b></th>
				  <th colspan=2 class="BWTableGreyHead"><b>Row</b></th>

				  <th colspan=1 class="BWTableWhiteHead"><b>User </b></th>
				  <th colspan=1 class="BWTableWhiteHead"><b>All</b></th>
				  <th colspan=1 class="BWTableWhiteHead"><b>Children  </b></th>
				  <th colspan=2 class="BWTableGreyHead"><b>All</b></th>
				  <th colspan=1 class="BWTableGreyHead"><b>User</b></th>
				  <th colspan=2 class="BWTableGreyHead"><b>Group</b></th>
				  <th colspan=2 class="BWTableGreyHead"><b>Group and Children</b></th>
				  <th colspan=2 class="BWTableGreyHead"><b>Group and All Children </b></th>
		</tr>

                <tr>
                  <th    align="center" width=15 class="BWTableWhiteHead"><b>Relationship</b></th>
                  <th    align="center" width=15 class="BWTableWhiteHead" ><b>Neighborhood</b></th>
                  <th    align="center" width=5 class="BWTableGreyHead" ><b><img alt="This allows the user to change access and delete the table"  src="images/clear.gif" height="2">Table</b></th>
                  <th     align="center" width=5 class="BWTableGreyHead" alt="This allows the user to add and delete columns to the table" ><b>Column</b></th>
                  <th    align="center" width=5 class="BWTableGreyHead"  alt="This allows the user to add and delete rows to the table"  ><b>Add</b></th>
                  <th    align="center" width=5 class="BWTableGreyHead"  alt="This allows the user to add and delete rows to the table"  ><b>Delete</b></th>                  
                  <th     align="center" width=5 class="BWTableWhiteHead"  alt="This allows the user to read and write to users latest view of the table" ><b>R/W</b></th>
                  <th     align="center" width=5 class="BWTableWhiteHead"  alt="This allows the user to read the entries of all users" ><b>R</b></th>
                  <th    align="center" width=5 class="BWTableWhiteHead"  alt="This allows the user to read the entries of all children" ><b>R</b></th>
                  <th    align="center" width=5 class="BWTableGreyHead"  alt="This allows the user to read latest view of the table" ><b>R</b></th>
                  <th    align="center" width=5 class="BWTableGreyHead"  alt="This allows the user to read and write to latest view of the table" ><b>R/W</b></th>
                  <th    align="center" width=5 class="BWTableGreyHead"  alt="This allows the user to read and write only the rows created by User" ><b>R/W</b></th>


                    <th    align="center" width=5 class="BWTableGreyHead"    alt="This allows the user to read  rows created by users group" ><b>R</b></th>
		    <th    align="center" width=5 class="BWTableGreyHead"  alt="This allows the user to read and write rows created by users group" ><b>R/W</b></th>
		    <th    align="center" width=5 class="BWTableGreyHead"  alt="This allows the user to read  rows created by users group and immediate children" ><b>R</b></th>
		    <th    align="center" width=5  class="BWTableGreyHead"  alt="This allows the user to read  and write rows created by users group and immediate children" ><b>R/W</b></th>
		    <th    align="center" width=5  class="BWTableGreyHead"  alt="This allows the user to read  rows created by users group and all children" ><b>R</b></th>
                  <th    align="center" width=5  class="BWTableGreyHead"    alt="This allows the user to read  and write rows created by users group and all children" ><b>R/W</b></th>

                </tr>
                <%

                	if ( relationships.size() > 0 )
                	{
                		Vector Private   = (Vector)relationships.get("PRIVATE");
                		relationships.remove("PRIVATE");
                		NeighborhoodId privateNh = (NeighborhoodId)Private.elementAt(0);

                		Vector Domain = (Vector)relationships.get("DOMAIN");
                		relationships.remove("DOMAIN");
                		NeighborhoodId domainNh = (NeighborhoodId)Domain.elementAt(0);

                		Vector Parent =(Vector) relationships.get("PARENT");
                		relationships.remove("PARENT");


                		Vector Children = (Vector)relationships.get("CHILDREN");
                		relationships.remove("CHILDREN");


                		Vector Peers = (Vector)relationships.get("PEER");
                		NeighborhoodId peersNh = null;
                		if ( Peers != null )
                		{
                			relationships.remove("PEER");
                			peersNh = (NeighborhoodId)Peers.elementAt(0);
                		}

                		TableAccessList creatorAccess =(TableAccessList)accessLists.get("CREATOR" );
                		TableAccessList privateAccess =(TableAccessList)accessLists.get("PRIVATE" );
                		TableAccessList domainAccess =(TableAccessList)accessLists.get("DOMAIN" );
                		TableAccessList parentAccess =(TableAccessList)accessLists.get("PARENT" );



   		%>

   				<tr align="center"  >
   				 <input type="hidden" name="CREATOR_id" value="<%=creatorAccess.getId()%>" ></input>
   				  <td>Creator</td>
				  <td >Your Self</td>

				  <td><input type="checkbox"  <% if (creatorAccess.canAdministerTable() ) { %> checked <% } %>  onclick="javascript:checkAccessSettings('CREATOR','canAdministerTable')" name="CREATOR_canAdministerTable" ></td>
				  <td><input type="checkbox"  <% if (creatorAccess.canAdministerColumn() ) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canAdministerColumn')" name="CREATOR_canAdministerColumn" ></td>
				  <td><input type="checkbox"  <% if (creatorAccess.canAddRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canAddRow')" name="CREATOR_canAddRow" ></td>
				  <td><input type="checkbox"  <% if (creatorAccess.canDeleteRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canDeleteRow')" name="CREATOR_canDeleteRow" ></td>
				  
				  <td><input type="checkbox"  <% if (creatorAccess.canReadWriteOnMyLatestView()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canReadWriteOnMyLatestView')" name="CREATOR_canReadWriteOnMyLatestView" ></td>
				  <td><input type="checkbox"  <% if (creatorAccess.canReadLatestViewOfAll()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canReadLatestViewOfAll')" name="CREATOR_canReadLatestViewOfAll" ></td>
				  <td><input type="checkbox"  <% if (creatorAccess.canReadLatestViewOfAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canReadLatestViewOfAllChildren')" name="CREATOR_canReadLatestViewOfAllChildren" ></td>
				  <td><input type="checkbox"  <% if (creatorAccess.canReadLatestOfTable()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canReadLatestOfTable')" name="CREATOR_canReadLatestOfTable" ></td>
				  <td><input type="checkbox"  <% if (creatorAccess.canWriteLatestOfTable()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canWriteLatestOfTable')" name="CREATOR_canWriteLatestOfTable"></td>
				  <td><input type="checkbox"  <% if (creatorAccess.canReadWriteLatestOfMyRows()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canReadWriteLatestOfMyRows')" name="CREATOR_canReadWriteLatestOfMyRows" ></td>

				  <td><input type="checkbox"  <% if (creatorAccess.canReadLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canReadLatestofMyGroup')" name="CREATOR_canReadLatestofMyGroup" ></td>
				  <td><input type="checkbox"  <% if (creatorAccess.canReadWriteLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canReadWriteLatestofMyGroup')" name="CREATOR_canReadWriteLatestofMyGroup" ></td>
				  <td><input type="checkbox"  <% if (creatorAccess.canReadLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canReadLatestofMyGroupAndImmediateChildren')" name="CREATOR_canReadLatestofMyGroupAndImmediateChildren" ></td>
				  <td><input type="checkbox"  <% if (creatorAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canReadWriteLatestofMyGroupAndImmediateChildren')" name="CREATOR_canReadWriteLatestofMyGroupAndImmediateChildren" ></td>
				  <td><input type="checkbox"  <% if (creatorAccess.canReadLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canReadLatestofMyGroupAndAllChildren')" name="CREATOR_canReadLatestofMyGroupAndAllChildren"></td>
				  <td><input type="checkbox"  <% if (creatorAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CREATOR','canReadWriteLatestofMyGroupAndAllChildren')" name="CREATOR_canReadWriteLatestofMyGroupAndAllChildren" ></td>





				  <input type="hidden"  name="CREATOR_ACL_canAdministerTable"  value=<% if (creatorAccess.canAdministerTable()) { %>on <% }else{%>off<%} %> >
				  <input type="hidden"  name="CREATOR_ACL_canAdministerColumn"  value=<% if (creatorAccess.canAdministerColumn()) { %>on <% }else{%>off<%} %> >
				  <input type="hidden"  name="CREATOR_ACL_canAddRow"  value=<% if (creatorAccess.canAddRow()) { %>on <% }else{%>off<%} %> >
				  <input type="hidden"  name="CREATOR_ACL_canDeleteRow"  value=<% if (creatorAccess.canDeleteRow()) { %>on <% }else{%>off<%} %> >				 
				  <input type="hidden"  name="CREATOR_ACL_canReadWriteOnMyLatestView"  value=<% if (creatorAccess.canReadWriteOnMyLatestView()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="CREATOR_ACL_canReadLatestViewOfAll"  value=<% if (creatorAccess.canReadLatestViewOfAll()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="CREATOR_ACL_canReadLatestViewOfAllChildren"  value=<% if (creatorAccess.canReadLatestViewOfAllChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="CREATOR_ACL_canReadLatestOfTable"  value=<% if (creatorAccess.canReadLatestOfTable()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="CREATOR_ACL_canWriteLatestOfTable"  value=<% if (creatorAccess.canWriteLatestOfTable()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="CREATOR_ACL_canReadWriteLatestOfMyRows"  value=<% if (creatorAccess.canReadWriteLatestOfMyRows()) { %>on <% }else{%>off<%} %> >


				  <input type="hidden"  name="CREATOR_ACL_canReadLatestofMyGroup"  value=<% if (creatorAccess.canReadLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="CREATOR_ACL_canReadWriteLatestofMyGroup"  value=<% if (creatorAccess.canReadWriteLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="CREATOR_ACL_canReadLatestofMyGroupAndImmediateChildren"  value=<% if (creatorAccess.canReadLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="CREATOR_ACL_canReadWriteLatestofMyGroupAndImmediateChildren"  value=<% if (creatorAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="CREATOR_ACL_canReadLatestofMyGroupAndAllChildren"  value=<% if (creatorAccess.canReadLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="CREATOR_ACL_canReadWriteLatestofMyGroupAndAllChildren"  value=<% if (creatorAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >


				</tr>


                		<tr align="center" >
                		 <input type="hidden" name="PRIVATE_id" value="<%=privateAccess.getId()%>" ></input>

                		  <td>Your Team</td>
				  <td ><%=privateNh.getName()%></td>
				  <td><input type="checkbox" <% if (privateAccess.canAdministerTable()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('PRIVATE','canAdministerTable')" name="PRIVATE_canAdministerTable" ></td>
				  <td><input type="checkbox" <% if (privateAccess.canAdministerColumn()) { %> checked <% } %>onclick="javascript:checkAccessSettings('PRIVATE','canAdministerColumn')" name="PRIVATE_canAdministerColumn" ></td>
				  <td><input type="checkbox"  <% if (privateAccess.canAddRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PRIVATE','canAddRow')" name="PRIVATE_canAddRow" ></td>
				  <td><input type="checkbox"  <% if (privateAccess.canDeleteRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PRIVATE','canDeleteRow')" name="PRIVATE_canDeleteRow" ></td>				  
				  <td><input type="checkbox"  <% if (privateAccess.canReadWriteOnMyLatestView()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('PRIVATE','canReadWriteOnMyLatestView')" name="PRIVATE_canReadWriteOnMyLatestView" ></td>
				  <td><input type="checkbox"  <% if (privateAccess.canReadLatestViewOfAll()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('PRIVATE','canReadLatestViewOfAll')" name="PRIVATE_canReadLatestViewOfAll" ></td>
				  <td><input type="checkbox"  <% if (privateAccess.canReadLatestViewOfAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PRIVATE','canReadLatestViewOfAllChildren')" name="PRIVATE_canReadLatestViewOfAllChildren" ></td>
				  <td><input type="checkbox"  <% if (privateAccess.canReadLatestOfTable()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PRIVATE','canReadLatestOfTable')" name="PRIVATE_canReadLatestOfTable" ></td>
				  <td><input type="checkbox"  <% if (privateAccess.canWriteLatestOfTable()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PRIVATE','canWriteLatestOfTable')" name="PRIVATE_canWriteLatestOfTable"></td>
				  <td><input type="checkbox"  <% if (privateAccess.canReadWriteLatestOfMyRows()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('PRIVATE','canReadWriteLatestOfMyRows')" name="PRIVATE_canReadWriteLatestOfMyRows" ></td>



				  <td><input type="checkbox"  <% if (privateAccess.canReadLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PRIVATE','canReadLatestofMyGroup')" name="PRIVATE_canReadLatestofMyGroup" ></td>
				  <td><input type="checkbox"  <% if (privateAccess.canReadWriteLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PRIVATE','canReadWriteLatestofMyGroup')" name="PRIVATE_canReadWriteLatestofMyGroup" ></td>
				  <td><input type="checkbox"  <% if (privateAccess.canReadLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PRIVATE','canReadLatestofMyGroupAndImmediateChildren')" name="PRIVATE_canReadLatestofMyGroupAndImmediateChildren" ></td>
				  <td><input type="checkbox"  <% if (privateAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PRIVATE','canReadWriteLatestofMyGroupAndImmediateChildren')" name="PRIVATE_canReadWriteLatestofMyGroupAndImmediateChildren" ></td>
				  <td><input type="checkbox"  <% if (privateAccess.canReadLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PRIVATE','canReadLatestofMyGroupAndAllChildren')" name="PRIVATE_canReadLatestofMyGroupAndAllChildren"></td>
				  <td><input type="checkbox"  <% if (privateAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PRIVATE','canReadWriteLatestofMyGroupAndAllChildren')" name="PRIVATE_canReadWriteLatestofMyGroupAndAllChildren" ></td>






				   <input type="hidden"  name="PRIVATE_ACL_canAdministerTable"  value=<% if (privateAccess.canAdministerTable()) { %>on <% }else{%>off<%} %> >
				  <input type="hidden"  name="PRIVATE_ACL_canAdministerColumn"  value=<% if (privateAccess.canAdministerColumn()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PRIVATE_ACL_canAddRow"  value=<% if (privateAccess.canAddRow()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PRIVATE_ACL_canDeleteRow"  value=<% if (privateAccess.canDeleteRow()) { %>on <% }else{%>off<%} %> >
				  <input type="hidden"  name="PRIVATE_ACL_canReadWriteOnMyLatestView"  value=<% if (privateAccess.canReadWriteOnMyLatestView()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PRIVATE_ACL_canReadLatestViewOfAll"  value=<% if (privateAccess.canReadLatestViewOfAll()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PRIVATE_ACL_canReadLatestViewOfAllChildren"  value=<% if (privateAccess.canReadLatestViewOfAllChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PRIVATE_ACL_canReadLatestOfTable"  value=<% if (privateAccess.canReadLatestOfTable()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PRIVATE_ACL_canWriteLatestOfTable"  value=<% if (privateAccess.canWriteLatestOfTable()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PRIVATE_ACL_canReadWriteLatestOfMyRows"  value=<% if (privateAccess.canReadWriteLatestOfMyRows()) { %>on <% }else{%>off<%} %> >


				    <input type="hidden"  name="PRIVATE_ACL_canReadLatestofMyGroup"  value=<% if (privateAccess.canReadLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PRIVATE_ACL_canReadWriteLatestofMyGroup"  value=<% if (privateAccess.canReadWriteLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PRIVATE_ACL_canReadLatestofMyGroupAndImmediateChildren"  value=<% if (privateAccess.canReadLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PRIVATE_ACL_canReadWriteLatestofMyGroupAndImmediateChildren"  value=<% if (privateAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PRIVATE_ACL_canReadLatestofMyGroupAndAllChildren"  value=<% if (privateAccess.canReadLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PRIVATE_ACL_canReadWriteLatestofMyGroupAndAllChildren"  value=<% if (privateAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >




				</tr>

				<tr align="center" >
						 <input type="hidden" name="DOMAIN_id" value="<%=domainAccess.getId()%>" ></input>
				  <td>Company</td>
				  <td><%=domainNh.getName()%></td>
				  <td><input type="checkbox" <% if (domainAccess.canAdministerTable()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('DOMAIN','canAdministerTable')"  name="DOMAIN_canAdministerTable" ></td>
				  <td><input type="checkbox" <% if (domainAccess.canAdministerColumn()) { %> checked <% } %>onclick="javascript:checkAccessSettings('DOMAIN','canAdministerColumn')"  name="DOMAIN_canAdministerColumn" ></td>
				  <td><input type="checkbox" <% if (domainAccess.canAddRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('DOMAIN','canAddRow')"  name="DOMAIN_canAddRow" ></td>
				  <td><input type="checkbox" <% if (domainAccess.canDeleteRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('DOMAIN','canDeleteRow')"  name="DOMAIN_canDeleteRow" ></td>
				  <td><input type="checkbox" <% if (domainAccess.canReadWriteOnMyLatestView()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('DOMAIN','canReadWriteOnMyLatestView')"  name="DOMAIN_canReadWriteOnMyLatestView" ></td>
				  <td><input type="checkbox" <% if (domainAccess.canReadLatestViewOfAll()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('DOMAIN','canReadLatestViewOfAll')"  name="DOMAIN_canReadLatestViewOfAll" ></td>
				  <td><input type="checkbox" <% if (domainAccess.canReadLatestViewOfAllChildren()) { %> checked <% } %>    onclick="javascript:checkAccessSettings('DOMAIN','canReadLatestViewOfAllChildren')"  name="DOMAIN_canReadLatestViewOfAllChildren" ></td>
				  <td><input type="checkbox" <% if (domainAccess.canReadLatestOfTable()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('DOMAIN','canReadLatestOfTable')"  name="DOMAIN_canReadLatestOfTable" ></td>
				  <td><input type="checkbox" <% if (domainAccess.canWriteLatestOfTable()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('DOMAIN','canWriteLatestOfTable')"  name="DOMAIN_canWriteLatestOfTable" ></td>
				  <td><input type="checkbox" <% if (domainAccess.canReadWriteLatestOfMyRows()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('DOMAIN','canReadWriteLatestOfMyRows')"  name="DOMAIN_canReadWriteLatestOfMyRows" ></td>


				  <td><input type="checkbox"  <% if (domainAccess.canReadLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('DOMAIN','canReadLatestofMyGroup')" name="DOMAIN_canReadLatestofMyGroup" ></td>
				  <td><input type="checkbox"  <% if (domainAccess.canReadWriteLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('DOMAIN','canReadWriteLatestofMyGroup')" name="DOMAIN_canReadWriteLatestofMyGroup" ></td>
				  <td><input type="checkbox"  <% if (domainAccess.canReadLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('DOMAIN','canReadLatestofMyGroupAndImmediateChildren')" name="DOMAIN_canReadLatestofMyGroupAndImmediateChildren" ></td>
				  <td><input type="checkbox"  <% if (domainAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('DOMAIN','canReadWriteLatestofMyGroupAndImmediateChildren')" name="DOMAIN_canReadWriteLatestofMyGroupAndImmediateChildren" ></td>
				  <td><input type="checkbox"  <% if (domainAccess.canReadLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('DOMAIN','canReadLatestofMyGroupAndAllChildren')" name="DOMAIN_canReadLatestofMyGroupAndAllChildren"></td>
				  <td><input type="checkbox"  <% if (domainAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('DOMAIN','canReadWriteLatestofMyGroupAndAllChildren')" name="DOMAIN_canReadWriteLatestofMyGroupAndAllChildren" ></td>








				   <input type="hidden"  name="DOMAIN_ACL_canAdministerTable"  value=<% if (domainAccess.canAdministerTable()) { %>on <% }else{%>off<%} %> >
				  <input type="hidden"  name="DOMAIN_ACL_canAdministerColumn"  value=<% if (domainAccess.canAdministerColumn()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="DOMAIN_ACL_canAddRow"  value=<% if (domainAccess.canAddRow()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="DOMAIN_ACL_canDeleteRow"  value=<% if (domainAccess.canDeleteRow()) { %>on <% }else{%>off<%} %> >
				  <input type="hidden"  name="DOMAIN_ACL_canReadWriteOnMyLatestView"  value=<% if (domainAccess.canReadWriteOnMyLatestView()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="DOMAIN_ACL_canReadLatestViewOfAll"  value=<% if (domainAccess.canReadLatestViewOfAll()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="DOMAIN_ACL_canReadLatestViewOfAllChildren"  value=<% if (domainAccess.canReadLatestViewOfAllChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="DOMAIN_ACL_canReadLatestOfTable"  value=<% if (domainAccess.canReadLatestOfTable()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="DOMAIN_ACL_canWriteLatestOfTable"  value=<% if (domainAccess.canWriteLatestOfTable()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="DOMAIN_ACL_canReadWriteLatestOfMyRows"  value=<% if (domainAccess.canReadWriteLatestOfMyRows()) { %>on <% }else{%>off<%} %> >


				  <input type="hidden"  name="DOMAIN_ACL_canReadLatestofMyGroup"  value=<% if (domainAccess.canReadLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="DOMAIN_ACL_canReadWriteLatestofMyGroup"  value=<% if (domainAccess.canReadWriteLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="DOMAIN_ACL_canReadLatestofMyGroupAndImmediateChildren"  value=<% if (domainAccess.canReadLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="DOMAIN_ACL_canReadWriteLatestofMyGroupAndImmediateChildren"  value=<% if (domainAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="DOMAIN_ACL_canReadLatestofMyGroupAndAllChildren"  value=<% if (domainAccess.canReadLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="DOMAIN_ACL_canReadWriteLatestofMyGroupAndAllChildren"  value=<% if (domainAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >



				</tr>
				<%

				if ( Parent != null && Parent.size() > 0 )
				{
					NeighborhoodId parentNh = (NeighborhoodId)Parent.elementAt(0);
					System.out.println(" Inside parent" );
					parentAccess.print();
				%>

				<tr align="center" >
						 <input type="hidden" name="PARENT_id" value="<%=parentAccess.getId()%>" ></input>
				   <td>Managing Dept</td>
				  <td><%=parentNh.getName()%></td>
				  <td><input type="checkbox" <% if (parentAccess.canAdministerTable()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('PARENT','canAdministerTable')"  name="PARENT_canAdministerTable" ></td>
				  <td><input type="checkbox" <% if (parentAccess.canAdministerColumn()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('PARENT','canAdministerColumn')"  name="PARENT_canAdministerColumn" ></td>
				  <td><input type="checkbox" <% if (parentAccess.canAddRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PARENT','canAddRow')"  name="PARENT_canAddRow" ></td>
				  <td><input type="checkbox" <% if (parentAccess.canDeleteRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PARENT','canDeleteRow')"  name="PARENT_canDeleteRow" ></td>
				  <td><input type="checkbox" <% if (parentAccess.canReadWriteOnMyLatestView()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('PARENT','canReadWriteOnMyLatestView')"  name="PARENT_canReadWriteOnMyLatestView" ></td>
				  <td><input type="checkbox" <% if (parentAccess.canReadLatestViewOfAll()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('PARENT','canReadLatestViewOfAll')"  name="PARENT_canReadLatestViewOfAll" ></td>

				  <td><input type="checkbox" <% if (parentAccess.canReadLatestViewOfAllChildren()) { %> checked <% } %>    onclick="javascript:checkAccessSettings('PARENT','canReadLatestViewOfAllChildren')"  name="PARENT_canReadLatestViewOfAllChildren" ></td>

				 <td><input type="checkbox" <% if (parentAccess.canReadLatestOfTable()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PARENT','canReadLatestOfTable')"  name="PARENT_canReadLatestOfTable" ></td>
				  <td><input type="checkbox" <% if (parentAccess.canWriteLatestOfTable()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PARENT','canWriteLatestOfTable')"  name="PARENT_canWriteLatestOfTable" ></td>
				  <td><input type="checkbox" <% if (parentAccess.canReadWriteLatestOfMyRows()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PARENT','canReadWriteLatestOfMyRows')"  name="PARENT_canReadWriteLatestOfMyRows" ></td>


				 <td><input type="checkbox"  <% if (parentAccess.canReadLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PARENT','canReadLatestofMyGroup')" name="PARENT_canReadLatestofMyGroup" ></td>
				  <td><input type="checkbox"  <% if (parentAccess.canReadWriteLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PARENT','canReadWriteLatestofMyGroup')" name="PARENT_canReadWriteLatestofMyGroup" ></td>
				  <td><input type="checkbox"  <% if (parentAccess.canReadLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PARENT','canReadLatestofMyGroupAndImmediateChildren')" name="PARENT_canReadLatestofMyGroupAndImmediateChildren" ></td>
				  <td><input type="checkbox"  <% if (parentAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PARENT','canReadWriteLatestofMyGroupAndImmediateChildren')" name="PARENT_canReadWriteLatestofMyGroupAndImmediateChildren" ></td>
				  <td><input type="checkbox"  <% if (parentAccess.canReadLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PARENT','canReadLatestofMyGroupAndAllChildren')" name="PARENT_canReadLatestofMyGroupAndAllChildren"></td>
				  <td><input type="checkbox"  <% if (parentAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PARENT','canReadWriteLatestofMyGroupAndAllChildren')" name="PARENT_canReadWriteLatestofMyGroupAndAllChildren" ></td>



				    <input type="hidden"  name="PARENT_ACL_canAdministerTable"  value=<% if (parentAccess.canAdministerTable()) { %>on <% }else{%>off<%} %> >
				  <input type="hidden"  name="PARENT_ACL_canAdministerColumn"  value=<% if (parentAccess.canAdministerColumn()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PARENT_ACL_canAddRow"  value=<% if (parentAccess.canAddRow()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PARENT_ACL_canDeleteRow"  value=<% if (parentAccess.canDeleteRow()) { %>on <% }else{%>off<%} %> >
				  <input type="hidden"  name="PARENT_ACL_canReadWriteOnMyLatestView"  value=<% if (parentAccess.canReadWriteOnMyLatestView()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PARENT_ACL_canReadLatestViewOfAll"  value=<% if (parentAccess.canReadLatestViewOfAll()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PARENT_ACL_canReadLatestViewOfAllChildren"  value=<% if (parentAccess.canReadLatestViewOfAllChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PARENT_ACL_canReadLatestOfTable"  value=<% if (parentAccess.canReadLatestOfTable()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PARENT_ACL_canWriteLatestOfTable"  value=<% if (parentAccess.canWriteLatestOfTable()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PARENT_ACL_canReadWriteLatestOfMyRows"  value=<% if (parentAccess.canReadWriteLatestOfMyRows()) { %>on <% }else{%>off<%} %> >

				  <input type="hidden"  name="PARENT_ACL_canReadLatestofMyGroup"  value=<% if (parentAccess.canReadLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PARENT_ACL_canReadWriteLatestofMyGroup"  value=<% if (parentAccess.canReadWriteLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PARENT_ACL_canReadLatestofMyGroupAndImmediateChildren"  value=<% if (parentAccess.canReadLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PARENT_ACL_canReadWriteLatestofMyGroupAndImmediateChildren"  value=<% if (parentAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PARENT_ACL_canReadLatestofMyGroupAndAllChildren"  value=<% if (parentAccess.canReadLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PARENT_ACL_canReadWriteLatestofMyGroupAndAllChildren"  value=<% if (parentAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >



				 </tr>




		<%
					}

					System.out.println(" Inside children" + Children);

				  	if (Children!= null &&  Children.size() > 0 )
				  	{

							System.out.println(" Inside children" );

				  			TableAccessList childrenAccess =(TableAccessList)accessLists.get("CHILDREN" );
				  			if ( childrenAccess == null )
				  				System.out.println("children is null");
				  		        else
				  		        	childrenAccess.print();
		  %>
				  		<tr align="center" >
						   <td>Your Teams</td>
						   <td>
						   <select  width=80 class="AccessTable"  height="14" name=childrenNh>




				<%
						for( int c = 0; c < Children.size(); c++ )
						{
							String childName = ((NeighborhoodId)Children.elementAt(c)).getName();
				%>
						  <option><%=childName%></option>
				<%
						}

				%>
						</select></td>
						<input type="hidden" name="CHILDREN_id" value="<%=childrenAccess.getId()%>" ></input>
						  <td><input type="checkbox" <% if (childrenAccess.canAdministerTable()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('CHILDREN','canAdministerTable')"  name="CHILDREN_canAdministerTable" ></td>
						  <td><input type="checkbox" <% if (childrenAccess.canAdministerColumn()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CHILDREN','canAdministerColumn')"  name="CHILDREN_canAdministerColumn" ></td>
						  <td><input type="checkbox"  <% if (childrenAccess.canAddRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CHILDREN','canAddRow')"  name="CHILDREN_canAddRow" ></td>
						  <td><input type="checkbox"  <% if (childrenAccess.canDeleteRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CHILDREN','canDeleteRow')"  name="CHILDREN_canDeleteRow" ></td>
						  <td><input type="checkbox" <% if (childrenAccess.canReadWriteOnMyLatestView()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CHILDREN','canReadWriteOnMyLatestView')"  name="CHILDREN_canReadWriteOnMyLatestView" ></td>
						  <td><input type="checkbox" <% if (childrenAccess.canReadLatestViewOfAll()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('CHILDREN','canReadLatestViewOfAll')"  name="CHILDREN_canReadLatestViewOfAll" ></td>
						  <td><input type="checkbox" <% if (childrenAccess.canReadLatestViewOfAllChildren()) { %> checked <% } %>     onclick="javascript:checkAccessSettings('CHILDREN','canReadLatestViewOfAllChildren')"  name="CHILDREN_canReadLatestViewOfAllChildren" ></td>
						  <td><input type="checkbox" <% if (childrenAccess.canReadLatestOfTable()) { %> checked <% } %>    onclick="javascript:checkAccessSettings('CHILDREN','canReadLatestOfTable')"  name="CHILDREN_canReadLatestOfTable" ></td>
						  <td><input type="checkbox" <% if (childrenAccess.canWriteLatestOfTable()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('CHILDREN','canWriteLatestOfTable')"  name="CHILDREN_canWriteLatestOfTable" ></td>
						  <td><input type="checkbox" <% if (childrenAccess.canReadWriteLatestOfMyRows()) { %> checked <% } %>    onclick="javascript:checkAccessSettings('CHILDREN','canReadWriteLatestOfMyRows')"  name="CHILDREN_canReadWriteLatestOfMyRows" ></td>


						  <td><input type="checkbox"  <% if (childrenAccess.canReadLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CHILDREN','canReadLatestofMyGroup')" name="CHILDREN_canReadLatestofMyGroup" ></td>
						  <td><input type="checkbox"  <% if (childrenAccess.canReadWriteLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CHILDREN','canReadWriteLatestofMyGroup')" name="CHILDREN_canReadWriteLatestofMyGroup" ></td>
						  <td><input type="checkbox"  <% if (childrenAccess.canReadLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CHILDREN','canReadLatestofMyGroupAndImmediateChildren')" name="CHILDREN_canReadLatestofMyGroupAndImmediateChildren" ></td>
						  <td><input type="checkbox"  <% if (childrenAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CHILDREN','canReadWriteLatestofMyGroupAndImmediateChildren')" name="CHILDREN_canReadWriteLatestofMyGroupAndImmediateChildren" ></td>
						  <td><input type="checkbox"  <% if (childrenAccess.canReadLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CHILDREN','canReadLatestofMyGroupAndAllChildren')" name="CHILDREN_canReadLatestofMyGroupAndAllChildren"></td>
						  <td><input type="checkbox"  <% if (childrenAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('CHILDREN','canReadWriteLatestofMyGroupAndAllChildren')" name="CHILDREN_canReadWriteLatestofMyGroupAndAllChildren" ></td>


						  </tr>

						      <input type="hidden"  name="CHILDREN_ACL_canAdministerTable"  value=<% if (childrenAccess.canAdministerTable()) { %>on <% }else{%>off<%} %> >
						  <input type="hidden"  name="CHILDREN_ACL_canAdministerColumn"  value=<% if (childrenAccess.canAdministerColumn()) { %>on <% }else{%>off<%} %> >
						 <input type="hidden"  name="CHILDREN_ACL_canAddRow"  value=<% if (childrenAccess.canAddRow()) { %>on <% }else{%>off<%} %> >
						 <input type="hidden"  name="CHILDREN_ACL_canDeleteRow"  value=<% if (childrenAccess.canDeleteRow()) { %>on <% }else{%>off<%} %> >
						  <input type="hidden"  name="CHILDREN_ACL_canReadWriteOnMyLatestView"  value=<% if (childrenAccess.canReadWriteOnMyLatestView()) { %>on <% }else{%>off<%} %> >
						 <input type="hidden"  name="CHILDREN_ACL_canReadLatestViewOfAll"  value=<% if (childrenAccess.canReadLatestViewOfAll()) { %>on <% }else{%>off<%} %> >
						 <input type="hidden"  name="CHILDREN_ACL_canReadLatestViewOfAllChildren"  value=<% if (childrenAccess.canReadLatestViewOfAllChildren()) { %>on <% }else{%>off<%} %> >
						 <input type="hidden"  name="CHILDREN_ACL_canReadLatestOfTable"  value=<% if (childrenAccess.canReadLatestOfTable()) { %>on <% }else{%>off<%} %> >
						 <input type="hidden"  name="CHILDREN_ACL_canWriteLatestOfTable"  value=<% if (childrenAccess.canWriteLatestOfTable()) { %>on <% }else{%>off<%} %> >
						 <input type="hidden"  name="CHILDREN_ACL_canReadWriteLatestOfMyRows"  value=<% if (childrenAccess.canReadWriteLatestOfMyRows()) { %>on <% }else{%>off<%} %> >

						   <input type="hidden"  name="CHILDREN_ACL_canReadLatestofMyGroup"  value=<% if (childrenAccess.canReadLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
						 <input type="hidden"  name="CHILDREN_ACL_canReadWriteLatestofMyGroup"  value=<% if (childrenAccess.canReadWriteLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
						 <input type="hidden"  name="CHILDREN_ACL_canReadLatestofMyGroupAndImmediateChildren"  value=<% if (childrenAccess.canReadLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
						 <input type="hidden"  name="CHILDREN_ACL_canReadWriteLatestofMyGroupAndImmediateChildren"  value=<% if (childrenAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
						 <input type="hidden"  name="CHILDREN_ACL_canReadLatestofMyGroupAndAllChildren"  value=<% if (childrenAccess.canReadLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >
						 <input type="hidden"  name="CHILDREN_ACL_canReadWriteLatestofMyGroupAndAllChildren"  value=<% if (childrenAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >




                	     <%
			              	}

			     	  	if ( Peers!= null && Peers.size() > 0 )
			       				  	{
			       				  		  System.out.println(" Inside peer" );

			       				  	TableAccessList peerAccess =(TableAccessList)accessLists.get("PEER" );
			       				  	if ( peerAccess == null )
			       				  		System.out.println(" peerAccess is null");
			       				       else
			       				       		peerAccess.print();
			       %>
			       <tr align="center" >

			       						   <td>Peer Depts</td>
			       						   <td>
			       						   <select  width=80 class="AccessTable"  height="14" name=peerNh>




			       				<%
			       						for( int p = 0; p < Peers.size(); p++ )
			       						{
			       							String peerName = ((NeighborhoodId)Peers.elementAt(p)).getName();
			       				%>
			       						  <option><%=peerName%></option>
			       				<%
			       						}
			       				%>
			      						</select>
			      						</td>



			      							<input type="hidden" name="PEER_id" value="<%=peerAccess.getId()%>" ></input>
			       						  <td ><input type="checkbox" <% if (peerAccess.canAdministerTable()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('PEER','canAdministerTable')"  name="PEER_canAdministerTable"    ></td>
			       						  <td><input type="checkbox" <% if (peerAccess.canAdministerColumn()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PEER','canAdministerColumn')"  name="PEER_canAdministerColumn" ></td>
			       						  <td><input type="checkbox" <% if (peerAccess.canAddRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PEER','canAddRow')"  name="PEER_canAddRow" ></td>
			       						  <td><input type="checkbox" <% if (peerAccess.canDeleteRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PEER','canDeleteRow')"  name="PEER_canDeleteRow" ></td>
			       						  <td><input type="checkbox" <% if (peerAccess.canReadWriteOnMyLatestView()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PEER','canReadWriteOnMyLatestView')"  name="PEER_canReadWriteOnMyLatestView" ></td>
			       						  <td><input type="checkbox" <% if (peerAccess.canReadLatestViewOfAll()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('PEER','canReadLatestViewOfAll')"  name="PEER_canReadLatestViewOfAll" ></td>
			       						  <td><input type="checkbox" <% if (peerAccess.canReadLatestViewOfAllChildren()) { %> checked <% } %>     onclick="javascript:checkAccessSettings('PEER','canReadLatestViewOfAllChildren')"  name="PEER_canReadLatestViewOfAllChildren" ></td>
			       						  <td><input type="checkbox" <% if (peerAccess.canReadLatestOfTable()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PEER','canReadLatestOfTable')"  name="PEER_canReadLatestOfTable" ></td>
			       						  <td><input type="checkbox" <% if (peerAccess.canWriteLatestOfTable()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('PEER','canWriteLatestOfTable')"  name="PEER_canWriteLatestOfTable" ></td>
			       						  <td><input type="checkbox" <% if (peerAccess.canReadWriteLatestOfMyRows()) { %> checked <% } %>     onclick="javascript:checkAccessSettings('PEER','canReadWriteLatestOfMyRows')"  name="PEER_canReadWriteLatestOfMyRows" ></td>


			       						  <td><input type="checkbox"  <% if (peerAccess.canReadLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PEER','canReadLatestofMyGroup')" name="PEER_canReadLatestofMyGroup" ></td>
									  <td><input type="checkbox"  <% if (peerAccess.canReadWriteLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PEER','canReadWriteLatestofMyGroup')" name="PEER_canReadWriteLatestofMyGroup" ></td>
									  <td><input type="checkbox"  <% if (peerAccess.canReadLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PEER','canReadLatestofMyGroupAndImmediateChildren')" name="PEER_canReadLatestofMyGroupAndImmediateChildren" ></td>
									  <td><input type="checkbox"  <% if (peerAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PEER','canReadWriteLatestofMyGroupAndImmediateChildren')" name="PEER_canReadWriteLatestofMyGroupAndImmediateChildren" ></td>
									  <td><input type="checkbox"  <% if (peerAccess.canReadLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PEER','canReadLatestofMyGroupAndAllChildren')" name="PEER_canReadLatestofMyGroupAndAllChildren"></td>
									  <td><input type="checkbox"  <% if (peerAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PEER','canReadWriteLatestofMyGroupAndAllChildren')" name="PEER_canReadWriteLatestofMyGroupAndAllChildren" ></td>



			       						  </tr>


												      <input type="hidden"  name="PEER_ACL_canAdministerTable"  value=<% if (peerAccess.canAdministerTable()) { %>on <% }else{%>off<%} %> >
												  <input type="hidden"  name="PEER_ACL_canAdministerColumn"  value=<% if (peerAccess.canAdministerColumn()) { %>on <% }else{%>off<%} %> >
												 <input type="hidden"  name="PEER_ACL_canAddRow"  value=<% if (peerAccess.canAddRow()) { %>on <% }else{%>off<%} %> >
												 <input type="hidden"  name="PEER_ACL_canDeleteRow"  value=<% if (peerAccess.canDeleteRow()) { %>on <% }else{%>off<%} %> >
												  <input type="hidden"  name="PEER_ACL_canReadWriteOnMyLatestView"  value=<% if (peerAccess.canReadWriteOnMyLatestView()) { %>on <% }else{%>off<%} %> >
												 <input type="hidden"  name="PEER_ACL_canReadLatestViewOfAll"  value=<% if (peerAccess.canReadLatestViewOfAll()) { %>on <% }else{%>off<%} %> >
												 <input type="hidden"  name="PEER_ACL_canReadLatestViewOfAllChildren"  value=<% if (peerAccess.canReadLatestViewOfAllChildren()) { %>on <% }else{%>off<%} %> >
												 <input type="hidden"  name="PEER_ACL_canReadLatestOfTable"  value=<% if (peerAccess.canReadLatestOfTable()) { %>on <% }else{%>off<%} %> >
												 <input type="hidden"  name="PEER_ACL_canWriteLatestOfTable"  value=<% if (peerAccess.canWriteLatestOfTable()) { %>on <% }else{%>off<%} %> >
												 <input type="hidden"  name="PEER_ACL_canReadWriteLatestOfMyRows"  value=<% if (peerAccess.canReadWriteLatestOfMyRows()) { %>on <% }else{%>off<%} %> >

												   <input type="hidden"  name="PEER_ACL_canReadLatestofMyGroup"  value=<% if (peerAccess.canReadLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
												 <input type="hidden"  name="PEER_ACL_canReadWriteLatestofMyGroup"  value=<% if (peerAccess.canReadWriteLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
												 <input type="hidden"  name="PEER_ACL_canReadLatestofMyGroupAndImmediateChildren"  value=<% if (peerAccess.canReadLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
												 <input type="hidden"  name="PEER_ACL_canReadWriteLatestofMyGroupAndImmediateChildren"  value=<% if (peerAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
												 <input type="hidden"  name="PEER_ACL_canReadLatestofMyGroupAndAllChildren"  value=<% if (peerAccess.canReadLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >
												 <input type="hidden"  name="PEER_ACL_canReadWriteLatestofMyGroupAndAllChildren"  value=<% if (peerAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >




			                       	     <%
			       			              	}


							if ( relationships.size() > 0 )
							{
								Enumeration friends = relationships.keys();

			      			       		while ( friends.hasMoreElements() )
			      			       		{
			      			       			String friendRelationship = (String)friends.nextElement();
			      			       			Vector friendNhs = (Vector)relationships.get(friendRelationship);
			      			       			TableAccessList friendAccess =(TableAccessList)accessLists.get(friendRelationship);
			       				  	 	 friendAccess.print();
			      			       %>
			      			       			<tr align="center" >
									   <td><%=friendRelationship%></td>
									   <td>
									   <select  width=80 class="AccessTable"  height="14" name=<%=friendRelationship%>Nh>




			      			       				<%
			      			       						for( int f = 0;f < friendNhs.size(); f++ )
			      			       						{
			      			       							String friendName = ((NeighborhoodId)friendNhs.elementAt(f)).getName();
			      			       				%>
			      			       						  <option><%=friendName%></option>
			      			       				<%
			      			       						}

			      			       				%>
			      			       						</select>
			      			       						</td>
			      			       						<input type="hidden" name="<%=friendRelationship%>_id" value="<%=friendAccess.getId()%>" ></input>
			      			       						  <td><input type="checkbox" <% if (friendAccess.canAdministerTable()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canAdministerTable')"  name="<%=friendRelationship%>_canAdministerTable" ></td>
			      			       						  <td><input type="checkbox" <% if (friendAccess.canAdministerColumn()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canAdministerColumn')"  name="<%=friendRelationship%>_canAdministerColumn" ></td>
			      			       						  <td><input type="checkbox" <% if (friendAccess.canAddRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canAddRow')"  name="<%=friendRelationship%>_canAddRow" ></td>
			      			       						  <td><input type="checkbox" <% if (friendAccess.canDeleteRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canDeleteRow')"  name="<%=friendRelationship%>_canDeleteRow" ></td>
			      			       						  <td><input type="checkbox" <% if (friendAccess.canReadWriteOnMyLatestView()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canReadWriteOnMyLatestView')"  name="<%=friendRelationship%>_canReadWriteOnMyLatestView" ></td>
			      			       						  <td><input type="checkbox" <% if (friendAccess.canReadLatestViewOfAll()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canReadLatestViewOfAll')"  name="<%=friendRelationship%>_canReadLatestViewOfAll" ></td>
			      			       						  <td><input type="checkbox" <% if (friendAccess.canReadLatestViewOfAllChildren()) { %> checked <% } %>    onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canReadLatestViewOfAllChildren')"  name="<%=friendRelationship%>_canReadLatestViewOfAllChildren" ></td>
			      			       						  <td><input type="checkbox" <% if (friendAccess.canReadLatestOfTable()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canReadLatestOfTable')"  name="<%=friendRelationship%>_canReadLatestOfTable" ></td>
			      			       						  <td><input type="checkbox" <% if (friendAccess.canWriteLatestOfTable()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canWriteLatestOfTable')"  name="<%=friendRelationship%>_canWriteLatestOfTable" ></td>
			      			       						  <td><input type="checkbox" <% if (friendAccess.canReadWriteLatestOfMyRows()) { %> checked <% } %>    onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canReadWriteLatestOfMyRows')"  name="<%=friendRelationship%>_canReadWriteLatestOfMyRows" ></td>

			      			       						  <td><input type="checkbox"  <% if (friendAccess.canReadLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canReadLatestofMyGroup')" name="<%=friendRelationship%>_canReadLatestofMyGroup" ></td>
												  <td><input type="checkbox"  <% if (friendAccess.canReadWriteLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canReadWriteLatestofMyGroup')" name="<%=friendRelationship%>_canReadWriteLatestofMyGroup" ></td>
												  <td><input type="checkbox"  <% if (friendAccess.canReadLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canReadLatestofMyGroupAndImmediateChildren')" name="<%=friendRelationship%>_canReadLatestofMyGroupAndImmediateChildren" ></td>
												  <td><input type="checkbox"  <% if (friendAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canReadWriteLatestofMyGroupAndImmediateChildren')" name="<%=friendRelationship%>_canReadWriteLatestofMyGroupAndImmediateChildren" ></td>
												  <td><input type="checkbox"  <% if (friendAccess.canReadLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canReadLatestofMyGroupAndAllChildren')" name="<%=friendRelationship%>_canReadLatestofMyGroupAndAllChildren"></td>
												  <td><input type="checkbox"  <% if (friendAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('<%=friendRelationship%>','canReadWriteLatestofMyGroupAndAllChildren')" name="<%=friendRelationship%>_canReadWriteLatestofMyGroupAndAllChildren" ></td>





											      <input type="hidden"  name="<%=friendRelationship%>_ACL_canAdministerTable"  value=<% if (friendAccess.canAdministerTable()) { %>on <% }else{%>off<%} %> >
											  <input type="hidden"  name="<%=friendRelationship%>_ACL_canAdministerColumn"  value=<% if (friendAccess.canAdministerColumn()) { %>on <% }else{%>off<%} %> >
											 <input type="hidden"  name="<%=friendRelationship%>_ACL_canAddRow"  value=<% if (friendAccess.canAddRow()) { %>on <% }else{%>off<%} %> >
											 <input type="hidden"  name="<%=friendRelationship%>_ACL_canDeleteRow"  value=<% if (friendAccess.canDeleteRow()) { %>on <% }else{%>off<%} %> >
											  <input type="hidden"  name="<%=friendRelationship%>_ACL_canReadWriteOnMyLatestView"  value=<% if (friendAccess.canReadWriteOnMyLatestView()) { %>on <% }else{%>off<%} %> >
											 <input type="hidden"  name="<%=friendRelationship%>_ACL_canReadLatestViewOfAll"  value=<% if (friendAccess.canReadLatestViewOfAll()) { %>on <% }else{%>off<%} %> >
											 <input type="hidden"  name="<%=friendRelationship%>_ACL_canReadLatestViewOfAllChildren"  value=<% if (friendAccess.canReadLatestViewOfAllChildren()) { %>on <% }else{%>off<%} %> >
											 <input type="hidden"  name="<%=friendRelationship%>_ACL_canReadLatestOfTable"  value=<% if (friendAccess.canReadLatestOfTable()) { %>on <% }else{%>off<%} %> >
											 <input type="hidden"  name="<%=friendRelationship%>_ACL_canWriteLatestOfTable"  value=<% if (friendAccess.canWriteLatestOfTable()) { %>on <% }else{%>off<%} %> >
											 <input type="hidden"  name="<%=friendRelationship%>_ACL_canReadWriteLatestOfMyRows"  value=<% if (friendAccess.canReadWriteLatestOfMyRows()) { %>on <% }else{%>off<%} %> >

											   <input type="hidden"  name="<%=friendRelationship%>_ACL_canReadLatestofMyGroup"  value=<% if (friendAccess.canReadLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
											 <input type="hidden"  name="<%=friendRelationship%>_ACL_canReadWriteLatestofMyGroup"  value=<% if (friendAccess.canReadWriteLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
											 <input type="hidden"  name="<%=friendRelationship%>_ACL_canReadLatestofMyGroupAndImmediateChildren"  value=<% if (friendAccess.canReadLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
											 <input type="hidden"  name="<%=friendRelationship%>_ACL_canReadWriteLatestofMyGroupAndImmediateChildren"  value=<% if (friendAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
											 <input type="hidden"  name="<%=friendRelationship%>_ACL_canReadLatestofMyGroupAndAllChildren"  value=<% if (friendAccess.canReadLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >
											 <input type="hidden"  name="<%=friendRelationship%>_ACL_canReadWriteLatestofMyGroupAndAllChildren"  value=<% if (friendAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >


			      			       						  </tr>

			      			                       	     <%
			       			              	}

			       			         }// END OF FRIENDS RELATIONSHIPS HASHTABLE
			       	TableAccessList publicAccess =(TableAccessList)accessLists.get("PUBLIC" );
                 %>
                		<tr align="center" >
                		<input type="hidden" name="PUBLIC_id" value="<%=publicAccess.getId()%>" ></input>
				  <td>Public</td>
				  <td>Others</td>
				  <td><input type="checkbox" <% if (publicAccess.canAdministerTable()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('PUBLIC','canAdministerTable')"  name="PUBLIC_canAdministerTable" ></td>
				  <td><input type="checkbox" <% if (publicAccess.canAdministerColumn()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PUBLIC','canAdministerColumn')"  name="PUBLIC_canAdministerColumn" ></td>
				  <td><input type="checkbox" <% if (publicAccess.canAddRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PUBLIC','canAddRow')"  name="PUBLIC_canAddRow" ></td>
				  <td><input type="checkbox" <% if (publicAccess.canDeleteRow()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PUBLIC','canDeleteRow')"  name="PUBLIC_canDeleteRow" ></td>
				  <td><input type="checkbox" <% if (publicAccess.canReadWriteOnMyLatestView()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PUBLIC','canReadWriteOnMyLatestView')"  name="PUBLIC_canReadWriteOnMyLatestView" ></td>
				  <td><input type="checkbox" <% if (publicAccess.canReadLatestViewOfAll()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('PUBLIC','canReadLatestViewOfAll')"  name="PUBLIC_canReadLatestViewOfAll" ></td>
				  <td><input type="checkbox" <% if (publicAccess.canReadLatestViewOfAllChildren()) { %> checked <% } %>    onclick="javascript:checkAccessSettings('PUBLIC','canReadLatestViewOfAllChildren')"  name="PUBLIC_canReadLatestViewOfAllChildren" ></td>
				  <td><input type="checkbox" <% if (publicAccess.canReadLatestOfTable()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PUBLIC','canReadLatestOfTable')"  name="PUBLIC_canReadLatestOfTable" ></td>
				  <td><input type="checkbox" <% if (publicAccess.canWriteLatestOfTable()) { %> checked <% } %>  onclick="javascript:checkAccessSettings('PUBLIC','canWriteLatestOfTable')"  name="PUBLIC_canWriteLatestOfTable" ></td>
				  <td><input type="checkbox" <% if (publicAccess.canReadWriteLatestOfMyRows()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PUBLIC','canReadWriteLatestOfMyRows')"  name="PUBLIC_canReadWriteLatestOfMyRows" ></td>

				  <td><input type="checkbox"  <% if (publicAccess.canReadLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PUBLIC','canReadLatestofMyGroup')" name="PUBLIC_canReadLatestofMyGroup" ></td>
				  <td><input type="checkbox"  <% if (publicAccess.canReadWriteLatestofMyGroup()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PUBLIC','canReadWriteLatestofMyGroup')" name="PUBLIC_canReadWriteLatestofMyGroup" ></td>
				  <td><input type="checkbox"  <% if (publicAccess.canReadLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PUBLIC','canReadLatestofMyGroupAndImmediateChildren')" name="PUBLIC_canReadLatestofMyGroupAndImmediateChildren" ></td>
				  <td><input type="checkbox"  <% if (publicAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PUBLIC','canReadWriteLatestofMyGroupAndImmediateChildren')" name="PUBLIC_canReadWriteLatestofMyGroupAndImmediateChildren" ></td>
				  <td><input type="checkbox"  <% if (publicAccess.canReadLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PUBLIC','canReadLatestofMyGroupAndAllChildren')" name="PUBLIC_canReadLatestofMyGroupAndAllChildren"></td>
				  <td><input type="checkbox"  <% if (publicAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %> checked <% } %>   onclick="javascript:checkAccessSettings('PUBLIC','canReadWriteLatestofMyGroupAndAllChildren')" name="PUBLIC_canReadWriteLatestofMyGroupAndAllChildren" ></td>







				<input type="hidden"  name="PUBLIC_ACL_canAdministerTable"  value=<% if (publicAccess.canAdministerTable()) { %>on <% }else{%>off<%} %> >
				  <input type="hidden"  name="PUBLIC_ACL_canAdministerColumn"  value=<% if (publicAccess.canAdministerColumn()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PUBLIC_ACL_canAddRow"  value=<% if (publicAccess.canAddRow()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PUBLIC_ACL_canDeleteRow"  value=<% if (publicAccess.canDeleteRow()) { %>on <% }else{%>off<%} %> >
				  <input type="hidden"  name="PUBLIC_ACL_canReadWriteOnMyLatestView"  value=<% if (publicAccess.canReadWriteOnMyLatestView()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PUBLIC_ACL_canReadLatestViewOfAll"  value=<% if (publicAccess.canReadLatestViewOfAll()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PUBLIC_ACL_canReadLatestViewOfAllChildren"  value=<% if (publicAccess.canReadLatestViewOfAllChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PUBLIC_ACL_canReadLatestOfTable"  value=<% if (publicAccess.canReadLatestOfTable()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PUBLIC_ACL_canWriteLatestOfTable"  value=<% if (publicAccess.canWriteLatestOfTable()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PUBLIC_ACL_canReadWriteLatestOfMyRows"  value=<% if (publicAccess.canReadWriteLatestOfMyRows()) { %>on <% }else{%>off<%} %> >

				   <input type="hidden"  name="PUBLIC_ACL_canReadLatestofMyGroup"  value=<% if (publicAccess.canReadLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PUBLIC_ACL_canReadWriteLatestofMyGroup"  value=<% if (publicAccess.canReadWriteLatestofMyGroup()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PUBLIC_ACL_canReadLatestofMyGroupAndImmediateChildren"  value=<% if (publicAccess.canReadLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PUBLIC_ACL_canReadWriteLatestofMyGroupAndImmediateChildren"  value=<% if (publicAccess.canReadWriteLatestofMyGroupAndImmediateChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PUBLIC_ACL_canReadLatestofMyGroupAndAllChildren"  value=<% if (publicAccess.canReadLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >
				 <input type="hidden"  name="PUBLIC_ACL_canReadWriteLatestofMyGroupAndAllChildren"  value=<% if (publicAccess.canReadWriteLatestofMyGroupAndAllChildren()) { %>on <% }else{%>off<%} %> >



				</tr>
                <%	} // END OF RELATIONSHIPS HASHTABLE

                %>


              </table>
              <p>&nbsp;</p>
            </td>
          </tr>
          <tr bgcolor="white">
            <td>
              <div align="center"><b>
                <a href="javascript:setActionAndSubmit('commitTableAccess')"><img src="images/security.gif" width="15" height="15">  Save</a>


                </b></div>
            </td>
          </tr>
<input type="hidden" name="action" value="commitTableAccess">
<input type="hidden" name="tableId" value="<%=tableId%>">
<input type="hidden" name="tableName" value="<%=tableName%>">
<input type="hidden" name="selNhid" value="<%=privateNhId.getId()%>">
<input type="hidden" name="ViewPreference" value="<%=ViewPreference%>">


          <%@include file='/jsp/common/commonparameters.jsp' %> </form>
        </table>
        <!--end table for table-->
        </tr>

     </table>
              <!--end table for main page table 2 -->


            </td>


            <td width="2" valign="top" bgcolor="#FFCC66"><img src="images/clear.gif" width="2" height="2"></td>



                   </tr>
        </table>

        <!--start main page table -->

    <br>
    </td>

  </tr>

<br>
<%@include file='/jsp/common/footer.jsp' %>

