package edu.ucsd.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.ApplicationContext;

import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.Sentence;
import edu.ucsd.model.Word;
import edu.ucsd.model.WordToWordDependency;
import edu.ucsd.system.SystemApplicationContext;
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
	public static void main(String[] args) {
	    ApplicationContext appContext = SystemApplicationContext.getApplicationContext();
	    SentenceDao sentenceDao = SentenceDao.class.cast(appContext.getBean("sentenceDao")); 
		
		Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    
	    // read some text in the text variable
	    String text = "The strongest rain ever recorded in India shut down the financial hub of Mumbai, snapped communication lines, closed airports and forced thousands of people to sleep in their offices or walk home during the night, officials said today.";
	    
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    
	    // Maintain a list of words that has already been seen
	    Map<String, Word> seenWords = new HashMap<String, Word>();
	    Word root = Word.newWord("ROOT");
	    sentenceDao.save(root);
	    seenWords.put(root.getText(), root);
	    
	    for(CoreMap sentence: sentences) {
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
	          System.out.println("Word, POS, and NE: " + word + ", " + pos + ", " + ne);
	          Word seenWord = seenWords.get(word);
	          if(seenWord == null) {
	        	  seenWord = Word.newWord(word);
	        	  seenWords.put(word, seenWord);
	          }
	          seenWord.addLabel(pos);
	          sentenceDao.save(seenWord);
	          newSentence.addWord(seenWord);
	        }
	        
	        System.out.println("Number of words: " + newSentence.countNumberOfWords());
	        
	        sentenceDao.save(newSentence);
	        
	    	// this is the parse tree of the current sentence
	        Tree tree = sentence.get(TreeAnnotation.class);
	        // Get dependency tree
	        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	        GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
	        Collection<TypedDependency> tds = gs.typedDependenciesCollapsed();
	        for(TypedDependency td : tds) {
	        	
	        	System.out.println(td);
	        	System.out.println(td.reln().getLongName());
	        	System.out.println(td.gov().nodeString());
	        	System.out.println("Dependency Name: " + td.dep().nodeString() +  " Node: " + td.reln());
	        	WordToWordDependency dependency = new WordToWordDependency(seenWords.get(td.gov().nodeString()), seenWords.get(td.dep().nodeString()), td.reln().toString());
	        	sentenceDao.save(dependency);
	        }
	        System.out.println("Number of dependencies: " + tds.size());
	    }
	}
}
