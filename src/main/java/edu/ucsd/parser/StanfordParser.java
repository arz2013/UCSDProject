package edu.ucsd.parser;

import java.util.List;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;

public class StanfordParser {
	public static void main(String[] args) {
		String grammar = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
		String[] options = { "-retainTMPSubcategories", "-outputFormat", "wordsAndTags,penn,typedDependencies" };
		  
		LexicalizedParser lp = LexicalizedParser.loadModel(grammar, options);
		String sentence = "The strongest rain ever recorded in India shut down the financial hub of Mumbai, snapped communication lines, closed airports and forced thousands of people to sleep in their offices or walk home during the night, officials said today.";

		Tree parse = lp.parse(sentence);
		List children = parse.getChildrenAsList();
		for(Object o : children) {
			System.out.println("Child is: " + o);
		}
	}
}
