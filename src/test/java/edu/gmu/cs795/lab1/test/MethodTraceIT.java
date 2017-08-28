package edu.gmu.cs795.lab1.test;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import edu.gmu.cs795.lab1.ProfileLogger;

public class MethodTraceIT {
	@Test
	public void testCleanup() throws Exception {
		ProfileLogger.dump();
		assertEquals(0, ProfileLogger.dump().size());
	}

	@Test
	public void testSingleCall() throws Exception {
		ProfileLogger.dump();
		otherMethod();
		HashSet<String> meths = ProfileLogger.dump();
		assertEquals(1, meths.size());
		assertEquals("edu/gmu/cs795/lab1/test/MethodTraceIT.otherMethod()V", meths.iterator().next());
	}

	private void otherMethod() {
	}

	@Test
	public void testJavaMethodsExcluded() throws Exception {
		ProfileLogger.dump();
		HashSet<Object> foo = new HashSet<Object>();
		assertEquals(0, ProfileLogger.dump().size());
	}
}
