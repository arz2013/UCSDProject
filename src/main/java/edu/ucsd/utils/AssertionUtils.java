package edu.ucsd.utils;

import edu.ucsd.exceptions.AssertionException;

public abstract class AssertionUtils {
	public static void assertTrue(boolean condition, String message) {
		if(!condition) {
			throw new AssertionException(message);
		}
	}
}
