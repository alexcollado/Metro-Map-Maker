package metro_map_maker.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

/**
 * Abstract base class extended by classes that implement EdgeWeightedGraph.
 *
 * @param <Node> The type of the nodes in the graph.
 * @author E. Stark
 * @version 20171124
 */
public abstract class AbstractEdgeWeightedGraph<Node extends Comparable<? super Node>>
        extends AbstractGraph<Node> implements EdgeWeightedGraph<Node> {

    /**
     * Class that pairs a Node with a weight, for use in Dijkstra's algorithm.
     * The natural ordering for this class compares first by weight, then the
     * underlying node.
     */
    private class NodeWithWeight implements Comparable<NodeWithWeight> {

        private final Node node;
        private final double weight;

        public NodeWithWeight(Node node, double weight) {
            this.node = node;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null)
                return false;
            if (other == this)
                return true;
            if (other.getClass() != getClass())
                return false;
            NodeWithWeight on = (NodeWithWeight) other;
            return node.equals(on.node) && weight == on.weight;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 61 * hash + Objects.hashCode(this.node);
            return hash;
        }

        @Override
        public int compareTo(NodeWithWeight other) {
            int b = Double.compare(weight, other.weight);
            if (b != 0) {
                return b;
            } else {
                return node.compareTo(other.node);
            }
        }
    }

    /**
     * Find a minimum-weight path from a specified starting node to a specified
     * goal node. Uses Dijkstra's algorithm, which assumes that the edge weights
     * are nonnegative.
     *
     * @param start The starting node.
     * @param goal The goal node.
     * @return a list of nodes that constitutes a minimum-weight path from the
     * starting node to the goal node if one exists; otherwise null.
     */
    public List<Node> minimumWeightPath(Node start, Node goal) {
        // Current minimum path weights from start node to other nodes.
        // For visited nodes, the values here are the correct minimums.
        // For unvisited nodes, the values may still decrease as the
        // search proceeds.
        HashMap<Node, Double> weights = new HashMap<>();
        
        // The nodes that have not yet been visited.
        HashSet<Node> unvisited = new HashSet<>();
        
        // Unvisited nodes with current best estimate of the path weight
        // from the start node, sorted so that we can efficiently choose
        // a node with minimum path weight.  Unvisited nodes that have not
        // yet been assigned finite distance are not stored in this set.
        //
        // If we had handy a priority queue with a decrease key operation,
        // that would be a better data structure to use here, but since we
        // don't have one we are using a TreeSet, which is fast enough
        // for our purposes.
        TreeSet<NodeWithWeight> treeSet = new TreeSet<>();
        
        // Predecessor map for tracing the path back to the start node once the
        // goal node has been reached.
        HashMap<Node, Node> predecessor = new HashMap<>();

        // Initialization: set the start node at distance 0 and set all other
        // nodes at infinite distance.
        for (Iterator<Node> it = nodeIterator(null); it.hasNext();) {
            Node node = it.next();
            unvisited.add(node);
            weights.put(node, Double.POSITIVE_INFINITY);
        }
        weights.put(start, 0.0);
        treeSet.add(new NodeWithWeight(start, 0.0));

        // Main loop: Iterate while there are still unvisited reachable nodes.
        while (treeSet.size() > 0) {
            // Choose an unvisited node "closest" to the start node.
            NodeWithWeight wn = treeSet.iterator().next();
            treeSet.remove(wn);
            Node closest = wn.node;
            unvisited.remove(closest);
            double weightToClosest = wn.weight;

            // If the closest node is the goal, then we have found a
            // minimum-weight path, so we can stop now.
            if (closest.equals(goal))
                break;

            // Update weights to account for possibly lower-weight paths
            // through the chosen node.  Note that we only have to
            // consider unvisited nodes accessible in one move from the
            // currently selected node.
            for (Iterator<Node> it = nodeIterator(closest); it.hasNext(); ) {
                Node n = it.next();
                if (unvisited.contains(n)) {
                    double ew = edgeWeight(closest, n);
                    double owgt = weights.get(n);
                    double nwgt = weightToClosest + ew;
                    if (nwgt < owgt) {
                        weights.put(n, nwgt);
                        predecessor.put(n, closest);
                        wn = new NodeWithWeight(n, owgt);
                        treeSet.remove(wn);
                        wn = new NodeWithWeight(n, nwgt);
                        treeSet.add(wn);
                        //System.out.println(n + " (" + owgt + " --> " + nwgt + ")");
                    }
                }
            }
        }

        // If we've managed to reach the goal, use the predecessor map to
        // retrace the minimum-weight path.
        if (weights.get(goal) == Double.POSITIVE_INFINITY) {
            return null;
        } else {
            List<Node> path = new LinkedList<>();
            Node n = goal;
            while (!start.equals(n)) {
                path.add(0, n);
                n = predecessor.get(n);
            }
            path.add(0, start);
            return path;
        }
    }

}
