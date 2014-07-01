package edu.ucsd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.kernel.Traversal;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.Rel;
import edu.ucsd.model.Sentence;
import edu.ucsd.model.Word;
import edu.ucsd.model.WordToWordDependency;
import edu.ucsd.parser.DisneyParser;
import edu.ucsd.system.SystemApplicationContext;
import edu.ucsd.utils.FileUtils;

public class SingleSentenceParseAndLoadTest {
	private GraphDatabaseService graphService;
	private SentenceDao sentenceDao;
	private String text;
	
	@Before
	public void setUp() {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("nlp-context-test.xml");	
		sentenceDao = SentenceDao.class.cast(appContext.getBean("sentenceDao")); 
		graphService = GraphDatabaseService.class.cast(appContext.getBean("graphDatabaseService"));
		graphService.beginTx();
		text = "Looking back at everything we’ve accomplished this year, I am once again awed by the tremendous creativity and commitment of the men and women who make up The Walt Disney Company.";
		List<String> disneyFinancialStatement = new ArrayList<String>();
		disneyFinancialStatement.add(text);
		DisneyParser parser = new DisneyParser(sentenceDao, disneyFinancialStatement);
		parser.parseAndLoad();
	}
	
	@Test
	public void testParseAndLoad() {
		Sentence sentence = sentenceDao.getSentenceByText(text);
		Assert.assertTrue(sentence.getSentenceNumber() == 0);
		Assert.assertEquals(sentence.getText(), text);
		List<Word> words = sentenceDao.getWordsBySentenceText(text);
		Assert.assertEquals(35, words.size());
		
		Collections.sort(words, new Comparator<Word>() {
			public int compare(Word w1, Word w2) {
				return new Integer(w1.getPosition()).compareTo(new Integer(w2.getPosition()));
			}
		});
		checkValues(words);
		checkWordDependencies(words);
		checkParseTree(sentence);
	}

	private void checkParseTree(Sentence sentence) {
		Node node = graphService.getNodeById(sentence.getId());
		List<String> pathInStrings = new ArrayList<String>();
		for ( Node pathNode : this.graphService.traversalDescription()
			    .depthFirst()
			    .relationships( Rel.HAS_PARSE_CHILD )
			    .evaluator( Evaluators.toDepth( 15 ) )
			    .traverse( node ).nodes() ) {
			if(pathNode.hasProperty("value")) {
				pathInStrings.add(pathNode.getProperty("value").toString());	
			} else if(pathNode.hasProperty("text")) {
				pathInStrings.add(pathNode.getProperty("text").toString());
			} 
		}
		
		try {
			List<String> comparisons = FileUtils.readFromFile("FlattenedParseTreeTest.txt");
			Assert.assertEquals(comparisons, pathInStrings);
		} catch(IOException ioe) {
			Assert.fail("IO Exception encountered during comparison");
		}
	}

	private void checkWordDependencies(List<Word> words) {
		checkDependency(words.get(15), words.get(1), "vmod");
		checkDependency(words.get(1), words.get(2), "advmod");
		checkDependency(words.get(1), words.get(3), "prep");
		checkDependency(words.get(3), words.get(4), "pobj");
		checkDependency(words.get(7), words.get(5), "nsubj");
		checkDependency(words.get(7), words.get(6), "aux");
		checkDependency(words.get(4), words.get(7), "rcmod");
		checkDependency(words.get(9), words.get(8), "det");
		checkDependency(words.get(7), words.get(9), "tmod");
		checkDependency(words.get(15), words.get(11), "nsubj");
		checkDependency(words.get(15), words.get(12), "aux");
		checkDependency(words.get(15), words.get(13), "advmod");
		checkDependency(words.get(15), words.get(14), "advmod");
		checkDependency(words.get(0), words.get(15), "root");
		checkDependency(words.get(15), words.get(16), "prep");
		checkDependency(words.get(19), words.get(17), "det");
		checkDependency(words.get(19), words.get(18), "amod");
		checkDependency(words.get(16), words.get(19), "pobj");
		checkDependency(words.get(19), words.get(20), "cc");
		checkDependency(words.get(19), words.get(21), "conj");
		checkDependency(words.get(19), words.get(22), "prep");
		checkDependency(words.get(24), words.get(23), "det");
		checkDependency(words.get(22), words.get(24), "pobj");
		checkDependency(words.get(24), words.get(25), "cc");
		checkDependency(words.get(24), words.get(26), "conj");
		checkDependency(words.get(28), words.get(27), "nsubj");
		checkDependency(words.get(24), words.get(28), "rcmod");
		checkDependency(words.get(28), words.get(29), "prt");
		checkDependency(words.get(33), words.get(30), "det");
		checkDependency(words.get(33), words.get(31), "nn");
		checkDependency(words.get(33), words.get(32), "nn");
		checkDependency(words.get(28), words.get(33), "dobj");
	}

	private void checkDependency(Word word, Word word2, String dependency) {
		String dep = sentenceDao.getRelationShip(word.getId(), word2.getId());
		Assert.assertEquals(dependency, dep);
	}

	private void checkValues(List<Word> words) {
		System.out.println("Asserting Values");
		this.checkValue(words.get(0), 0, "ROOT", null, null);
		this.checkValue(words.get(1), 1, "Looking", "VBG", "O");
		this.checkValue(words.get(2), 2, "back", "RB", "O");
		this.checkValue(words.get(3), 3, "at", "IN", "O");
		this.checkValue(words.get(4), 4, "everything", "NN", "O");
		this.checkValue(words.get(5), 5, "we", "PRP", "O");
		this.checkValue(words.get(6), 6, "'ve", "VBP", "O");
		this.checkValue(words.get(7), 7, "accomplished", "VBN", "O");
		this.checkValue(words.get(8), 8, "this", "DT", "DATE");
		this.checkValue(words.get(9), 9, "year", "NN", "DATE");
		this.checkValue(words.get(10), 10, ",", ",", "O");
		this.checkValue(words.get(11), 11, "I", "PRP", "O");
		this.checkValue(words.get(12), 12, "am", "VBP", "O");
		this.checkValue(words.get(13), 13, "once", "RB", "DATE");
		this.checkValue(words.get(14), 14, "again", "RB", "O");
		this.checkValue(words.get(15), 15, "awed", "JJ", "O");
		this.checkValue(words.get(16), 16, "by", "IN", "O");
		this.checkValue(words.get(17), 17, "the", "DT", "O");
		this.checkValue(words.get(18), 18, "tremendous", "JJ", "O");
		this.checkValue(words.get(19), 19, "creativity", "NN", "O");
		this.checkValue(words.get(20), 20, "and", "CC", "O");
		this.checkValue(words.get(21), 21, "commitment", "NN", "O");
		this.checkValue(words.get(22), 22, "of", "IN", "O");
		this.checkValue(words.get(23), 23, "the", "DT", "O");
		this.checkValue(words.get(24), 24, "men", "NNS", "O");
		this.checkValue(words.get(25), 25, "and", "CC", "O");
		this.checkValue(words.get(26), 26, "women", "NNS", "O");
		this.checkValue(words.get(27), 27, "who", "WP", "O");
		this.checkValue(words.get(28), 28, "make", "VBP", "O");
		this.checkValue(words.get(29), 29, "up", "RP", "O");
		this.checkValue(words.get(30), 30, "The", "DT", "ORGANIZATION");
		this.checkValue(words.get(31), 31, "Walt", "NNP", "ORGANIZATION");
		this.checkValue(words.get(32), 32, "Disney", "NNP", "ORGANIZATION");
		this.checkValue(words.get(33), 33, "Company", "NNP", "ORGANIZATION");
		this.checkValue(words.get(34), 34, ".", ".", "O");
	}

	private void checkValue(Word word, int i, String text, String posTag,
			String neTag) {
		Assert.assertNotNull(word);
		Assert.assertTrue(word.getPosition() == i);
		Assert.assertEquals(word.getText(), text);
		if(posTag != null) {
			Assert.assertEquals(posTag, word.getPosTag());
		}
		
		if(neTag != null) {
			Assert.assertEquals(neTag, word.getNeTag());
		}	
	}
	

}
