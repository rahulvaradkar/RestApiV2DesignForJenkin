@ECHO OFF
cd "C:\Program Files\Apache Software Foundation\Tomcat 8.0\webapps\BAE_4_3_TAS\src\testNG\"
SET CATALINA_HOME="C:\Program Files\Apache Software Foundation\Tomcat 8.0"
SET JAVA_HOME="C:\Program Files\Java\jdk1.8.0_121"

SET CLASSPATH=.;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\*;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\junit-4.12.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\mockito-all-1.9.5.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\commons-io-1.1.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\commons-fileupload-1.1.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\activation.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\mailapi.jar;%CATALINA_HOME%\common\lib\servlet.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\jdbcext.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\jtds-0.5.1.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\jtds-1.2.4.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\msbase.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\msutil.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\classes;%CATALINA_HOME%\lib\servlet-api.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\xerces.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\xercesImpl.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\xmlParserAPIs.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\jsch.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\log4j-1.2.15.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\commons-codec-1.4.jar;%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\lib\mssqlserver.jar
SET TARGET_DIR=%CATALINA_HOME%\webapps\BAE_4_3_TAS\WEB-INF\classes

rem %JAVA_HOME%\bin\javac servlets\BWExcelAuthenticationFilter.java -classpath %CLASSPATH% -d %TARGET_DIR%
rem %JAVA_HOME%\bin\javac com\boardwalk\user\UserManager.java -classpath %CLASSPATH% -d %TARGET_DIR%
rem %JAVA_HOME%\bin\javac servlets\httpt_vb_Login.java -classpath %CLASSPATH% -d %TARGET_DIR%
rem %JAVA_HOME%\bin\javac servlets\xlLinkImportService.java -classpath %CLASSPATH% -d %TARGET_DIR%
rem %JAVA_HOME%\bin\javac com\boardwalk\exception\BoardwalkMessages.java -classpath %CLASSPATH% -d %TARGET_DIR%
rem %JAVA_HOME%\bin\javac %CATALINA_HOME%\webapps\BAE_4_3_TAS\src\testNG\xlLinkImportServiceTest.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\java -cp %CLASSPATH% testNG.TestRunner4

PAUSE

@ECHO ON