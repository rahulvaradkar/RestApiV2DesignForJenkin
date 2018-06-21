package test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import servlets.JUnitTest;

public class TestRunnerLE {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(xlLinkExportServiceTest.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      System.out.println("Test Result:"+result.getFailures());
      System.out.println("Run Count :"+result.getRunCount());
      System.out.println("Suuceesful result :"+result.wasSuccessful());
      System.out.println("Failure Count :"+result.getFailureCount());
      System.out.println("Ignore Count"+result.getIgnoreCount());
      System.out.println("Run Time in milisecond :"+result. getRunTime());
    
   }
}  