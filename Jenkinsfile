//'* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//'* Module  		: Jenkins Pipeline Steps
//'* Description 	: This is a pipeline script file includes eight stages 
//'*1.Environment setup Stage
//'*2.War Build Stage
//'*3.War Deployment Stage
//'*4.Test Resources compilation Stage
//'*5.Unit Testing Stage
//'*6.Mockito Testing Stage
//'*7.REST API Testing Stage
//'*8.Resource Packaging Stage
//'* Created 		: 07-27-2018 12:01
//'* Pranav Bhagwat
//'* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 

pipeline {
    agent any
	
	environment {
	ENV_BUILD_NO = "${env.BUILD_NUMBER}"
	JENKINS_URL = "${env.JENKINS_URL}"
	JOB_NAME = "${env.JOB_NAME}"
	JENKINS_HOME = "${env.JENKINS_HOME}"
	GIT_BRANCH = "${env.GIT_BRANCH}" 
	
	JJOB_NAME = "BAE_4_4"

	REST_PATH = "${env.WORKSPACE}"+"\\Reports\\Rest_Reports\\"+"${JJOB_NAME}"+"-"+"${env.GIT_BRANCH}"+"\\"+"${env.BUILD_NUMBER}"	
    UNIT_PATH = "${env.WORKSPACE}"+"\\Reports\\Unit_Reports\\"+"${JJOB_NAME}"+"-"+"${env.GIT_BRANCH}"+"\\"+"${env.BUILD_NUMBER}"
    MOCKITO_PATH = "${env.WORKSPACE}"+"\\Reports\\Mockito_Reports\\"+"${JJOB_NAME}"+"-"+"${env.GIT_BRANCH}"+"\\"+"${env.BUILD_NUMBER}"
	
	ZIP_STORAGE_PATH = "${env.WORKSPACE}"+"\\Reports\\ZIPS\\"+"${JJOB_NAME}"+"-"+"${env.GIT_BRANCH}"+"\\"+"${env.BUILD_NUMBER}" 
	ZIP_STORAGE_PATH2 = "${env.WORKSPACE}"+"\\Reports\\ZIPS\\"+"${JJOB_NAME}"+"-"+"${env.GIT_BRANCH}"+"\\"+"${env.BUILD_NUMBER}"+"\\nonsrc" 
	
	REST_REPORT_PATH = "${env.JENKINS_HOME}"+"\\userContent\\"+"${JJOB_NAME}"+"-"+"${env.GIT_BRANCH}"+"\\Rest_Reports\\"+"${env.BUILD_NUMBER}"
	UNIT_REPORT_PATH = "${env.JENKINS_HOME}"+"\\userContent\\"+"${JJOB_NAME}"+"-"+"${env.GIT_BRANCH}"+"\\Unit_Reports\\"+"${env.BUILD_NUMBER}"
	MOCKITO_REPORT_PATH = "${env.JENKINS_HOME}"+"\\userContent\\"+"${JJOB_NAME}"+"-"+"${env.GIT_BRANCH}"+"\\Mockito_Reports\\"+"${env.BUILD_NUMBER}"
    
	WEBAPPS_PATH = "C:/Tomcat8/webapps"
	WEBAPPS_PATH2 = "C:\\Tomcat8\\webapps"
	WEBAPP_INSTANCE_NAME = "BAE_4_4"
	WEBAPP_INSTANCE_PATH = "${WEBAPPS_PATH}" + "/" + "${WEBAPP_INSTANCE_NAME}"
	WEBAPP_INSTANCE_PATH2 = "${WEBAPPS_PATH2}" + "\\" + "${WEBAPP_INSTANCE_NAME}"
	
	
	BATCH_PATH = "C:/Users/p.bhagwat/AppData/Roaming/npm/node_modules/qunit-puppeteer/bin/BAE_4_4_multi/"
	BATCH_PATH2 = "C:\\Users\\p.bhagwat\\AppData\\Roaming\\npm\\node_modules\\qunit-puppeteer\\bin\\BAE_4_4_multi\\"
	
	CODE_EDIT = "nochange"
	DOC_EDIT = "nochange"
	J_EDIT = ""
	TRIGGER_CAUSE = ""
	
	
    }

    stages {
	
	
			   	   stage ('Environment setup Stage') {
          
				steps { 
					echo 'changed data       '+currentBuild.changeSets
					echo 'Env build number   ' +"${ENV_BUILD_NO}"
					echo 'Jenkins URL        ' +"${JENKINS_URL}"
					echo 'JOB NAME   		 ' +"${JOB_NAME}"
					echo 'JENKINS HOME   	 ' +"${JENKINS_HOME}"
					echo 'GIT BRANCH   		 ' +"${GIT_BRANCH}"
					echo 'WORKSPACE   		 ' +"${WORKSPACE}" 
					
					
					bat  ''+ "${BATCH_PATH}" + 'sleep5.bat'
					
					
			script{
			
						TRIGGER_CAUSE = ""+"${currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause)}"
					
						if(TRIGGER_CAUSE.indexOf("UserIdCause") >= 0){
							CODE_EDIT = "change"
							DOC_EDIT = "change"
																}
			
			
					def changes = "Changes:\n"
					build = currentBuild
					while(build != null && build.result != 'SUCCESS') {
						changes += "In ${build.id}:\n"
						for (changeLog in build.changeSets) {
							for(entry in changeLog.items) {
								for(file in entry.affectedFiles) {
								
									changes += "* ${file.path}\n"
								}
							}
						}
						build = build.previousBuild
					}
					
					echo changes
					

					
					if (changes.indexOf("Jenkinsfile") >= 0) {
						DOC_EDIT = "change"
						CODE_EDIT = "change"			
							} 
					
					
					if ((changes.indexOf("Documents/") >= 0) || (changes.indexOf("SQLScripts/") >= 0)  || (changes.indexOf("Templates/") >= 0) ) {
						DOC_EDIT = "change"
							} 
							
					if (changes.indexOf("BAE/") >= 0) {
						CODE_EDIT = "change"
							} 

						echo CODE_EDIT
						echo DOC_EDIT
			}				
						}
										}
	
	
	
		        stage ('War Build Stage') {
				
			when {   	  expression {
			return CODE_EDIT == 'change';}
					
			 }

            steps {
		   
		   
               echo 'Building .war file'
              

			//copying latest source to file to webapps 
			bat  ''+ "${BATCH_PATH}" + 'copysrc.bat ' + "${env.WORKSPACE}" + ''
			
			 //cd to BAE folder and then mvn install.	
			bat  ''+ "${BATCH_PATH}" + 'maveninstall.bat ' + "${env.WORKSPACE}" + ''
			
			
			   
              
            }
        }
		
		        stage ('War Deployment Stage') {
				
				 when {   	  expression {
									return CODE_EDIT == 'change';
									}
					
			 }

            steps {
               echo 'Deploying .war file'
               //copy war file from jenkins workspace to webapps
            
			 bat  ''+ "${BATCH_PATH}" + 'sleep5.bat'

    		 bat  ''+ "${BATCH_PATH}" + 'deploybae4_4.bat ' + "${env.WORKSPACE}" + ''
			      
			 bat  ''+ "${BATCH_PATH}" + 'sleep30.bat'
			 
			 bat  ''+ "${BATCH_PATH}" + 'deployprops.bat ' + "${env.WORKSPACE}" + ''
			 
			 bat  ''+ "${BATCH_PATH}" + 'sleep5.bat'
			 
			 bat  ''+ "${BATCH_PATH}" + 'deployrest.bat ' + "${env.WORKSPACE}" + ''
			 
			 bat  ''+ "${BATCH_PATH}" + 'sleep5.bat'
			   
			 bat  ''+ "${BATCH_PATH}" + 'deployunit.bat ' + "${env.WORKSPACE}" + ''
			 
			 bat  ''+ "${BATCH_PATH}" + 'sleep5.bat'
			   
			 bat  ''+ "${BATCH_PATH}" + 'deploymockito.bat ' + "${env.WORKSPACE}" + ''
			 
			 bat  ''+ "${BATCH_PATH}" + 'sleep5.bat'
			   
              
            }
        }
		
		
				stage ('Test Resources compilation Stage') {

				 when {   	  expression {
									return CODE_EDIT == 'change';
									}
					
			 }
				
				
            steps {
               echo 'Test resources being compiled'
               //compile testNGRunner and MockitoTestRunner

				bat  ''+ "${BATCH_PATH}" + 'sleep5.bat'
				
			     bat '' + "${WEBAPP_INSTANCE_PATH}" +'/src/testNG/testng_compiler.bat'
				 
				 bat '' + "${WEBAPP_INSTANCE_PATH}" +'/src/test/mockito_compiler.bat'
				   
            }
        }
		
		
	    
        stage ('Unit Testing Stage') {
		
		 when {   	  expression {
									return CODE_EDIT == 'change';
									}
					
			 }

            steps {
               echo 'Unit Test Run'
               bat  ''+ "${BATCH_PATH}" + 'sleep5.bat'
               //running unit tests through batch file in src\\testng
               bat '' + "${WEBAPP_INSTANCE_PATH}" +'/src/testNG/TestNGRunner.bat'
            }
        }
		
		
		stage ('Mockito Testing Stage') {
		
		when {   	  expression {
									return CODE_EDIT == 'change';
									}		
			 }

            steps {
                //running mockito tests through batch file in src\\test foles
                echo 'Mockito TEST Run'
                bat  ''+ "${BATCH_PATH}" + 'sleep5.bat'
                bat '' + "${WEBAPP_INSTANCE_PATH}" +'/src/test/MockitoRunner2.bat'
            }
		}
		
		stage ('Rest API Testing Stage') {
		
		when {   	  expression {
									return CODE_EDIT == 'change';
									}
					
			 }

            steps {
               echo 'Rest API  Test Run'
               bat  ''+ "${BATCH_PATH}" + 'sleep5.bat'
			   //running rest api test cases using batch file operating headless chrome simulation.
			   bat  ''+ "${BATCH_PATH}" + 'jenkinsrunnerTEST_4_4.bat'
			   
			   
			   
            }
        }
		
		stage ('Resource Packaging Stage') {
		
		when {   	  expression {
									return DOC_EDIT == 'change';
									}
					
			 }

            steps {
               echo 'Documents zip creation started'
               
			   bat  ''+ BATCH_PATH + 'zip_docs_creation.bat ' + "${env.WORKSPACE}" + ''
			   
			   
			   
            }
        }
		
		
    }
	
	post {
	
        always {
		echo 'Post Script running'
		
	
	//creating xml storage folders for unit,mockito and rest api testing.
	
		
	
	bat 'mkdir '+"${REST_PATH}"
	bat 'mkdir '+"${UNIT_PATH}"
	bat 'mkdir '+"${MOCKITO_PATH}"
	bat 'mkdir '+"${ZIP_STORAGE_PATH}"
	bat 'mkdir '+"${ZIP_STORAGE_PATH2}"
	
	
	//creating report storage folders for unit,mockito for rest api testing.
	
	bat 'mkdir '+"${REST_REPORT_PATH}"
	bat 'mkdir '+"${UNIT_REPORT_PATH}"
	bat 'mkdir '+"${MOCKITO_REPORT_PATH}"	
	


	 bat  ''+ "${BATCH_PATH}" + 'sleep5.bat'
	
	//Copying xml files to Jenkins path 
	
    //    bat   'copy /b '+ "${BATCH_PATH2}" + 'resultstest4_4.xml+ '+"${REST_PATH}"+''
		
		bat 'xcopy '+ "${BATCH_PATH2}" + '\\Rest '+"${REST_PATH}" + ' /E /H /C /R /Q /Y'
	
    	bat 'xcopy '+ "${WEBAPP_INSTANCE_PATH2}" +'\\Reports\\Unit_Test_Reports\\junitreports '+"${UNIT_PATH}"+' /E /H /C /R /Q /Y'
    
		bat 'xcopy '+ "${WEBAPP_INSTANCE_PATH2}" +'\\Reports\\Mockito_Test_Reports\\junitreports '+"${MOCKITO_PATH}"+' /E /H /C /R /Q /Y'
	
    //	bat 'xcopy '+ "${env.JENKINS_HOME}"+'/userContent/BAE_TEST/Mockito_Reports/report\" '+"${MOCKITO_PATH}"+' /E /H /C /R /Q /Y'
    
        bat  ''+ "${BATCH_PATH}" + 'sleep5.bat'
	
	//Copying zip files folders to Reports folder
	
	bat 'copy ' +"${env.WORKSPACE}"+'\\BAE\\target\\BAE_4_4.war '+"${ZIP_STORAGE_PATH}"+''
	
	bat  ''+ "${BATCH_PATH}" + 'repackage.bat ' + "${env.WORKSPACE}" + ''
	
	bat 'xcopy '+ "${env.WORKSPACE}" +'\\BAE\\target\\Documents '+"${ZIP_STORAGE_PATH}"+' /E /H /C /R /Q /Y'
	
	bat 'copy ' +"${env.WORKSPACE}"+'\\BAE\\target\\BAE_4_4.war '+"${ZIP_STORAGE_PATH2}"+''
	
	//bat 'copy ' +"${env.WORKSPACE}"+'\\BAE\\target\\Documents.zip '+"${ZIP_STORAGE_PATH}"+''
	
	//bat 'copy ' +"${env.WORKSPACE}"+'\\BAE\\target\\SQLScripts.zip '+"${ZIP_STORAGE_PATH}"+''
	
	//bat 'copy ' +"${env.WORKSPACE}"+'\\BAE\\target\\Templates.zip '+"${ZIP_STORAGE_PATH}"+''
	
		//Copying Report folders to UserContent

		//bat 'copy /b '+ "${BATCH_PATH2}" + 'resultstest4_4.xml+ '+"${REST_REPORT_PATH}"+''
		
			bat 'xcopy '+ "${BATCH_PATH2}" + '\\Rest '+"${REST_REPORT_PATH}" +' /E /H /C /R /Q /Y'
	
	    bat 'xcopy '+ "${WEBAPP_INSTANCE_PATH2}" +'\\Reports\\Unit_Test_Reports '+"${UNIT_REPORT_PATH}"+' /E /H /C /R /Q /Y'
		
		bat 'xcopy '+ "${WEBAPP_INSTANCE_PATH2}" +'\\Reports\\Mockito_Test_Reports '+"${MOCKITO_REPORT_PATH}"+' /E /H /C /R /Q /Y'
    
		bat  ''+ "${BATCH_PATH}" + 'sleep5.bat'
        

        junit 'Reports/Mockito_Reports/'+"${JJOB_NAME}"+'-'+"${env.GIT_BRANCH}"+'/'+"${env.BUILD_NUMBER}"+'/TEST-*.xml'
        junit 'Reports/Unit_Reports/'+"${JJOB_NAME}"+'-'+"${env.GIT_BRANCH}"+'/'+"${env.BUILD_NUMBER}"+'/TEST-*.xml'
        junit 'Reports/Rest_Reports/'+"${JJOB_NAME}"+'-'+"${env.GIT_BRANCH}"+'/'+"${env.BUILD_NUMBER}"+'/resultstest4_4.xml'
		
	  //archiveArtifacts artifacts: 'BAE/target/*.war', fingerprint: true
		archiveArtifacts artifacts: 'Reports/ZIPS/'+"${JJOB_NAME}"+'-'+"${env.GIT_BRANCH}"+'/'+"${env.BUILD_NUMBER}"+'/BAE_4_4.war', fingerprint: true
		archiveArtifacts artifacts: 'Reports/ZIPS/'+"${JJOB_NAME}"+'-'+"${env.GIT_BRANCH}"+'/'+"${env.BUILD_NUMBER}"+'/nonsrc/BAE_4_4.war', fingerprint: true
		archiveArtifacts artifacts: 'Reports/ZIPS/'+"${JJOB_NAME}"+'-'+"${env.GIT_BRANCH}"+'/'+"${env.BUILD_NUMBER}"+'/Templates.zip', fingerprint: true
		archiveArtifacts artifacts: 'Reports/ZIPS/'+"${JJOB_NAME}"+'-'+"${env.GIT_BRANCH}"+'/'+"${env.BUILD_NUMBER}"+'/Documents.zip', fingerprint: true
		archiveArtifacts artifacts: 'Reports/ZIPS/'+"${JJOB_NAME}"+'-'+"${env.GIT_BRANCH}"+'/'+"${env.BUILD_NUMBER}"+'/SQLScripts.zip', fingerprint: true
		      
						echo 'Report Mail sending'
            script {
                    emailext subject: ' BAE-SQL-Version/'+ "${GIT_BRANCH}" + ' $DEFAULT_SUBJECT' ,    
			
			body: "Click the link below to show REST API Testing Results for your current build :  \n"+"${JENKINS_URL}"+"blue/organizations/jenkins/"+"${JJOB_NAME}"+"/detail/"+"${GIT_BRANCH}"+"/activity/"+"\n\n\n Click the link below to show Mockito TestNG Results for your current build :  \n"+"${JENKINS_URL}"+"/userContent/"+"${JJOB_NAME}"+'-'+"${GIT_BRANCH}"+"/Mockito_Reports/"+"${ENV_BUILD_NO}"+"/index.html" +"\n\n\n Click the link below to show Unit Testing Results for your current build :  \n"+"${JENKINS_URL}"+"/userContent/"+"${JJOB_NAME}"+'-'+"${GIT_BRANCH}"+"/Unit_Reports/"+"${ENV_BUILD_NO}"+"/index.html" ,
                        attachLog: true,
                        replyTo: '$DEFAULT_REPLYTO',
                        to: '$DEFAULT_RECIPIENTS'          	
            }
        }
    }
    
}


