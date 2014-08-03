package edu.ucsd.patternmatching;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import edu.ucsd.model.Document;
import edu.ucsd.model.Rel;

public class PenultimateParseTreeNodeMatcher {
	private static Logger logger = LoggerFactory.getLogger(PenultimateParseTreeNodeMatcher.class);
	private Map<Node, Set<Node>> sentenceNodeToPenultimateParseTreeNodes;
	
	@Inject
	private Neo4jTemplate template;
	
	public PenultimateParseTreeNodeMatcher() {
		sentenceNodeToPenultimateParseTreeNodes = new HashMap<Node, Set<Node>>();
	}
	
	private void collectPenultimateNodes(Node sentence, Node startNode) {
		Iterable<Relationship> parseChildren = startNode.getRelationships(Direction.OUTGOING, Rel.HAS_PARSE_CHILD);
		if(parseChildren.iterator().hasNext()) {
			for(Relationship parseChild : parseChildren) {
				Iterable<Relationship> parseGrandChildren = parseChild.getEndNode().getRelationships(Direction.OUTGOING, Rel.HAS_PARSE_CHILD);
				if(parseGrandChildren.iterator().hasNext()) {
					for(Relationship parseGrandChild : parseGrandChildren) {
						if(!parseGrandChild.getEndNode().hasRelationship(Direction.OUTGOING, Rel.HAS_PARSE_CHILD)) {
							Set<Node> candidateNodes = this.sentenceNodeToPenultimateParseTreeNodes.get(sentence);
							candidateNodes.add(startNode);
						}
					}					
				}
			} 
		}
		parseChildren = startNode.getRelationships(Direction.OUTGOING, Rel.HAS_PARSE_CHILD);
		for(Relationship parseChild : parseChildren) {
			collectPenultimateNodes(sentence, parseChild.getEndNode());
		}
	}
	
	private void performMatchingSentence(Node node) {
		sentenceNodeToPenultimateParseTreeNodes.put(node, new HashSet<Node>());
		collectPenultimateNodes(node, node);
		logger.info("Text : " + node.getProperty("text"));
		for(Node candidateNode : this.sentenceNodeToPenultimateParseTreeNodes.get(node)) {
			StringBuilder sb = new StringBuilder();
			candidateNodeAsString(candidateNode);
			logger.info(sb.toString());
		}
	}
	
	private void candidateNodeAsString(Node candidateNode) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(candidateNode.getProperty("value"));
		sb.append(" ");
		List<Node> candidateNodeWords = new ArrayList<Node>();
		Iterable<Relationship> parseChildren = candidateNode.getRelationships(Direction.OUTGOING, Rel.HAS_PARSE_CHILD);
		for(Relationship parseChild : parseChildren) {
			Node child = parseChild.getEndNode();
			candidateNodeWords.add(child);
		}
		Collections.sort(candidateNodeWords, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				return new Long(o1.getId()).compareTo(o2.getId());
			}
		});
		for(Node node : candidateNodeWords) {
			Iterable<Relationship> parseGrandChildren = node.getRelationships(Direction.OUTGOING, Rel.HAS_PARSE_CHILD);
			for(Relationship parseGrandChild : parseGrandChildren) {
				if(parseGrandChild.getEndNode().hasProperty("text")) {
					sb.append("(");
					sb.append(node.getProperty("value"));
					sb.append(" ");
					sb.append(parseGrandChild.getEndNode().getProperty("text"));
					sb.append(")");
				}
			}
		}
		sb.append(")");
		logger.info(sb.toString());
		
	}
	
	public void performMatching(Document document) {
		Node documentNode = template.getNode(document.getId());
		Iterable<Relationship> sentences = documentNode.getRelationships(Direction.OUTGOING, Rel.HAS_SENTENCE);
		int numberOfSentences = 0;
		for(Relationship relationship : sentences) {
			performMatchingSentence(relationship.getEndNode());
			numberOfSentences++;
			
		}
		logger.info("Number of sentences:" + numberOfSentences);
	}
}