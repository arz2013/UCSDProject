package edu.ucsd.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipExpander;

public abstract class Neo4JUtils {
	public static Node getAncestor(Node node, RelationshipExpander expander) {
		Iterator<Relationship> relIterator = expander.expand(node).iterator();
		List<Node> ancestors = new ArrayList<Node>();
		
        while (relIterator.hasNext()) {
            Relationship rel = relIterator.next();
            node = rel.getOtherNode(node);
            ancestors.add(node);
        }
        
        AssertionUtils.assertTrue(ancestors.size() == 1, "This method is to be used only when you know for sure that you have one ancestor!!");;
        return ancestors.get(0);
	}
}
