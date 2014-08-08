package edu.ucsd.patternmatching;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

public class TregexMain {
	private final static Logger logger = LoggerFactory.getLogger(TregexMain.class);

	public static void main(String[] args) throws IOException {
		String result = FileUtils.readFileToString(new File(TregexMain.class.getClassLoader().getResource("SingleParseTree.txt").getFile()));
		Tree t = Tree.valueOf(result);
		TregexPattern p = TregexPattern.compile("NP < (DT . NNP . NNP . NNP . NNP)");
		TregexMatcher m = p.matcher(t);
		while (m.find()) {
			m.getMatch().pennPrint();
		}
		p = TregexPattern.compile("NP < (DT $. NNP)");
		m = p.matcher(t);
		while (m.find()) {
			m.getMatch().pennPrint();
			logger.info("Node names: " + m.getNodeNames());
			Tree org = m.getNode("organization");
			org.pennPrint();
		}
		p = TregexPattern.compile("NP < ((NP < (((NNP $. /,/) $.. NNP) $. /,/)) $. CC $ (NP < (NNP $. POS)))");
		m = p.matcher(t);
		while (m.find()) {
			logger.info("Here's the match");
			m.getMatch().pennPrint();
		}
		p = TregexPattern.compile("NP < (NNP $+ /,/ $++ NNP $+ /,/)");
		m = p.matcher(t);
		while (m.find()) {
			m.getMatch().pennPrint();
		}
		p = TregexPattern.compile("NP < (NNP $+ POS)");
		m = p.matcher(t);
		while (m.find()) {
			m.getMatch().pennPrint();
		}
	}
}
