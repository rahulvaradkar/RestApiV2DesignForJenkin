<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.neighborhood.*,java.io.*, java.util.*" %> 

<%
Vector nhTree = (Vector)request.getAttribute("nhTree");
request.setAttribute("heading", "Neighborhood Tree");
%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>


<tr> 
    <td>
        <!--start main page table-->
        <table width="680" border="0" cellspacing="0" cellpadding="0" align="center" class="body">

          <tr> 
            <td valign="top">
                <!--start main page table 2 -->

		<table width="500" border="0" cellspacing="0" cellpadding="0" align="center" valign="top">
                    <tr valign="top" bgcolor="#FFCC66"> 
                      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
                    </tr>
				
                    <tr valign="top"> 
                        <td class="body" align=center> 
                            <br>
                                
                            <br>
                        <table width="450" border="0" cellspacing="2" cellpadding="2" align="center" class="body">
                                <form method="post" action="BW_Neighborhoods">
                                <!--start table for table-->
	<tr bgcolor="#fddeb9" height=20> 
       		<td class="body"> <b>Select </b></td>
                <td class="body"> <b>Name </b></td>                
                      </tr>

                            
<%
    // iterate over the neighborhood tree
    Iterator nhIter = nhTree.iterator();
    while (nhIter.hasNext()) {
	NHTree nht = (NHTree)nhIter.next();
	int nhid = nht.getNeighborhood().getId();
	String nhName = nht.getName();
System.out.println("Level 0 : " + nhName);
	// Print out the level 0 Neighborhoods
%>
	<tr bgcolor="#eeeeee" valign="top"> 
	  <td body="class" > 
	    <input onClick="javascript:checkBoxCheck(this)" type="checkbox" name="selNhid" value="<%=nhid%>">
	    </td>
	  <td body="class">
	   <%=nhName%>
	  </td>
	</tr>
<%
	Vector nh1Tree = nht.getChildren();
	// iterate over the neighborhood tree
	Iterator nh1Iter = nh1Tree.iterator();
	while (nh1Iter.hasNext()) {
	    NHTree nh1t = (NHTree)nh1Iter.next();
	    int nh1id = nh1t.getNeighborhood().getId();
	    String nh1Name = nh1t.getName();
	    // Print out the level 1 Neighborhoods

System.out.println("  Level 1 : " + nhName);
    %>
	    <tr bgcolor="white" valign="top"> 
	      <td body="class" >
		<input onClick="javascript:checkBoxCheck(this)" type="checkbox" name="selNhid" value="<%=nh1id%>">
		</td>
	      <td body="class">
	     &nbsp;&nbsp;&nbsp;&nbsp; <%=nh1Name%>
	   
	      </td>
	    </tr>
    <%
	    Vector nh2Tree = nh1t.getChildren();
	    // iterate over the neighborhood tree
	    Iterator nh2Iter = nh2Tree.iterator();
	    while (nh2Iter.hasNext()) {
		NHTree nh2t = (NHTree)nh2Iter.next();
		int nh2id = nh2t.getNeighborhood().getId();
		String nh2Name = nh2t.getName();
		// Print out the level 2 Neighborhoods

System.out.println("    Level 2 : " + nhName);
	%>
		<tr bgcolor="#eeeeee" valign="top"> 
		  <td body="class"> 
		    <input onClick="javascript:checkBoxCheck(this)" type="checkbox" name="selNhid" value="<%=nh2id%>">
		    </td>
		  <td body="class">
		 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  <%=nh2Name%>
		  </td>
		</tr>
	    <%
		Vector nh3Tree = nh2t.getChildren();
		// iterate over the neighborhood tree
		Iterator nh3Iter = nh3Tree.iterator();
		while (nh3Iter.hasNext()) {
		    NHTree nh3t = (NHTree)nh3Iter.next();
		    int nh3id = nh3t.getNeighborhood().getId();
		    String nh3Name = nh3t.getName();
		    // Print out the level 3 Neighborhoods

System.out.println("      Level 3 : " + nhName);
	    %>
		    <tr bgcolor="white" valign="top"> 
		      <td body="class"> 
			<input onClick="javascript:checkBoxCheck(this)" type="checkbox" name="selNhid" value="<%=nh3id%>">
			</td>
		      <td body="class">
		      
		     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      <%=nh3Name%>

		      </td>
		    </tr>
<%
    } //while NH3
%>
<%
    } //while NH2
%>
<%
    } //while NH1
%>
<%
    } //while NH0
%>

            <tr bgcolor="#cccccc"> 
                        <td class="body" colspan="2" align="center"> 
                          <b>

               <a href="javascript:setActionAndSubmit('createNH')">Create New</a> |
              <a href="javascript:checkCheckBoxsetActionAndSubmit('createNH')">Create Child</a> | 
	      <a href="javascript:checkCheckBoxsetActionAndSubmit('membersNH')">Memberships</a> | 
	      <a href="javascript:checkCheckBoxsetActionAndSubmit('deleteNH')">Delete 
                        </td>
            </tr> 
            <input type="hidden" name="action" value="">	
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
  <tr bgcolor="#000000"> 
    <td colspan="2" height="10"><img src="images/clear.gif" width="2" height="10"></td>
  </tr>
<br>
<%@include file='/jsp/common/footer.jsp' %>
