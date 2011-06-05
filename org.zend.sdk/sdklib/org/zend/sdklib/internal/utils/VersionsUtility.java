/*******************************************************************************
 * Copyright (c) May 18, 2011 Zend Technologies Ltd. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 *******************************************************************************/
package org.zend.sdklib.internal.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helps with version compare operations
 * 
 * @author Roy, 2011
 */
public class VersionsUtility {

	private final static String patternRange = "[\\(\\[]\\d{1,3}.\\d{1,3}(.\\d{1,3})?(.\\d{1,3})?(\\s)*,(\\s)*\\d{1,3}.\\d{1,3}(.\\d{1,3})?(.\\d{1,3})?[\\)\\]]";
	private final static String patternVersion = "\\d{1,3}.\\d{1,3}(.\\d{1,3})?(.\\d{1,3})?";

	/**
	 * Tests that a version is in between a given range.
	 * 
	 * <pre>
	 * assertTrue(inBetween(&quot;5.1&quot;, &quot;[5.0, 6.4]&quot;));
	 * assertTrue(inBetween(&quot;5.0&quot;, &quot;[5.0, 6.4]&quot;));
	 * assertFalse(inBetween(&quot;5.0&quot;, &quot;(5.0, 6.4]&quot;));
	 * assertFalse(inBetween(&quot;4.9&quot;, &quot;[5.0, 6.4]&quot;));
	 * assertTrue(inBetween(&quot;6.4&quot;, &quot;[5.0, 6.4]&quot;));
	 * assertFalse(inBetween(&quot;6.4&quot;, &quot;[5.0, 6.4)&quot;));
	 * assertFalse(inBetween(&quot;7.0&quot;, &quot;[5.0, 6.4]&quot;));
	 * </pre>
	 * 
	 * @param currentVersion
	 * @param range
	 * @return
	 */
	public static boolean inBetween(String currentVersion, String range) {

		if (!range.matches(patternRange) || invalidVersion(currentVersion)) {
			throw new IllegalStateException(
					"Corrupted range or version specified in site, "
							+ "consider contacting your site administrator");
		}

		final Matcher matcher = Pattern.compile(patternVersion).matcher(range);
		boolean startInclusive = range.startsWith("[");
		boolean endInclusive = range.endsWith("]");
		matcher.find();
		String start = matcher.group();
		matcher.find();
		String end = matcher.group();

		long s = normalizeVersion(start);
		long e = normalizeVersion(end);
		long c = normalizeVersion(currentVersion);

		return (s < c || (startInclusive && s == c))
				&& (e > c || (endInclusive && e == c));
	}

	/**
	 * Compare two given versions.
	 * <pre> < 0 </pre> ver1 is greater than ver2
	 * <pre> = 0 </pre> ver1 is equal to ver2
	 * <pre> > 0 </pre> ver1 is smaller than ver2
	 * 
	 * @param ver1
	 * @param ver2
	 * @return
	 */
	public static long versionCompare(String ver1, String ver2) {
		if (invalidVersion(ver1) || invalidVersion(ver2)) {
			throw new IllegalArgumentException("illegal format of version: "
					+ ver1 + " or " + ver2);
		}
		
		return normalizeVersion(ver1) - normalizeVersion(ver2);
	}
	
	public static long normalizeVersion(String version) {
		if (invalidVersion(version)) {
			throw new IllegalArgumentException("illegal format of version: "
					+ version);
		}

		final String[] split = version.split("\\.");
		long sum = 0;
		for (int i = 0; i < split.length; i++) {
			sum += Integer.valueOf(split[i]) * Math.pow(10, (9 - i * 3));
		}

		return sum;
	}

	public static boolean invalidVersion(String currentVersion) {
		return !currentVersion.matches(patternVersion);
	}

}
