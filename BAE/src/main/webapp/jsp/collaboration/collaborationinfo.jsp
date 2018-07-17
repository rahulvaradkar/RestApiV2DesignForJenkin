

   <td width="200" valign="top">
            
             
             <!--start table for info-->
              <table width="160" border="1" cellspacing="0" cellpadding="5" align="center" class="small-text"  bgcolor="#eeeeee">
                <tr valign="top"> 
                  <td class="small-text"><b>Login: </b>
                    <%=userName%>
                    <br>
                    <br>
                    <b>Collaboration :</b> <a href="MyCollaborations?action=editCollab&collabId=<%=collab.getId()%>"><%=collab.getName()%></a>

                    <br>
                    <br>
                 
                    <b>Purpose:</b> <%=collab.getPurpose()%>
                    <br><br>
                 
                     <b>Access:</b>
                            <br><b>Private access :</b> <%=collab.getPrivateAccessLevel()%>  
                            <br><b>Peer Access: </b><%=collab.getPeerAccessLevel()%> 
                            <br><b>Friend Access: </b><%=collab.getFriendAccessLevel()%>                      
                     <br><br>
                 
                     <b>Your Access:</b> <%=collab.getAccessLevel()%>
                    
                      <br><br>
                 
                      <b>Neighborhood:</b> <%=collab.getNeighborhood()%>
					 
              <br>
                    </td>
                </tr>
              </table>
              <!--end table for info-->
            <br>
              </td>

