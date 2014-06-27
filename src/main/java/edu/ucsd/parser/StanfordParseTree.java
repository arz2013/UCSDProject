package edu.ucsd.parser;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.springframework.context.ApplicationContext;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.Sentence;
import edu.ucsd.model.Word;
import edu.ucsd.system.SystemApplicationContext;

public class StanfordParseTree {

	public static void main(String[] args) {
		ApplicationContext appContext = SystemApplicationContext.getApplicationContext();
		SentenceDao sentenceDao = SentenceDao.class.cast(appContext.getBean("sentenceDao")); 

		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		String text = "My dog also likes eating sausage.";
		
		Sentence rawSentence = Sentence.newSentence(text);
		sentenceDao.save(rawSentence);

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);
		
		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for(CoreMap sentence: sentences) {
			// this is the parse tree of the current sentence
	        Tree tree = sentence.get(TreeAnnotation.class);
	        DFS dfs = new DFS(sentenceDao, rawSentence, new HashSet<Word>());
	        dfs.performDepthFirstTraversal(tree);
	        System.out.println("Name of the class: " + tree.getClass().getName());
	        System.out.println(tree);
		}
	}
}
