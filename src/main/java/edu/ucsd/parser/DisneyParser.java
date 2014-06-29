package edu.ucsd.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.IndexAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.ucsd.dao.SentenceDao;
import edu.ucsd.model.Sentence;
import edu.ucsd.model.Word;
import edu.ucsd.model.WordToWordDependency;

public class DisneyParser {
	private SentenceDao sentenceDao;
	private List<String> disneyFinancialStatement;
	
	public DisneyParser(SentenceDao sentenceDao,
			List<String> disneyFinancialStatement) {
		super();
		if(sentenceDao == null || disneyFinancialStatement == null) {
			throw new IllegalArgumentException("Neither parameters to constructor can be null");
		}
		this.sentenceDao = sentenceDao;
		this.disneyFinancialStatement = disneyFinancialStatement;
	}
	
	public void parseAndLoad() {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		int noSentence = 0;

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
				Map<Word.TextAndPosition, Word> seenWords = new HashMap<Word.TextAndPosition, Word>();
				Word root = Word.newWord("ROOT", 0);
				sentenceDao.save(root);
				seenWords.put(root.getTextAndPosition(), root);

				// traversing the words in the current sentence
				// a CoreLabel is a CoreMap with additional token-specific methods
				Sentence newSentence = Sentence.newSentence(text, noSentence);
				sentenceDao.save(newSentence);

				// this is the parse tree of the current sentence
				Tree tree = sentence.get(TreeAnnotation.class);
				// Get dependency tree
				TreebankLanguagePack tlp = new PennTreebankLanguagePack();
				GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
				GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
				Collection<TypedDependency> tds = gs.typedDependenciesCollapsed();
				for(TypedDependency td : tds) {
					// Check for existence of words
					Word startWord = getOrCreateWord(td.gov(), seenWords);
					Word endWord = getOrCreateWord(td.dep(), seenWords);
					
					WordToWordDependency dependency = new WordToWordDependency(startWord, endWord, td.reln().toString());
					sentenceDao.save(dependency);
				}
				
				// Now save the parse trees as well
				DFS dfs = new DFS(sentenceDao, newSentence, seenWords);
				dfs.performDepthFirstTraversal(tree);
				
				noSentence++;
				seenWords.clear();
			}
		}
	}
	
	private Word getOrCreateWord(TreeGraphNode node, Map<Word.TextAndPosition, Word> seenWords) {
		Word word = Word.newWord(node.nodeString(), node.label().get(IndexAnnotation.class));
		
		if(!seenWords.containsKey(word.getTextAndPosition())) {
			word.setPosTag(node.label().get(PartOfSpeechAnnotation.class));
			word.setNameEntityTag(node.label().get(NamedEntityTagAnnotation.class));
			sentenceDao.save(word);
			seenWords.put(word.getTextAndPosition(), word);
		} else {
			word = seenWords.get(word.getTextAndPosition());
		}
		
		return word;
	}
	
}
