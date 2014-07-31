package edu.ucsd.patternmatching;

import java.util.Set;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import edu.ucsd.model.Dependency;
import edu.ucsd.model.Document;
import edu.ucsd.model.ParseTreeTags;
import edu.ucsd.model.Rel;

public class ActiveVerbSearch {
	private final static Logger logger = LoggerFactory.getLogger(ActiveVerbSearch.class);
	
	@Inject
	private Neo4jTemplate template;

	public void searchVerb(Document document) {
		int numberOfSentences = 0;
		Node documentNode = template.getNode(document.getId());
		Iterable<Relationship> sentences = documentNode.getRelationships(Direction.OUTGOING, Rel.HAS_SENTENCE);
		for(Relationship relationship : sentences) {
			performMatchingVerbs(relationship.getEndNode(), ParseTreeTags.getVerbTagsAsString());
			numberOfSentences++;
			
		}
		logger.info("Number of sentences:" + numberOfSentences);
	}

	private void performMatchingVerbs(Node sentence, Set<String> verbTags) {
		Iterable<Relationship> words = sentence.getRelationships(Direction.OUTGOING, Rel.HAS_WORD);
		for(Relationship word : words) {
			Iterable<Relationship> wordDependencies = word.getEndNode().getRelationships(Direction.OUTGOING, Rel.WORD_DEPENDENCY);
			for(Relationship wordDependency : wordDependencies) {
				if(Dependency.NSUB.getValue().equals(wordDependency.getProperty("dependency"))) {
					if(verbTags.contains(word.getEndNode().getProperty("posTag"))) {
						logger.info("Verb: " + word.getEndNode().getProperty("text") + " with dependency: " + wordDependency.getEndNode().getProperty("text"));
					}
				}
			}
		}
	}
}
