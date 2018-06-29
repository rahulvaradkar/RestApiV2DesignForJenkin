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
	REST_PATH = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\workspace\\"+"${env.JOB_NAME}"+"\\Rest_Reports\\"+"${env.JOB_NAME}"+"\\"+"${env.BUILD_NUMBER}"
    UNIT_PATH = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\workspace\\"+"${env.JOB_NAME}"+"\\Unit_Reports\\"+"${env.JOB_NAME}"+"\\"+"${env.BUILD_NUMBER}"
    MOCKITO_PATH = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\workspace\\"+"${env.JOB_NAME}"+"\\Mockito_Reports\\"+"${env.JOB_NAME}"+"\\"+"${env.BUILD_NUMBER}"
	
	
	REST_REPORT_PATH = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\userContent\\"+"${env.JOB_NAME}"+"\\Rest_Reports\\"+"${env.BUILD_NUMBER}"
	UNIT_REPORT_PATH = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\userContent\\"+"${env.JOB_NAME}"+"\\Unit_Reports\\"+"${env.BUILD_NUMBER}"
	MOCKITO_REPORT_PATH = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\userContent\\"+"${env.JOB_NAME}"+"\\Mockito_Reports\\"+"${env.BUILD_NUMBER}"
	
	MOCKITO_REPORT_PATH2 = "C:\\Windows\\System32\\config\\systemprofile\\.jenkins\\userContent\\"+"${env.JOB_NAME}"+"\\Mockito_Reports\\report"
    
	WAR_BUILD_PATH = "C:/Tomcat8/webapps"
    

	

    }

    stages {
	
	
	
	    
		
		        stage ('War Build Stage') {

            steps {
               echo 'Building .war file'
               //cd to BAE folder and then mvn install.
			 //bat  'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/maveninstall.bat'
			   
             
            }
        }
		
		        stage ('War Deployment Stage') {

            steps {
               echo 'Building .war file'
               //copy war file from jenkins workspace to webapps
		     //bat 'C:\\Users\\p.bhagwat\\AppData\\Roaming\\npm\\node_modules\\qunit-puppeteer\\bin\\sleep5.bat'
			//	bat 'xcopy \"C:/Windows/System32/config/systemprofile/.jenkins/workspace/BAE_TEST/BAE/target/BAE_TEST.war\" \"C:/Tomcat8/webapps\" /E /H /C /R /Q /Y'
			   
              
            }
        }
		
		
		
	    
        stage ('Unit Testing Stage') {

            steps {
               echo 'Unit Test Run'
               //running unit tests through batch file in src\\testng
            //  bat 'C:/Tomcat8/webapps/'+"${env.JOB_NAME}"+'/src/testNG/TestNGRunner.bat'
            }
        }
		
		
		stage ('Mockito Testing Stage') {

            steps {
                //running mockito tests through batch file in src\\test foles
                echo 'Mockito TEST Run'
             //   bat 'C:/Tomcat8/webapps/'+"${env.JOB_NAME}"+'/src/test/MockitoRunner2.bat'
            }
		}
		
		stage ('Rest API Testing Stage') {

            steps {
               echo 'Rest API  Test Run'
			   //running rest api test cases using batch file operating headless chrome simulation.
			 //  bat 'C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/jenkinsrunner2.bat'
			   
			   
			   
            }
        }
		
		
    }
	
	post {
	
        always {
		echo 'Post Script running'
		
	
	//creating xml storage folders for unit,mockito and rest api testing.
	
	////bat 'mkdir '+"${HTML_PATH}" 	
	
	//bat 'mkdir '+"${REST_PATH}"
	//bat 'mkdir '+"${UNIT_PATH}"
	//bat 'mkdir '+"${MOCKITO_PATH}"	
	
	//creating report storage folders for unit,mockito for rest api testing.
	
	//bat 'mkdir '+"${REST_REPORT_PATH}"
	//bat 'mkdir '+"${UNIT_REPORT_PATH}"
	//bat 'mkdir '+"${MOCKITO_REPORT_PATH}"	
	


	//bat 'C:\\Users\\p.bhagwat\\AppData\\Roaming\\npm\\node_modules\\qunit-puppeteer\\bin\\sleep5.bat'
	
	//Copying xml files to Jenkins path 
	
    //    bat 'copy  C:\\Users\\p.bhagwat\\AppData\\Roaming\\npm\\node_modules\\qunit-puppeteer\\bin\\results2.xml '+"${REST_PATH}"+''
	
    //	bat 'xcopy \"C:/Tomcat8/webapps/BAE_4_3_JENKINS_AUTOMATION/Reports/Unit_Test_Reports/junitreports\" '+"${UNIT_PATH}"+' /E /H /C /R /Q /Y'
    
    //	bat 'xcopy  \"C:/Windows/System32/config/systemprofile/.jenkins/userContent/BAE_4_3_JENKINS_AUTOMATION/Mockito_Reports/report\" '+"${MOCKITO_PATH}"+' /E /H /C /R /Q /Y'
    
    //    bat 'C:\\Users\\p.bhagwat\\AppData\\Roaming\\npm\\node_modules\\qunit-puppeteer\\bin\\sleep5.bat'
		
	//Copying Report folders to UserContent

	//	bat 'copy  C:\\Users\\p.bhagwat\\AppData\\Roaming\\npm\\node_modules\\qunit-puppeteer\\bin\\results2.xml '+"${REST_REPORT_PATH}"+''
	
	//    bat 'xcopy \"C:/Tomcat8/webapps/BAE_4_3_JENKINS_AUTOMATION/Reports/Unit_Test_Reports\" '+"${UNIT_REPORT_PATH}"+' /E /H /C /R /Q /Y'
    
    //	bat 'xcopy  \"C:/Windows/System32/config/systemprofile/.jenkins/userContent/BAE_4_3_JENKINS_AUTOMATION/Mockito_Reports/report\" '+"${MOCKITO_REPORT_PATH}"+' /E /H /C /R /Q /Y'
	
	//	bat 'C:\\Users\\p.bhagwat\\AppData\\Roaming\\npm\\node_modules\\qunit-puppeteer\\bin\\sleep5.bat'
        
    //Copying Report folder to webapps    
        
       // step([$class: 'JUnitResultArchiver', testResults: '**results2.xml'])
        
      //  step([$class: 'JUnitResultArchiver', testResults: 'Mockito_Reports/BAE_4_3_JENKINS_AUTOMATION/24/TEST-*.xml'])



      //  junit 'Mockito_Reports/'+"${env.JOB_NAME}"+'/'+"${env.BUILD_NUMBER}"+'/TEST-*.xml'
     //   junit 'Unit_Reports/'+"${env.JOB_NAME}"+'/'+"${env.BUILD_NUMBER}"+'/TEST-*.xml'
     //   junit 'Rest_Reports/'+"${env.JOB_NAME}"+'/'+"${env.BUILD_NUMBER}"+'/results2.xml'
       
      
		
		
			


        }
    }
    

}
