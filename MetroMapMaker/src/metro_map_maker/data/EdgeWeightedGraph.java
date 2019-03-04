package metro_map_maker.data;

/**
 * Extension of Graph that is implemented by graphs whose edges have associated
 * weights.
 * 
 * @author E. Stark
 * @version 20171124
 */
public interface EdgeWeightedGraph<Node> extends Graph<Node> {
    
    /**
     * Get the weight of the edge from node "from" to node "to".
     * 
     * @param from  the starting node of the edge.
     * @param to  then ending node of the edge.
     * @return  the weight of the edge from the starting node to the ending
     * node, if an edge exists, otherwise Double.POSITIVE_INFINITY.
     */
    public double edgeWeight(Node from, Node to);
}
