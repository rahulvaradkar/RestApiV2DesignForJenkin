//'* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//'* Module  :    Jenkins Pipeline Steps
//'* Description : This is a pipeline script file includes three stages - compile,test,deploy,notification.
//'* Created :    18-05-2018 17:07
// Pranav Bhagwat
//'* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 

pipeline {
    agent any
	
	environment {
    ENV_NAME = "${env.BRANCH_NAME}"
	ENV_BUILD_NO = "${env.BUILD_NUMBER}"
	JENKINS_URL = "${env.JENKINS_URL}"
	JOB_NAME = "${env.JOB_NAME}"
	JENKINS_HOME = "${env.JENKINS_HOME}"
	
	HTML_PATH = "C:\\Tomcat8\\webapps\\"+"${env.JOB_NAME}"+"\\Reports\\"+"${env.JOB_NAME}"+"\\"+"${env.BUILD_NUMBER}"
	REST_PATH = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\workspace\\"+"${env.JOB_NAME}"+"\\Reports\\Rest_Reports\\"+"${env.JOB_NAME}"+"\\"+"${env.BUILD_NUMBER}"
    UNIT_PATH = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\workspace\\"+"${env.JOB_NAME}"+"\\Reports\\Unit_Reports\\"+"${env.JOB_NAME}"+"\\"+"${env.BUILD_NUMBER}"
    MOCKITO_PATH = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\workspace\\"+"${env.JOB_NAME}"+"\\Reports\\Mockito_Reports\\"+"${env.JOB_NAME}"+"\\"+"${env.BUILD_NUMBER}"
	ZIP_STORAGE_PATH = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\workspace\\"+"${env.JOB_NAME}"+"\\Reports\\ZIPS\\"+"${env.JOB_NAME}"+"\\"+"${env.BUILD_NUMBER}" 
	ZIP_STORAGE_PATH2 = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\workspace\\"+"${env.JOB_NAME}"+"\\Reports\\ZIPS\\"+"${env.JOB_NAME}"+"\\"+"${env.BUILD_NUMBER}"+"\\nonsrc" 
	
	
	REST_REPORT_PATH = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\userContent\\"+"${env.JOB_NAME}"+"\\Rest_Reports\\"+"${env.BUILD_NUMBER}"
	UNIT_REPORT_PATH = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\userContent\\"+"${env.JOB_NAME}"+"\\Unit_Reports\\"+"${env.BUILD_NUMBER}"
	MOCKITO_REPORT_PATH = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\userContent\\"+"${env.JOB_NAME}"+"\\Mockito_Reports\\"+"${env.BUILD_NUMBER}"
	
	MOCKITO_REPORT_PATH2 = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\userContent\\"+"${env.JOB_NAME}"+"\\Mockito_Reports\\report"
    
	WAR_BUILD_PATH = "C:\\Tomcat8\\webapps"

    }

    stages {
	
		        stage ('War Build Stage') {

            steps {
               echo 'Building .war file'
               //cd to BAE folder and then mvn install.
			 
			bat 'mkdir '+"${ZIP_STORAGE_PATH2}"  
			bat  'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/maveninstall.bat'
			bat 'copy  C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\workspace\\BAE_TEST\\BAE\\target\\BAE_4_4.war '+"${ZIP_STORAGE_PATH2}"+''
			 
			bat 'C:\\Users\\p.bhagwat\\AppData\\Roaming\\npm\\node_modules\\qunit-puppeteer\\bin\\BAE_4_4\\copysrc.bat'
			 
			bat  'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/maveninstall.bat'
			   
              
            }
        }
		
		        stage ('War Deployment Stage') {

            steps {
               echo 'Deploying .war file'
               //copy war file from jenkins workspace to webapps
            bat 'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/sleep5.bat'

   
    		bat  'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/deploybae4_4.bat'
			   
			   
			 bat 'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/sleep30.bat'
			 
			  bat  'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/deployprops.bat'
			 
			  bat 'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/sleep5.bat'
			 
			 bat  'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/deployrest.bat'
			 
			  bat 'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/sleep5.bat'
			   
			 bat  'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/deployunit.bat'
			 
			  bat 'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/sleep5.bat'
			   
			 bat  'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/deploymockito.bat'
			 
			  bat 'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/sleep5.bat'
			   
              
            }
        }
		
		
				stage ('Test Resources compilation Stage') {

            steps {
               echo 'Test resources being compiled'
               //compile testNGRunner and MockitoTestRunner

				bat 'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/sleep5.bat'
				
			     bat 'C:/Tomcat8/webapps/BAE_4_4/src/testNG/testng_compiler.bat'
				 
				 bat 'C:/Tomcat8/webapps/BAE_4_4/src/test/mockito_compiler.bat'
				 
				 
			   
              
            }
        }
		
		
	    
        stage ('Unit Testing Stage') {

            steps {
               echo 'Unit Test Run'
               bat 'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/sleep5.bat'
               //running unit tests through batch file in src\\testng
              bat 'C:/Tomcat8/webapps/BAE_4_4/src/testNG/TestNGRunner.bat'
            }
        }
		
		
		stage ('Mockito Testing Stage') {

            steps {
                //running mockito tests through batch file in src\\test foles
                echo 'Mockito TEST Run'
                bat 'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/sleep5.bat'
                bat 'C:/Tomcat8/webapps/BAE_4_4/src/test/MockitoRunner2.bat'
            }
		}
		
		stage ('Rest API Testing Stage') {

            steps {
               echo 'Rest API  Test Run'
               bat 'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/sleep5.bat'
			   //running rest api test cases using batch file operating headless chrome simulation.
			   bat 'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/jenkinsrunnerTEST_4_4.bat'
			   
			   
			   
            }
        }
		
		stage ('Documentation Zipping Stage') {

            steps {
               echo 'Documents zip creation started'
               
			   bat 'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4/zip_docs_creation.bat'
			   
			   
			   
            }
        }
		
		
    }
	
	post {
	
        always {
		echo 'Post Script running'
		
	
	//creating xml storage folders for unit,mockito and rest api testing.
	
	////bat 'mkdir '+"${HTML_PATH}" 	
	
	bat 'mkdir '+"${REST_PATH}"
	bat 'mkdir '+"${UNIT_PATH}"
	bat 'mkdir '+"${MOCKITO_PATH}"
	//bat 'mkdir '+"${ZIP_STORAGE_PATH}"
	
	
	//creating report storage folders for unit,mockito for rest api testing.
	
	bat 'mkdir '+"${REST_REPORT_PATH}"
	bat 'mkdir '+"${UNIT_REPORT_PATH}"
	bat 'mkdir '+"${MOCKITO_REPORT_PATH}"	
	


	bat 'C:\\Users\\p.bhagwat\\AppData\\Roaming\\npm\\node_modules\\qunit-puppeteer\\bin\\BAE_4_4\\sleep5.bat'
	
	//Copying xml files to Jenkins path 
	
        bat 'copy  C:\\Users\\p.bhagwat\\AppData\\Roaming\\npm\\node_modules\\qunit-puppeteer\\bin\\BAE_4_4\\resultstest4_4.xml '+"${REST_PATH}"+''
	
    	bat 'xcopy \"C:/Tomcat8/webapps/BAE_4_4/Reports/Unit_Test_Reports/junitreports\" '+"${UNIT_PATH}"+' /E /H /C /R /Q /Y'
    
		bat 'xcopy \"C:/Tomcat8/webapps/BAE_4_4/Reports/Mockito_Test_Reports/junitreports\" '+"${MOCKITO_PATH}"+' /E /H /C /R /Q /Y'
	
    //	bat 'xcopy  \"C:/Windows/System32/config/systemprofile/.jenkins/userContent/BAE_TEST/Mockito_Reports/report\" '+"${MOCKITO_PATH}"+' /E /H /C /R /Q /Y'
    
        bat 'C:\\Users\\p.bhagwat\\AppData\\Roaming\\npm\\node_modules\\qunit-puppeteer\\bin\\BAE_4_4\\sleep5.bat'
	
	//Copying zip files folders to Reports folder
	
	bat 'copy  C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\workspace\\BAE_TEST\\BAE\\target\\BAE_4_4.war '+"${ZIP_STORAGE_PATH}"+''
	
	bat 'copy  C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\workspace\\BAE_TEST\\BAE\\target\\Documents.zip '+"${ZIP_STORAGE_PATH}"+''
	
	bat 'copy  C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\workspace\\BAE_TEST\\BAE\\target\\SQLScripts.zip '+"${ZIP_STORAGE_PATH}"+''
	
	bat 'copy  C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\workspace\\BAE_TEST\\BAE\\target\\Templates.zip '+"${ZIP_STORAGE_PATH}"+''
	
	//Copying Report folders to UserContent

		bat 'copy  C:\\Users\\p.bhagwat\\AppData\\Roaming\\npm\\node_modules\\qunit-puppeteer\\bin\\BAE_4_4\\resultstest4_4.xml '+"${REST_REPORT_PATH}"+''
	
	    bat 'xcopy \"C:/Tomcat8/webapps/BAE_4_4/Reports/Unit_Test_Reports\" '+"${UNIT_REPORT_PATH}"+' /E /H /C /R /Q /Y'
		
		bat 'xcopy \"C:/Tomcat8/webapps/BAE_4_4/Reports/Mockito_Test_Reports\" '+"${MOCKITO_REPORT_PATH}"+' /E /H /C /R /Q /Y'
    
		bat 'C:\\Users\\p.bhagwat\\AppData\\Roaming\\npm\\node_modules\\qunit-puppeteer\\bin\\BAE_4_4\\sleep5.bat'
        
	



        junit 'Reports/Mockito_Reports/'+"${env.JOB_NAME}"+'/'+"${env.BUILD_NUMBER}"+'/TEST-*.xml'
        junit 'Reports/Unit_Reports/'+"${env.JOB_NAME}"+'/'+"${env.BUILD_NUMBER}"+'/TEST-*.xml'
        junit 'Reports/Rest_Reports/'+"${env.JOB_NAME}"+'/'+"${env.BUILD_NUMBER}"+'/resultstest4_4.xml'
		
		//archiveArtifacts artifacts: 'BAE/target/*.war', fingerprint: true
		archiveArtifacts artifacts: 'Reports/ZIPS/'+"${env.JOB_NAME}"+'/'+"${env.BUILD_NUMBER}"+'/BAE_4_4.war', fingerprint: true
		archiveArtifacts artifacts: 'Reports/ZIPS/'+"${env.JOB_NAME}"+'/'+"${env.BUILD_NUMBER}"+'/nonsrc/BAE_4_4.war', fingerprint: true
		archiveArtifacts artifacts: 'Reports/ZIPS/'+"${env.JOB_NAME}"+'/'+"${env.BUILD_NUMBER}"+'/Templates.zip', fingerprint: true
		archiveArtifacts artifacts: 'Reports/ZIPS/'+"${env.JOB_NAME}"+'/'+"${env.BUILD_NUMBER}"+'/Documents.zip', fingerprint: true
		archiveArtifacts artifacts: 'Reports/ZIPS/'+"${env.JOB_NAME}"+'/'+"${env.BUILD_NUMBER}"+'/SQLScripts.zip', fingerprint: true
		
		
		
		
       
      
						echo 'Report Mail sending'
            script {
                    emailext subject: '$DEFAULT_SUBJECT',    
			body: "Click the link below to show REST API Testing Results for your current build :  \n"+JENKINS_URL+"blue/organizations/jenkins/"+JOB_NAME+"/detail/"+JOB_NAME+"/activity/"+"\n\n\n Click the link below to show Mockito TestNG Results for your current build :  \n"+" https://bwrestapiqa.boardwalktech.com:8443/jenkins/userContent/"+JOB_NAME+"/Mockito_Reports/"+ENV_BUILD_NO+"/index.html" +"\n\n\n Click the link below to show Unit Testing Results for your current build :  \n"+" https://bwrestapiqa.boardwalktech.com:8443/jenkins/userContent/"+JOB_NAME+"/Unit_Reports/"+ENV_BUILD_NO+"/index.html" ,
                        attachLog: true,
                        replyTo: '$DEFAULT_REPLYTO',
                        to: '$DEFAULT_RECIPIENTS'          	
            }
		
		
			


        }
    }
    

}
