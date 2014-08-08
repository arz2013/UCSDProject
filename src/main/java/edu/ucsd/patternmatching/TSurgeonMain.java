package edu.ucsd.patternmatching;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.trees.tregex.tsurgeon.Tsurgeon;
import edu.stanford.nlp.trees.tregex.tsurgeon.TsurgeonPattern;

public class TSurgeonMain {
	private static TsurgeonPattern createSubtreeOperation() {
		return Tsurgeon.parseOperation("createSubtree Maybe remove");
	}

	public static void main(String[] args) {
		Tree t = Tree.valueOf("(ROOT (S (NP (NP (NNP Bank)) (PP (IN of) (NP (NNP America)))) (VP (VBD called)) (. .)))");
		t.pennPrint();
		TregexPattern pat = TregexPattern.compile("NP <1 (NP << Bank) <2 PP=remove");
		TsurgeonPattern surgery = createSubtreeOperation();
		Tsurgeon.processPattern(pat, surgery, t).pennPrint();
	}

}
