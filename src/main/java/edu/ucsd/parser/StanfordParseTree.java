package edu.ucsd.parser;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefChain.CorefMention;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class StanfordParseTree {
	private static Logger logger = Logger.getLogger(StanfordParseTree.class);

	public static void main(String[] args) {
		Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        props.put("dcoref.score", true);
        
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        StopWatch sw = new StopWatch("Annotate and Resolve coreferences");
        sw.start();
        Annotation document = new Annotation("If they are angry about the music, the neighbors will call the cops.");

        pipeline.annotate(document);
        Map<Integer, CorefChain> coref = document.get(CorefChainAnnotation.class);
        if(coref != null) {
        	for(Map.Entry<Integer, CorefChain> entry : coref.entrySet()) {
        		CorefChain c = entry.getValue();
        		//this is because it prints out a lot of self references which aren't that useful
        		
        		if(c.getMentionsInTextualOrder().size() <= 1)
        			continue;
				
        		CorefMention cm = c.getRepresentativeMention();
        		
        		String clust = "";
        		List<CoreLabel> tks = document.get(SentencesAnnotation.class).get(cm.sentNum-1).get(TokensAnnotation.class);
        		logger.info("Start Index: " + cm.startIndex + " End Index: " + cm.endIndex);
        		for(int i = cm.startIndex-1; i < cm.endIndex-1; i++) {
        			clust += tks.get(i).get(TextAnnotation.class) + " ";
        		}
        		clust = clust.trim();
        		logger.info("representative mention: \"" + clust + "\" is mentioned by:");

        		for(CorefMention m : c.getMentionsInTextualOrder()) {
        			String clust2 = "";
        			tks = document.get(SentencesAnnotation.class).get(m.sentNum-1).get(TokensAnnotation.class);
        			for(int i = m.startIndex-1; i < m.endIndex-1; i++)
        				clust2 += tks.get(i).get(TextAnnotation.class) + " ";
        			clust2 = clust2.trim();
        			//don't need the self mention
        			
        			if(clust.equals(clust2))
        				continue;
					
        			logger.info("\t" + clust2);
        		}
        	}
        }
        sw.stop();
        logger.info(sw.prettyPrint());
	}
}
	
