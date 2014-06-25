package edu.ucsd.utils;

public abstract class LabelUtils {
	private static String LABELSTRATEGY_PREFIX = "_";
	
	public static String createLabel(String userSuppliedLabel) {
		return LABELSTRATEGY_PREFIX + userSuppliedLabel;
	}
}
