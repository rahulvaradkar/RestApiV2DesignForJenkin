package test;

import org.testng.annotations.Test;

import org.testng.AssertJUnit;

public class TestNgMavenExampleTest2 {

	
	@Test
	public void testEquality() {
		System.out.println("Testing test with priority 2");
		
		String s = " ";
		AssertJUnit.assertEquals(" ",s);
	}
	
}
