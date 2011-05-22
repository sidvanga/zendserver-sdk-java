package org.zend.sdk.test.sdkcli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.zend.sdklib.internal.utils.EnvironmentUtils;

public class TestEnvironmetUtils {

	@Test
	public void testGetOsWindows() {
		setSystem("Windows");
		assertTrue(EnvironmentUtils.isUnderWindows());
		assertFalse(EnvironmentUtils.isUnderLinux());
		assertFalse(EnvironmentUtils.isUnderMaxOSX());
	}

	@Test
	public void testGetOsLinux() {
		setSystem("Linux");
		assertFalse(EnvironmentUtils.isUnderWindows());
		assertTrue(EnvironmentUtils.isUnderLinux());
		assertFalse(EnvironmentUtils.isUnderMaxOSX());
	}

	@Test
	public void testGetOsMac() {
		setSystem("Mac");
		assertFalse(EnvironmentUtils.isUnderWindows());
		assertFalse(EnvironmentUtils.isUnderLinux());
		assertTrue(EnvironmentUtils.isUnderMaxOSX());
	}

	private void setSystem(String name) {
		System.setProperty("os.name", name);
	}

}
