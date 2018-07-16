@echo off

SET JAVA_HOME=C:\jdk1.7.0_21
SET DEPLOY_TOMCAT_HOME=C:\Tomcat7

SET CLASSPATH=.;%DEPLOY_TOMCAT_HOME%\webapps\BAE_4_4\WEB-INF\lib\xerces.jar;%DEPLOY_TOMCAT_HOME%\webapps\BAE_4_4\WEB-INF\lib\commons-io-1.1.jar;%DEPLOY_TOMCAT_HOME%\webapps\BAE_4_4\WEB-INF\lib\commons-fileupload-1.1.jar;%DEPLOY_TOMCAT_HOME%\webapps\BAE_4_4\WEB-INF\lib\activation.jar;%DEPLOY_TOMCAT_HOME%\webapps\BAE_4_4\WEB-INF\lib\mailapi.jar;%DEPLOY_TOMCAT_HOME%\webapps\BAE_4_4\WEB-INF\lib\mail.jar;%DEPLOY_TOMCAT_HOME%\common\lib\servlet-api.jar;%DEPLOY_TOMCAT_HOME%\webapps\BAE_4_4\WEB-INF\lib\jdbcext.jar;%DEPLOY_TOMCAT_HOME%\webapps\BAE_4_4\WEB-INF\lib\jtds-1.2.4.jar;%DEPLOY_TOMCAT_HOME%\webapps\BAE_4_4\WEB-INF\lib\mssqlserver.jar;%DEPLOY_TOMCAT_HOME%\webapps\BAE_4_4\WEB-INF\lib\msbase.jar;%DEPLOY_TOMCAT_HOME%\webapps\BAE_4_4\WEB-INF\lib\msutil.jar;%DEPLOY_TOMCAT_HOME%\webapps\BAE_4_4\WEB-INF\classes;%DEPLOY_TOMCAT_HOME%\webapps\BAE_4_4\WEB-INF\lib;%DEPLOY_TOMCAT_HOME%\webapps\BAE_4_4\WEB-INF\lib\jsch.jar

set DATESTAMP=%DATE:~10,4%/%DATE:~4,2%/%DATE:~7,2%
set TIMESTAMP=%TIME:~0,2%:%TIME:~3,2%:%TIME:~6,2%
set DATEANDTIME=%DATESTAMP%%TIMESTAMP%

REM --REPLACE 'XYZ' WITH YOUR TABLE NAME
echo %DATEANDTIME% PS for Table XYZ - Start >> ..\PeriodicShift\PSLogs.txt

REM --REPLACE THE APPLICATION NAME WITH YOUR APPLICATION
%JAVA_HOME%\bin\java -server -Xms512M -Xmx512M  -classpath %CLASSPATH%  boardwalk.table.APP_PeriodicColumnManager C:\Tomcat7\webapps\BAE_4_4\props\boardwalk.properties 2000003 State2 State3 " 2000005 " FALSE FALSE >> ..\PeriodicShift\PSLogs.txt

REM --REPLACE 'XYZ' WITH YOUR TABLE NAME
echo %DATEANDTIME% PS for Table XYZ - End >> ..\PeriodicShift\PSLogs.txt

echo %JAVA_HOME% 
echo %DEPLOY_TOMCAT_HOME%

PAUSE
@echo on
