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
	request.setAttribute("heading", " Column Access Control for \"" +tableName +  "\" from the perspective of Neighborhood " + privateNhId.getName());


	TableColumnInfo tci =(TableColumnInfo) request.getAttribute("tableColumnInfo");
	Vector columns = tci.getColumnVector();
	Hashtable columnsById = tci.getColumnHash();

	ColumnAccessList cal = (ColumnAccessList) request.getAttribute("ColumnAccessList");
	Hashtable acc = cal.getAccess();
	Vector accList = cal.getColumns();

	System.out.println("Number of Current Restricted Columns = " + accList.size());

	String ViewPreference = (String)request.getAttribute("ViewPreference");

	System.out.println("ViewPreference" + ViewPreference );


%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<br>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>
<script LANGUAGE=JAVASCRIPT TYPE="TEXT/JAVASCRIPT">

function saveSettings()
{
	document.forms[0].action.value = 'commitColumnAccess'
	document.forms[0].submit()
}
function accessClick(cid, rel, access)
{

  var obj = eval("document.all."+rel+"_"+cid);
  obj.value = access;
  //alert(rel)
  //alert(access)
  //alert(obj.value)

}
function remWS(str)
{
  for (i=0; i<str.length; i++)
  {
    str = str.replace(' ', '_')
  }
  return str;
}
</script>


<tr>
    <td>

        <!--start main page table-->
        <table border="0" cellspacing="0" cellpadding="0" class="body">

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
       <form method="post" name="accform" id="accform" action="MyTables?tableid=<%=tableId%>">
          <tr valign="top">
            <td>
              <table class="body" border="0" cellspacing="2" cellpadding="2">
                <tr bgcolor="#fddeb9">
		<td>
		 <a href="javascript:setActionAndSubmit('commitColumnAccess')">  Save Settings </a>
		</td>

                </tr>
                </table>
             <b>
              </b></td>
          </tr>



          <tr bgcolor="#eeeeee" valign="top">
            <td>
              <table class="AccessTable"  border="1" cellspacing="1" cellpadding="2"  valign="top" >

                <tr border="1"  >
		                  <th colspan=2 class="BWTableWhiteHead"><b>Users</b>
<%
	Iterator rci = accList.iterator();
	while (rci.hasNext())
	{
	  Column c = (Column)columnsById.get(rci.next());
	  System.out.println("Setting header information " + c.getColumnName());
%>
		<th colspan=3 class="BWTableGreyHead"><b><%=c.getColumnName()%></b></th>
		<input type="hidden" name="AllColumns" value="<%=c.getId()%>">
<%
	} // while
%>
		</tr>


                <tr>
                  <th align="center" width=15 class="BWTableWhiteHead"><b>Relationship</b></th>
                  <th align="center" width=15 class="BWTableWhiteHead" ><b>Neighborhood</b></th>
<%
	rci = accList.iterator();
	while (rci.hasNext())
	{
	  Column c = (Column)columnsById.get(rci.next());
	  System.out.println("Setting header Read Write etc");
%>
		  <th align="center" width=15 class="BWTableWhiteHead" ><b>Read</b></th>
		  <th align="center" width=15 class="BWTableWhiteHead" ><b>Write</b></th>
		  <th align="center" width=15 class="BWTableWhiteHead" ><b>None</b></th>
<%
	}//while
	System.out.println("Finished headers");
%>
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

%>

	<tr align="center" border=1  >
	  <td>Creator</td>
	  <td >Your Self</td>
<%
	rci = accList.iterator();
	while (rci.hasNext())
	{
	  int colId = ((Integer)rci.next()).intValue();
	  int access = 2;
	  Column c = (Column)columnsById.get(new Integer(colId));
	  // does it have access for this particular relationship?
	  Integer accInt = (Integer)acc.get("CREATOR"+":"+colId);
	  if (accInt != null) {
	  	access = accInt.intValue();
	  }
	  System.out.println("Creator Access = " + access);

%>
	<td><input type="radio" name='<%="CREATOR"+c.getId()%>' value="ro" onClick="accessClick(<%=c.getId()%>,'CREATOR', 1)" <%=access==1?"checked":""%>></td>
	<td><input type="radio" name='<%="CREATOR"+c.getId()%>' value="wr" onClick="accessClick(<%=c.getId()%>,'CREATOR', 2)" <%=access==2?"checked":""%>></td>
	<td><input type="radio" name='<%="CREATOR"+c.getId()%>' value="no" onClick="accessClick(<%=c.getId()%>,'CREATOR', 0)" <%=access==0?"checked":""%> disabled></td>
	<input type="hidden" name=<%="\"" + "CREATOR_" +c.getId()+ "\""%>  id=<%="\"" +"CREATOR_"+ c.getId() + "\""%> value="<%=access%>">
	

<%
	}//while
	
%>
	<input type="hidden" name="AllRels" value="CREATOR">
	</tr>


	<tr align="center" >
	  <td>Your Team</td>
	  <td ><%=privateNh.getName()%></td>
<%
	rci = accList.iterator();
	while (rci.hasNext())
	{
	  int colId = ((Integer)rci.next()).intValue();
	  int access = 2;
	  Column c = (Column)columnsById.get(new Integer(colId));
	  // does it have access for this particular relationship?
	  Integer accInt = (Integer)acc.get("PRIVATE"+":"+colId);
	  if (accInt != null) {
	  	access = accInt.intValue();
	  }
%>
	<td><input type="radio" name='<%="PRIVATE"+c.getId()%>' value="ro" onClick="accessClick(<%=c.getId()%>,'PRIVATE', 1)" <%=access==1?"checked":""%>></td>
	<td><input type="radio" name='<%="PRIVATE"+c.getId()%>' value="wr" onClick="accessClick(<%=c.getId()%>,'PRIVATE', 2)" <%=access==2?"checked":""%>></td>
	<td><input type="radio" name='<%="PRIVATE"+c.getId()%>' value="no" onClick="accessClick(<%=c.getId()%>,'PRIVATE', 0)" <%=access==0?"checked":""%>></td>
	<input type="hidden" name=<%="\"" + "PRIVATE_" +c.getId()+ "\""%>  id=<%="\"" +"PRIVATE_"+ c.getId() + "\""%> value="<%=access%>">
	
<%
	}//while
	
%>
	<input type="hidden" name="AllRels" value="PRIVATE">
	</tr>

	<tr align="center" >
	  <td>Company</td>
	  <td><%=domainNh.getName()%></td>
<%
	rci = accList.iterator();
	while (rci.hasNext())
	{
	  int colId = ((Integer)rci.next()).intValue();
	  int access = 2;
	  Column c = (Column)columnsById.get(new Integer(colId));
	  // does it have access for this particular relationship?
	  Integer accInt = (Integer)acc.get("DOMAIN"+":"+colId);
	  if (accInt != null) {
	  	access = accInt.intValue();
	  }
%>
	<td><input type="radio" name='<%="DOMAIN"+c.getId()%>' value="ro" onClick="accessClick(<%=c.getId()%>,'DOMAIN', 1)" <%=access==1?"checked":""%>></td>
	<td><input type="radio" name='<%="DOMAIN"+c.getId()%>' value="wr" onClick="accessClick(<%=c.getId()%>,'DOMAIN', 2)" <%=access==2?"checked":""%>></td>
	<td><input type="radio" name='<%="DOMAIN"+c.getId()%>' value="no" onClick="accessClick(<%=c.getId()%>,'DOMAIN', 0)" <%=access==0?"checked":""%>></td>
	<input type="hidden" name=<%="\"" + "DOMAIN_" +c.getId()+ "\""%>  id=<%="\"" +"DOMAIN_"+ c.getId() + "\""%> value="<%=access%>">
	
<%
	}//while
	
%>
	<input type="hidden" name="AllRels" value="DOMAIN">
	</tr>
	<%

    if ( Parent != null && Parent.size() > 0 )
    {
	NeighborhoodId parentNh = (NeighborhoodId)Parent.elementAt(0);
	System.out.println(" Inside parent" );
%>

	<tr align="center" >
	   <td>Managing Dept</td>
	  <td><%=parentNh.getName()%></td>
  <%
	rci = accList.iterator();
	while (rci.hasNext())
	{
	  int colId = ((Integer)rci.next()).intValue();
	  int access = 2;
	  Column c = (Column)columnsById.get(new Integer(colId));
	  // does it have access for this particular relationship?
	  Integer accInt = (Integer)acc.get("PARENT"+":"+colId);
	  if (accInt != null) {
	  	access = accInt.intValue();
	  }
  %>
	<td><input type="radio" name='<%="PARENT"+c.getId()%>' value="ro" onClick="accessClick(<%=c.getId()%>,'PARENT', 1)" <%=access==1?"checked":""%>></td>
	<td><input type="radio" name='<%="PARENT"+c.getId()%>' value="wr" onClick="accessClick(<%=c.getId()%>,'PARENT', 2)" <%=access==2?"checked":""%>></td>
	<td><input type="radio" name='<%="PARENT"+c.getId()%>' value="no" onClick="accessClick(<%=c.getId()%>,'PARENT', 0)" <%=access==0?"checked":""%>></td>
	<input type="hidden" name=<%="\"" + "PARENT_" +c.getId()+ "\""%>  id=<%="\"" +"PARENT_"+ c.getId() + "\""%> value="<%=access%>">
	
  <%
	}//while
	
  %>
  	<input type="hidden" name="AllRels" value="PARENT">
	 </tr>
<%
     }

	System.out.println(" Inside children" + Children);

	if (Children!= null &&  Children.size() > 0 )
	{

	System.out.println(" Inside children" );

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
<%
	rci = accList.iterator();
	while (rci.hasNext())
	{
	  int colId = ((Integer)rci.next()).intValue();
	  int access = 2;
	  Column c = (Column)columnsById.get(new Integer(colId));
	  // does it have access for this particular relationship?
	  Integer accInt = (Integer)acc.get("CHILDREN"+":"+colId);
	  if (accInt != null) {
		access = accInt.intValue();
	  }
%>
	<td><input type="radio" name='<%="CHILDREN"+c.getId()%>' value="ro" onClick="accessClick(<%=c.getId()%>,'CHILDREN', 1)" <%=access==1?"checked":""%>></td>
	<td><input type="radio" name='<%="CHILDREN"+c.getId()%>' value="wr" onClick="accessClick(<%=c.getId()%>,'CHILDREN', 2)" <%=access==2?"checked":""%>></td>
	<td><input type="radio" name='<%="CHILDREN"+c.getId()%>' value="no" onClick="accessClick(<%=c.getId()%>,'CHILDREN', 0)" <%=access==0?"checked":""%>></td>
	<input type="hidden" name=<%="\"" + "CHILDREN_" +c.getId()+ "\""%>  id=<%="\"" +"CHILDREN_"+ c.getId() + "\""%> value="<%=access%>">
	
	<%
	}//while
		
	%>
	<input type="hidden" name="AllRels" value="CHILDREN">
	</tr>
	<%
	}
	if ( Peers!= null && Peers.size() > 0 )
	{
	  System.out.println(" Inside peer" );

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



<%
	rci = accList.iterator();
	while (rci.hasNext())
	{
	  int colId = ((Integer)rci.next()).intValue();
	  int access = 2;
	  Column c = (Column)columnsById.get(new Integer(colId));
	  // does it have access for this particular relationship?
	  Integer accInt = (Integer)acc.get("PEER"+":"+colId);
	  if (accInt != null)
	  {
	    access = accInt.intValue();
	  }
%>
	<td><input type="radio" name='<%="PEER"+c.getId()%>' value="ro" onClick="accessClick(<%=c.getId()%>,'PEER', 1)" <%=access==1?"checked":""%>></td>
	<td><input type="radio" name='<%="PEER"+c.getId()%>' value="wr" onClick="accessClick(<%=c.getId()%>,'PEER', 2)" <%=access==2?"checked":""%>></td>
	<td><input type="radio" name='<%="PEER"+c.getId()%>' value="no" onClick="accessClick(<%=c.getId()%>,'PEER', 0)" <%=access==0?"checked":""%>></td>
	<input type="hidden" name=<%="\"" + "PEER_" +c.getId()+ "\""%>  id=<%="\"" +"PEER_"+ c.getId() + "\""%> value="<%=access%>">
	
<%
	}//while
	
%>
	<input type="hidden" name="AllRels" value="PEER">
	</tr>
<%
	}


if ( relationships.size() > 0 )
{
	Enumeration friends = relationships.keys();

	while ( friends.hasMoreElements() )
	{
		String friendRelationship = (String)friends.nextElement();
		Vector friendNhs = (Vector)relationships.get(friendRelationship);
		String friendRelationshipIdStr = friendRelationship.replace (' ' , '_');
		friendRelationshipIdStr = friendRelationshipIdStr.replace('-', '_');

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
<%
	rci = accList.iterator();
	while (rci.hasNext())
	{
	  int colId = ((Integer)rci.next()).intValue();
	  int access = 2;
	  Column c = (Column)columnsById.get(new Integer(colId));
	  // does it have access for this particular relationship?
	  Integer accInt = (Integer)acc.get(friendRelationship+":"+colId);
	  if (accInt != null)
	  {
	    access = accInt.intValue();
	  }
%>
	<td><input type="radio" name='<%=friendRelationshipIdStr+c.getId()%>' value="ro" onClick="accessClick(<%=c.getId()%>,'<%=friendRelationshipIdStr%>', 1)" <%=access==1?"checked":""%>></td>
	<td><input type="radio" name='<%=friendRelationshipIdStr+c.getId()%>' value="wr" onClick="accessClick(<%=c.getId()%>,'<%=friendRelationshipIdStr%>', 2)" <%=access==2?"checked":""%>></td>
	<td><input type="radio" name='<%=friendRelationshipIdStr+c.getId()%>' value="no" onClick="accessClick(<%=c.getId()%>,'<%=friendRelationshipIdStr%>', 0)" <%=access==0?"checked":""%>></td>
	<input type="hidden" name=<%="\"" + friendRelationshipIdStr +"_" +c.getId()+ "\""%>  id=<%="\"" +friendRelationshipIdStr +"_"+ c.getId() + "\""%> value="<%=access%>">
	
<%
	}//while
	
%>
	<input type="hidden" name="AllRels" value="<%=friendRelationship%>">
	</tr>
<%
    }

 }// END OF FRIENDS RELATIONSHIPS HASHTABLE
 %>
		<tr align="center" >
		  <td>Public</td>
		  <td>Others</td>
<%
	rci = accList.iterator();
	while (rci.hasNext())
	{
	  int colId = ((Integer)rci.next()).intValue();
	  int access = 2;
	  Column c = (Column)columnsById.get(new Integer(colId));
	  // does it have access for this particular relationship?
	  Integer accInt = (Integer)acc.get("PUBLIC"+":"+colId);
	  if (accInt != null)
	  {
	    access = accInt.intValue();
	  }
%>
	<td><input type="radio" name='<%="PUBLIC"+c.getId()%>' value="ro" onClick="accessClick(<%=c.getId()%>,'PUBLIC', 1)" <%=access==1?"checked":""%>></td>
	<td><input type="radio" name='<%="PUBLIC"+c.getId()%>' value="wr" onClick="accessClick(<%=c.getId()%>,'PUBLIC', 2)" <%=access==2?"checked":""%>></td>
	<td><input type="radio" name='<%="PUBLIC"+c.getId()%>' value="no" onClick="accessClick(<%=c.getId()%>,'PUBLIC', 0)" <%=access==0?"checked":""%>></td>
	<input type="hidden" name=<%="\"" + "PUBLIC_" +c.getId()+ "\""%>  id=<%="\"" +"PUBLIC_"+ c.getId() + "\""%> value="<%=access%>">
	
<%
	}//while
%>
	<input type="hidden" name="AllRels" value="PUBLIC">

	</tr>
<%	} // END OF RELATIONSHIPS HASHTABLE

%>


              </table>
              <p>&nbsp;</p>
            </td>
          </tr>

<input type="hidden" name="action" value="commitColumnAccess">
<input type="hidden" name="tableId" value="<%=tableId%>">
<input type="hidden" name="tableName" value="<%=tableName%>">
<input type="hidden" name="selNhid" value="<%=privateNhId.getId()%>">
<input type="hidden" name="ViewPreference" value="<%=ViewPreference%>">
<input type="hidden" name="selectedColumnId" value="-1">


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

