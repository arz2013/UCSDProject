package edu.ucsd.parser;

import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.Sentence;
import edu.ucsd.model.Word;
import edu.ucsd.model.WordToWordDependency;
import edu.ucsd.system.SystemApplicationContext;
import edu.ucsd.utils.LabelUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.util.StopWatch;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;

public class StanfordParser2 {
	
	private static List<String> readDisneyFinancialStatement() throws IOException {
		InputStream is = SentenceBreaker.class.getClassLoader().getResourceAsStream("DFS.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String inputLine; 
		while((inputLine = br.readLine()) != null) {
			sb.append(inputLine);
		}
		
		int count = 0;		
		String paragraph = sb.toString();
		List<String> sentences = new ArrayList<String>();
		BreakIterator boundary = BreakIterator.getSentenceInstance();
		boundary.setText(paragraph);
		String previous = "";
		int start = boundary.first();
	    for (int end = boundary.next();
	          end != BreakIterator.DONE;
	          start = end, end = boundary.next()) {
	    	  String text = paragraph.substring(start, end);
	    	  if (text.startsWith("Banks from Walt Disney Studios.") || text.startsWith("It will continue to grow as long as there is imagination left")) {
	    		  previous = previous.concat(text.trim());
	    		  sentences.set(count - 1, previous);
	    	  } else {
	    		  sentences.add(text.trim());
	    		  count++;
	    	  }
	    	  
	          previous = text;
	    }

	    return sentences;
	}
	
	public static void main(String[] args) throws IOException {
		StopWatch stopWatch = new StopWatch("Parsing and Insertion");
		
		stopWatch.start();
		ApplicationContext appContext = SystemApplicationContext.getApplicationContext();
		SentenceDao sentenceDao = SentenceDao.class.cast(appContext.getBean("sentenceDao")); 

		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		List<String> disneyFinancialStatement = readDisneyFinancialStatement();

		for(String text : disneyFinancialStatement ) {
			// create an empty Annotation just with the given text
			Annotation document = new Annotation(text);

			// run all Annotators on this text
			pipeline.annotate(document);

			// these are all the sentences in this document
			// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
			List<CoreMap> sentences = document.get(SentencesAnnotation.class);

			for(CoreMap sentence: sentences) {
				// Maintain a list of words that has already been seen
				Map<String, Word> seenWords = new HashMap<String, Word>();
				Word root = Word.newWord("ROOT");
				sentenceDao.save(root);
				seenWords.put(root.getText(), root);

				// traversing the words in the current sentence
				// a CoreLabel is a CoreMap with additional token-specific methods
				Sentence newSentence = Sentence.newSentence(text);

				for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
					// this is the text of the token
					String word = token.get(TextAnnotation.class);
					// this is the POS tag of the token
					String pos = token.get(PartOfSpeechAnnotation.class);
					// this is the NER label of the token
					String ne = token.get(NamedEntityTagAnnotation.class);
					// System.out.println("Word, POS, and NE: " + word + ", " + pos + ", " + ne);
					Word seenWord = seenWords.get(word);
					if(seenWord == null) {
						seenWord = Word.newWord(word);
						seenWords.put(word, seenWord);
					}
					seenWord.addPosTag(pos);
					seenWord.addNameEntityTag(ne);
					sentenceDao.save(seenWord);
					newSentence.addWord(seenWord);
				}

				sentenceDao.save(newSentence);

				// this is the parse tree of the current sentence
				Tree tree = sentence.get(TreeAnnotation.class);
				// Get dependency tree
				TreebankLanguagePack tlp = new PennTreebankLanguagePack();
				GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
				GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
				Collection<TypedDependency> tds = gs.typedDependenciesCollapsed();
				for(TypedDependency td : tds) {
					WordToWordDependency dependency = new WordToWordDependency(seenWords.get(td.gov().nodeString()), seenWords.get(td.dep().nodeString()), td.reln().toString());
					sentenceDao.save(dependency);
				}
				
				// Now save the parse trees as well
				DFS dfs = new DFS(sentenceDao, newSentence, new HashSet<Word>());
				dfs.performDepthFirstTraversal(tree);
				
				seenWords.clear();
			}
		}
		stopWatch.stop();
		System.out.println(stopWatch.prettyPrint());
	}
}
