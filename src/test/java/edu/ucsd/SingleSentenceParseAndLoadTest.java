package edu.ucsd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.OrderedByTypeExpander;
import org.neo4j.kernel.Traversal;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.Document;
import edu.ucsd.model.Rel;
import edu.ucsd.model.Sentence;
import edu.ucsd.model.Word;
import edu.ucsd.parser.DisneyParser;
import edu.ucsd.utils.FileUtils;

public class SingleSentenceParseAndLoadTest {
	private GraphDatabaseService graphService;
	private SentenceDao sentenceDao;
	private String text;
	private String text1;
	
	@Before
	public void setUp() {
		Injector injector = Guice.createInjector(new SpringTestModule());
		sentenceDao = injector.getInstance(SentenceDao.class); 
		graphService = injector.getInstance(GraphDatabaseService.class);
		graphService.beginTx();
		text = "Looking back at everything we’ve accomplished this year, I am once again awed by the tremendous creativity and commitment of the men and women who make up The Walt Disney Company.";
		text1 = "On behalf of everyone at Disney, I thank you for your continued support as we strive to create the next generation of fantastic family entertainment.";
		List<String> disneyFinancialStatement = new ArrayList<String>();
		disneyFinancialStatement.add(text + " " + text1);
		// disneyFinancialStatement.add(text1);
		DisneyParser parser = injector.getInstance(DisneyParser.class);
		parser.parseAndLoad(disneyFinancialStatement, new Document("Disney Financial Statement", 2013, 0));
	}
	
	@Test
	public void testParseAndLoad() {
		validateDocument();
		validateFirstSentence();
		validateSecondSentence();
		validateReference();
	}
	
	private void validateReference() {
		// TODO Auto-generated method stub
		Sentence sentence = sentenceDao.getSentenceByText(text1);
		Node word = sentenceDao.getWord(sentence.getSentenceNumber(), 6);
		Assert.assertEquals(word.getProperty("text"), "Disney");
		int count = 0; 
		Node nounPhrase = null;
		for(Relationship rel : word.getRelationships(Rel.REFERS_TO, Direction.OUTGOING)) {
			count++;
			nounPhrase = rel.getEndNode();
		}
		Assert.assertEquals(count, 1);
		Assert.assertEquals(nounPhrase.getProperty("value"), "NP");
		Set<String> children = new HashSet<String>();
		for(Relationship rel : nounPhrase.getRelationships(Rel.HAS_PARSE_CHILD, Direction.OUTGOING)) {
			Node leafWord = rel.getEndNode().getRelationships(Rel.HAS_PARSE_CHILD, Direction.OUTGOING).iterator().next().getEndNode();
			children.add((String)leafWord.getProperty("text"));
		}
		Assert.assertEquals(children.size(), 4);
		Assert.assertTrue(children.contains("The"));
		Assert.assertTrue(children.contains("Walt"));
		Assert.assertTrue(children.contains("Disney"));
		Assert.assertTrue(children.contains("Company"));
	}

	private void validateDocument() {
		Document doc = sentenceDao.getDocumentByTitleYearAndNumber("Disney Financial Statement", 2013, 0);
		Assert.assertNotNull(doc);
		List<Sentence> sentences = sentenceDao.getSentencesBasedOnDocument(doc.getId());
		Assert.assertTrue(sentences.size() == 2);
		Assert.assertEquals(text, sentences.get(0).getText());
		Assert.assertEquals(text1,sentences.get(1).getText());
	}
	
	private void validateFirstSentence() {
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
		checkValuesFirstSentence(words);
		checkWordDependenciesFirstSentence(words);
		checkParseTree(sentence, "FlattenedParseTreeTest.txt");
	}
	
	private void validateSecondSentence() {
		Sentence sentence = sentenceDao.getSentenceByText(text1);
		Assert.assertTrue(sentence.getSentenceNumber() == 1);
		Assert.assertEquals(sentence.getText(), text1);
		List<Word> words = sentenceDao.getWordsBySentenceText(text1);
		Assert.assertEquals(28, words.size());
		
		Collections.sort(words, new Comparator<Word>() {
			public int compare(Word w1, Word w2) {
				return new Integer(w1.getPosition()).compareTo(new Integer(w2.getPosition()));
			}
		});
		checkValuesSecondSentence(words);
		checkWordDependenciesSecondSentence(words);
		checkParseTree(sentence, "FlattenedParseTreeTest1.txt");
	}

	private void checkParseTree(Sentence sentence, String expected) {
		Node node = graphService.getNodeById(sentence.getId());
		List<String> pathInStrings = new ArrayList<String>();
		PathExpander<?> expander = new OrderedByTypeExpander().add(Rel.FIRST_CHILD, Direction.OUTGOING).add(Rel.NEXT, Direction.OUTGOING);
		TraversalDescription DFS_TRAVERSAL = Traversal.description()
			    .depthFirst().expand(expander);
		for(Path path : DFS_TRAVERSAL.traverse(node)) {
			Node pathNode = path.endNode();
			if(pathNode.hasProperty("value")) {
				pathInStrings.add(pathNode.getProperty("value").toString());	
			} else if(pathNode.hasProperty("text")) {
				pathInStrings.add(pathNode.getProperty("text").toString());
			} 
		}
		
		try {
			List<String> comparisons = FileUtils.readFromFile(expected);
			Assert.assertEquals(comparisons, pathInStrings);
		} catch(IOException ioe) {
			Assert.fail("IO Exception encountered during comparison");
		}
	}

	private void checkWordDependenciesFirstSentence(List<Word> words) {
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
	
	private void checkWordDependenciesSecondSentence(List<Word> words) {
		checkDependency(words.get(9), words.get(1), "prep");
		checkDependency(words.get(1), words.get(2), "pobj");
		checkDependency(words.get(2), words.get(3), "prep");
		checkDependency(words.get(3), words.get(4), "pobj");
		checkDependency(words.get(4), words.get(5), "prep");
		checkDependency(words.get(5), words.get(6), "pobj");
		checkDependency(words.get(9), words.get(8), "nsubj");
		checkDependency(words.get(0), words.get(9), "root");
		checkDependency(words.get(9), words.get(10), "dobj");
		checkDependency(words.get(9), words.get(11), "prep");
		checkDependency(words.get(14), words.get(12), "poss");
		checkDependency(words.get(14), words.get(13), "amod");
		checkDependency(words.get(11), words.get(14), "pobj");
		checkDependency(words.get(17), words.get(15), "mark");
		checkDependency(words.get(17), words.get(16), "nsubj");
		checkDependency(words.get(9), words.get(17), "advcl");
		checkDependency(words.get(19), words.get(18), "aux");
		checkDependency(words.get(17), words.get(19), "xcomp");
		checkDependency(words.get(22), words.get(20), "det");
		checkDependency(words.get(22), words.get(21), "amod");
		checkDependency(words.get(19), words.get(22), "dobj");
		checkDependency(words.get(22), words.get(23), "prep");
		checkDependency(words.get(25), words.get(24), "dep");
		checkDependency(words.get(26), words.get(25), "amod");
		checkDependency(words.get(23), words.get(26), "pobj");
	}


	private void checkDependency(Word word, Word word2, String dependency) {
		String dep = sentenceDao.getRelationShip(word.getId(), word2.getId());
		Assert.assertEquals(dependency, dep);
	}

	private void checkValuesFirstSentence(List<Word> words) {
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
	
	private void checkValuesSecondSentence(List<Word> words) {
		System.out.println("Asserting Values");
		this.checkValue(words.get(0), 0, "ROOT", null, null);
		this.checkValue(words.get(1), 1, "On", "IN", "O");
		this.checkValue(words.get(2), 2, "behalf", "NN", "O");
		this.checkValue(words.get(3), 3, "of", "IN", "O");
		this.checkValue(words.get(4), 4, "everyone", "NN", "O");
		this.checkValue(words.get(5), 5, "at", "IN", "O");
		this.checkValue(words.get(6), 6, "Disney", "NNP", "ORGANIZATION");
		this.checkValue(words.get(7), 7, ",", ",", "O");
		this.checkValue(words.get(8), 8, "I", "PRP", "O");
		this.checkValue(words.get(9), 9, "thank", "VBP", "O");
		this.checkValue(words.get(10), 10, "you", "PRP", "O");
		this.checkValue(words.get(11), 11, "for", "IN", "O");
		this.checkValue(words.get(12), 12, "your", "PRP$", "O");
		this.checkValue(words.get(13), 13, "continued", "JJ", "O");
		this.checkValue(words.get(14), 14, "support", "NN", "O");
		this.checkValue(words.get(15), 15, "as", "IN", "O");
		this.checkValue(words.get(16), 16, "we", "PRP", "O");
		this.checkValue(words.get(17), 17, "strive", "VBP", "O");
		this.checkValue(words.get(18), 18, "to", "TO", "O");
		this.checkValue(words.get(19), 19, "create", "VB", "O");
		this.checkValue(words.get(20), 20, "the", "DT", "O");
		this.checkValue(words.get(21), 21, "next", "JJ", "O");
		this.checkValue(words.get(22), 22, "generation", "NN", "O");
		this.checkValue(words.get(23), 23, "of", "IN", "O");
		this.checkValue(words.get(24), 24, "fantastic", "JJ", "O");
		this.checkValue(words.get(25), 25, "family", "NN", "O");
		this.checkValue(words.get(26), 26, "entertainment", "NN", "O");
		this.checkValue(words.get(27), 27, ".", ".", "O");
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
