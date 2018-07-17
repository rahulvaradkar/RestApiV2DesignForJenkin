package test;

import org.testng.annotations.Test;

import org.testng.AssertJUnit;

public class TestNgMavenExampleTest {

	@Test(priority = 0)
	public void exampleOfTestNgMaven() {
		System.out.println("Testing test with priority 0");		
		String s = "initial";
		AssertJUnit.assertEquals(s, "initial");
		AssertJUnit.assertNotNull(s);
	}
	
	@Test(priority = 1)
	public void testNullablity() {
		System.out.println("Testing test with priority 1");
		
		String s = " ";
		AssertJUnit.assertNotNull(s);
	}

	
}
