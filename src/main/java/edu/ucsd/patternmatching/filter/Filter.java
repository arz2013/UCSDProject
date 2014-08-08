package edu.ucsd.patternmatching.filter;

public interface Filter<T> {
	public boolean match(T toFilter);
}
