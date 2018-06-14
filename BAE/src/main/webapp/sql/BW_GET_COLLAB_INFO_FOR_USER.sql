SELECT COLLAB.ID , COLLAB.NAME, COLLAB.PURPOSE, COLLAB.IS_ACTIVE, COLLAB.PRIVATE_ACCESS ACCESS_, COLLAB.PRIVATE_ACCESS, COLLAB.PEER_ACCESS, COLLAB.FRIEND_ACCESS, COLLAB_NH.NAME   COLLAB_NH_NAME
FROM BW_COLLAB   COLLAB, BW_NH   MY_NH, BW_MEMBER   MEMBER, BW_NH   COLLAB_NH
WHERE
      COLLAB.MEMBER_ID = MEMBER.ID
        AND MEMBER.USER_ID = ?
       AND  COLLAB.ID = ?
	AND COLLAB.NEIGHBORHOOD_ID = MY_NH.ID
	AND MY_NH.ID = MEMBER.NEIGHBORHOOD_ID
	AND COLLAB_NH.ID = COLLAB.NEIGHBORHOOD_ID

