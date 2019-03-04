package metro_map_maker.data;

import java.util.Iterator;

/**
 * Interface implemented by objects that can be viewed as a graph.
 *
 * @author E. Stark
 * @version 20171124
 */
public interface Graph<Node> {
    
    /**
     * Test whether a specified node belongs to this graph.
     * 
     * @param node  the node to test.
     * @return true if the specified node belongs to the graph; otherwise
     * false.
     */
    public boolean containsNode(Node node);
    
    /**
     * Determine whether two nodes are adjacent in this graph.
     * 
     * @param from the starting node.
     * @param to the node to be tested for adjacency to the starting node.
     * @return true node "to" can be reached by an edge from node "from";
     * otherwise false.
     */
    public boolean isAdjacent(Node from, Node to);
        
    /**
     * Get an iterator over the nodes of this graph that are adjacent to a
     * specified node.
     * 
     * @param from a specified node, or null.
     * @return an iterator over the nodes of this graph that are adjacent to
     * the specified node, or if the specified node is null, then an iterator
     * over all the nodes of this graph.
     * 
     */
    public Iterator<Node> nodeIterator(Node from);
        
}
