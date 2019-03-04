/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import jtps.jTPS_Transaction;

/**
 *
 * @author alexc
 */
public class RemoveMapElement implements jTPS_Transaction {
    ObservableList<Node> nodes;
    Node node;
    
    public RemoveMapElement(ObservableList<Node> nodes, Node node) {
        this.nodes = nodes;
        this.node = node;
    }

    @Override
    public void doTransaction() {
        nodes.remove(node);
    }

    @Override
    public void undoTransaction() {
        nodes.add(node);
    }
    
}
