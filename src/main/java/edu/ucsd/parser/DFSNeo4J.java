package edu.ucsd.parser;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.OrderedByTypeExpander;
import org.neo4j.kernel.Traversal;
import org.springframework.context.ApplicationContext;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import edu.ucsd.model.Rel;
import edu.ucsd.system.SystemApplicationContext;

public class DFSNeo4J {
	@Inject
	private Neo4jTemplate template; 
	
	public void depthFirstSearch() {
		Node node = template.getNode(37);
		PathExpander<?> expander = new OrderedByTypeExpander().add(Rel.FIRST_CHILD, Direction.OUTGOING).add(Rel.NEXT, Direction.OUTGOING);
		TraversalDescription DFS_TRAVERSAL = Traversal.description()
			    .depthFirst().expand(expander);
		for(Path path : DFS_TRAVERSAL.traverse(node)) {
			Node endNode = path.endNode();
			if(endNode.hasProperty("value")) {
				System.out.println(endNode.getProperty("value"));
			} else if(endNode.hasProperty("text")) {
				System.out.println(endNode.getProperty("text"));
			}
		}
	}
	
	public static void main(String[] args) {
		ApplicationContext context = SystemApplicationContext.getApplicationContext();
		DFSNeo4J dfsNeo4J = DFSNeo4J.class.cast(context.getBean("dfsNeo4J"));
		dfsNeo4J.depthFirstSearch();
	}

}
