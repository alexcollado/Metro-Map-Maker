/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metro_map_maker.transactions;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.text.Text;
import jtps.jTPS_Transaction;
import metro_map_maker.data.DraggableText;

/**
 *
 * @author alexc
 */
public class AddLabel implements jTPS_Transaction {
    DraggableText label;
    ObservableList<Node> nodes;
    //String text;
    
    public AddLabel(DraggableText label, ObservableList<Node> nodes) {
        this.label = label;
        this.nodes = nodes;
        //this.text = text;
    }

    @Override
    public void doTransaction() {
        nodes.add(label);
    }

    @Override
    public void undoTransaction() {
        nodes.remove(label);
    }
    
}
