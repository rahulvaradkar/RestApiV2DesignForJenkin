select DISTINCT(MEMBER.ID), NH0.NAME  NEIGHBORHOOD_NAME, MEMBER.NEIGHBORHOOD_ID
from 
    BW_MEMBER  MEMBER, 
    BW_NH_LEVEL_0  NH0
where 
    MEMBER.USER_ID = ?
    and NH0.NEIGHBORHOOD_ID = MEMBER.NEIGHBORHOOD_ID
union
select DISTINCT(MEMBER.ID), NH1.NAME  NEIGHBORHOOD_NAME, MEMBER.NEIGHBORHOOD_ID
from 
    BW_MEMBER  MEMBER, 
    BW_NH_LEVEL_1  NH1
where 
    MEMBER.USER_ID = ?
    and NH1.NEIGHBORHOOD_ID = MEMBER.NEIGHBORHOOD_ID
union
select DISTINCT(MEMBER.ID), NH2.NAME  NEIGHBORHOOD_NAME, MEMBER.NEIGHBORHOOD_ID
from 
    BW_MEMBER  MEMBER, 
    BW_NH_LEVEL_2  NH2
where 
    MEMBER.USER_ID =?
    and NH2.NEIGHBORHOOD_ID = MEMBER.NEIGHBORHOOD_ID
union
select DISTINCT(MEMBER.ID), NH3.NAME  NEIGHBORHOOD_NAME, MEMBER.NEIGHBORHOOD_ID
from 
    BW_MEMBER  MEMBER, 
    BW_NH_LEVEL_3  NH3
where 
    MEMBER.USER_ID = ?
    and NH3.NEIGHBORHOOD_ID = MEMBER.NEIGHBORHOOD_ID