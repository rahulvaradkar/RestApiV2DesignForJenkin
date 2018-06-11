@ECHO OFF
SET CATALINA_HOME=Z:\Tomcat8.5_new
SET JAVA_HOME=C:\jdk1.7.0_21

REM SET CLASSPATH=.;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\commons-io-1.1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\commons-fileupload-1.1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\activation.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\mailapi.jar;%CATALINA_HOME%\common\lib\servlet.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jdbcext.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jtds-0.5.1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jtds-1.2.4.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\msbase.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\msutil.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\classes;%CATALINA_HOME%\lib\servlet-api.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\xerces.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\xercesImpl.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\xmlParserAPIs.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jsch.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\log4j-1.2.15.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\commons-codec-1.4.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\mssqlserver.jar

SET CLASSPATH=.;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\activation.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\aopalliance-repackaged-2.5.0-b42.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\cdi-api-1.1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\com.fasterxml.jackson.annotations.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\com.fasterxml.jackson.core.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\com.fasterxml.jackson.databind.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\commons-codec-1.4.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\commons-fileupload-1.1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\commons-io-1.1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\el-api-2.2.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\gson-2.2.4.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\hk2-api-2.5.0-b42.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\hk2-locator-2.5.0-b42.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\hk2-utils-2.5.0-b42.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jackson-annotations-2.4.5.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jackson-core-2.4.5.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jackson-databind-2.4.5.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jackson-dataformat-xml-2.4.5.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jackson-dataformat-yaml-2.4.5.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jackson-datatype-joda-2.4.5.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jackson-jaxrs-base-2.4.5.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jackson-jaxrs-json-provider-2.4.5.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jackson-module-jaxb-annotations-2.4.5.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\javassist-3.22.0-CR2.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\javax.annotation-api-1.2.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\javax.inject-1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\javax.inject-2.5.0-b42.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\javax.json-1.1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\javax.json-api-1.1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\javax.json.bind-api-1.0.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\javax.servlet-api-3.0.1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\javax.ws.rs-api-2.1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jaxb-api-2.2.7.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jboss-interceptors-api_1.1_spec-1.0.0.Beta1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jdbcext.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jersey-client.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jersey-common.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jersey-container-servlet-core.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jersey-container-servlet.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jersey-hk2.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jersey-media-jaxb.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jersey-media-json-binding.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jersey-media-multipart-2.19.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jersey-media-sse.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jersey-server.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jndi.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\joda-time-2.2.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jspbook.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jsr250-api-1.0.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\jtds-1.2.4.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\mail.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\mailapi.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\msbase.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\mssqlserver.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\msutil.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\org.osgi.core-4.2.0.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\osgi-resource-locator-1.0.1.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\persistence-api-1.0.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\swagger-annotations-1.5.7.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\swagger-core-1.5.7.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\swagger-jaxrs-1.5.7.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\swagger-models-1.5.7.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\validation-api-1.1.0.Final.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\xerces.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\xercesImpl.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\xmlParserAPIs.jar;%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\lib\yasson-1.0.jar;%CATALINA_HOME%\lib\servlet-api.jar;
SET TARGET_DIR=%CATALINA_HOME%\webapps\BAE_4_3\WEB-INF\classes

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
%JAVA_HOME%\bin\javac boardwalk\rest\*.java -classpath %CLASSPATH% -d %TARGET_DIR%

%JAVA_HOME%\bin\javac io\swagger\model\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac io\swagger\api\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac io\swagger\api\factories\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
%JAVA_HOME%\bin\javac io\swagger\api\impl\*.java -classpath %CLASSPATH% -d %TARGET_DIR%
PAUSE

@ECHO ON

PAUSE

%JAVA_HOME%\bin\javac servlets\*.java -classpath %CLASSPATH%  -d  %TARGET_DIR%


@ECHO ON