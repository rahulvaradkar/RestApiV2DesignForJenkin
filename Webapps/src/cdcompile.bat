@ECHO OFF
SET CATALINA_HOME=Z:\Tomcat8.5_new
SET JAVA_HOME=C:\jdk1.7.0_21

SET CLASSPATH=.;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\commons-io-1.1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\commons-fileupload-1.1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\activation.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\mailapi.jar;%CATALINA_HOME%\common\lib\servlet.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jdbcext.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jtds-0.5.1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jtds-1.2.4.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\msbase.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\msutil.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\classes;%CATALINA_HOME%\lib\servlet-api.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\xerces.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\xercesImpl.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\xmlParserAPIs.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jsch.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\log4j-1.2.15.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\commons-codec-1.4.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\mssqlserver.jar
SET TARGET_DIR=%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\classes

%JAVA_HOME%\bin\javac servlets\*.java -classpath %CLASSPATH%  -d  %TARGET_DIR%
REM PAUSE

%JAVA_HOME%\bin\javac com\boardwalk\neighborhood\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac com\boardwalk\collaboration\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac com\boardwalk\exception\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac com\boardwalk\query\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac com\boardwalk\user\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac com\boardwalk\whiteboard\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac com\boardwalk\excel\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac com\boardwalk\distribution\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
REM %JAVA_HOME%\bin\javac com\boardwalk\integration\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac com\boardwalk\database\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac com\boardwalk\member\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac com\boardwalk\table\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac com\boardwalk\wizard\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac com\boardwalk\util\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
REM PAUSE

%JAVA_HOME%\bin\javac boardwalk\connection\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac boardwalk\table\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac boardwalk\collaboration\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac boardwalk\neighborhood\*.java -classpath %CLASSPATH% -d %TARGET_DIR% 
%JAVA_HOME%\bin\javac boardwalk\common\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
PAUSE

@ECHO ON