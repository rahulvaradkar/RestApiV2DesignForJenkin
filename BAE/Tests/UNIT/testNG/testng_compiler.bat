@ECHO OFF
SET CATALINA_HOME=C:\Tomcat8
SET JAVA_HOME="C:\Program Files\Java\jdk1.8.0_171"
SET INSTANCE_NAME=BAE_4_4

ECHO webapps\%INSTANCE_NAME%\WEB-INF\lib
 SET CLASSPATH=.;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\*;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\org.yaml.snakeyaml_1.17.0.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\testng-6.14.3.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\org.hamcrest.core_1.3.0.v201303031735.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\org.apache-extras.beanshell.bsh_2.0.0.b6.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\junit-3.8.1.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\com.beust.jcommander_1.72.0.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\junit-4.12.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\mockito-all-1.9.5.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\commons-io-1.1.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\commons-fileupload-1.1.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\activation.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\mailapi.jar;%CATALINA_HOME%\common\lib\servlet.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\jdbcext.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\jtds-1.2.4.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\msbase.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\msutil.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\classes;%CATALINA_HOME%\lib\servlet-api.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\xerces.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\xercesImpl.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\xmlParserAPIs.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\log4j-1.2.15.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\commons-codec-1.4.jar;%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\lib\mssqlserver.jar
 SET TARGET_DIR=%CATALINA_HOME%\webapps\%INSTANCE_NAME%\WEB-INF\classes

rem %JAVA_HOME%\bin\javac servlets\BWExcelAuthenticationFilter.java -classpath %CLASSPATH% -d %TARGET_DIR%
rem %JAVA_HOME%\bin\javac com\boardwalk\user\UserManager.java -classpath %CLASSPATH% -d %TARGET_DIR%
rem %JAVA_HOME%\bin\javac servlets\httpt_vb_Login.java -classpath %CLASSPATH% -d %TARGET_DIR%
rem %JAVA_HOME%\bin\javac servlets\xlLinkImportService.java -classpath %CLASSPATH% -d %TARGET_DIR%
rem %JAVA_HOME%\bin\javac com\boardwalk\exception\BoardwalkMessages.java -classpath %CLASSPATH% -d %TARGET_DIR%

rem %JAVA_HOME%\bin\javac %CATALINA_HOME%\webapps\%INSTANCE_NAME%\src\testNG\xlLinkImportServiceTest.java -classpath %CLASSPATH% -d %TARGET_DIR% -Xlint
rem %JAVA_HOME%\bin\javac %CATALINA_HOME%\webapps\%INSTANCE_NAME%\src\testNG\DataSet.java -classpath %CLASSPATH% -d %TARGET_DIR% -Xlint
 %JAVA_HOME%\bin\javac %CATALINA_HOME%\webapps\%INSTANCE_NAME%\src\testNG\DataSet.java -classpath %CLASSPATH% -d %TARGET_DIR% -Xlint
 %JAVA_HOME%\bin\javac %CATALINA_HOME%\webapps\%INSTANCE_NAME%\src\testNG\TestRunner2.java -classpath %CLASSPATH% -d %TARGET_DIR% -Xlint
 %JAVA_HOME%\bin\javac %CATALINA_HOME%\webapps\%INSTANCE_NAME%\src\testNG\xlLinkImportServiceTest.java -classpath %CLASSPATH% -d %TARGET_DIR% -Xlint
 
 
 
rem %JAVA_HOME%\bin\javac %CATALINA_HOME%\webapps\%INSTANCE_NAME%\src\test\MockitoRunner2.java -classpath %CLASSPATH% -d %TARGET_DIR% -Xlint

PAUSE
 
@ECHO ON

rem AddUserServletTest '
rem AggreagationServiceServletTest'
rem Bw_Get_Objects_ServletTest
rem Get_Boardwalk_Template_PropServletTest
rem http_vb_getTableInfoServletTest
rem http_vb_getTableNamesServletTest
rem httpt_vb_login_test
rem LoginSevletTest
rem xlCollaborationservletTest
rem xlExportChangeServiceServletTest
rem xlGetQueryListServletTest
rem xlLinkImportServiceServletTest
rem xlLoginServiceTest
rem xlLogoutServiceServletTest
rem xlSqlQueryServletTest