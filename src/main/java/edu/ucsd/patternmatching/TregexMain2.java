package edu.ucsd.patternmatching;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

public class TregexMain2 {

	public static void main(String[] args) {
		Tree t = Tree.valueOf("(S (SP (NP goon) (PP and) (GG Google)))");
		TregexPattern p = TregexPattern.compile("SP < (NP . PP. GG)");
		TregexMatcher m = p.matcher(t);
		while (m.find()) {
			m.getMatch().pennPrint();
		}
	}

}
