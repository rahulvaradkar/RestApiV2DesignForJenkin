   <td width="200" valign="top">
            
             
             <!--start table for info-->
              <table width="160" border="1" cellspacing="0" cellpadding="5" align="center" class="small-text"  bgcolor="#eeeeee">
                <tr valign="top"> 
                  <td class="small-text"><b>Login: </b>
                    <%=userName%>
                    <br>
                    <br>
                    <b>Collaboration :</b> <a href="MyCollaborations?action=editCollab&collabId=<%=tbi.getCollaborationId()%>"> <%=tbi.getCollaborationName()%> </a>
                    <br>
                    <br>
                 
                    <b>Whiteboard:</b><a href="Whiteboard?wbid=<%=tbi.getWhiteboardId()%>&action=edit&collabId=<%=tbi.getCollaborationId()%>"> <%=tbi.getWhiteboardName()%> </a>
                    <br><br>
                                      
                    
                    <b>Table Purpose:</b> <%=tbi.getTablePurpose()%>
                    <br><br>
                 
                     <b>Access:</b>
                            <br><b>Private:</b> <%=tbi.getTablePrivateAccessLevel()%>  
                            <br><b>Peer: </b><%=tbi.getTablePeerAccessLevel()%> 
                            <br><b>Friend: </b><%=tbi.getTableFriendAccessLevel()%>                      
                     <br><br>
                 
                     <b>Your Access:</b> <%=tbi.getTableAccessLevel()%>
                    
                      <br><br>
                 
                      <b>Neighborhood:</b> <%=tbi.getNeighborhood()%>
					 
              <br>
                    </td>
                </tr>
              </table>
              <!--end table for info-->
            <br>
              </td>
