@ECHO OFF
SET INSTANCE_NAME=BAE_4_4

chdir /d C:\Tomcat8\webapps\%INSTANCE_NAME%\src\testNG

SET CATALINA_HOME=C:\Tomcat8
SET JAVA_HOME="C:\Program Files\Java\jdk1.8.0_171"

SET CLASSPATH=.;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\*;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\mockito-all-1.9.5.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\commons-io-1.1.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\commons-fileupload-1.1.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\activation.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\mailapi.jar;%CATALINA_HOME%\common\lib\servlet.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\jdbcext.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\jtds-0.5.1.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\jtds-1.2.4.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\msbase.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\msutil.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\classes;%CATALINA_HOME%\lib\servlet-api.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\xerces.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\xercesImpl.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\xmlParserAPIs.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\jsch.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\log4j-1.2.15.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\commons-codec-1.4.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\mssqlserver.jar
SET TARGET_DIR=%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\classes

rem %JAVA_HOME%\bin\javac servlets\BWExcelAuthenticationFilter.java -classpath %CLASSPATH% -d %TARGET_DIR%
rem %JAVA_HOME%\bin\javac com\boardwalk\user\UserManager.java -classpath %CLASSPATH% -d %TARGET_DIR%
rem %JAVA_HOME%\bin\javac servlets\httpt_vb_Login.java -classpath %CLASSPATH% -d %TARGET_DIR%
rem %JAVA_HOME%\bin\javac servlets\xlLinkImportService.java -classpath %CLASSPATH% -d %TARGET_DIR%
rem %JAVA_HOME%\bin\javac com\boardwalk\exception\BoardwalkMessages.java -classpath %CLASSPATH% -d %TARGET_DIR%

rem %JAVA_HOME%\bin\javac %CATALINA_HOME%\webapps\%INSTANCE_NAME%\src\testNG\xlLinkImportServiceTest.java -classpath %CLASSPATH% -d %TARGET_DIR%


%JAVA_HOME%\bin\java -cp %CLASSPATH% testNG.TestRunner2

PAUSE

@ECHO ON